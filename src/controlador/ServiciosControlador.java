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

public class ServiciosControlador {
    private ServiciosVista vista;
    
    public ServiciosControlador(ServiciosVista vista) {
        this.vista = vista;
        
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
        AgregarSVista agregarVista = new AgregarSVista();
        new AgregarSControlador(agregarVista);
        agregarVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirModificarServicio() {
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
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarServicio() {
        String idStr = JOptionPane.showInputDialog(vista, "INGRESE EL ID DEL SERVICIO:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                if (ServiciosModelo.eliminarServicio(id)) {
                    JOptionPane.showMessageDialog(vista, "SERVICIO ELIMINADO CORRECTAMENTE");
                } else {
                    JOptionPane.showMessageDialog(vista, "EL ID INGRESADO NO EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "ID INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVerServicios() {
        VerSVista verVista = new VerSVista();
        new VerSControlador(verVista);
        verVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void regresar() {
        MenuAVista menuVista = new MenuAVista();
        new MenuAControlador(menuVista, "admin");
        menuVista.setVisible(true);
        vista.dispose();
    }
}