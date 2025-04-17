package controlador;

import vista.ModificarCAVista;
import modelo.ClientesAutosModelo;
import modelo.ClientesAutosModelo.Cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.MenuAVista;

public class ModificarCAControlador {
    private ModificarCAVista vista;
    private Cliente clienteActual;
    private int idCliente;
    
    public ModificarCAControlador(ModificarCAVista vista, int idCliente) {
        this.vista = vista;
        this.idCliente = idCliente;
        this.clienteActual = ClientesAutosModelo.buscarCliente(idCliente);
        
        if (clienteActual != null) {
            //LLENAR CAMPOS CON DATOS ACTUALIZADOS
            vista.getCampoNombre().setText(clienteActual.getNombreCompleto());
            vista.getCampoUsuario().setText(clienteActual.getUsuario());
            vista.getCampoContraseña().setText(clienteActual.getContraseña());
            vista.getCampoTipoCliente().setText(clienteActual.getTipoCliente());
            
            //CONVERTIR ARRAY DE AUTOS A STRING
            StringBuilder automovilesStr = new StringBuilder();
            for (String auto : clienteActual.getAutomoviles()) {
                automovilesStr.append(auto).append(";");
            }
            vista.getCampoAutomovil().setText(automovilesStr.toString());
        }
        
        //LISTENERS
        vista.getBotonAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarClienteAuto();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void modificarClienteAuto() {
        try {
            String nombreCompleto = vista.getCampoNombre().getText();
            String usuario = vista.getCampoUsuario().getText();
            String contraseña = vista.getCampoContraseña().getText();
            String tipoCliente = vista.getCampoTipoCliente().getText();
            String automovilesStr = vista.getCampoAutomovil().getText();
            
            String[] automoviles = ClientesAutosModelo.parseAutomoviles(automovilesStr);
            
            boolean exito = ClientesAutosModelo.modificarCliente(
                idCliente, 
                nombreCompleto, 
                usuario, 
                contraseña, 
                tipoCliente, 
                automoviles
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(vista, "CLIENTE MODIFICADO CON EXITO");
                regresar();
            } else {
                JOptionPane.showMessageDialog(vista, "ERROR AL MODIFICAR EL CLIENTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "ERROR: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresar() {
        MenuAVista menuVista = new MenuAVista();
        new MenuAControlador(menuVista, "admin");
        menuVista.setVisible(true);
        vista.dispose();
    }
}