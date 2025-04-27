package controlador;

import vista.VerSVista;
import modelo.ServiciosModelo;
import modelo.ServiciosModelo.Servicio;
import javax.swing.table.DefaultTableModel;
import vista.ServiciosVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class VerSControlador {
    private VerSVista vista;
    private String usuarioActual;
    
    public VerSControlador(VerSVista vista) {
        this.vista = vista;
        cargarTablaServicios();
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
        //LISTENERS
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void cargarTablaServicios() {
        Servicio[] servicios = ServiciosModelo.obtenerTodosServicios();
        DefaultTableModel modelo = (DefaultTableModel) vista.getTabla().getModel();
        modelo.setRowCount(0); //LIMPIAR TABLA
        
        for (Servicio servicio : servicios) {
            //OBTENER NOMBRE DE REPUESTOS COMO CADENA
            StringBuilder repuestosStr = new StringBuilder();
            for (int i = 0; i < servicio.getContadorRepuestos(); i++) {
                if (i > 0) repuestosStr.append(", ");
                repuestosStr.append(servicio.getRepuestos()[i].getNombre());
            }
            
            Object[] fila = {
                servicio.getId(),
                servicio.getNombre(),
                servicio.getMarca(),
                servicio.getModelo(),
                repuestosStr.toString(),
                servicio.getPrecioManoObra(),
                servicio.getPrecioTotal()
            };
            modelo.addRow(fila);
        }
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Ver Servicios", "Éxito", "Se cerró la ventana de ver servicios");
        ServiciosVista serviciosVista = new ServiciosVista();
        new ServiciosControlador(serviciosVista);
        serviciosVista.setVisible(true);
        vista.dispose();
    }
}