package controlador;

import vista.VerRVista;
import modelo.RepuestosModelo;
import modelo.RepuestosModelo.Repuesto;
import javax.swing.table.DefaultTableModel;
import vista.RepuestosVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VerRControlador {
    private VerRVista vista;
    
    public VerRControlador(VerRVista vista) {
        this.vista = vista;
        cargarTablaRepuestos();
        
        //LISTENERS
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void cargarTablaRepuestos() {
        Repuesto[] repuestos = RepuestosModelo.obtenerTodosRepuestos();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTabla().getModel();
        modelo.setRowCount(0); //LIMPIAR TABLA
        
        for (Repuesto repuesto : repuestos) {
            Object[] fila = {
                repuesto.getId(),
                repuesto.getNombre(),
                repuesto.getMarca(),
                repuesto.getModelo(),
                repuesto.getExistencias(),
                repuesto.getPrecio()
            };
            modelo.addRow(fila);
        }
    }
    
    private void regresar() {
        RepuestosVista repuestosVista = new RepuestosVista();
        new RepuestosControlador(repuestosVista);
        repuestosVista.setVisible(true);
        vista.dispose();
    }
}