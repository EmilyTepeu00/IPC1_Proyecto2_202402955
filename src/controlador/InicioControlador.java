package controlador;

import modelo.InicioModelo;
import modelo.RegistroModelo;
import vista.InicioVista;
import vista.MenuAVista;
import vista.MenuCVista;
import vista.RegistroVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class InicioControlador {
    private InicioVista vista;
    private InicioModelo modelo;
    private RegistroModelo registroModelo;

    public InicioControlador(InicioVista vista, InicioModelo modelo, RegistroModelo registroModelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.registroModelo = registroModelo;
        
        vista.getBotonIniciar().addActionListener(this::iniciarSesion);
        vista.getBotonRegistro().addActionListener(e -> abrirRegistro());
    }
    
    private void iniciarSesion(ActionEvent e) {
        String usuario = vista.getCampoUsuario().getText().trim();
        String contrasena = new String(vista.getCampoContraseña().getPassword()).trim();
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String tipoUsuario = modelo.verificarTipoUsuario(usuario, contrasena);
        
        switch (tipoUsuario) {
            case "ADMIN":
                registroModelo.setUsuarioActual(usuario);
                abrirMenuAdministrador();
                break;
            case "CLIENTE":
                registroModelo.setUsuarioActual(usuario);
                abrirMenuCliente();
                break;
            default:
                JOptionPane.showMessageDialog(vista, "USUARIO O CONTRASEÑA INCORRECTOS", "ERROR", JOptionPane.ERROR_MESSAGE);
                //LIMPIAR CAMPOS
                vista.getCampoContraseña().setText("");
        }
    }
 
    private void abrirMenuAdministrador() {
        MenuAVista menuAVista = new MenuAVista();
        new MenuAControlador(menuAVista, registroModelo.getUsuarioActual());
        vista.dispose();
        menuAVista.setVisible(true);
    }
    
    private void abrirMenuCliente() {
        MenuCVista menuCVista = new MenuCVista();
        new MenuCControlador(menuCVista, registroModelo.getUsuarioActual());
        vista.dispose();
        menuCVista.setVisible(true);
    }
    
    private void abrirRegistro() {
        RegistroVista registroVista = new RegistroVista();
        new RegistroControlador(registroVista, registroModelo, vista);
        vista.dispose();
        registroVista.setVisible(true);
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}