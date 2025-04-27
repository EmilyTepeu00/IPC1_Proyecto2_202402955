package controlador;

import vista.ServiciosVista;
import vista.AgregarSVista;
import vista.ModificarSVista;
import vista.VerSVista;
import vista.MenuAVista;
import modelo.ServiciosModelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class ServiciosControlador {
    private ServiciosVista vista;
    private String usuarioActual;
    
    public ServiciosControlador(ServiciosVista vista) {
        this.vista = vista;
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
        
        //LISTENERS
        vista.getBotonAgregar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirAgregarServicio();
            }
        });
        
        vista.getBotonModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirModificarServicio();
            }
        });
        
        vista.getBotonEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarServicio();
            }
        });
        
        vista.getBotonVer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVerServicios();
            }
        });
        
        vista.getBotonRegresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });
    }
    
    private void abrirAgregarServicio() {
        BitacoraModelo.registrarEvento(usuarioActual, "Agregar Servicio", "Éxito", "Se abrió la ventana para agregar servicios");
        AgregarSVista agregarVista = new AgregarSVista();
        new AgregarSControlador(agregarVista);
        agregarVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirModificarServicio() {
        BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Información", "Se pide el ID del servicio");
        String idStr = JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL SERVICIO:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (ServiciosModelo.buscarServicio(id) != null) {
                    ModificarSVista modificarVista = new ModificarSVista();
                    new ModificarSControlador(modificarVista, id);
                    modificarVista.setVisible(true);
                    vista.setVisible(false);
                } else {
                    BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado no existe");
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado no es válido");
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarServicio() {
        BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Información", "Se pide el ID del servicio");
        String idStr = JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL SERVICIO:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (ServiciosModelo.eliminarServicio(id)) {
                    BitacoraModelo.registrarEvento(usuarioActual, "Eliminación de Servicio", "Éxito", "Se eliminó el servicio correctamente");
                    JOptionPane.showMessageDialog(vista, "SERVICIO ELIMINADO CORRECTAMENTE");
                } else {
                    BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado no existe");
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                BitacoraModelo.registrarEvento(usuarioActual, "Validación de ID", "Error", "El ID ingresado no es válido");
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVerServicios() {
        BitacoraModelo.registrarEvento(usuarioActual, "Abrir Servicios", "Éxito", "Se abrió la ventana para los Servicios");
        VerSVista verVista = new VerSVista();
        new VerSControlador(verVista);
        verVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void regresar() {
        BitacoraModelo.registrarEvento(usuarioActual, "Cierre de Agregar Servicios", "Éxito", "Se cerró la ventana de Servicios");
        MenuAVista menuVista = new MenuAVista();
        new MenuAControlador(menuVista, "admin");
        menuVista.setVisible(true);
        vista.dispose();
    }
}