/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vistas;

import com.controller.RolJpaController;
import com.entities.Rol;
import com.utilidades.Mensajeria;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre del Formulario: FrmRoles
 * Fecha: 09/11/2020 
 * CopyRight: Pedro Campos
 * modificación:09/11/2020 
 * Version: 1.0
 * @author pedro
 */
public class FrmRoles extends javax.swing.JInternalFrame {

    RolJpaController daoRol = new RolJpaController();
    Rol rol = new Rol();
    Mensajeria message = new Mensajeria();
    
    public FrmRoles() {
        initComponents();
        mostrarDatos();
        deshabilitar();
    }

    public void deshabilitar() {
        txtNombre.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    public void habilitar() {
        txtNombre.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Rol", "Tipo"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[2];
        try {
            List lista;
            lista = daoRol.findRolEntities();
            for (int i = 0; i < lista.size(); i++) {
                rol = (Rol) lista.get(i);
                datos[0] = rol.getIdRol();
                datos[1] = rol.getTipo();
                tabla.addRow(datos);
            }
            this.TablaDatos.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void llenarTabla() {
        Rol list;
        int fila = this.TablaDatos.getSelectedRow();
        this.txtIdRol.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 0)));
        this.txtNombre.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 1)));
    }

    public void limpiarCampos() {
        txtIdRol.setText("");
        txtNombre.setText("");
    }

    public void setearValores() {
        rol.setIdRol(0);
        rol.setTipo(txtNombre.getText());
    }

    public void insertar() {
        try {
            setearValores();

            daoRol.create(rol);

            mostrarDatos();
            limpiarCampos();

            message.printMessageAlerts("¡Registro insertado correctamente!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            message.printMessageAlerts("¡Error!", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificar() {
        try {
            int respuesta = message.printMessageConfirm("¿Desea modificar los datos?", "Mensaje", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                setearValores();

                rol.setIdRol(Integer.parseInt(txtIdRol.getText()));
                daoRol.edit(rol);

                mostrarDatos();
                limpiarCampos();

                message.printMessageAlerts("¡Registro modificado correctamente!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarDatos();
                limpiarCampos();
            }
        } catch (Exception e) {
            message.printMessageAlerts("¡Error!", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        try {
            int respuesta = message.printMessageConfirm("¿Desea eliminar los datos?", "Mensaje", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                setearValores();

                daoRol.destroy(Integer.parseInt(txtIdRol.getText()));

                mostrarDatos();
                limpiarCampos();

                message.printMessageAlerts("¡Registro eliminados correctamente!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarDatos();
                limpiarCampos();
            }
        } catch (Exception e) {
            message.printMessageAlerts("¡Error!", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIdRol = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        btnNuevoRegistro = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Gestión de roles");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("ID Rol:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setText("Nombre:");

        txtIdRol.setEditable(false);
        txtIdRol.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        TablaDatos.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        TablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaDatosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablaDatos);

        btnNuevoRegistro.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnNuevoRegistro.setText("Nuevo");
        btnNuevoRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoRegistroMouseClicked(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
        });

        btnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdRol, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNuevoRegistro)
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEliminar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(172, 172, 172)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtIdRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevoRegistro)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TablaDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaDatosMouseClicked
        llenarTabla();
    }//GEN-LAST:event_TablaDatosMouseClicked

    private void btnNuevoRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoRegistroMouseClicked
        habilitar();
        limpiarCampos();
    }//GEN-LAST:event_btnNuevoRegistroMouseClicked

    private void btnGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseClicked
        insertar();
    }//GEN-LAST:event_btnGuardarMouseClicked

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        modificar();
    }//GEN-LAST:event_btnModificarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        eliminar();
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevoRegistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtIdRol;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
