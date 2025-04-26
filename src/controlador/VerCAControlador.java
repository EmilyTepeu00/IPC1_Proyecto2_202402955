package controlador;

import vista.VerCAVista;
import modelo.VerCAModelo;
import javax.swing.table.DefaultTableModel;
import vista.ClientesAutosVista;

public class VerCAControlador {
    private VerCAVista vista;
    
    public VerCAControlador(VerCAVista vista) {
        this.vista = vista;
        cargarTablaClientes();
        configurarListeners();
    }
    
    private void configurarListeners() {
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    private void cargarTablaClientes() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTabla().getModel();
        modelo.setRowCount(0); //LIMPIAR TABLA
        
        String[][] datos = VerCAModelo.obtenerDatosClientesYAutos();
        for (String[] fila : datos) {
            modelo.addRow(fila);
        }
        
        //AJUSTE DE ALTURA DE FILAS
        vista.getTabla().setRowHeight(60); //AUMENTO DE ALTURA PARA MOSTRAR VARIOS CARROS
    }
    
    private void regresar() {
        ClientesAutosVista clientesVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesVista);
        clientesVista.setVisible(true);
        vista.dispose();
    }
}