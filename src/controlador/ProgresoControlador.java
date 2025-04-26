package controlador;

import vista.ProgresoVista;
import modelo.ProgresoModelo;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ServiciosModelo;
import vista.MenuCVista;

public class ProgresoControlador {
    private final ProgresoVista vista;
    private final ProgresoModelo modelo;
    private String usuarioActual;
    
    public ProgresoControlador(ProgresoVista vista, String usuario) {
        this.vista = vista;
        this.modelo = new ProgresoModelo();
        this.usuarioActual = usuario;
        
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
            vista.getListaVehiculo().removeAllItems(); //LIMPIAR
            for (String[] vehiculo : vehiculos) {
                if (vehiculo != null && vehiculo.length >= 3) {
                    vista.getListaVehiculo().addItem(vehiculo[0] + " - " + vehiculo[1] + " " + vehiculo[2]);
                }
            }
        } else {
            JOptionPane.showMessageDialog(vista, "NO SE ENCONTRARON VEHICULOS REGISTRADOS", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cargarServiciosDisponibles() {
    ServiciosModelo.Servicio[] servicios = ServiciosModelo.obtenerTodosServicios();
    if (servicios != null) {
        vista.getListaServicios().removeAllItems(); //LIMPIAR
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
            JOptionPane.showMessageDialog(vista, "DEBE SELECCIONAR UN VEHICULO Y UN SERVICIO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //EXTRAER DATOS DEL VEHICULO
        String[] partesVehiculo = vehiculoSeleccionado.split(" - ");
        String placa = partesVehiculo[0];
        String[] marcaModelo = partesVehiculo[1].split(" ");
        String marca = marcaModelo[0];
        String modelo = marcaModelo.length > 1 ? marcaModelo[1] : "";
        
        //VERIFICAR COMPATIBILIDAD DE MARCA Y MODELO
        if (!this.modelo.verificarCompatibilidad(marca, modelo, servicioSeleccionado)) {
            JOptionPane.showMessageDialog(vista, "EL SERVICIO NO ES COMPATIBLE CON EL VEHICULO SELECCIONADO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //AÑADIR A LA TABLA
        DefaultTableModel tableModel = (DefaultTableModel) vista.getTablaVehiculo().getModel();
        tableModel.addRow(new Object[]{placa, marca, modelo, servicioSeleccionado});
        
        //AÑADIR EL MODELO
        this.modelo.agregarVehiculoProceso(placa, marca, modelo, servicioSeleccionado);
    }
    
    private void procesarServicios(ActionEvent e) {
        if (modelo.getVehiculosEnProceso().length == 0) {
            JOptionPane.showMessageDialog(vista, "NO HAY VEHICULOS PARA PROCESAR", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //SIMULACION DEL PROCESO
        new Thread(() -> {
            try {
                //COLA DE ESPERA 14 SEGUNDOS
                for (int i = 0; i <= 100; i++) {
                    vista.getColaEspera().setValue(i);
                    Thread.sleep(140); // 140ms * 100 = 14s
                }
                
                //ENS ERVICIO 9 SEGUNDOS
                for (int i = 0; i <= 100; i++) {
                    vista.getEnServicio().setValue(i);
                    Thread.sleep(90); // 90ms * 100 = 9s
                }
                
                //LISTO EN 6 SEGUNDOS
                for (int i = 0; i <= 100; i++) {
                    vista.getListoEntrega().setValue(i);
                    Thread.sleep(60); // 60ms * 100 = 6s
                }
                
                JOptionPane.showMessageDialog(vista, "SERVICIO COMPLETADO CON EXITO", "EXITO", JOptionPane.INFORMATION_MESSAGE);
                
                //LIMPIAR DESPUES DE COMPLETAR
                ((DefaultTableModel) vista.getTablaVehiculo().getModel()).setRowCount(0);
                vista.getColaEspera().setValue(0);
                vista.getEnServicio().setValue(0);
                vista.getListoEntrega().setValue(0);
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
    private void regresar() {
        MenuCVista menuVista = new MenuCVista();
        new MenuCControlador(menuVista, usuarioActual);
        vista.dispose();
    menuVista.setVisible(true);
    }
}