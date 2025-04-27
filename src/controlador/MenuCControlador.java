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
import modelo.BitacoraModelo;

public class MenuCControlador {
    private MenuCVista vista;
    private InicioVista inicioVista;
    private String usuarioActual;

    public MenuCControlador(MenuCVista vista, String usuario) {
        this.vista = vista;
        this.usuarioActual = usuario;
        mostrarBienvenida();
        configurarListeners();
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
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
        BitacoraModelo.registrarEvento(usuarioActual, "Registro de Autos", "Éxito", "Se abrió el registro de autos");
        RegistrarAutosVista registrarAutos = new RegistrarAutosVista();
        new RegistrarAutosControlador(registrarAutos, vista, usuarioActual);
        vista.setVisible(false);
        registrarAutos.setVisible(true);
    }
    
    private void abrirVerAutos() {
        BitacoraModelo.registrarEvento(usuarioActual, "Ver Autos", "Éxito", "Se abrió la ventana para ver autos");
        VerAutosVista verAutos = new VerAutosVista();
        new VerAutosControlador(verAutos, vista, usuarioActual);
        vista.setVisible(false);
        verAutos.setVisible(true);
    }
    
    private void abrirProgreso(ActionEvent e) {
        BitacoraModelo.registrarEvento(usuarioActual, "Progreso", "Éxito", "Se abrió la ventana para el progreso");
        ProgresoVista progresoVista = new ProgresoVista();
        new ProgresoControlador(progresoVista, usuarioActual);
        vista.setVisible(false);
        progresoVista.setVisible(true);
    }
    
    private void cerrarSesion(ActionEvent e) {
        int opcion = JOptionPane.showConfirmDialog(vista, "SEGURO QUE DESEA CERRAR SESION?", 
            "CONFIRMAR", JOptionPane.YES_NO_OPTION);
    
        if (opcion == JOptionPane.YES_OPTION) {
            BitacoraModelo.registrarEvento(usuarioActual, "Cierre de sesión", "Éxito", "Sesión finalizada por el usuario");
            InicioVista inicio = new InicioVista();
            RegistroModelo registro = RegistroModelo.getInstance();
            InicioModelo modelo = new InicioModelo(registro); 
            new InicioControlador(inicio, modelo, registro);
            vista.dispose();
            inicio.setVisible(true);
        } else {
            BitacoraModelo.registrarEvento(usuarioActual, "Intento de cierre de sesión", "Cancelado", "Usuario canceló el cierre de sesión");
        }
    }
    
    private void mostrarMensaje(String modulo) {
        JOptionPane.showMessageDialog(vista, "Módulo " + modulo + " en desarrollo", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}