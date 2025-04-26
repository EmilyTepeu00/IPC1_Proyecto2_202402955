package controlador;

import vista.MenuCVista;
import vista.InicioVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelo.InicioModelo;
import modelo.RegistroModelo;
import vista.RegistrarAutosVista;
import vista.VerAutosVista;
import vista.ProgresoVista;

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
        vista.getBotonAutos().addActionListener(e -> abrirVerAutos());
        vista.getBotonFacturas().addActionListener(e -> mostrarMensaje("Facturas"));
        vista.getBotonProgreso().addActionListener(this::abrirProgreso);
    }
    
    private void abrirRegistroAutos() {
        RegistrarAutosVista registrarAutos = new RegistrarAutosVista();
        new RegistrarAutosControlador(registrarAutos, vista, usuarioActual);
        vista.setVisible(false);
        registrarAutos.setVisible(true);
    }
    
    private void abrirVerAutos() {
        VerAutosVista verAutos = new VerAutosVista();
        new VerAutosControlador(verAutos, vista, usuarioActual);
        vista.setVisible(false);
        verAutos.setVisible(true);
    }
    
    private void abrirProgreso(ActionEvent e) {
        ProgresoVista progresoVista = new ProgresoVista();
        new ProgresoControlador(progresoVista, usuarioActual);
        vista.setVisible(false);
        progresoVista.setVisible(true);
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