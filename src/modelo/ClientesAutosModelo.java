package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientesAutosModelo {
    private static final int MAX_CLIENTES = 100;
    private static Cliente[] clientes = new Cliente[MAX_CLIENTES];
    private static int contadorClientes = 0;
    private static final String ARCHIVO_CLIENTES = "clientes.dat";
    private static final String CARPETA_AUTOS = "autos/";
    private static final String CARPETA_CREDENCIALES = "credenciales/";

    public static class Cliente {
        private String dpi;
        private String nombreCompleto;
        private String usuario;
        private String contraseña;
        private String tipoCliente;
        private String automovil;

        public Cliente(String dpi, String nombreCompleto, String usuario, 
                      String contraseña, String tipoCliente, String automovil) {
            this.dpi = dpi;
            this.nombreCompleto = nombreCompleto;
            this.usuario = usuario;
            this.contraseña = contraseña;
            this.tipoCliente = tipoCliente;
            this.automovil = automovil;
        }

        // Getters
        public String getDpi() { return dpi; }
        public String getNombreCompleto() { return nombreCompleto; }
        public String getUsuario() { return usuario; }
        public String getContraseña() { return contraseña; }
        public String getTipoCliente() { return tipoCliente; }
        public String getAutomovil() { return automovil; }
        
        // Setters
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public void setContraseña(String contraseña) { this.contraseña = contraseña; }
        public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
        public void setAutomovil(String automovil) { this.automovil = automovil; }
    }

    static {
        new File(CARPETA_AUTOS).mkdirs();
        new File(CARPETA_CREDENCIALES).mkdirs();
        cargarClientes();
    }

    private static void cargarClientes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_CLIENTES))) {
            String linea;
            while ((linea = reader.readLine()) != null && contadorClientes < MAX_CLIENTES) {
                String[] datos = linea.split(",");
                if (datos.length == 6) {
                    clientes[contadorClientes++] = new Cliente(
                        datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]
                    );
                }
            }
        } catch (IOException e) {
            //CREAR ARCHIVO SI NO EXISTE
        }
    }

    public static boolean guardarClientes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CLIENTES))) {
            for (int i = 0; i < contadorClientes; i++) {
                Cliente c = clientes[i];
                writer.write(String.join(",",
                    c.getDpi(),
                    c.getNombreCompleto(),
                    c.getUsuario(),
                    c.getContraseña(),
                    c.getTipoCliente(),
                    c.getAutomovil()
                ));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //BUSCAR CLIENTE SEGUN SU DPI
    public static Cliente buscarClientePorDPI(String dpi) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getDpi().equals(dpi)) {
                return clientes[i];
            }
        }
        return null;
    }

    //BUSCAR USUARIO
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

    public static boolean agregarCliente(String dpi, String nombre, String usuario, 
                                       String contraseña, String tipoCliente, String automovil) {
        if (contadorClientes >= MAX_CLIENTES || buscarClientePorDPI(dpi) != null) {
            return false;
        }
        
        clientes[contadorClientes++] = new Cliente(dpi, nombre, usuario, contraseña, tipoCliente, automovil);
        return guardarClientes();
    }

    public static boolean modificarCliente(String dpi, String nombre, String usuario, String contraseña, String tipoCliente, String automovil) {
        Cliente cliente = buscarClientePorDPI(dpi);
        if (cliente == null) return false;
        
        cliente.setNombreCompleto(nombre);
        cliente.setUsuario(usuario);
        cliente.setContraseña(contraseña);
        cliente.setTipoCliente(tipoCliente);
        cliente.setAutomovil(automovil);
        
        return guardarClientes();
    }

    public static boolean eliminarCliente(String dpi) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getDpi().equals(dpi)) {
                //ELIMINAR ARCHIVO DE AUTO ASOCIADOS
                new File(CARPETA_AUTOS + dpi + ".dat").delete();
                
                //MVER ELEMENTOS RESTANTES
                for (int j = i; j < contadorClientes - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                contadorClientes--;
                return guardarClientes();
            }
        }
        return false;
    }

    public static Cliente[] obtenerTodosClientes() {
        Cliente[] resultado = new Cliente[contadorClientes];
        System.arraycopy(clientes, 0, resultado, 0, contadorClientes);
        return resultado;
    }
}