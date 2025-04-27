package controlador;

import vista.ProgresoVista;
import modelo.ProgresoModelo;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.ServiciosModelo;
import vista.MenuCVista;
import javax.swing.SwingWorker;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class ProgresoControlador {
    private final ProgresoVista vista;
    private final ProgresoModelo modelo;
    private String usuarioActual;
    
    public ProgresoControlador(ProgresoVista vista, String usuario) {
        this.vista = vista;
        this.modelo = ProgresoModelo.getInstance();
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
        configurarVista();
        configurarEventos();
    }
    
    private void configurarVista() {
        cargarVehiculosCliente();
        cargarServiciosDisponibles();
    }
    
    private void cargarVehiculosCliente() {
        String[][] vehiculos = modelo.obtenerVehiculosCliente(usuarioActual);
        if (vehiculos != null) {
            vista.getListaVehiculo().removeAllItems();
            for (String[] vehiculo : vehiculos) {
                if (vehiculo != null && vehiculo.length >= 3) {
                    vista.getListaVehiculo().addItem(vehiculo[0] + " - " + vehiculo[1] + " " + vehiculo[2]);
                }
            }
        } else {
            BitacoraModelo.registrarEvento(usuarioActual, "Vehiculos Registrados", "Advertencia", "No cuenta con vehiculos registrados");
            JOptionPane.showMessageDialog(vista, "NO SE ENCONTRARON VEHICULOS REGISTRADOS", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cargarServiciosDisponibles() {
        ServiciosModelo.Servicio[] servicios = ServiciosModelo.obtenerTodosServicios();
        if (servicios != null) {
            vista.getListaServicios().removeAllItems();
            for (ServiciosModelo.Servicio servicio : servicios) {
                if (servicio != null) {
                    vista.getListaServicios().addItem(servicio.getNombre());
                }
            }
        }
    }
    
    private void configurarEventos() {
        vista.getBotonAñadir().addActionListener(this::añadirVehiculoATabla);
        vista.getBotonProcesar().addActionListener(this::procesarServicios);
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    private void añadirVehiculoATabla(ActionEvent e) {
        String vehiculoSeleccionado = (String) vista.getListaVehiculo().getSelectedItem();
        String servicioSeleccionado = (String) vista.getListaServicios().getSelectedItem();
        
        if (vehiculoSeleccionado == null || servicioSeleccionado == null) {
            BitacoraModelo.registrarEvento(usuarioActual, "Vehiculo y Servicio", "Error", "Se debe seleccionar un vehiculo y uns servicio");
            JOptionPane.showMessageDialog(vista, "DEBE SELECCIONAR UN VEHICULO Y UN SERVICIO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String[] partesVehiculo = vehiculoSeleccionado.split(" - ");
        String placa = partesVehiculo[0];
        String[] marcaModelo = partesVehiculo[1].split(" ");
        String marca = marcaModelo[0];
        String modelo = marcaModelo.length > 1 ? marcaModelo[1] : "";
        
        if (!this.modelo.verificarCompatibilidad(marca, modelo, servicioSeleccionado)) {
            BitacoraModelo.registrarEvento(usuarioActual, "Compatibilidad del Servicio", "Error", "El servicio no es compatible con el vehiculo");
            JOptionPane.showMessageDialog(vista, "EL SERVICIO NO ES COMPATIBLE CON EL VEHICULO SELECCIONADO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel tableModel = (DefaultTableModel) vista.getTablaVehiculo().getModel();
        tableModel.addRow(new Object[]{placa, marca, modelo, servicioSeleccionado});
        
        this.modelo.agregarVehiculoProceso(placa, marca, modelo, servicioSeleccionado, usuarioActual);
    }
    
    private void procesarServicios(ActionEvent e) {
        if (modelo.getVehiculosEnProceso().length == 0) {
            BitacoraModelo.registrarEvento(usuarioActual, "Procesamiento del Vehiculo", "Error", "No hay vehiculos para procesar");
            JOptionPane.showMessageDialog(vista, "NO HAY VEHICULOS PARA PROCESAR", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    publish(i);
                    Thread.sleep(140);
                }
                
                for (int i = 0; i <= 100; i++) {
                    vista.getEnServicio().setValue(i);
                    Thread.sleep(90);
                }
                
                for (int i = 0; i <= 100; i++) {
                    vista.getListoEntrega().setValue(i);
                    Thread.sleep(60);
                }
                
                modelo.servicioCompletado(usuarioActual);
                return null;
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                vista.getColaEspera().setValue(progress);
            }
            
            @Override
            protected void done() {
                BitacoraModelo.registrarEvento(usuarioActual, "Servicios Completado", "Éxito", "El servicio se completó con éxito");
                JOptionPane.showMessageDialog(vista, "SERVICIO COMPLETADO CON EXITO", "EXITO", JOptionPane.INFORMATION_MESSAGE);
                ((DefaultTableModel) vista.getTablaVehiculo().getModel()).setRowCount(0);
                vista.getColaEspera().setValue(0);
                vista.getEnServicio().setValue(0);
                vista.getListoEntrega().setValue(0);
            }
        }.execute();
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Progreso", "Éxito", "Se cerró la ventana del progreso");
        MenuCVista menuVista = new MenuCVista();
        new MenuCControlador(menuVista, usuarioActual);
        vista.dispose();
        menuVista.setVisible(true);
    }
}