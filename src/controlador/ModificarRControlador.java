package controlador;

import vista.ModificarRVista;
import modelo.RepuestosModelo;
import modelo.RepuestosModelo.Repuesto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.RepuestosVista;

public class ModificarRControlador {
    private ModificarRVista vista;
    private Repuesto repuestoActual;
    
    public ModificarRControlador(ModificarRVista vista) {
        this.vista = vista;
        
        //PEDIR ID
        pedirIdRepuesto();
        
        //LISTENERS
        vista.getBotonAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarRepuesto();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void pedirIdRepuesto() {
        String idStr = JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL REPUESTO:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                repuestoActual = RepuestosModelo.buscarRepuesto(id);
                
                if (repuestoActual != null) {
                    //LLENAR CAMPOS CON DATOS ACTUALIZADOS
                    vista.getCampoNombre().setText(repuestoActual.getNombre());
                    vista.getCampoMarca().setText(repuestoActual.getMarca());
                    vista.getCampoModelo().setText(repuestoActual.getModelo());
                    vista.getCampoExistencias().setText(String.valueOf(repuestoActual.getExistencias()));
                    vista.getCampoPrecio().setText(String.valueOf(repuestoActual.getPrecio()));
                } else {
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                    regresar();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
                regresar();
            }
        } else {
            regresar();
        }
    }
    
    private void modificarRepuesto() {
        try {
            String nombre = vista.getCampoNombre().getText();
            String marca = vista.getCampoMarca().getText();
            String modelo = vista.getCampoModelo().getText();
            int existencias = Integer.parseInt(vista.getCampoExistencias().getText());
            double precio = Double.parseDouble(vista.getCampoPrecio().getText());
            
            boolean exito = RepuestosModelo.modificarRepuesto(
                repuestoActual.getId(), 
                nombre, 
                marca, 
                modelo, 
                existencias, 
                precio
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(vista, "REPUESTO MODIFICADO CORRECTAMENTE");
                regresar();
            } else {
                JOptionPane.showMessageDialog(vista, "ERROR AL MODIFICAR EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresar() {
        RepuestosVista repuestosVista = new RepuestosVista();
        new RepuestosControlador(repuestosVista);
        repuestosVista.setVisible(true);
        vista.dispose();
    }
}