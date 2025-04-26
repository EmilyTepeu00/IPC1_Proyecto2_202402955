package controlador;

import vista.AgregarCAVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import vista.ClientesAutosVista;
import modelo.InicioModelo;
import modelo.AgregarCAModelo;

public class AgregarCAControlador {
    private AgregarCAVista vista;
    private AgregarCAModelo modelo;
    
    public AgregarCAControlador(AgregarCAVista vista) {
        this.vista = vista;
        this.modelo = new AgregarCAModelo();
        configurarListeners();
    }
    
    private void configurarListeners() {
        vista.getBotonAceptar().addActionListener(this::agregarClienteAutos);
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }
    
    private void agregarClienteAutos(ActionEvent e) {
        String dpi = vista.getCampoID().getText().trim();
        String nombre = vista.getCampoNombre().getText().trim();
        String usuario = vista.getCampoUsuario().getText().trim();
        String contraseña = vista.getCampoContraseña().getText().trim();
        String tipoCliente = vista.getCampoTipoCliente().getText().trim();
        String automoviles = vista.getCampoAutomovil().getText().trim();
        
        if (validarCampos(dpi, nombre, usuario, contraseña, tipoCliente, automoviles)) {
            boolean exito = modelo.agregarClienteAutos(
                dpi, nombre, usuario, contraseña, tipoCliente, automoviles
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(vista, "CLIENTE Y AUTOS REGISTRADOS CON EXITO");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "EL USUARIO YA EXISTE O DATOS INVALIDOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validarCampos(String dpi, String nombre, String usuario, String contraseña, String tipoCliente, String automoviles) {
        if (dpi.isEmpty() || nombre.isEmpty() || usuario.isEmpty() || 
            contraseña.isEmpty() || tipoCliente.isEmpty() || automoviles.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (usuario.equals(InicioModelo.ADMIN_USER) || usuario.equals(InicioModelo.MECANICO_USER)) {
            JOptionPane.showMessageDialog(vista, "NOMBRE DE USUARIO NO PERMITIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        //VALIDAR FORMATO DE AUTOS
        String[] autos = automoviles.split(";");
        for (String auto : autos) {
            String[] datos = auto.trim().split(",");
            if (datos.length != 4) {
                JOptionPane.showMessageDialog(vista,  "FORMATO DE AUTOS INCORRECTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
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