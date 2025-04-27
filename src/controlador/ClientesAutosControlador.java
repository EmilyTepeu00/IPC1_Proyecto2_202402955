package controlador;

import vista.ClientesAutosVista;
import vista.AgregarCAVista;
import vista.ModificarCAVista;
import vista.VerCAVista;
import modelo.ClientesAutosModelo;
import javax.swing.JOptionPane;
import vista.MenuAVista;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class ClientesAutosControlador {
    private ClientesAutosVista vista;
    private String usuarioActual;
    
    public ClientesAutosControlador(ClientesAutosVista vista) {
        this.vista = vista;
        configurarListeners();
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
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
        BitacoraModelo.registrarEvento(usuarioActual, "Modificar DPI", "Información", "Se pide el DPI del cliente a modificar");
        String dpi = JOptionPane.showInputDialog(vista, "INGRESE EL DPI DEL CLIENTE A MODIFICAR: ");
        if (dpi != null && !dpi.isEmpty()) {
            if (ClientesAutosModelo.buscarClientePorDPI(dpi) != null) {
                ModificarCAVista modificarVista = new ModificarCAVista();
                new ModificarCAControlador(modificarVista, dpi);
                modificarVista.setVisible(true);
                vista.dispose();
            } else {
                BitacoraModelo.registrarEvento(usuarioActual, "DPI Cliente", "Error", "El DPI ingresado no existe");
                JOptionPane.showMessageDialog(vista, "EL DPI INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarClienteAuto() {
        BitacoraModelo.registrarEvento(usuarioActual, "Eliminar DPI", "Información", "Se pide el DPI del cliente a eliminar");
        String dpi = JOptionPane.showInputDialog(vista, "INGRESE EL DPI DEL CLIENTE A ELIMINAR:");
        if (dpi != null && !dpi.isEmpty()) {
            if (ClientesAutosModelo.eliminarCliente(dpi)) {
                BitacoraModelo.registrarEvento(usuarioActual, "Eliminar DPI", "Éxito", "Cliente eliminado con éxito");
                JOptionPane.showMessageDialog(vista, "CLIENTE ELIMINADO CON EXITO");
            } else {
                BitacoraModelo.registrarEvento(usuarioActual, "DPI Cliente", "Error", "El DPI ingresado no existe");
                JOptionPane.showMessageDialog(vista, "EL DPI INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVerClientesAutos() {
        BitacoraModelo.registrarEvento(usuarioActual, "Abrir Clientes y Autos", "Éxito", "Se cabrió la ventana de clientes y autos");
        VerCAVista verVista = new VerCAVista();
        new VerCAControlador(verVista);
        verVista.setVisible(true);
        vista.dispose();
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Agregar Cliente/Auto", "Éxito", "Se cerró la ventana de agregar cliente y autos");
        MenuAVista menuVista = new MenuAVista();
        new MenuAControlador(menuVista, "admin");
        menuVista.setVisible(true);
        vista.dispose();
    }
}