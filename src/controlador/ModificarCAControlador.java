package controlador;

import vista.ModificarCAVista;
import modelo.ClientesAutosModelo;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import vista.ClientesAutosVista;
import java.io.File;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class ModificarCAControlador {
    private ModificarCAVista vista;
    private String dpiCliente;
    private String usuarioCliente;
    private String usuarioActual;

    public ModificarCAControlador(ModificarCAVista vista, String dpi) {
        this.vista = vista;
        this.dpiCliente = dpi;
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
        cargarDatosCliente();
        configurarListeners();
    }

    private void cargarDatosCliente() {
        ClientesAutosModelo.Cliente cliente = ClientesAutosModelo.buscarClientePorDPI(dpiCliente);
        if (cliente != null) {
            this.usuarioCliente = cliente.getUsuario();
            vista.getCampoNombre().setText(cliente.getNombreCompleto());
            vista.getCampoUsuario().setText(cliente.getUsuario());
            vista.getCampoContraseña().setText(cliente.getContraseña());
            vista.getCampoTipoCliente().setText(cliente.getTipoCliente());
            vista.getCampoAutomovil().setText(cliente.getAutomovil());
        }
    }

    private void configurarListeners() {
        vista.getBotonAceptar().addActionListener(this::modificarCliente);
        vista.getBotonRegresar().addActionListener(e -> regresar());
    }

    private void modificarCliente(ActionEvent e) {
        String nombre = vista.getCampoNombre().getText().trim();
        String usuario = vista.getCampoUsuario().getText().trim();
        String contraseña = vista.getCampoContraseña().getText().trim();
        String tipoCliente = vista.getCampoTipoCliente().getText().trim();
        String automovil = vista.getCampoAutomovil().getText().trim();

        if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || 
            tipoCliente.isEmpty() || automovil.isEmpty()) {
            BitacoraModelo.registrarEvento(usuarioActual, "Validación de campos", "Error", "Debe llenar todos los campos");
            JOptionPane.showMessageDialog(vista, "DEBE LLENAR TODOS LOS CAMPOS", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //ACTUALIZAR ARCHIVOS DE AUTOS AL CAMBIAR DE USUARIO
        if (!usuario.equals(usuarioCliente)) {
            File carpetaAutos = new File("datos_autos");
            File[] archivosAutos = carpetaAutos.listFiles((dir, name) -> name.startsWith(usuarioCliente + "_"));
            
            if (archivosAutos != null) {
                for (File archivoAuto : archivosAutos) {
                    String nuevoNombre = archivoAuto.getName().replace(usuarioCliente + "_", usuario + "_");
                    archivoAuto.renameTo(new File(carpetaAutos, nuevoNombre));
                }
            }
        }

        boolean exito = ClientesAutosModelo.modificarCliente(
            dpiCliente, nombre, usuario, contraseña, tipoCliente, automovil
        );

        if (exito) {
            BitacoraModelo.registrarEvento(usuarioActual, "Modificación de Cliente/Auto", "Error", "Se modificó el cliente");
            JOptionPane.showMessageDialog(vista, "CLIENTE MODIFICADO CON EXITO");
            regresar();
        } else {
            BitacoraModelo.registrarEvento(usuarioActual, "Modificación de Cliente/Auto", "Error", "No se pudo modificar el cliente");
            JOptionPane.showMessageDialog(vista, "ERROR AL MODIFICAR EL CLIENTE", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Modificar Cliente/Auto", "Éxito", "Se cerró la ventana de modificar clientes y autos");
        ClientesAutosVista clientesVista = new ClientesAutosVista();
        new ClientesAutosControlador(clientesVista);
        clientesVista.setVisible(true);
        vista.dispose();
    }
}