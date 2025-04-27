package vista;

import controlador.MenuAControlador;

public class MenuAVista extends javax.swing.JFrame {

    public MenuAVista() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonRepuestos = new javax.swing.JButton();
        botonServicios = new javax.swing.JButton();
        botonClientes = new javax.swing.JButton();
        botonProgreso = new javax.swing.JButton();
        botonReporte = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        botonCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        botonRepuestos.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonRepuestos.setText("REPUESTOS");

        botonServicios.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonServicios.setText("SERVICIOS");

        botonClientes.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonClientes.setText("<html><center>CLIENTES<br>Y AUTOS</center></html>");

        botonProgreso.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonProgreso.setText("<html><center>PROGRESO<br>DE AUTOS</center></html>");

        botonReporte.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonReporte.setText("REPORTES");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("MENU ADMINISTRATIVO");

        botonCerrar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        botonCerrar.setText("Cerrar Sesión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(botonServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(botonClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(botonProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(botonReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(82, 82, 82))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(botonCerrar)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonRepuestos)
                    .addComponent(botonServicios))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(botonReporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(botonCerrar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //Getters
    public javax.swing.JButton getBotonCerrar() {
        return botonCerrar;
    }

    public javax.swing.JButton getBotonClientes() {
        return botonClientes;
    }

    public javax.swing.JButton getBotonProgreso() {
        return botonProgreso;
    }

    public javax.swing.JButton getBotonReporte() {
        return botonReporte;
    }   

    public javax.swing.JButton getBotonRepuestos() {
        return botonRepuestos;
    }

    public javax.swing.JButton getBotonServicios() {
        return botonServicios;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCerrar;
    private javax.swing.JButton botonClientes;
    private javax.swing.JButton botonProgreso;
    private javax.swing.JButton botonReporte;
    private javax.swing.JButton botonRepuestos;
    private javax.swing.JButton botonServicios;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
