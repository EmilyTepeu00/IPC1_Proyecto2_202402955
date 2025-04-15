package modelo;

import java.util.HashMap;
import java.util.Map;

public class RegistroModelo {
    private static RegistroModelo instancia;
    private Map<String, Cliente> clientes;
    private String usuarioActual;

    public RegistroModelo() {
        clientes = new HashMap<>();
        usuarioActual = "";
    }

    //OBTENER INSTANCIA UNICA
    public static synchronized RegistroModelo getInstance() {
        if (instancia == null) {
            instancia = new RegistroModelo();
        }
        return instancia;
    }

    //CLASE PARA ALMACENAR DATOS DEL CLIENTE
    private static class Cliente {
        String dpi;
        String nombreCompleto;
        String contrasena;
        String tipoCliente;
        int serviciosRealizados;

        public Cliente(String dpi, String nombreCompleto, String contrasena, String tipoCliente) {
            this.dpi = dpi;
            this.nombreCompleto = nombreCompleto;
            this.contrasena = contrasena;
            this.tipoCliente = tipoCliente;
            this.serviciosRealizados = 0;
        }
    }

    public boolean registrarCliente(String dpi, String nombreCompleto, String usuario, String contrasena, String tipoCliente) {
        if (clientes.containsKey(usuario)) {
            return false;
        }
        
        clientes.put(usuario, new Cliente(dpi, nombreCompleto, contrasena, tipoCliente));
        return true;
    }

    public boolean verificarCredenciales(String usuario, String contrasena) {
        Cliente cliente = clientes.get(usuario);
        if (cliente != null && cliente.contrasena.equals(contrasena)) {
            usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public boolean puedeCambiarAOro(String usuario) {
        Cliente cliente = clientes.get(usuario);
        return cliente != null && cliente.serviciosRealizados >= 4;
    }

    public void actualizarTipoCliente(String usuario, String nuevoTipo) {
        Cliente cliente = clientes.get(usuario);
        if (cliente != null) {
            cliente.tipoCliente = nuevoTipo;
        }
    }

    public void incrementarServicios(String usuario) {
        Cliente cliente = clientes.get(usuario);
        if (cliente != null) {
            cliente.serviciosRealizados++;
        }
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean existeUsuario(String usuario) {
        return clientes.containsKey(usuario);
    }
}