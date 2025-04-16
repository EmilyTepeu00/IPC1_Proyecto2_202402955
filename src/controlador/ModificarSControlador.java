package controlador;

import vista.ModificarSVista;
import modelo.ServiciosModelo;
import modelo.ServiciosModelo.Servicio;
import modelo.RepuestosModelo;
import modelo.RepuestosModelo.Repuesto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.ServiciosVista;

public class ModificarSControlador {
    private ModificarSVista vista;
    private Servicio servicioActual;
    
    public ModificarSControlador(ModificarSVista vista, int id) {
        this.vista = vista;
        this.servicioActual = ServiciosModelo.buscarServicio(id);
        
        //LLENAR CAMPOS CON LOS DATOS ACTUALES
        if (servicioActual != null) {
            vista.getCampoNombre().setText(servicioActual.getNombre());
            vista.getCampoMarca().setText(servicioActual.getMarca());
            vista.getCampoModelo().setText(servicioActual.getModelo());
            vista.getCampoPMano().setText(String.valueOf(servicioActual.getPrecioManoObra()));
            vista.getCampoPTotal().setText(String.valueOf(servicioActual.getPrecioTotal()));
            
            cargarRepuestos();
            
            //PARA SELECCIONAR EL PRIMER REPUESTO
            if (servicioActual.getContadorRepuestos() > 0) {
                Repuesto repuesto = servicioActual.getRepuestos()[0];
                vista.getListaRepuestos().setSelectedItem(repuesto.getId() + " - " + repuesto.getNombre());
            }
        }
        
        //LISTENERS
        vista.getBotonAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarServicio();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void cargarRepuestos() {
        Repuesto[] repuestos = RepuestosModelo.obtenerTodosRepuestos();
        vista.getListaRepuestos().removeAllItems();
        
        for (Repuesto repuesto : repuestos) {
            vista.getListaRepuestos().addItem(repuesto.getId() + " - " + repuesto.getNombre());
        }
    }
    
    private void modificarServicio() {
        try {
            String nombre = vista.getCampoNombre().getText();
            String marca = vista.getCampoMarca().getText();
            String modelo = vista.getCampoModelo().getText();
            double precioManoObra = Double.parseDouble(vista.getCampoPMano().getText());
            
            //OBTENER REPUESTO SELECCIONADO
            String repuestoSeleccionado = (String) vista.getListaRepuestos().getSelectedItem();
            int idRepuesto = Integer.parseInt(repuestoSeleccionado.split(" - ")[0]);
            
            //MODIFICAR SERVICIO
            boolean exito = ServiciosModelo.modificarServicio(
                servicioActual.getId(), 
                nombre, 
                marca, 
                modelo, 
                precioManoObra
            );
            
            //AGREGAR REPUESTO AL SERVICIO
            if (exito) {
                if (ServiciosModelo.agregarRepuestoAServicio(servicioActual.getId(), idRepuesto)) {
                    // Calcular precio total
                    double precioTotal = precioManoObra + RepuestosModelo.buscarRepuesto(idRepuesto).getPrecio();
                    vista.getCampoPTotal().setText(String.valueOf(precioTotal));
                    
                    JOptionPane.showMessageDialog(vista, "SERVICIO MODIFICADO CON EXITO");
                    regresar();
                } else {
                    JOptionPane.showMessageDialog(vista, "MARCA O MODELO NO COINCIDEN CON EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "ERROR AL MODIFICAR EL SERVICIO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresar() {
        ServiciosVista serviciosVista = new ServiciosVista();
        new ServiciosControlador(serviciosVista);
        serviciosVista.setVisible(true);
        vista.dispose();
    }
}