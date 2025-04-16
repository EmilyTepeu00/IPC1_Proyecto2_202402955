package modelo;

import modelo.RepuestosModelo.Repuesto;

public class ServiciosModelo {
    private static final int MAX_SERVICIOS = 100;
    private static Servicio[] servicios = new Servicio[MAX_SERVICIOS];
    private static int contadorServicios = 0;
    private static int siguienteId = 1001;

    public static class Servicio {
        private int id;
        private String nombre;
        private String marca;
        private String modelo;
        private Repuesto[] repuestos;
        private double precioManoObra;
        private double precioTotal;
        private int contadorRepuestos;

        public Servicio(int id, String nombre, String marca, String modelo, 
                       double precioManoObra) {
            this.id = id;
            this.nombre = nombre;
            this.marca = marca;
            this.modelo = modelo;
            this.precioManoObra = precioManoObra;
            this.repuestos = new Repuesto[1000]; 
            this.contadorRepuestos = 0;
            calcularPrecioTotal();
        }

        public void agregarRepuesto(Repuesto repuesto) {
            if (contadorRepuestos < repuestos.length) {
                repuestos[contadorRepuestos++] = repuesto;
                calcularPrecioTotal();
            }
        }

        private void calcularPrecioTotal() {
            double sumaRepuestos = 0;
            for (int i = 0; i < contadorRepuestos; i++) {
                if (repuestos[i] != null) {
                    sumaRepuestos += repuestos[i].getPrecio();
                }
            }
            this.precioTotal = sumaRepuestos + precioManoObra;
        }

        //GETTERS
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getMarca() { return marca; }
        public String getModelo() { return modelo; }
        public Repuesto[] getRepuestos() { return repuestos; }
        public int getContadorRepuestos() { return contadorRepuestos; }
        public double getPrecioManoObra() { return precioManoObra; }
        public double getPrecioTotal() { return precioTotal; }
        
        //SETTERS CON VALIDACION
        public void setNombre(String nombre) { 
            if (nombre != null && !nombre.trim().isEmpty()) {
                this.nombre = nombre.trim(); 
            }
        }
        
        public void setMarca(String marca) { 
            if (marca != null && !marca.trim().isEmpty()) {
                this.marca = marca.trim(); 
            }
        }
        
        public void setModelo(String modelo) { 
            if (modelo != null && !modelo.trim().isEmpty()) {
                this.modelo = modelo.trim(); 
            }
        }
        
        public void setPrecioManoObra(double precioManoObra) { 
            if (precioManoObra >= 0) {
                this.precioManoObra = precioManoObra; 
                calcularPrecioTotal();
            }
        }
    }

    public static int agregarServicio(String nombre, String marca, String modelo, 
                                     double precioManoObra) {
        if (contadorServicios >= MAX_SERVICIOS || 
            nombre == null || nombre.trim().isEmpty() ||
            marca == null || marca.trim().isEmpty() ||
            modelo == null || modelo.trim().isEmpty()) {
            return -1;
        }
        
        int id = siguienteId++;
        servicios[contadorServicios++] = new Servicio(id, nombre.trim(), marca.trim(), modelo.trim(), precioManoObra);
        return id;
    }

    public static Servicio buscarServicio(int id) {
        for (int i = 0; i < contadorServicios; i++) {
            if (servicios[i] != null && servicios[i].getId() == id) {
                return servicios[i];
            }
        }
        return null;
    }

    public static boolean modificarServicio(int id, String nombre, String marca, String modelo, double precioManoObra) {
        Servicio servicio = buscarServicio(id);
        if (servicio == null) return false;
        
        servicio.setNombre(nombre);
        servicio.setMarca(marca);
        servicio.setModelo(modelo);
        servicio.setPrecioManoObra(precioManoObra);
        return true;
    }

    public static boolean eliminarServicio(int id) {
        for (int i = 0; i < contadorServicios; i++) {
            if (servicios[i] != null && servicios[i].getId() == id) {
                //MOVER LOS SERVICIOS HACIA ATRAS
                for (int j = i; j < contadorServicios - 1; j++) {
                    servicios[j] = servicios[j + 1];
                }
                servicios[--contadorServicios] = null;
                return true;
            }
        }
        return false;
    }

    public static Servicio[] obtenerTodosServicios() {
        Servicio[] resultado = new Servicio[contadorServicios];
        System.arraycopy(servicios, 0, resultado, 0, contadorServicios);
        return resultado;
    }
    
    public static boolean agregarRepuestoAServicio(int idServicio, int idRepuesto) {
        Servicio servicio = buscarServicio(idServicio);
        Repuesto repuesto = RepuestosModelo.buscarRepuesto(idRepuesto);
        
        if (servicio == null || repuesto == null) return false;
        
        //VERIFICAR QUE LA MARCA Y MODELO CONICIDAN
        if (!servicio.getMarca().equals(repuesto.getMarca()) || 
            !servicio.getModelo().equals(repuesto.getModelo())) {
            return false;
        }
        
        servicio.agregarRepuesto(repuesto);
        return true;
    }
    
    public static int getSiguienteId() {
        return siguienteId;
    }
}