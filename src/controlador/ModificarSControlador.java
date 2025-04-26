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
        
        if (servicioActual == null) {
            JOptionPane.showMessageDialog(vista, "EL SERVICIO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            regresar();
            return;
        }
        
        // LLENAR CAMPOS CON LOS DATOS ACTUALES
        vista.getCampoID().setText(String.valueOf(servicioActual.getId()));
        vista.getCampoNombre().setText(servicioActual.getNombre());
        vista.getCampoMarca().setText(servicioActual.getMarca());
        vista.getCampoModelo().setText(servicioActual.getModelo());
        vista.getCampoPMano().setText(String.valueOf(servicioActual.getPrecioManoObra()));
        vista.getCampoPTotal().setText(String.valueOf(servicioActual.getPrecioTotal()));
        
        cargarRepuestos();
        
        // SELECCIONAR EL REPUESTO ACTUAL SI EXISTE
        if (servicioActual.getContadorRepuestos() > 0) {
            Repuesto repuesto = servicioActual.getRepuestos()[0];
            for (int i = 0; i < vista.getListaRepuestos().getItemCount(); i++) {
                if (vista.getListaRepuestos().getItemAt(i).startsWith(repuesto.getId() + " - ")) {
                    vista.getListaRepuestos().setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // LISTENERS
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
            // VALIDAR CAMPOS VACIOS
            if (vista.getCampoID().getText().trim().isEmpty() ||
                vista.getCampoNombre().getText().trim().isEmpty() ||
                vista.getCampoMarca().getText().trim().isEmpty() ||
                vista.getCampoModelo().getText().trim().isEmpty() ||
                vista.getCampoPMano().getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // VALIDAR ID
            int nuevoId;
            try {
                nuevoId = Integer.parseInt(vista.getCampoID().getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "EL ID DEBE SER UN NUMERO VALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // VERIFICAR SI EL ID FUE MODIFICADO Y SI YA EXISTE
            if (nuevoId != servicioActual.getId() && 
                ServiciosModelo.buscarServicio(nuevoId) != null) {
                JOptionPane.showMessageDialog(vista, "EL ID YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String nombre = vista.getCampoNombre().getText().trim();
            String marca = vista.getCampoMarca().getText().trim();
            String modelo = vista.getCampoModelo().getText().trim();
            double precioManoObra;
            
            try {
                precioManoObra = Double.parseDouble(vista.getCampoPMano().getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "INGRESE UN PRECIO VALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // OBTENER REPUESTO SELECCIONADO
            Repuesto repuesto = obtenerRepuestoSeleccionado();
            if (repuesto == null) return;
            
            // VALIDAR COINCIDENCIA DE MARCA Y MODELO
            if (!marca.equalsIgnoreCase(repuesto.getMarca()) || !modelo.equalsIgnoreCase(repuesto.getModelo())) {
                JOptionPane.showMessageDialog(vista, "MARCA Y MODELO NO COINCIDEN CON EL REPUESTO SELECCIONADO\n" + "REPUESTO: " + repuesto.getMarca() + " " + repuesto.getModelo(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // MODIFICAR SERVICIO
            boolean exito = ServiciosModelo.modificarServicio(
                servicioActual.getId(), 
                nuevoId,
                nombre, 
                marca, 
                modelo, 
                precioManoObra
            );
            
            // AGREGAR REPUESTO AL SERVICIO
            if (exito) {
                if (ServiciosModelo.agregarRepuestoAServicio(nuevoId, repuesto.getId())) {
                    // CALCULAR PRECIO TOTAL
                    double precioTotal = precioManoObra + repuesto.getPrecio();
                    vista.getCampoPTotal().setText(String.valueOf(precioTotal));
                    
                    JOptionPane.showMessageDialog(vista, "SERVICIO MODIFICADO CON EXITO", "EXITO", JOptionPane.INFORMATION_MESSAGE);
                    regresar();
                } else {
                    JOptionPane.showMessageDialog(vista, "ERROR AL ASIGNAR EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "ERROR AL MODIFICAR EL SERVICIO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS EN TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Repuesto obtenerRepuestoSeleccionado() {
        String seleccion = (String) vista.getListaRepuestos().getSelectedItem();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(vista, "DEBE SELECCIONAR UN REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try {
            int idRepuesto = Integer.parseInt(seleccion.split(" - ")[0]);
            return RepuestosModelo.buscarRepuesto(idRepuesto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "ERROR AL OBTENER EL REPUESTO SELECCIONADO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    private void regresar() {
        ServiciosVista serviciosVista = new ServiciosVista();
        new ServiciosControlador(serviciosVista);
        serviciosVista.setVisible(true);
        vista.dispose();
    }
}