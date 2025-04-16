package vista;

import javax.swing.TransferHandler;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class AgregarSVista extends javax.swing.JFrame {

    public AgregarSVista() {
        initComponents();
        DragAndDrop();
    }
    
    //CONFIGURACION DEL DRAG AND DROP
    private void DragAndDrop() {
        areaDrop.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                try {
                    java.util.List<?> files = (java.util.List<?>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (files.isEmpty()) return false;
                    
                    java.io.File file = (java.io.File) files.get(0);
                    
                    if (!file.getName().toLowerCase().endsWith(".tms")) {
                        javax.swing.JOptionPane.showMessageDialog(AgregarSVista.this, "EL ARCHIVO DEBET TENER LA EXTENSION .tms", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    java.util.Scanner scanner = new java.util.Scanner(file);
                    if (scanner.hasNextLine()) {
                        String linea = scanner.nextLine();
                        String[] partes = linea.split("-");

                        if (partes.length != 5) {
                            javax.swing.JOptionPane.showMessageDialog(AgregarSVista.this, "FORMATO INCORRECTO", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        //LLENAR LOS CAMPOS CON LOS DATOS DEL ARCHIVO
                        campoNombre.setText(partes[0]);
                        campoMarca.setText(partes[1]);
                        campoModelo.setText(partes[2]);
                        
                        //PROCESAR LISTA DE REPUESTOS
                        if (!partes[3].isEmpty()) {
                            String[] idsRepuestos = partes[3].split("\\.");
                            if (idsRepuestos.length > 0) {
                                try {
                                    int idRepuesto = Integer.parseInt(idsRepuestos[0]);
                                    for (int i = 0; i < listaRepuestos.getItemCount(); i++) {
                                        String item = listaRepuestos.getItemAt(i);
                                        if (item.startsWith(idRepuesto + " - ")) {
                                            listaRepuestos.setSelectedIndex(i);
                                            break;
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                }
                            }
                        }
                        
                        campoPMano.setText(partes[4]);
                        return true;
                    }
                    scanner.close();
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        botonRegresar = new javax.swing.JButton();
        campoID = new javax.swing.JTextField();
        campoMarca = new javax.swing.JTextField();
        campoModelo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        campoPTotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        campoPMano = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        botonAceptar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        areaDrop = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        listaRepuestos = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel6.setText("PRECIO MANO DE OBRA:");

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel7.setText("PRECIO TOTAL:");

        botonRegresar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonRegresar.setText("REGRESAR");

        campoID.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        campoMarca.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        campoModelo.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("AGREGAR SERVICIOS");

        campoPTotal.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("NOMBRE:");

        campoNombre.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("MARCA:");

        campoPMano.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("ID SERVICIO:");

        botonAceptar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonAceptar.setText("ACEPTAR");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel5.setText("MODELO:");

        areaDrop.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        areaDrop.setForeground(new java.awt.Color(153, 153, 153));
        areaDrop.setText("Arrastre un archivo tmr aca");

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel8.setText("REPUESTO:");

        listaRepuestos.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        listaRepuestos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(areaDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonRegresar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jLabel1)
                        .addGap(0, 99, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listaRepuestos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoNombre))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoMarca))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoModelo))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoPMano))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoPTotal))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoID, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(campoMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(campoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(listaRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(campoPMano, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(campoPTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonRegresar)
                    .addComponent(botonAceptar)
                    .addComponent(areaDrop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //GETTERS
    public javax.swing.JButton getBotonAceptar() {
        return botonAceptar;
    }

    public javax.swing.JButton getBotonRegresar() {
        return botonRegresar;
    }

    public javax.swing.JTextField getCampoID() {
        return campoID;
    }

    public javax.swing.JTextField getCampoMarca() {
        return campoMarca;
    }

    public javax.swing.JTextField getCampoModelo() {
        return campoModelo;
    }

    public javax.swing.JTextField getCampoNombre() {
        return campoNombre;
    }

    public javax.swing.JTextField getCampoPMano() {
        return campoPMano;
    }

    public javax.swing.JTextField getCampoPTotal() {
        return campoPTotal;
    }

    public javax.swing.JComboBox<String> getListaRepuestos() {
        return listaRepuestos;
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
            java.util.logging.Logger.getLogger(AgregarSVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarSVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarSVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarSVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgregarSVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaDrop;
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonRegresar;
    private javax.swing.JTextField campoID;
    private javax.swing.JTextField campoMarca;
    private javax.swing.JTextField campoModelo;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextField campoPMano;
    private javax.swing.JTextField campoPTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JComboBox<String> listaRepuestos;
    // End of variables declaration//GEN-END:variables
}
