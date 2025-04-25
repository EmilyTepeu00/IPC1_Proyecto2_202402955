package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InicioModelo {
    public static final String ADMIN_USER = "adminipc1f";
    public static final String ADMIN_PASS = "adminipc1f";
    public static final String MECANICO_USER = "mecanicoipc1f";
    public static final String MECANICO_PASS = "mecanicoipc1f";

    private RegistroModelo registroModelo;

    public InicioModelo(RegistroModelo registroModelo) {
        this.registroModelo = registroModelo;
    }

    //VERIFICAR TIPO DE USUARIO SEGUN LAS CREDNCIALES
    public String verificarTipoUsuario(String usuario, String contrasena) {
        //VERIFICAR SI ES ADMINISTRADOR
        if (ADMIN_USER.equals(usuario)) {
            return ADMIN_PASS.equals(contrasena) ? "ADMIN" : "ERROR";
        }
        
        //VERIFICAR SI ES MECANICO
        if (MECANICO_USER.equals(usuario)) {
            return MECANICO_PASS.equals(contrasena) ? "MECANICO" : "ERROR";
        }
        
        //VERIFICAR SI ES CLIENTE (REGISTRO)
        if (registroModelo.verificarCredenciales(usuario, contrasena)) {
            return "CLIENTE";
        }
        
        //VERIFICAR SI ES (AGREGADO POR ADMIN)
        if (ClientesAutosModelo.verificarCredenciales(usuario, contrasena)) {
            return "CLIENTE";
        }
        
        return "ERROR";
    }

    //OBTENER DPI ASOCIADO AL USUARIO
    public String obtenerDpiCliente(String usuario) {
        //BUSCAR EN EL REGISTRO
        String dpi = registroModelo.getDpiCliente(usuario);
        if (dpi != null) {
            return dpi;
        }
        
        //BUSCAR EN AgregarCAVista
        ClientesAutosModelo.Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuario);
        if (cliente != null) {
            return cliente.getDpi();
        }
        
        return null;
    }

    //VERIFICAR SI EL USUARIO YA EXISTE
    public boolean existeUsuario(String usuario) {
        //VERIFICAR EN USUARIOS DEL SISTEMA
        if (ADMIN_USER.equals(usuario) || MECANICO_USER.equals(usuario)) {
            return true;
        }
        
        //VERIFICAR EN EL REGISTRO
        if (registroModelo.existeUsuario(usuario)) {
            return true;
        }
        
        //VERIFICAR EN AgregarCAVista
        return ClientesAutosModelo.buscarClientePorUsuario(usuario) != null;
    }
}