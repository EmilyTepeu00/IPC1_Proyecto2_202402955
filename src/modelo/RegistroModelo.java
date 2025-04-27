package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistroModelo {
    private static RegistroModelo instancia;
    private Map<String, Cliente> clientes;
    private String usuarioActual;
    private static final String CARPETA_CLIENTES = "datos_clientes";

    public RegistroModelo() {
        clientes = new HashMap<>();
        usuarioActual = "";
        new File(CARPETA_CLIENTES).mkdirs();
    }

    public static synchronized RegistroModelo getInstance() {
        if (instancia == null) {
            instancia = new RegistroModelo();
        }
        return instancia;
    }

    public static class Cliente {
        String dpi;
        String nombreCompleto;
        String contrasena;
        String tipoCliente;
        String automovil;

        public Cliente(String dpi, String nombreCompleto, String contrasena, String tipoCliente) {
            this.dpi = dpi;
            this.nombreCompleto = nombreCompleto;
            this.contrasena = contrasena;
            this.tipoCliente = tipoCliente;
        }
        
        public String getTipoCliente() {
            return tipoCliente;
        }
    
        public void setTipoCliente(String tipoCliente) {
            this.tipoCliente = tipoCliente;
        }
    
        public String getAutomovil() {
            return automovil;
        }
    
        public void setAutomovil(String automovil) {
            this.automovil = automovil;
        }
    }

    public boolean registrarCliente(String dpi, String nombreCompleto, String usuario, String contrasena, String tipoCliente) {
        if (existeUsuario(usuario)) {
            return false;
        }
        
        try {
            File archivoCliente = new File(CARPETA_CLIENTES, usuario + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCliente))) {
                writer.write("DPI: " + dpi + "\n");
                writer.write("Nombre: " + nombreCompleto + "\n");
                writer.write("Usuario: " + usuario + "\n");
                writer.write("Contraseña: " + contrasena + "\n");
                writer.write("TipoCliente: " + tipoCliente + "\n");
            }
            clientes.put(usuario, new Cliente(dpi, nombreCompleto, contrasena, tipoCliente));
            return true;
        } catch (IOException e) {
            System.err.println("ERROR AL REGISTRAR AL CLIENTE: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarCredenciales(String usuario, String contrasena) {
        Cliente cliente = clientes.get(usuario);
        if (cliente != null && cliente.contrasena.equals(contrasena)) {
            usuarioActual = usuario;
            return true;
        }
        
        return verificarCredencialesEnArchivo(usuario, contrasena);
    }
    
    private boolean verificarCredencialesEnArchivo(String usuario, String contrasena) {
        File archivoCliente = new File(CARPETA_CLIENTES, usuario + ".txt");
        if (!archivoCliente.exists()) {
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCliente))) {
            String linea;
            String contraseñaGuardada = null;
            String usuarioGuardado = null;
            
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("Usuario: ")) {
                    usuarioGuardado = linea.substring("Usuario: ".length()).trim();
                } else if (linea.startsWith("Contraseña: ")) {
                    contraseñaGuardada = linea.substring("Contraseña: ".length()).trim();
                }
            }
            
            if (usuario.equals(usuarioGuardado) && contrasena.equals(contraseñaGuardada)) {
                usuarioActual = usuario;
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("ERROR AL VERIFICAR CREDENCIALES: " + e.getMessage());
            return false;
        }
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean existeUsuario(String usuario) {
        return clientes.containsKey(usuario) || new File(CARPETA_CLIENTES, usuario + ".txt").exists();
    }
    
    public String getDpiCliente(String usuario) {
        Cliente cliente = clientes.get(usuario);
        if (cliente != null) {
            return cliente.dpi;
        }
        
        File archivoCliente = new File(CARPETA_CLIENTES, usuario + ".txt");
        if (archivoCliente.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoCliente))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("DPI: ")) {
                        return linea.substring("DPI: ".length()).trim();
                    }
                }
            } catch (IOException e) {
                System.err.println("ERROR AL OBTENER EL DPI: " + e.getMessage());
            }
        }
        return null;
    }
}