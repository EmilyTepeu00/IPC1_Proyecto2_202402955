package controlador;

import vista.ModificarRVista;
import modelo.RepuestosModelo;
import modelo.RepuestosModelo.Repuesto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.RepuestosVista;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class ModificarRControlador {
    private ModificarRVista vista;
    private Repuesto repuestoActual;
    private String usuarioActual;
    
    public ModificarRControlador(ModificarRVista vista) {
        this.vista = vista;
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
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
        BitacoraModelo.registrarEvento(usuarioActual, "ID del repuesto", "Información", "Se pide el ID del repuesto");
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
                    BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado no existe");
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                    regresar();
                }
            } catch (NumberFormatException e) {
                BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El DI ingresado es inválido");
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
                BitacoraModelo.registrarEvento(usuarioActual, "Modificación de Repuesto", "Éxito", "Se modificó el repuesto");
                JOptionPane.showMessageDialog(vista, "REPUESTO MODIFICADO CORRECTAMENTE");
                regresar();
            } else {
                BitacoraModelo.registrarEvento(usuarioActual, "Modificación de Repuesto", "Error", "No se pudo modificar el repuesto");
                JOptionPane.showMessageDialog(vista, "ERROR AL MODIFICAR EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            BitacoraModelo.registrarEvento(usuarioActual, "Validación de Datos", "Error", "Los datos ingresados son inválidos");
            JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Modificar Repuestos", "Éxito", "Se cerró la ventana de modificar repuestos");
        RepuestosVista repuestosVista = new RepuestosVista();
        new RepuestosControlador(repuestosVista);
        repuestosVista.setVisible(true);
        vista.dispose();
    }
}