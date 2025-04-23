package controlador;

import vista.AgregarCAVista;
import modelo.ClientesAutosModelo;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import vista.ClientesAutosVista;
import modelo.ServiciosModelo;

public class AgregarCAControlador {
    private AgregarCAVista vista;
    
    public AgregarCAControlador(AgregarCAVista vista) {
        this.vista = vista;
        
        //ID INICIAL
        actualizarID();
        
        //ID AUTOMATICO
        vista.getCampoID().setText(String.valueOf(ClientesAutosModelo.getSiguienteId()));
        vista.getCampoID().setEditable(false);
        
        //LISTENERS
        vista.getBotonAceptar().addActionListener(this::agregarClienteAuto);
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    //ACTUALIZAR ID
    private void actualizarID() {
        vista.getCampoID().setText(String.valueOf(ServiciosModelo.getSiguienteId()));
        vista.getCampoID().setEditable(false);
    }
    
    private void agregarClienteAuto(ActionEvent e) {
        try {
            //VALIDAR CAMPOS VACIOS
            if (!validarCampos()) return;
            
            String nombreCompleto = vista.getCampoNombre().getText();
            String usuario = vista.getCampoUsuario().getText();
            String contraseña = vista.getCampoContraseña().getText();
            String tipoCliente = vista.getCampoTipoCliente().getText();
            String automovilesStr = vista.getCampoAutomovil().getText();
            
            String[] automoviles = ClientesAutosModelo.parseAutomoviles(automovilesStr);
            
            int id = ClientesAutosModelo.agregarCliente( nombreCompleto, usuario, contraseña, tipoCliente, automoviles);
            
            if (id != -1) {
                JOptionPane.showMessageDialog(vista, "CLIENTE AGREGADO CON ID: " + id);
                limpiarCampos();
                actualizarID();
            } else {
                JOptionPane.showMessageDialog(vista, "NO SE PUDO AGREGAR EL CLIENTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vista, "INGRESE DATOS VALIDOS", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        if (vista.getCampoNombre().getText().trim().isEmpty() ||
            vista.getCampoUsuario().getText().trim().isEmpty() ||
            vista.getCampoContraseña().getText().trim().isEmpty() ||
            vista.getCampoAutomovil().getText().trim().isEmpty() ||
            vista.getCampoTipoCliente().getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void regresar() {
        ClientesAutosVista clientesAVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesAVista);
        clientesAVista.setVisible(true);
        vista.dispose();
    }
    
    private void limpiarCampos() {
        vista.getCampoNombre().setText("");
        vista.getCampoUsuario().setText("");
        vista.getCampoContraseña().setText("");
        vista.getCampoAutomovil().setText("");
        vista.getCampoTipoCliente().setText("");
    }
}