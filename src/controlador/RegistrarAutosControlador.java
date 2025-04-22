package controlador;

import vista.RegistrarAutosVista;
import vista.MenuCVista;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import modelo.RegistrarAutosModelo;

public class RegistrarAutosControlador {
    private RegistrarAutosVista vista;
    private MenuCVista menuVista;
    private ImageIcon imagenAuto;
    private String usuarioActual;
    private RegistrarAutosModelo modelo;

    public RegistrarAutosControlador(RegistrarAutosVista vista, MenuCVista menuVista, String usuario) {
        this.vista = vista;
        this.menuVista = menuVista;
        this.usuarioActual = usuario;
        this.modelo = new RegistrarAutosModelo();
        configurarListeners();
        DragAndDrop();
    }

    private void configurarListeners() {
        vista.getBotonRegresar().addActionListener(e -> {
            vista.dispose();
            menuVista.setVisible(true);
        });

        vista.getBotonBuscar().addActionListener(e -> buscarImagen());
        vista.getBotonAceptar().addActionListener(e -> registrarAuto());
    }

    //CONFIGURACION DEL DRAG AND DROP
    private void DragAndDrop() {
        new DropTarget(vista.getPanelImagen(), new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) 
                        dtde.getTransferable().getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                    
                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        procesarImagen(file);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "ERROR AL PROCESAR LA IMAGEN: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void buscarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagenes JPG", "jpg", "jpeg");
        fileChooser.setFileFilter(filter);
        
        int returnValue = fileChooser.showOpenDialog(vista);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            procesarImagen(selectedFile);
        }
    }

    private void procesarImagen(File file) {
        try {
            String fileName = file.getName().toLowerCase();
            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                throw new IOException("SOLO SE PERMITEN ARCHIVOS .jpg o .jpeg");
            }
            
            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage == null) {
                throw new IOException("LA IMAGEN NO ES VALIDA");
            }
            
            //AJUSTAR IMAGEN AL PANEL
            Image scaledImage = originalImage.getScaledInstance(
                vista.getPanelImagen().getWidth(), 
                vista.getPanelImagen().getHeight(), 
                Image.SCALE_SMOOTH);
            
            imagenAuto = new ImageIcon(scaledImage);
            
            //COLOCAR LA IMAGEN EN EL PANEL
            vista.getPanelImagen().setLayout(null);
            vista.getPanelImagen().getGraphics().drawImage(scaledImage, 0, 0, null);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista, "ERROR AL CARGAR LA IMAGEN: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAuto() {
        String placa = vista.getCampoPlaca().getText().trim();
        String marca = vista.getCampoMarca().getText().trim();
        String modeloText = vista.getCampoModelo().getText().trim();
        
        if (placa.isEmpty() || marca.isEmpty() || modeloText.isEmpty() || imagenAuto == null) {
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //VERIFICAR SI EL AUTO YA EXISTE
        String[] autoExistente = modelo.buscarAuto(usuarioActual, placa);
        if (autoExistente != null) {
            JOptionPane.showMessageDialog(vista, "LA PLACA INGRESADA YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //GUARDAR DATOS DEL AUTO
        boolean exito = modelo.guardarAuto(usuarioActual, placa, marca, modeloText, imagenAuto);
        
        if (exito) {
            JOptionPane.showMessageDialog(vista, "AUTO REGISTRADO CON EXITO", "EXITO", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "ERROR AL GUARDAR EL AUTO", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.getCampoPlaca().setText("");
        vista.getCampoMarca().setText("");
        vista.getCampoModelo().setText("");
        vista.getPanelImagen().removeAll();
        vista.getPanelImagen().repaint();
        imagenAuto = null;
    }

    //GETTERS
    public RegistrarAutosVista getVista() {
        return vista;
    }
}