package controlador;

import modelo.RegistroModelo;
import vista.RegistroVista;
import vista.InicioVista;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelo.InicioModelo;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class RegistroControlador {
    private final RegistroVista vista;
    private final RegistroModelo modelo;
    private final InicioVista inicioVista;
    private String usuarioActual;

    public RegistroControlador(RegistroVista vista, RegistroModelo modelo, InicioVista inicioVista) {
        this.vista = vista;
        this.modelo = modelo;
        this.inicioVista = inicioVista;
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
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
                BitacoraModelo.registrarEvento("Sistema", "Registro de cliente", "Éxito", "Cliente: " + nombre + " - Tipo: " + tipoCliente);
                JOptionPane.showMessageDialog(vista, "REGISTRO COMPLETADO CON EXITO", "EXITO", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                regresarAInicio();
            } else {
                BitacoraModelo.registrarEvento("Sistema", "Registro de cliente", "Error", "Usuario ya existe: " + usuario);
                JOptionPane.showMessageDialog(vista, "EL USUARIO YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            BitacoraModelo.registrarEvento("Sistema", "Registro de cliente", "Error", "Validación de campos fallida");
        }
    }
    
    private boolean validarCampos(String dpi, String nombre, String usuario, String contra) {
        if (dpi.isEmpty() || nombre.isEmpty() || usuario.isEmpty() || contra.isEmpty()) {
            BitacoraModelo.registrarEvento("Sistema", "Validación de datos", "Error", "Se debe llenar todos los campos");
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (usuario.equals(InicioModelo.ADMIN_USER) || usuario.equals(InicioModelo.MECANICO_USER)) {
            BitacoraModelo.registrarEvento(usuarioActual, "Nombre de Usuario", "Error", "Nombre de usuario no permitido");
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
        BitacoraModelo.registrarEvento("Sistema", "Regreso al Inicio", "Éxito", "Se regresó al Inicio");
        inicioVista.setVisible(true);
        vista.dispose();
    }
}