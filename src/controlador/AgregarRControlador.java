package controlador;

import vista.AgregarRVista;
import modelo.RepuestosModelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.RepuestosVista;

public class AgregarRControlador {
    private AgregarRVista vista;
    
    public AgregarRControlador(AgregarRVista vista) {
        this.vista = vista;
        
        //ID INICIAL
        actualizarID();
        
        //IDENTIFICADOR UNICO
        vista.getCampoID().setText(String.valueOf(RepuestosModelo.getSiguienteId()));
        vista.getCampoID().setEditable(false);
        
        //LISTENERS
        vista.getBotonAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarRepuesto();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    //ACTUALIZAR ID
    private void actualizarID() {
        vista.getCampoID().setText(String.valueOf(RepuestosModelo.getSiguienteId()));
        vista.getCampoID().setEditable(false);
    }
    
    private void agregarRepuesto() {
        try {
            String nombre = vista.getCampoNombre().getText();
            String marca = vista.getCampoMarca().getText();
            String modelo = vista.getCampoModelo().getText();
            int existencias = Integer.parseInt(vista.getCampoExistencias().getText());
            double precio = Double.parseDouble(vista.getCampoPrecio().getText());
            
            int id = RepuestosModelo.agregarRepuesto(nombre, marca, modelo, existencias, precio);
            
            if (id != -1) {
                javax.swing.JOptionPane.showMessageDialog(vista, "REPUESTO AGREGADO CON ID: " + id);
                limpiarCampos();
                actualizarID();
            } else {
                javax.swing.JOptionPane.showMessageDialog(vista, "NO SE PUDO AGREGAR EL REPUESTO", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresar() {
        RepuestosVista repuestosVista = new RepuestosVista();
        new RepuestosControlador(repuestosVista);
        repuestosVista.setVisible(true);
        vista.dispose();
    }
    
    private void limpiarCampos() {
        vista.getCampoNombre().setText("");
        vista.getCampoMarca().setText("");
        vista.getCampoModelo().setText("");
        vista.getCampoExistencias().setText("");
        vista.getCampoPrecio().setText("");
    }
}