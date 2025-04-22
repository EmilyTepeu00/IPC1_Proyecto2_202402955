package controlador;

import vista.MenuCVista;
import vista.InicioVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelo.InicioModelo;
import modelo.RegistroModelo;
import vista.RegistrarAutosVista;

public class MenuCControlador {
    private MenuCVista vista;
    private InicioVista inicioVista;
    private String usuarioActual;

    public MenuCControlador(MenuCVista vista, String usuario) {
        this.vista = vista;
        this.usuarioActual = usuario;
        mostrarBienvenida();
        configurarListeners();
    }
    
    private void mostrarBienvenida() {
        JOptionPane.showMessageDialog(vista, "BIENVENIDO " + usuarioActual, "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void configurarListeners() {
        vista.getBotonCerrar().addActionListener(this::cerrarSesion);
        vista.getBotonRegistrar().addActionListener(e -> abrirRegistroAutos());
        vista.getBotonAutos().addActionListener(e -> mostrarMensaje("Ver Autos"));
        vista.getBotonFacturas().addActionListener(e -> mostrarMensaje("Facturas"));
        vista.getBotonProgreso().addActionListener(e -> mostrarMensaje("Progreso"));
    }
    
    private void abrirRegistroAutos() {
        RegistrarAutosVista registrarAutos = new RegistrarAutosVista();
        new RegistrarAutosControlador(registrarAutos, vista, usuarioActual);
        vista.setVisible(false);
        registrarAutos.setVisible(true);
    }
    
    private void cerrarSesion(ActionEvent e) {
        int opcion = JOptionPane.showConfirmDialog(vista, "SEGURO QUE DESEA CERRAR SESION?", 
            "CONFIRMAR", JOptionPane.YES_NO_OPTION);
    
        if (opcion == JOptionPane.YES_OPTION) {
            InicioVista inicio = new InicioVista();
            RegistroModelo registro = RegistroModelo.getInstance();
            InicioModelo modelo = new InicioModelo(registro);
            new InicioControlador(inicio, modelo, registro);
            vista.dispose();
            inicio.setVisible(true);
        }
    }
    
    private void mostrarMensaje(String modulo) {
        JOptionPane.showMessageDialog(vista, "Módulo " + modulo + " en desarrollo", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}