package controlador;

import vista.MenuAVista;
import vista.InicioVista;
import vista.RepuestosVista;
import vista.ServiciosVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelo.InicioModelo;
import modelo.RegistroModelo;
import vista.ClientesAutosVista;
import modelo.BitacoraModelo;

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
        String tipo = usuarioActual.equals(InicioModelo.ADMIN_USER) ? "ADMINISTRADOR" : "MECANICO";
        JOptionPane.showMessageDialog(vista, "BIENVENIDO " + tipo, "BIENVENIDO", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void configurarListeners() {
        vista.getBotonCerrar().addActionListener(this::cerrarSesion);
        vista.getBotonRepuestos().addActionListener(this::abrirRepuestos);
        vista.getBotonServicios().addActionListener(e -> abrirServicios());
        vista.getBotonClientes().addActionListener(e -> abrirClientesAutos());
        vista.getBotonProgreso().addActionListener(e -> mostrarMensaje("Progreso"));
        vista.getBotonReporte().addActionListener(e -> mostrarMensaje("Reportes"));
    }
    
    private void abrirRepuestos(ActionEvent e) {
        BitacoraModelo.registrarEvento(usuarioActual, "Abrir Repuestos", "Éxito", "Se abrió los repuestos");
        RepuestosVista repuestosVista = new RepuestosVista();
        new RepuestosControlador(repuestosVista);
        repuestosVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirServicios() {
        BitacoraModelo.registrarEvento(usuarioActual, "Abrir Servicios", "Éxito", "Se abró los sevicios");
        ServiciosVista serviciosVista = new ServiciosVista();
        new ServiciosControlador(serviciosVista);
        serviciosVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirClientesAutos() {
        BitacoraModelo.registrarEvento(usuarioActual, "Abrir Clientes y Autos", "Éxito", "Se abrió clientes y autos");
        ClientesAutosVista clientesAutosVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesAutosVista);
        clientesAutosVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void cerrarSesion(ActionEvent e) {
        int opcion = JOptionPane.showConfirmDialog(vista, "SEGURO QUE DESEA CERRAR SESION?", "Confirmar", JOptionPane.YES_NO_OPTION);
    
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