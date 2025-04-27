package controlador;

import vista.VerCAVista;
import modelo.VerCAModelo;
import javax.swing.table.DefaultTableModel;
import vista.ClientesAutosVista;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class VerCAControlador {
    private VerCAVista vista;
    private String usuarioActual;
    
    public VerCAControlador(VerCAVista vista) {
        this.vista = vista;
        cargarTablaClientes();
        configurarListeners();
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
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
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Ver Clientes y Autos", "Éxito", "Se cerró la ventana de ver clientes y autos");
        ClientesAutosVista clientesVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesVista);
        clientesVista.setVisible(true);
        vista.dispose();
    }
}