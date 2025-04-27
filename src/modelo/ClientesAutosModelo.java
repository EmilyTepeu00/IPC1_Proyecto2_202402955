package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class ClientesAutosModelo {
    private static final int MAX_CLIENTES = 100;
    private static Cliente[] clientes = new Cliente[MAX_CLIENTES];
    private static int contadorClientes = 0;
    private static final String ARCHIVO_DATOS = "clientes.dat";
    private static final SerializadorModelo serializador = new SerializadorModelo();

    public static class Cliente implements Serializable {
        private String dpi;
        private String nombreCompleto;
        private String usuario;
        private String contraseña;
        private String tipoCliente;
        private String automovil;
        private static final long serialVersionUID = 1L;
        
        public Cliente(String dpi, String nombreCompleto, String usuario, String contraseña, String tipoCliente, String automovil) {
            this.dpi = dpi;
            this.nombreCompleto = nombreCompleto;
            this.usuario = usuario;
            this.contraseña = contraseña;
            this.tipoCliente = tipoCliente;
            this.automovil = automovil;
        }

        //GETETRS
        public String getDpi() { return dpi; }
        public String getNombreCompleto() { return nombreCompleto; }
        public String getUsuario() { return usuario; }
        public String getContraseña() { return contraseña; }
        public String getTipoCliente() { return tipoCliente; }
        public String getAutomovil() { return automovil; }
        
        //SETTERS
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public void setContraseña(String contraseña) { this.contraseña = contraseña; }
        public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
        public void setAutomovil(String automovil) { this.automovil = automovil; }
    }

    public static void guardarDatos() {
        DatosClientes datos = new DatosClientes(clientes, contadorClientes);
        serializador.guardarDatos(ARCHIVO_DATOS, datos);
    }

    public static void cargarDatos() {
        DatosClientes datos = (DatosClientes) serializador.cargarDatos(ARCHIVO_DATOS);
        if (datos != null) {
            clientes = datos.clientes;
            contadorClientes = datos.contadorClientes;
        }
    }

    private static class DatosClientes implements Serializable {
        private static final long serialVersionUID = 1L;
        final Cliente[] clientes;
        final int contadorClientes;

        public DatosClientes(Cliente[] clientes, int contadorClientes) {
            this.clientes = clientes;
            this.contadorClientes = contadorClientes;
        }
    }

    public static Cliente buscarClientePorDPI(String dpi) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getDpi().equals(dpi)) {
                return clientes[i];
            }
        }
        return null;
    }

    public static Cliente buscarClientePorUsuario(String usuario) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getUsuario().equals(usuario)) {
                return clientes[i];
            }
        }
        return null;
    }

    public static boolean verificarCredenciales(String usuario, String contraseña) {
        Cliente cliente = buscarClientePorUsuario(usuario);
        return cliente != null && cliente.getContraseña().equals(contraseña);
    }

    public static boolean agregarCliente(String dpi, String nombre, String usuario, String contraseña, String tipoCliente, String automovil) {
        if (contadorClientes >= MAX_CLIENTES || buscarClientePorDPI(dpi) != null) {
            return false;
        }
        
        clientes[contadorClientes++] = new Cliente(dpi, nombre, usuario, contraseña, tipoCliente, automovil);
        guardarDatos();
        return true;
    }

    public static boolean modificarCliente(String dpi, String nombre, String usuario, 
                                         String contraseña, String tipoCliente, String automovil) {
        Cliente cliente = buscarClientePorDPI(dpi);
        if (cliente == null) return false;
        
        cliente.setNombreCompleto(nombre);
        cliente.setUsuario(usuario);
        cliente.setContraseña(contraseña);
        cliente.setTipoCliente(tipoCliente);
        cliente.setAutomovil(automovil);
        
        guardarDatos();
        return true;
    }

    public static boolean eliminarCliente(String dpi) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getDpi().equals(dpi)) {
                for (int j = i; j < contadorClientes - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                contadorClientes--;
                guardarDatos();
                return true;
            }
        }
        return false;
    }

    public static Cliente[] obtenerTodosClientes() {
        Cliente[] resultado = new Cliente[contadorClientes];
        System.arraycopy(clientes, 0, resultado, 0, contadorClientes);
        return resultado;
    }
    
    static {
        cargarDatos();
    }
}