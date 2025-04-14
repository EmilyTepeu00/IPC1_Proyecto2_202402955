package controlador;

import vista.MenuAVista;
import vista.InicioVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelo.InicioModelo;
import modelo.RegistroModelo;

public class MenuAControlador {
    private MenuAVista vista;
    private InicioVista inicioVista;
    private String usuarioActual;

    public MenuAControlador(MenuAVista vista, String usuario) {
        this.vista = vista;
        this.usuarioActual = usuario;
        configurarBienvenida();
        configurarListeners();
    }
    
    private void configurarBienvenida() {
        String tipo = usuarioActual.equals(InicioModelo.ADMIN_USER) ? "Administrador" : "Mecánico";
        JOptionPane.showMessageDialog(vista, "BIENVENIDO " + tipo, "BIENVENIDO", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void configurarListeners() {
        vista.getBotonCerrar().addActionListener(this::cerrarSesion);
        vista.getBotonRepuestos().addActionListener(e -> mostrarMensaje("Repuestos"));
        vista.getBotonServicios().addActionListener(e -> mostrarMensaje("Servicios"));
        vista.getBotonClientes().addActionListener(e -> mostrarMensaje("Clientes"));
        vista.getBotonProgreso().addActionListener(e -> mostrarMensaje("Progreso"));
        vista.getBotonReporte().addActionListener(e -> mostrarMensaje("Reportes"));
    }
    
    private void cerrarSesion(ActionEvent e) {
        int opcion = JOptionPane.showConfirmDialog(vista, "SEGURO QUE DESEA CERRAR SESION?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            InicioVista inicio = new InicioVista();
            InicioModelo modelo = new InicioModelo(new RegistroModelo());
            new InicioControlador(inicio, modelo, new RegistroModelo());
            vista.dispose();
            inicio.setVisible(true);
        }
    }
    
    private void mostrarMensaje(String modulo) {
        JOptionPane.showMessageDialog(vista, "Módulo " + modulo + " en desarrollo", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}