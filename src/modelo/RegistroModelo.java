package modelo;

import java.util.Vector;

public class RegistroModelo {
    private Vector<String> dpiClientes;
    private Vector<String> nombresCompletos;
    private Vector<String> nombresUsuario;
    private Vector<String> contrasenas;
    private Vector<String> tiposCliente;
    private Vector<Integer> serviciosRealizados;
    private String usuarioActual;

    public RegistroModelo() {
        dpiClientes = new Vector<>();
        nombresCompletos = new Vector<>();
        nombresUsuario = new Vector<>();
        contrasenas = new Vector<>();
        tiposCliente = new Vector<>();
        serviciosRealizados = new Vector<>();
        usuarioActual = "";
    }

    public boolean registrarCliente(String dpi, String nombreCompleto, String usuario, String contrasena, String tipoCliente) {
        //VERIFICAR SI EL USUARIO Y EXISTE
        if (nombresUsuario.contains(usuario)) {
            return false;
        }
        
        //GUARDAR DATOS INGRESADOS
        dpiClientes.add(dpi);
        nombresCompletos.add(nombreCompleto);
        nombresUsuario.add(usuario);
        contrasenas.add(contrasena); 
        tiposCliente.add(tipoCliente);
        serviciosRealizados.add(0);
        return true;
    }

    public boolean verificarCredenciales(String usuario, String contrasena) {
        //BUSCAR USUARIO
        int index = nombresUsuario.indexOf(usuario);
        
        //VERIFICAR CREDENCIALES
        if (index != -1 && contrasenas.get(index).equals(contrasena)) {
            usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public boolean puedeCambiarAOro(String usuario) {
        int index = nombresUsuario.indexOf(usuario);
        return index != -1 && serviciosRealizados.get(index) >= 4;
    }

    public void actualizarTipoCliente(String usuario, String nuevoTipo) {
        int index = nombresUsuario.indexOf(usuario);
        if (index != -1) {
            tiposCliente.set(index, nuevoTipo);
        }
    }

    public void incrementarServicios(String usuario) {
        int index = nombresUsuario.indexOf(usuario);
        if (index != -1) {
            serviciosRealizados.set(index, serviciosRealizados.get(index) + 1);
        }
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean existeUsuario(String usuario) {
        return nombresUsuario.contains(usuario);
    }
}