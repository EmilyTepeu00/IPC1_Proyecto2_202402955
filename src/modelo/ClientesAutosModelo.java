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
    private static final String CARPETA_AUTOS = "datos_autos/";
    private static final String CARPETA_CLIENTES = "datos_clientes/";

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

        //GETTERS
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

    static {
        new File(CARPETA_AUTOS).mkdirs();
        new File(CARPETA_CLIENTES).mkdirs();
        cargarClientes();
    }

    private static void cargarClientes() {
        File carpetaClientes = new File(CARPETA_CLIENTES);
        File[] archivosClientes = carpetaClientes.listFiles();
        
        if (archivosClientes != null) {
            for (File archivo : archivosClientes) {
                if (contadorClientes >= MAX_CLIENTES) break;
                
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    String dpi = "";
                    String nombre = "";
                    String usuario = "";
                    String contraseña = "";
                    String tipoCliente = "";
                    String automovil = "";
                    
                    while ((linea = reader.readLine()) != null) {
                        if (linea.startsWith("DPI: ")) dpi = linea.substring(5);
                        else if (linea.startsWith("Nombre: ")) nombre = linea.substring(8);
                        else if (linea.startsWith("Usuario: ")) usuario = linea.substring(9);
                        else if (linea.startsWith("Contraseña: ")) contraseña = linea.substring(12);
                        else if (linea.startsWith("TipoCliente: ")) tipoCliente = linea.substring(13);
                    }
                    
                    final String usuarioFinal = usuario;
                    File carpetaAutos = new File(CARPETA_AUTOS);
                    File[] archivosAutos = carpetaAutos.listFiles(new java.io.FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.startsWith(usuarioFinal + "_");
                        }
                    });
                    
                    if (archivosAutos != null && archivosAutos.length > 0) {
                        try (BufferedReader autoReader = new BufferedReader(new FileReader(archivosAutos[0]))) {
                            String lineaAuto;
                            while ((lineaAuto = autoReader.readLine()) != null) {
                                if (lineaAuto.startsWith("Placa: ")) {
                                    automovil = "Placa: " + lineaAuto.substring(7);
                                } else if (lineaAuto.startsWith("Marca: ")) {
                                    automovil += ", Marca: " + lineaAuto.substring(7);
                                } else if (lineaAuto.startsWith("Modelo: ")) {
                                    automovil += ", Modelo: " + lineaAuto.substring(8);
                                }
                            }
                        }
                    }
                    
                    clientes[contadorClientes++] = new Cliente(dpi, nombre, usuario, contraseña, tipoCliente, automovil);
                } catch (IOException e) {
                    System.err.println("ERROR AL LEER EL ARCHIVO: " + e.getMessage());
                }
            }
        }
    }

    public static boolean guardarClientes() {
        return true;
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
        
        // Guardar en archivo
        try {
            File archivoCliente = new File(CARPETA_CLIENTES + usuario + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCliente))) {
                writer.write("DPI: " + dpi + "\n");
                writer.write("Nombre: " + nombre + "\n");
                writer.write("Usuario: " + usuario + "\n");
                writer.write("Contraseña: " + contraseña + "\n");
                writer.write("TipoCliente: " + tipoCliente + "\n");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean modificarCliente(String dpi, String nombre, String usuario, String contraseña, String tipoCliente, String automovil) {
        Cliente cliente = buscarClientePorDPI(dpi);
        if (cliente == null) return false;
        
        cliente.setNombreCompleto(nombre);
        cliente.setUsuario(usuario);
        cliente.setContraseña(contraseña);
        cliente.setTipoCliente(tipoCliente);
        cliente.setAutomovil(automovil);
        
        //ACTUALIZAR ARCHIVO
        try {
            File archivoCliente = new File(CARPETA_CLIENTES + cliente.getUsuario() + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCliente))) {
                writer.write("DPI: " + dpi + "\n");
                writer.write("Nombre: " + nombre + "\n");
                writer.write("Usuario: " + usuario + "\n");
                writer.write("Contraseña: " + contraseña + "\n");
                writer.write("TipoCliente: " + tipoCliente + "\n");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean eliminarCliente(String dpi) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getDpi().equals(dpi)) {
                //ELIMINAR ARCHIVOS ASOCIADOS
                String usuario = clientes[i].getUsuario();
                new File(CARPETA_CLIENTES + usuario + ".txt").delete();
                
                //ELIMINAR AUTOS ASOCIADOS
                File carpetaAutos = new File(CARPETA_AUTOS);
                File[] archivosAutos = carpetaAutos.listFiles((dir, name) -> name.startsWith(usuario + "_"));
                if (archivosAutos != null) {
                    for (File archivoAuto : archivosAutos) {
                        archivoAuto.delete();
                    }
                }
                
                //MOVER ELEMENTOS RESTANTES
                for (int j = i; j < contadorClientes - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                contadorClientes--;
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
    
    public static String[] obtenerDatosAuto(String usuario, String placa) {
        final String usuarioFinal = usuario; 
        File archivoAuto = new File(CARPETA_AUTOS + usuarioFinal + "_" + placa + ".txt");
        if (!archivoAuto.exists()) return null;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoAuto))) {
            String[] datos = new String[4];
            String linea;
            
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("Placa: ")) datos[0] = linea.substring(7);
                else if (linea.startsWith("Marca: ")) datos[1] = linea.substring(7);
                else if (linea.startsWith("Modelo: ")) datos[2] = linea.substring(8);
                else if (linea.startsWith("Imagen: ")) datos[3] = linea.substring(8);
            }
            
            return datos;
        } catch (IOException e) {
            return null;
        }
    }
}