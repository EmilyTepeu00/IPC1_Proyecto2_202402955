package controlador;

import vista.VerCAVista;
import modelo.ClientesAutosModelo;
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
        
        ClientesAutosModelo.Cliente[] clientes = ClientesAutosModelo.obtenerTodosClientes();
        for (ClientesAutosModelo.Cliente cliente : clientes) {
            Object[] fila = {
                cliente.getDpi(),
                cliente.getNombreCompleto(),
                cliente.getUsuario(),
                cliente.getContraseña(),
                cliente.getTipoCliente(),
                cliente.getAutomovil()
            };
            modelo.addRow(fila);
        }
    }
    
    private void regresar() {
        ClientesAutosVista clientesVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesVista);
        clientesVista.setVisible(true);
        vista.dispose();
    }
}