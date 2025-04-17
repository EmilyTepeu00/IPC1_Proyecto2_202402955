package vista;

import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class AgregarCAVista extends javax.swing.JFrame {

    public AgregarCAVista() {
        initComponents();
        DragAndDrop();
    }

    //CONFIGURACION DEL DRAG AND DROP
    private void DragAndDrop() {
        areaDrop.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    File file = files.get(0);

                    if (!file.getName().endsWith(".tmca")) {
                        JOptionPane.showMessageDialog(AgregarCAVista.this, 
                            "EL ARCHIVO DEBE TENER EXTENSION .tmca", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    Scanner scanner = new Scanner(file);
                    if (scanner.hasNextLine()) {
                        String linea = scanner.nextLine();
                        String[] partes = linea.split("-");

                        if (partes.length != 6) {
                            JOptionPane.showMessageDialog(AgregarCAVista.this, 
                                "FORMATO DE ARCHIVO INCORRECTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        campoID.setText(partes[0]);
                        campoNombre.setText(partes[1]);
                        campoUsuario.setText(partes[2]);
                        campoContraseña.setText(partes[3]);
                        campoTipoCliente.setText(partes[4]);
                        campoAutomovil.setText(partes[5]);

                        return true;
                    }

                    scanner.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(AgregarCAVista.this, 
                        "ERROR AL PROCESAR EL ARCHIVO: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                return false;
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        campoAutomovil = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        campoTipoCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        botonAceptar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        areaDrop = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        botonRegresar = new javax.swing.JButton();
        campoID = new javax.swing.JTextField();
        campoUsuario = new javax.swing.JTextField();
        campoContraseña = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("AGREGAR CLIENTE Y AUTO");

        campoAutomovil.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("NOMBRE:");

        campoNombre.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("USUARIO:");

        campoTipoCliente.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("IDENTIFICADOR:");

        botonAceptar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonAceptar.setText("ACEPTAR");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel5.setText("CONTRASEÑA:");

        areaDrop.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        areaDrop.setForeground(new java.awt.Color(153, 153, 153));
        areaDrop.setText("Arrastre un archivo tmca aca");

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel6.setText("TIPO CLIENTE:");

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel7.setText("AUTOMOVIL:");

        botonRegresar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonRegresar.setText("REGRESAR");

        campoID.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        campoUsuario.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        campoContraseña.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(areaDrop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonRegresar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoAutomovil))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoTipoCliente))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoContraseña))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoUsuario))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoNombre))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoID, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 50, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(campoID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(campoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(campoContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(campoTipoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(campoAutomovil, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonRegresar)
                    .addComponent(botonAceptar)
                    .addComponent(areaDrop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //GETTERS
    public javax.swing.JTextField getCampoID() {
        return campoID;
    }

    public javax.swing.JTextField getCampoNombre() {
        return campoNombre;
    }

    public javax.swing.JTextField getCampoUsuario() {
        return campoUsuario;
    }

    public javax.swing.JTextField getCampoContraseña() {
        return campoContraseña;
    }

    public javax.swing.JTextField getCampoTipoCliente() {
        return campoTipoCliente;
    }

    public javax.swing.JTextField getCampoAutomovil() {
        return campoAutomovil;
    }

    public javax.swing.JButton getBotonAceptar() {
        return botonAceptar;
    }

    public javax.swing.JButton getBotonRegresar() {
        return botonRegresar;
    }

    public javax.swing.JLabel getAreaDrop() {
        return areaDrop;
    }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AgregarCAVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarCAVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarCAVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarCAVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgregarCAVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaDrop;
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonRegresar;
    private javax.swing.JTextField campoAutomovil;
    private javax.swing.JTextField campoContraseña;
    private javax.swing.JTextField campoID;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextField campoTipoCliente;
    private javax.swing.JTextField campoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
