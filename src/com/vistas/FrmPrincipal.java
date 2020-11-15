package com.vistas;

import com.utilidades.ValidarAccesos;

/**
 * Nombre de la clase: FrmPrincipal 
 * Fecha: 1/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 12/11/2020 
 * Version: 1.4
 * @author pedro
 */
public class FrmPrincipal extends javax.swing.JFrame {

    static public String nivel;
    private FrmProyecto formulario = null;
    private FrmUsuario usuario = null;
    private FrmUbicacion ubicacion = null;
    private FrmRoles roles = null;
    private FrmEmpleado empleado = null;
    private FrmPagoEmpleado pagoEmpleado = null;
    private FrmMaquinaria maquinaria = null;
    private FrmTipoMaquinaria tipoMaquinaria= null;

    public FrmPrincipal() {
        initComponents();
        this.setTitle("Menu Principal");
        //setResizable(true);
        setLocationRelativeTo(null);
        setExtendedState(FrmPrincipal.MAXIMIZED_BOTH);
        nivelAcceso();
    }

    public void nivelAcceso() {
        ValidarAccesos obj = new ValidarAccesos();

        try {

            switch (obj.getAcceso().getTipoNivel()) {
                case "Administrador":
                    break;
                default:
                    //bloquea la accion de los item de menu
                    ubicacionItem.setEnabled(false);
                    usuarioItem.setEnabled(false);
                    ubicacionItem.setEnabled(false);
                    rolesItem.setEnabled(false);
                    empleadoItem.setEnabled(false);
                    pagoEmpleadoItem.setEnabled(false);
                    maquinariaItem.setEnabled(false);
                    tipoMaquinaria.setEnabled(false);
                    
                    ubicacionItem.setVisible(false);
                    usuarioItem.setVisible(false);
                    ubicacionItem.setVisible(false);
                    rolesItem.setVisible(false);
                    empleadoItem.setVisible(false);
                    pagoEmpleadoItem.setVisible(false);
                    maquinariaItem.setVisible(false);
                    tipoMaquinaria.setVisible(false);
                    break;
            }
        } catch (Exception e) {
        }
    }

        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dskPrincipal = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        ubicacionItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        usuarioItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        ubicacionesItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        rolesItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        empleadoItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        pagoEmpleadoItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        tipoMaquinariaITEM = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        maquinariaItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout dskPrincipalLayout = new javax.swing.GroupLayout(dskPrincipal);
        dskPrincipal.setLayout(dskPrincipalLayout);
        dskPrincipalLayout.setHorizontalGroup(
            dskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 555, Short.MAX_VALUE)
        );
        dskPrincipalLayout.setVerticalGroup(
            dskPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 377, Short.MAX_VALUE)
        );

        fileMenu.setMnemonic('f');
        fileMenu.setText("Ventanas");

        ubicacionItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/proyecto.png"))); // NOI18N
        ubicacionItem.setText("Proyectos");
        ubicacionItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubicacionItemActionPerformed(evt);
            }
        });
        fileMenu.add(ubicacionItem);
        fileMenu.add(jSeparator1);

        usuarioItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/usuario.png"))); // NOI18N
        usuarioItem.setText("Usuarios");
        usuarioItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usuarioItemActionPerformed(evt);
            }
        });
        fileMenu.add(usuarioItem);
        fileMenu.add(jSeparator2);

        ubicacionesItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/pin-de-mapa.png"))); // NOI18N
        ubicacionesItem.setText("Ubicaciones");
        ubicacionesItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubicacionesItemActionPerformed(evt);
            }
        });
        fileMenu.add(ubicacionesItem);
        fileMenu.add(jSeparator3);

        rolesItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nivel.png"))); // NOI18N
        rolesItem.setText("Roles");
        rolesItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rolesItemActionPerformed(evt);
            }
        });
        fileMenu.add(rolesItem);
        fileMenu.add(jSeparator4);

        empleadoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/niveles.png"))); // NOI18N
        empleadoItem.setText("Empleados");
        empleadoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empleadoItemActionPerformed(evt);
            }
        });
        fileMenu.add(empleadoItem);
        fileMenu.add(jSeparator5);

        pagoEmpleadoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/financiero.png"))); // NOI18N
        pagoEmpleadoItem.setText("Pago Empleados");
        pagoEmpleadoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagoEmpleadoItemActionPerformed(evt);
            }
        });
        fileMenu.add(pagoEmpleadoItem);
        fileMenu.add(jSeparator6);

        tipoMaquinariaITEM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-configuración-3-24.png"))); // NOI18N
        tipoMaquinariaITEM.setText("Tipo Maquinaria");
        tipoMaquinariaITEM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoMaquinariaITEMActionPerformed(evt);
            }
        });
        fileMenu.add(tipoMaquinariaITEM);
        fileMenu.add(jSeparator7);

        maquinariaItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/maquinaria-pesada.png"))); // NOI18N
        maquinariaItem.setText("Maquinaria");
        maquinariaItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maquinariaItemActionPerformed(evt);
            }
        });
        fileMenu.add(maquinariaItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Reportes");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPrincipal)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ubicacionItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubicacionItemActionPerformed
        if (formulario == null || formulario.isClosed()) {
            formulario = new FrmProyecto();
            this.dskPrincipal.add(formulario);
        }
        formulario.setVisible(true);
    }//GEN-LAST:event_ubicacionItemActionPerformed

    private void usuarioItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usuarioItemActionPerformed
        if (usuario == null || usuario.isClosed()) {
            usuario = new FrmUsuario();
            this.dskPrincipal.add(usuario);
        }
        usuario.setVisible(true);
    }//GEN-LAST:event_usuarioItemActionPerformed

    private void ubicacionesItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubicacionesItemActionPerformed
        if(ubicacion == null || ubicacion.isClosed()){
            ubicacion = new FrmUbicacion();
            this.dskPrincipal.add(ubicacion);
        }
        ubicacion.setVisible(true);
    }//GEN-LAST:event_ubicacionesItemActionPerformed

    private void rolesItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rolesItemActionPerformed
        if(roles == null || roles.isClosed()){
            roles = new FrmRoles();
            this.dskPrincipal.add(roles);
        }
        roles.setVisible(true);
    }//GEN-LAST:event_rolesItemActionPerformed

    private void empleadoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empleadoItemActionPerformed
        if(empleado == null || empleado.isClosed()){
            empleado = new FrmEmpleado();
            this.dskPrincipal.add(empleado);
        }
        empleado.setVisible(true);
    }//GEN-LAST:event_empleadoItemActionPerformed

    private void pagoEmpleadoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagoEmpleadoItemActionPerformed
        if(pagoEmpleado == null || pagoEmpleado.isClosed()){
            pagoEmpleado = new FrmPagoEmpleado();
            this.dskPrincipal.add(pagoEmpleado);
        }
        pagoEmpleado.setVisible(true);
    }//GEN-LAST:event_pagoEmpleadoItemActionPerformed

    private void maquinariaItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maquinariaItemActionPerformed
        if(maquinaria == null || maquinaria.isClosed()){
            maquinaria = new FrmMaquinaria();
            this.dskPrincipal.add(maquinaria);
        }
        maquinaria.setVisible(true);
    }//GEN-LAST:event_maquinariaItemActionPerformed

    private void tipoMaquinariaITEMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoMaquinariaITEMActionPerformed
        if(tipoMaquinaria== null||tipoMaquinaria.isClosed()){
            tipoMaquinaria = new FrmTipoMaquinaria();
            
            this.dskPrincipal.add(tipoMaquinaria);
        }
        tipoMaquinaria.setVisible(true);
    }//GEN-LAST:event_tipoMaquinariaITEMActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JDesktopPane dskPrincipal;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem empleadoItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JMenuItem maquinariaItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem pagoEmpleadoItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem rolesItem;
    private javax.swing.JMenuItem tipoMaquinariaITEM;
    private javax.swing.JMenuItem ubicacionItem;
    private javax.swing.JMenuItem ubicacionesItem;
    private javax.swing.JMenuItem usuarioItem;
    // End of variables declaration//GEN-END:variables

}
