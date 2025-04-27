package modelo;

import java.io.Serializable;

public class RepuestosModelo {
    private static final int MAX_REPUESTOS = 100;
    private static Repuesto[] repuestos = new Repuesto[MAX_REPUESTOS];
    private static int contadorRepuestos = 0;
    private static int siguienteId = 1001;
    private static final String ARCHIVO_DATOS = "repuestos.dat";
    private static final SerializadorModelo serializador = new SerializadorModelo();

    public static class Repuesto implements Serializable {
        private int id;
        private String nombre;
        private String marca;
        private String modelo;
        private int existencias;
        private double precio;
        private static final long serialVersionUID = 1L;

        public Repuesto(int id, String nombre, String marca, String modelo, int existencias, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.marca = marca;
            this.modelo = modelo;
            this.existencias = existencias;
            this.precio = precio;
        }

        //GETTERS Y SETTERS
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }
        public int getExistencias() { return existencias; }
        public void setExistencias(int existencias) { this.existencias = existencias; }
        public double getPrecio() { return precio; }
        public void setPrecio(double precio) { this.precio = precio; }
    }
    
    //METODO DE SERIALIZACION
    public static void guardarDatos() {
        DatosRepuestos datos = new DatosRepuestos(repuestos, contadorRepuestos, siguienteId);
        serializador.guardarDatos(ARCHIVO_DATOS, datos);
    }
    
    public static void cargarDatos() {
        DatosRepuestos datos = (DatosRepuestos) serializador.cargarDatos(ARCHIVO_DATOS);
        if (datos != null) {
            repuestos = datos.repuestos;
            contadorRepuestos = datos.contadorRepuestos;
            siguienteId = datos.siguienteId;
        }
    }
    
    private static class DatosRepuestos implements Serializable {
        private static final long serialVersionUID = 1L;
        final Repuesto[] repuestos;
        final int contadorRepuestos;
        final int siguienteId;

        public DatosRepuestos(Repuesto[] repuestos, int contadorRepuestos, int siguienteId) {
            this.repuestos = repuestos;
            this.contadorRepuestos = contadorRepuestos;
            this.siguienteId = siguienteId;
        }
    }

    //PARA GESTIONAR LOS REPUESTOS
    public static int agregarRepuesto(String nombre, String marca, String modelo, int existencias, double precio) {
        if (contadorRepuestos >= MAX_REPUESTOS) return -1;
        
        int id = siguienteId++;
        repuestos[contadorRepuestos++] = new Repuesto(id, nombre, marca, modelo, existencias, precio);
        guardarDatos();
        return id;
    }

    public static Repuesto buscarRepuesto(int id) {
        for (int i = 0; i < contadorRepuestos; i++) {
            if (repuestos[i].getId() == id) {
                return repuestos[i];
            }
        }
        return null;
    }

    public static boolean modificarRepuesto(int id, String nombre, String marca, String modelo, int existencias, double precio) {
        Repuesto repuesto = buscarRepuesto(id);
        if (repuesto == null) return false;
        
        repuesto.setNombre(nombre);
        repuesto.setMarca(marca);
        repuesto.setModelo(modelo);
        repuesto.setExistencias(existencias);
        repuesto.setPrecio(precio);
        guardarDatos();
        return true;
    }

    //PARA ELIMINAR REPUESTOS
    public static boolean eliminarRepuesto(int id) {
        for (int i = 0; i < contadorRepuestos; i++) {
            if (repuestos[i].getId() == id) {
                //MOVER LOS REPUESTOS ANTERIORES HACIA ATRAS
                for (int j = i; j < contadorRepuestos - 1; j++) {
                    repuestos[j] = repuestos[j + 1];
                }
                contadorRepuestos--;
                guardarDatos();
                return true;
            }
        }
        return false;
    }

    public static Repuesto[] obtenerTodosRepuestos() {
        Repuesto[] resultado = new Repuesto[contadorRepuestos];
        System.arraycopy(repuestos, 0, resultado, 0, contadorRepuestos);
        return resultado;
    }
    
    public static int getSiguienteId() {
        return siguienteId;
    }
    
    static {
        cargarDatos();
    }
}