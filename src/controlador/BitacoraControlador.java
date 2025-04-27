package controlador;

import modelo.BitacoraModelo;
import vista.BitacoraVista;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingUtilities;

public class BitacoraControlador implements BitacoraModelo.BitacoraListener {
    private BitacoraVista vista;
    private DefaultTableModel modelo;
    
    public BitacoraControlador(BitacoraVista vista) {
        this.vista = vista;
        this.modelo = (DefaultTableModel) vista.getTablaBitacora().getModel();
        configurarTabla();
        BitacoraModelo.addListener(this);
        cargarRegistrosExistentes();
    }
    
    private void configurarTabla() {
        modelo.setColumnIdentifiers(new String[]{
            "MARCA TEMPORAL", "USUARIO", "ACCION", "RESULTADO", "DETALLES"
        });
        vista.getTablaBitacora().setModel(modelo);
    }
    
    private void cargarRegistrosExistentes() {
        for (BitacoraModelo.RegistroBitacora registro : BitacoraModelo.obtenerRegistros()) {
            agregarRegistroATabla(registro);
        }
    }
    
    private void agregarRegistroATabla(BitacoraModelo.RegistroBitacora registro) {
        modelo.addRow(new Object[]{
            registro.getMarcaTemporal(),
            registro.getUsuario(),
            registro.getAccion(),
            registro.getResultado(),
            registro.getDetalles()
        });
        
        //AUTO-SCROLL AL ULTIMO REGISTRO
        vista.getTablaBitacora().scrollRectToVisible(
            vista.getTablaBitacora().getCellRect(modelo.getRowCount()-1, 0, true)
        );
    }
    
    @Override
    public void onNuevoRegistro(BitacoraModelo.RegistroBitacora registro) {
        SwingUtilities.invokeLater(() -> {
            agregarRegistroATabla(registro);
        });
    }
}