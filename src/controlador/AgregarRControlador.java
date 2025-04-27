package controlador;

import vista.AgregarRVista;
import modelo.RepuestosModelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.RepuestosVista;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class AgregarRControlador {
    private AgregarRVista vista;
    private String usuarioActual;
    
    public AgregarRControlador(AgregarRVista vista) {
        this.vista = vista;
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
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
                BitacoraModelo.registrarEvento(usuarioActual, "Repuesto Agregado", "Éxito",  "Repuesto agregado con ID: " + id) ;
                javax.swing.JOptionPane.showMessageDialog(vista, "REPUESTO AGREGADO CON ID: " + id);
                limpiarCampos();
                actualizarID();
            } else {
                BitacoraModelo.registrarEvento(usuarioActual, "No se agregó el repuesto", "Error", "No se puedo agregar el repuesto");
                javax.swing.JOptionPane.showMessageDialog(vista, "NO SE PUDO AGREGAR EL REPUESTO", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            BitacoraModelo.registrarEvento(usuarioActual, "Validación de datos", "Error", "Datos invalidos");
            javax.swing.JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Agregar Repuesto", "Éxito", "Se cerró la ventana de agregar repuestos");
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