package controlador;

import vista.ClientesAutosVista;
import vista.AgregarCAVista;
import vista.ModificarCAVista;
import vista.VerCAVista;
import modelo.ClientesAutosModelo;
import javax.swing.JOptionPane;
import vista.MenuAVista;

public class ClientesAutosControlador {
    private ClientesAutosVista vista;
    
    public ClientesAutosControlador(ClientesAutosVista vista) {
        this.vista = vista;
        configurarListeners();
    }

    private void configurarListeners() {
        vista.getBotonAgregar().addActionListener(e -> abrirAgregarClienteAuto());
        vista.getBotonModificar().addActionListener(e -> abrirModificarClienteAuto());
        vista.getBotonEliminar().addActionListener(e -> eliminarClienteAuto());
        vista.getBotonVer().addActionListener(e -> abrirVerClientesAutos());
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    private void abrirAgregarClienteAuto() {
        AgregarCAVista agregarVista = new AgregarCAVista();
        new AgregarCAControlador(agregarVista);
        agregarVista.setVisible(true);
        vista.dispose();
    }
    
    private void abrirModificarClienteAuto() {
        String dpi = JOptionPane.showInputDialog(vista, "INGRESE EL DPI DEL CLIENTE A MODIFICAR: ");
        if (dpi != null && !dpi.isEmpty()) {
            if (ClientesAutosModelo.buscarClientePorDPI(dpi) != null) {
                ModificarCAVista modificarVista = new ModificarCAVista();
                new ModificarCAControlador(modificarVista, dpi);
                modificarVista.setVisible(true);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista, "EL DPI INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarClienteAuto() {
        String dpi = JOptionPane.showInputDialog(vista, "INGRESE EL DPI DEL CLIENTE A ELIMINAR:");
        if (dpi != null && !dpi.isEmpty()) {
            if (ClientesAutosModelo.eliminarCliente(dpi)) {
                JOptionPane.showMessageDialog(vista, "CLIENTE ELIMINADO CON EXITO");
            } else {
                JOptionPane.showMessageDialog(vista, "EL DPI INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVerClientesAutos() {
        VerCAVista verVista = new VerCAVista();
        new VerCAControlador(verVista);
        verVista.setVisible(true);
        vista.dispose();
    }
    
    private void regresar() {
        MenuAVista menuVista = new MenuAVista();
        new MenuAControlador(menuVista, "admin");
        menuVista.setVisible(true);
        vista.dispose();
    }
}