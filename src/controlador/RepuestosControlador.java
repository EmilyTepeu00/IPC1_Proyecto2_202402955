package controlador;

import vista.*;
import modelo.RepuestosModelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepuestosControlador {
    private RepuestosVista vista;
    
    public RepuestosControlador(RepuestosVista vista) {
        this.vista = vista;
        
        //LISTENERS
        vista.getBotonAgregar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirAgregarRepuesto();
            }
        });
        
        vista.getBotonModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirModificarRepuesto();
            }
        });
        
        vista.getBotonEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirEliminarRepuesto();
            }
        });
        
        vista.getBotonVer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVerRepuestos();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MenuAVista menuVista = new MenuAVista();
            new MenuAControlador(menuVista, "admin"); 
            menuVista.setVisible(true);
            vista.dispose();
        }
    });
    }
    
    private void abrirAgregarRepuesto() {
        AgregarRVista agregarVista = new AgregarRVista();
        new AgregarRControlador(agregarVista);
        agregarVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirModificarRepuesto() {
        ModificarRVista modificarVista = new ModificarRVista();
        new ModificarRControlador(modificarVista);
        modificarVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirEliminarRepuesto() {
        String idStr = javax.swing.JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL REPUESTO: ");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (RepuestosModelo.eliminarRepuesto(id)) {
                    javax.swing.JOptionPane.showMessageDialog(vista, "REPUESTO ELIMINADO CORRECTAMENTE");
                } else {
                    javax.swing.JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVerRepuestos() {
        VerRVista verVista = new VerRVista();
        new VerRControlador(verVista);
        verVista.setVisible(true);
        vista.setVisible(false);
    }
}