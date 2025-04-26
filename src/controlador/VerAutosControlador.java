package controlador;

import vista.VerAutosVista;
import vista.MenuCVista;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.VerAutosModelo;
import modelo.ClientesAutosModelo;

public class VerAutosControlador {
    private VerAutosVista vista;
    private MenuCVista menuVista;
    private VerAutosModelo modelo;
    private String usuarioActual;
    private String[][] autos;
    
    public VerAutosControlador(VerAutosVista vista, MenuCVista menuVista, String usuario) {
        this.vista = vista;
        this.menuVista = menuVista;
        this.usuarioActual = usuario;
        this.modelo = new VerAutosModelo();
        
        configurarButtonGroup();
        configurarListeners();
        cargarAutos();
        configurarTabla();
        configurarRenderizadorImagenes();
        actualizarTabla();
    }
    
    private void configurarButtonGroup() {
        javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
        group.add(vista.getBotonAscendente());
        group.add(vista.getBotonDescendente());
        vista.getBotonAscendente().setSelected(true);
    }
    
    private void configurarListeners() {
        vista.getBotonRegresar().addActionListener(e -> {
            vista.dispose();
            menuVista.setVisible(true);
        });
        
        vista.getBotonAscendente().addActionListener(e -> ordenarAutos(true));
        vista.getBotonDescendente().addActionListener(e -> ordenarAutos(false));
    }
    
    private void cargarAutos() {
        //OBTENER LOS DATOS DEL SISTEMA DE ARCHIVOS
        autos = modelo.obtenerAutosUsuario(usuarioActual);
        
        //SI NO HAY ARCHIVOS SE BUSCA EN LSO DATOS DEL CLIENTE
        if (autos == null || autos.length == 0) {
            autos = obtenerAutosDesdeCliente();
        }
    }
    
    //PARA OBTENER LOS DATOS DEL CLIENTE
    private String[][] obtenerAutosDesdeCliente() {
        ClientesAutosModelo.Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuarioActual);
        if (cliente != null && cliente.getAutomovil() != null && !cliente.getAutomovil().isEmpty()) {
            String[] datosAuto = cliente.getAutomovil().split(",");
            if (datosAuto.length >= 4) {
                return new String[][]{
                    {datosAuto[0].trim(),  // Placa
                     datosAuto[1].trim(),  // Marca
                     datosAuto[2].trim(),  // Modelo
                     datosAuto[3].trim()}  // Imagen
                };
            }
        }
        return null;
    }
    
    private void configurarTabla() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"PLACA", "MARCA", "MODELO", "IMAGEN"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? ImageIcon.class : String.class;
            }
        };
        vista.getTabla().setModel(model);
        vista.getTabla().setRowHeight(100);
    }
    
    private void configurarRenderizadorImagenes() {
        vista.getTabla().getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                
                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                } else {
                    label.setText("SIN IMAGEN");
                }
                return label;
            }
        });
    }
    
    private void ordenarAutos(boolean ascendente) {
        if (autos != null && autos.length > 1) {
            modelo.shellSort(autos, ascendente);
            actualizarTabla();
        }
    }
    
    private void actualizarTabla() {
        DefaultTableModel model = (DefaultTableModel) vista.getTabla().getModel();
        model.setRowCount(0);
        
        if (autos != null) {
            for (String[] auto : autos) {
                ImageIcon imagen = modelo.obtenerImagen(auto[3], 120, 80);
                model.addRow(new Object[]{auto[0], auto[1], auto[2], imagen});
            }
        }
    }
}