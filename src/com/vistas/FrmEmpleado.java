 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vistas;

import com.controller.EmpleadoJpaController;
import com.controller.ProyectoJpaController;
import com.entities.Empleado;
import com.entities.Proyecto;
import com.utilidades.ComboItem;
import com.utilidades.Mensajeria;
import com.utilidades.ValidarCampos;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre del Formulario: FrmEmpleado
 * Fecha: 09/11/2020 
 * CopyRight: Pedro Campos
 * modificación:09/11/2020 
 * Version: 1.0
 * @author pedro
 */
public class FrmEmpleado extends javax.swing.JInternalFrame {

    ProyectoJpaController daoProyecto = new ProyectoJpaController();
    EmpleadoJpaController daoEmpleado = new EmpleadoJpaController();
    Proyecto proyecto = new Proyecto();
    Empleado empleado = new Empleado();
    Mensajeria message = new Mensajeria();
    ValidarCampos validarCampos = new ValidarCampos();
    
    public FrmEmpleado() {
        initComponents();
        cargarComboRol(cmbProyecto, (List<Proyecto>) daoProyecto.findProyectoEntities());
        mostrarDatos();
        deshabilitar();
    }

    public void deshabilitar() {
        txtNombre.setEnabled(false);
        txtSalario.setEnabled(false);
        cmbProyecto.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    public void habilitar() {
        txtNombre.setEnabled(true);
        txtSalario.setEnabled(true);
        cmbProyecto.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Empleado", "Nombre", "Salario", "Proyecto"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[4];
        try {
            List lista;
            lista = daoEmpleado.findEmpleadoEntities();
            for (int i = 0; i < lista.size(); i++) {
                empleado = (Empleado) lista.get(i);
                datos[0] = empleado.getIdEmpleado();
                datos[1] = empleado.getNombreEmpleado();
                datos[2] = empleado.getSalario();
                datos[3] = empleado.getIdProyecto().getIdProyecto();
                tabla.addRow(datos);
            }
            this.TablaDatos.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void llenarTabla() {
        int fila = this.TablaDatos.getSelectedRow();
        this.txtIdEmpleado.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 0)));
        this.txtNombre.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 1)));
        this.txtSalario.setText(validarCampos.numberFormat(String.valueOf(this.TablaDatos.getValueAt(fila, 2))));
        
        //Selecciona en ComboBox
        int Seleccionado = Integer.parseInt(String.valueOf(this.TablaDatos.getValueAt(fila, 3)));

        for (Proyecto obj : daoProyecto.getProyecto(Seleccionado)) {
            this.cmbProyecto.getModel().setSelectedItem(obj.getNombreProyecto());
        }
    }

    private void cargarComboRol(JComboBox combo, List<Proyecto> list) {
        for (Proyecto item : list) {
            combo.addItem(new ComboItem(item.getIdProyecto(), item.getNombreProyecto()));
        }
    }

    public void limpiarCampos() {
        txtIdEmpleado.setText("");
        txtNombre.setText("");
        txtSalario.setText("");
        cmbProyecto.setSelectedIndex(0);
    }

    public void setearValores() {
        empleado.setIdEmpleado(0);
        empleado.setNombreEmpleado(txtNombre.getText());
        String precio = this.txtSalario.getText().replace("$", "").replace(",", "");
        empleado.setSalario(Double.parseDouble(precio));
        
        //recuperar datos cmbProyecto
        String Seleccionado = cmbProyecto.getSelectedItem().toString();
        ComboItem item = new ComboItem();

        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            if (Seleccionado.equals(cmbProyecto.getItemAt(i).toString())) {
                item = cmbProyecto.getModel().getElementAt(i);
            }
        }
        //Seguimos obteniendo del cmbProyecto
        proyecto.setIdProyecto(item.getValue());
        
        empleado.setIdProyecto(proyecto);
    }

    public void insertar() {
        try {
            setearValores();

            daoEmpleado.create(empleado);

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

                empleado.setIdEmpleado(Integer.parseInt(txtIdEmpleado.getText()));
                daoEmpleado.edit(empleado);

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

                daoEmpleado.destroy(Integer.parseInt(txtIdEmpleado.getText()));

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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIdEmpleado = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        cmbProyecto = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        btnNuevoRegistro = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtSalario = new javax.swing.JTextField();

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Gestión de empleados");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("ID Empleado:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setText("Salario:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setText("Proyecto:");

        txtIdEmpleado.setEditable(false);
        txtIdEmpleado.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        cmbProyecto.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

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

        txtSalario.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbProyecto, 0, 140, Short.MAX_VALUE)
                            .addComponent(txtSalario))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevoRegistro)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(153, 153, 153))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
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
    private javax.swing.JComboBox<ComboItem> cmbProyecto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSalario;
    // End of variables declaration//GEN-END:variables
}
