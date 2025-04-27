package modelo;

import modelo.RepuestosModelo.Repuesto;
import java.io.Serializable;

public class ServiciosModelo {
    private static final int MAX_SERVICIOS = 100;
    private static Servicio[] servicios = new Servicio[MAX_SERVICIOS];
    private static int contadorServicios = 0;
    private static int siguienteId = 1001;
    private static final String ARCHIVO_DATOS = "servicios.dat";
    private static final SerializadorModelo serializador = new SerializadorModelo();

    public static class Servicio implements Serializable {
        private int id;
        private String nombre;
        private String marca;
        private String modelo;
        private Repuesto[] repuestos;
        private double precioManoObra;
        private double precioTotal;
        private int contadorRepuestos;
        private static final long serialVersionUID = 1L;

        public Servicio(int id, String nombre, String marca, String modelo, double precioManoObra) {
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

        // GETTERS
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getMarca() { return marca; }
        public String getModelo() { return modelo; }
        public Repuesto[] getRepuestos() { return repuestos; }
        public int getContadorRepuestos() { return contadorRepuestos; }
        public double getPrecioManoObra() { return precioManoObra; }
        public double getPrecioTotal() { return precioTotal; }
        
        // SETTERS
        public void setId(int id) { 
            this.id = id; 
        }
        
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
    
    public static void guardarDatos() {
        DatosServicios datos = new DatosServicios(servicios, contadorServicios, siguienteId);
        serializador.guardarDatos(ARCHIVO_DATOS, datos);
    }

    public static void cargarDatos() {
        DatosServicios datos = (DatosServicios) serializador.cargarDatos(ARCHIVO_DATOS);
        if (datos != null) {
            servicios = datos.servicios;
            contadorServicios = datos.contadorServicios;
            siguienteId = datos.siguienteId;
        }
    }
    
    private static class DatosServicios implements Serializable {
        private static final long serialVersionUID = 1L;
        final Servicio[] servicios;
        final int contadorServicios;
        final int siguienteId;

        public DatosServicios(Servicio[] servicios, int contadorServicios, int siguienteId) {
            this.servicios = servicios;
            this.contadorServicios = contadorServicios;
            this.siguienteId = siguienteId;
        }
    }

     public static boolean agregarServicioConId(int id, String nombre, String marca, String modelo, double precioManoObra) {
        if (contadorServicios >= MAX_SERVICIOS || 
            nombre == null || nombre.trim().isEmpty() ||
            marca == null || marca.trim().isEmpty() ||
            modelo == null || modelo.trim().isEmpty() ||
            buscarServicio(id) != null) {
            return false;
        }
        
        servicios[contadorServicios++] = new Servicio(id, nombre.trim(), marca.trim(), modelo.trim(), precioManoObra);
        
        if (id >= siguienteId) {
            siguienteId = id + 1;
        }
        
        guardarDatos();
        return true;
    }

    public static Servicio buscarServicio(int id) {
        for (int i = 0; i < contadorServicios; i++) {
            if (servicios[i] != null && servicios[i].getId() == id) {
                return servicios[i];
            }
        }
        return null;
    }

    public static boolean modificarServicio(int idActual, int nuevoId, String nombre, String marca, String modelo, double precioManoObra) {
        Servicio servicio = buscarServicio(idActual);
        if (servicio == null) return false;
        
        //VERIFICAR QUE EL NUEVO ID NO EXISTA
        if (idActual != nuevoId && buscarServicio(nuevoId) != null) {
            return false;
        }
        
        servicio.setId(nuevoId);
        servicio.setNombre(nombre);
        servicio.setMarca(marca);
        servicio.setModelo(modelo);
        servicio.setPrecioManoObra(precioManoObra);
        
        //ACTUALIZAR SIGUIENTE ID
        if (nuevoId >= siguienteId) {
            siguienteId = nuevoId + 1;
        }
        
        guardarDatos();
        return true;
    }

    public static boolean eliminarServicio(int id) {
        for (int i = 0; i < contadorServicios; i++) {
            if (servicios[i] != null && servicios[i].getId() == id) {
                for (int j = i; j < contadorServicios - 1; j++) {
                    servicios[j] = servicios[j + 1];
                }
                servicios[--contadorServicios] = null;
                guardarDatos();
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
        
        if (!servicio.getMarca().equalsIgnoreCase(repuesto.getMarca()) || 
            !servicio.getModelo().equalsIgnoreCase(repuesto.getModelo())) {
            return false;
        }
        
        servicio.agregarRepuesto(repuesto);
        guardarDatos();
        return true;
    }
    
    public static int getSiguienteId() {
        return siguienteId;
    }
    
    static {
        cargarDatos();
    }
}