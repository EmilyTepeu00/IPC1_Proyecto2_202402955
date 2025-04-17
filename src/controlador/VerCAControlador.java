package controlador;

import vista.VerCAVista;
import modelo.ClientesAutosModelo;
import modelo.ClientesAutosModelo.Cliente;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.ClientesAutosVista;

public class VerCAControlador {
    private VerCAVista vista;
    
    public VerCAControlador(VerCAVista vista) {
        this.vista = vista;
        cargarTablaClientes();
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void cargarTablaClientes() {
        Cliente[] clientes = ClientesAutosModelo.obtenerTodosClientes();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTabla().getModel();
        modelo.setRowCount(0); //LIMPIAR TABLA
        
        for (Cliente cliente : clientes) {
            //CONVERTIR ARRAY DE AUTOS A STRING
            StringBuilder automovilesStr = new StringBuilder();
            for (String auto : cliente.getAutomoviles()) {
                automovilesStr.append(auto).append("; ");
            }
            
            Object[] fila = {
                cliente.getId(),
                cliente.getNombreCompleto(),
                cliente.getUsuario(),
                cliente.getContraseña(),
                cliente.getTipoCliente(),
                automovilesStr.toString()
            };
            modelo.addRow(fila);
        }
    }
    
    private void regresar() {
        ClientesAutosVista clientesAVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesAVista);
        clientesAVista.setVisible(true);
        vista.dispose();
    }
}