package controlador;

import vista.AgregarSVista;
import modelo.ServiciosModelo;
import modelo.RepuestosModelo;
import modelo.RepuestosModelo.Repuesto;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import vista.ServiciosVista;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class AgregarSControlador {
    private final AgregarSVista vista;
    private String usuarioActual;
    
    public AgregarSControlador(AgregarSVista vista) {
        this.vista = vista;
        configurarVista();
        configurarEventos();
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
    }
    
    private void configurarVista() {
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
            // VALIDAR CAMPOS VACIOS
            if (!validarCampos()) return;
            
            // VALIDAR ID
            int idServicio;
            try {
                idServicio = Integer.parseInt(vista.getCampoID().getText().trim());
            } catch (NumberFormatException ex) {
                BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado no es valido");
                JOptionPane.showMessageDialog(vista, "EL ID DEBE SER UN NUMERO VALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // VERIFICAR SI EL ID YA EXISTE
            if (ServiciosModelo.buscarServicio(idServicio) != null) {
                BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado ya existe");
                JOptionPane.showMessageDialog(vista, "EL ID INGRESADO YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String nombre = vista.getCampoNombre().getText().trim();
            String marca = vista.getCampoMarca().getText().trim();
            String modelo = vista.getCampoModelo().getText().trim();
            double precioManoObra;
            
            try {
                precioManoObra = Double.parseDouble(vista.getCampoPMano().getText());
            } catch (NumberFormatException ex) {
                BitacoraModelo.registrarEvento(usuarioActual, "Validación de precio", "Error", "El Precio ingresado no es válido");
                JOptionPane.showMessageDialog(vista, "INGRESE UN PRECIO VALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // OBTENER REPUESTO SELECCIONADO
            Repuesto repuesto = obtenerRepuestoSeleccionado();
            if (repuesto == null) return;
            
            // VALIDAR COINCIDENCIA DE MARCA Y MODELO
            if (!validarMarcaModelo(marca, modelo, repuesto)) return;
            
            // CREAR SERVICIO 
            boolean creado = ServiciosModelo.agregarServicioConId(idServicio, nombre, marca, modelo, precioManoObra);
            if (!creado) {
                BitacoraModelo.registrarEvento(usuarioActual, "Crear Servicio", "Error", "No se puedo crear el servicio");
                JOptionPane.showMessageDialog(vista, "NO SE PUDO CREAR EL SERVICIO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // ASIGNAR REPUESTO Y CALCULAR PRECIO TOTAL
            if (ServiciosModelo.agregarRepuestoAServicio(idServicio, repuesto.getId())) {
                double precioTotal = precioManoObra + repuesto.getPrecio();
                vista.getCampoPTotal().setText(String.valueOf(precioTotal));
                
                BitacoraModelo.registrarEvento(usuarioActual, "Servicio Creado", "Éxito", "Servicio creado con ID:" + idServicio);
                JOptionPane.showMessageDialog(vista, "SERVICIO CREADO CON ID: " + idServicio, "EXITO", JOptionPane.INFORMATION_MESSAGE);
                regresar();
            } else {
                ServiciosModelo.eliminarServicio(idServicio); // LIMPIAR SERVICIO MAL CREADO
                BitacoraModelo.registrarEvento(usuarioActual, "Asignar Repuesto", "Error", "Error al asignar el repuesto");
                JOptionPane.showMessageDialog(vista, "ERROR AL ASIGNAR EL REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            BitacoraModelo.registrarEvento(usuarioActual, "Validación de datos", "Error", "Se ingresó datos inválidos/No se llenaron todos los campos");
            JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS EN TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        if (vista.getCampoID().getText().trim().isEmpty() ||
            vista.getCampoNombre().getText().trim().isEmpty() ||
            vista.getCampoMarca().getText().trim().isEmpty() ||
            vista.getCampoModelo().getText().trim().isEmpty() ||
            vista.getCampoPMano().getText().trim().isEmpty()) {
            
            BitacoraModelo.registrarEvento(usuarioActual, "Validación de datos", "Error", "Debe llenar todos los campos");
            JOptionPane.showMessageDialog(vista, "TODOS LOS CAMPOS SON OBLIGATORIOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private Repuesto obtenerRepuestoSeleccionado() {
        String seleccion = (String) vista.getListaRepuestos().getSelectedItem();
        if (seleccion == null) {
            BitacoraModelo.registrarEvento(usuarioActual, "Seleccion de Repuesto", "Error", "No se seleccionó un repuesto");
            JOptionPane.showMessageDialog(vista, "DEBE SELECCIONAR UN REPUESTO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try {
            int idRepuesto = Integer.parseInt(seleccion.split(" - ")[0]);
            return RepuestosModelo.buscarRepuesto(idRepuesto);
        } catch (NumberFormatException e) {
            BitacoraModelo.registrarEvento(usuarioActual, "Obtener Repuesto", "Error", "No se pudo obtener el repuesto seleccionado");
            JOptionPane.showMessageDialog(vista, "ERROR AL OBTENER EL REPUESTO SELECCIONADO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    private boolean validarMarcaModelo(String marca, String modelo, Repuesto repuesto) {
        if (!marca.equalsIgnoreCase(repuesto.getMarca()) || !modelo.equalsIgnoreCase(repuesto.getModelo())) {
            BitacoraModelo.registrarEvento(usuarioActual, "Marca y Modelo", "Error", "No coinciden con el repuesto seleccionado");
            JOptionPane.showMessageDialog(vista, "MARCA Y MODELO NO COINCIDEN CON EL REPUESTO SELECCIONADO\n" + "REPUESTO: " + repuesto.getMarca() + " " + repuesto.getModelo(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Agregar Servicio", "Éxito", "Se cerró la ventana de servicios");
        ServiciosVista serviciosVista = new ServiciosVista();
        new ServiciosControlador(serviciosVista);
        serviciosVista.setVisible(true);
        vista.dispose();
    }
}