package controlador;

import modelo.RegistroModelo;
import vista.RegistroVista;
import vista.InicioVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelo.InicioModelo;

public class RegistroControlador {
    private final RegistroVista vista;
    private final RegistroModelo modelo;
    private final InicioVista inicioVista;

    public RegistroControlador(RegistroVista vista, RegistroModelo modelo, InicioVista inicioVista) {
        this.vista = vista;
        this.modelo = modelo;
        this.inicioVista = inicioVista;
        
        vista.getButtonGroup1().add(vista.getBotonNormal());
        vista.getButtonGroup1().add(vista.getBotonOro());
        vista.getBotonNormal().setSelected(true);
        vista.getBotonOro().setEnabled(false);
        
        vista.getBotonAceptar().addActionListener(this::registrarCliente);
        vista.getBotonRegresar1().addActionListener(e -> regresarAInicio());
    }
    
    private void registrarCliente(ActionEvent e) {
        String dpi = vista.getCampoDPI().getText().trim();
        String nombre = vista.getCampoNombre().getText().trim();
        String usuario = vista.getCampoUsuario().getText().trim();
        String contra = vista.getCampoContra().getText().trim();
        String tipoCliente = vista.getBotonNormal().isSelected() ? "NORMAL" : "ORO";
        
        if (validarCampos(dpi, nombre, usuario, contra)) {
            if (modelo.registrarCliente(dpi, nombre, usuario, contra, tipoCliente)) {
                JOptionPane.showMessageDialog(vista, "REGISTRO COMPLETADO CON EXITO", "EXITO", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                regresarAInicio();
            } else {
                JOptionPane.showMessageDialog(vista, "EL USUARIO YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validarCampos(String dpi, String nombre, String usuario, String contra) {
        if (dpi.isEmpty() || nombre.isEmpty() || usuario.isEmpty() || contra.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (usuario.equals(InicioModelo.ADMIN_USER) || usuario.equals(InicioModelo.MECANICO_USER)) {
            JOptionPane.showMessageDialog(vista, "NOMBRE DE USUARIO NO PERMITIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void limpiarCampos() {
        vista.getCampoDPI().setText("");
        vista.getCampoNombre().setText("");
        vista.getCampoUsuario().setText("");
        vista.getCampoContra().setText("");
        vista.getBotonNormal().setSelected(true);
    }
    
    private void regresarAInicio() {
        inicioVista.setVisible(true);
        vista.dispose();
    }
}