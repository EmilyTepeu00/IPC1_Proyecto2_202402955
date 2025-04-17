package controlador;

import vista.AgregarSVista;
import modelo.ServiciosModelo;
import modelo.RepuestosModelo;
import modelo.RepuestosModelo.Repuesto;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import vista.ServiciosVista;

public class AgregarSControlador {
    private final AgregarSVista vista;
    
    public AgregarSControlador(AgregarSVista vista) {
        this.vista = vista;
        configurarVista();
        configurarEventos();
    }
    
    private void configurarVista() {
        vista.getCampoID().setText(String.valueOf(ServiciosModelo.getSiguienteId()));
        cargarRepuestos();
    }
    
    private void cargarRepuestos() {
        Repuesto[] repuestos = RepuestosModelo.obtenerTodosRepuestos();
        vista.getListaRepuestos().removeAllItems();
        
        for (Repuesto repuesto : repuestos) {
            vista.getListaRepuestos().addItem(repuesto.getId() + " - " + repuesto.getNombre());
        }
    }
    
    private void configurarEventos() {
        vista.getBotonAceptar().addActionListener(this::agregarServicio);
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    private void agregarServicio(ActionEvent e) {
        try {
            //VALIDAR CAMPOS VACIOS
            if (!validarCampos()) return;
            
            String nombre = vista.getCampoNombre().getText().trim();
            String marca = vista.getCampoMarca().getText().trim();
            String modelo = vista.getCampoModelo().getText().trim();
            double precioManoObra = Double.parseDouble(vista.getCampoPMano().getText());
            
            //OBTENER REPUESTO SELECCIONADO
            Repuesto repuesto = obtenerRepuestoSeleccionado();
            if (repuesto == null) return;
            
            //VALIDAR COINCIDENCIA DE MARCA Y MODELO
            if (!validarMarcaModelo(marca, modelo, repuesto)) return;
            
            //CREAR SERVICIO
            int idServicio = ServiciosModelo.agregarServicio(nombre, marca, modelo, precioManoObra);
            if (idServicio == -1) {
                JOptionPane.showMessageDialog(vista, "NO SE PUDO CREAR EL SERVICIO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            //ASIGNAR REPUESTO Y CALCULAR PRECIO TOTAL
            if (ServiciosModelo.agregarRepuestoAServicio(idServicio, repuesto.getId())) {
                double precioTotal = precioManoObra + repuesto.getPrecio();
                vista.getCampoPTotal().setText(String.valueOf(precioTotal));
                
                JOptionPane.showMessageDialog(vista, "SERVICIO CREADO CON ID: " + idServicio);
                regresar();
            } else {
                ServiciosModelo.eliminarServicio(idServicio); //LIMPIAR SERVICIO MAL CREADO
                JOptionPane.showMessageDialog(vista, "ERROR AL ASIGNAR EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        if (vista.getCampoNombre().getText().trim().isEmpty() ||
            vista.getCampoMarca().getText().trim().isEmpty() ||
            vista.getCampoModelo().getText().trim().isEmpty() ||
            vista.getCampoPMano().getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(vista, "TODOS LOS CAMPOS SON OBLIGATORIOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private Repuesto obtenerRepuestoSeleccionado() {
        String seleccion = (String) vista.getListaRepuestos().getSelectedItem();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(vista, "DEBE SELECCIONAR UN REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        int idRepuesto = Integer.parseInt(seleccion.split(" - ")[0]);
        return RepuestosModelo.buscarRepuesto(idRepuesto);
    }
    
    private boolean validarMarcaModelo(String marca, String modelo, Repuesto repuesto) {
        if (!marca.equals(repuesto.getMarca()) || !modelo.equals(repuesto.getModelo())) {
            JOptionPane.showMessageDialog(vista, "MARCA Y MODELO NO COINCIDEN CON EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void regresar() {
        ServiciosVista serviciosVista = new ServiciosVista();
        new ServiciosControlador(serviciosVista);
        serviciosVista.setVisible(true);
        vista.dispose();
    }
}