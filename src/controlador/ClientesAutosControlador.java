package controlador;

import vista.ClientesAutosVista;
import vista.AgregarCAVista;
import vista.ModificarCAVista;
import vista.VerCAVista;
import modelo.ClientesAutosModelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.MenuAVista;

public class ClientesAutosControlador {
    private ClientesAutosVista vista;
    
    public ClientesAutosControlador(ClientesAutosVista vista) {
        this.vista = vista;
        
        //LISTENERS
        vista.getBotonAgregar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirAgregarClienteAuto();
            }
        });
        
        vista.getBotonModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirModificarClienteAuto();
            }
        });
        
        vista.getBotonEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarClienteAuto();
            }
        });
        
        vista.getBotonVer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVerClientesAutos();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void abrirAgregarClienteAuto() {
        AgregarCAVista agregarVista = new AgregarCAVista();
        new AgregarCAControlador(agregarVista);
        agregarVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirModificarClienteAuto() {
        String idStr = JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL CLIENTE:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (ClientesAutosModelo.buscarCliente(id) != null) {
                    ModificarCAVista modificarVista = new ModificarCAVista();
                    new ModificarCAControlador(modificarVista, id);
                    modificarVista.setVisible(true);
                    vista.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarClienteAuto() {
        String idStr = JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL CLIENTE:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (ClientesAutosModelo.eliminarCliente(id)) {
                    JOptionPane.showMessageDialog(vista, "CLIENTE ELIMINADO CORRECTAMENTE");
                } else {
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVerClientesAutos() {
        VerCAVista verVista = new VerCAVista();
        new VerCAControlador(verVista);
        verVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void regresar() {
        MenuAVista menuVista = new MenuAVista();
        new MenuAControlador(menuVista, "admin");
        menuVista.setVisible(true);
        vista.dispose();
    }
}