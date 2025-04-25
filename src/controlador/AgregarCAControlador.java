package controlador;

import vista.AgregarCAVista;
import modelo.ClientesAutosModelo;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import vista.ClientesAutosVista;
import modelo.InicioModelo;

public class AgregarCAControlador {
    private AgregarCAVista vista;
    
    public AgregarCAControlador(AgregarCAVista vista) {
        this.vista = vista;
        configurarListeners();
    }
    
    private void configurarListeners() {
        vista.getBotonAceptar().addActionListener(this::agregarClienteAuto);
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    private void agregarClienteAuto(ActionEvent e) {
        String dpi = vista.getCampoID().getText().trim();
        String nombre = vista.getCampoNombre().getText().trim();
        String usuario = vista.getCampoUsuario().getText().trim();
        String contraseña = vista.getCampoContraseña().getText().trim();
        String tipoCliente = vista.getCampoTipoCliente().getText().trim();
        String automovil = vista.getCampoAutomovil().getText().trim();
        
        if (validarCampos(dpi, nombre, usuario, contraseña, tipoCliente, automovil)) {
            boolean exito = ClientesAutosModelo.agregarCliente(
                dpi, nombre, usuario, contraseña, tipoCliente, automovil
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(vista, "CLIENTE Y AUTOREGISTRADO CON EXITO");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "EL DPI O USUARIO YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validarCampos(String dpi, String nombre, String usuario, String contraseña, String tipoCliente, String automovil) {
        if (dpi.isEmpty() || nombre.isEmpty() || usuario.isEmpty() || 
            contraseña.isEmpty() || tipoCliente.isEmpty() || automovil.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validar que no sea igual a los usuarios del sistema
        if (usuario.equals(InicioModelo.ADMIN_USER) || usuario.equals(InicioModelo.MECANICO_USER)) {
            JOptionPane.showMessageDialog(vista, "NOMBRE DE USUARIO NO PERMITIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        vista.getCampoID().setText("");
        vista.getCampoNombre().setText("");
        vista.getCampoUsuario().setText("");
        vista.getCampoContraseña().setText("");
        vista.getCampoTipoCliente().setText("");
        vista.getCampoAutomovil().setText("");
    }
    
    private void regresar() {
        ClientesAutosVista clientesVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesVista);
        clientesVista.setVisible(true);
        vista.dispose();
    }
}