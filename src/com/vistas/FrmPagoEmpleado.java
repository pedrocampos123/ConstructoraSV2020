/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vistas;

import com.controller.EmpleadoJpaController;
import com.controller.PagoempleadoJpaController;
import com.entities.Empleado;
import com.entities.Pagoempleado;
import com.entities.Proyecto;
import com.utilidades.ComboItem;
import com.utilidades.Mensajeria;
import com.utilidades.ValidarCampos;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre del Formulario: FrmPagoEmpleado
 * Fecha: 09/11/2020 
 * CopyRight: Pedro Campos
 * modificación: 12/11/2020 
 * Version: 1.1
 * @author pedro
 */
public class FrmPagoEmpleado extends javax.swing.JInternalFrame {

    EmpleadoJpaController daoEmpleado = new EmpleadoJpaController();
    PagoempleadoJpaController daoPagoEmpleado = new PagoempleadoJpaController();
    Pagoempleado pagoEmpleado = new Pagoempleado();
    Empleado empleado = new Empleado();
    Mensajeria message = new Mensajeria();
    ValidarCampos validarCampos = new ValidarCampos();
    
    public FrmPagoEmpleado() {
        initComponents();
        this.setTitle("Pago de Empleados");
        //setResizable(false);
        jPanel1.setOpaque(false);
        cargarComboRol(cmbEmpleado, (List<Empleado>) daoEmpleado.findEmpleadoEntities());
        mostrarDatos();
        deshabilitar();
    }

    public void deshabilitar() {
        txtSalario.setEnabled(false);
        cmbEmpleado.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    public void habilitar() {
        txtSalario.setEnabled(true);
        cmbEmpleado.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Pago", "Salario", "Empleado"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[3];
        try {
            List lista;
            lista = daoPagoEmpleado.findPagoempleadoEntities();
            
            if(lista != null)
                for (int i = 0; i < lista.size(); i++) {
                    pagoEmpleado = (Pagoempleado) lista.get(i);
                    datos[0] = pagoEmpleado.getIdPago();
                    datos[1] = pagoEmpleado.getPago();
                    datos[2] = pagoEmpleado.getIdEmpleado().getIdEmpleado();
                    tabla.addRow(datos);
                }
            this.TablaDatos.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void llenarTabla() {
        try {
            int fila = this.TablaDatos.getSelectedRow();
            this.txtIdPago.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 0)));
            this.txtSalario.setText(validarCampos.numberFormat(String.valueOf(this.TablaDatos.getValueAt(fila, 1))));

            int Seleccionado = Integer.parseInt(String.valueOf(this.TablaDatos.getValueAt(fila, 2)));

            for (Empleado obj : daoEmpleado.getEmpleado(Seleccionado)) {
                this.cmbEmpleado.getModel().setSelectedItem(obj.getNombreEmpleado());
            }
        } catch (Exception e) {
        }
    }

    private void cargarComboRol(JComboBox combo, List<Empleado> list) {
        for (Empleado item : list) {
            combo.addItem(new ComboItem(item.getIdEmpleado(), item.getNombreEmpleado()));
        }
    }

    public void limpiarCampos() {
        try {
            txtIdPago.setText("");
            txtSalario.setText("");
            cmbEmpleado.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

    public void setearValores() {
        try {
            pagoEmpleado.setIdPago(0);
        
            String precio = this.txtSalario.getText().replace("$", "").replace(",", "");
            pagoEmpleado.setPago(Double.parseDouble(precio));

            //recuperar datos cmbEmpleado
            String Seleccionado = cmbEmpleado.getSelectedItem().toString();
            ComboItem item = new ComboItem();

            for (int i = 0; i < cmbEmpleado.getItemCount(); i++) {
                if (Seleccionado.equals(cmbEmpleado.getItemAt(i).toString())) {
                    item = cmbEmpleado.getModel().getElementAt(i);
                }
            }
            empleado.setIdEmpleado(item.getValue());

            pagoEmpleado.setIdEmpleado(empleado);
        } catch (Exception e) {
        }
    }

    public void insertar() {
        try {
            setearValores();

            daoPagoEmpleado.create(pagoEmpleado);

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

                pagoEmpleado.setIdPago(Integer.parseInt(txtIdPago.getText()));
                daoPagoEmpleado.edit(pagoEmpleado);

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

                daoPagoEmpleado.destroy(Integer.parseInt(txtIdPago.getText()));

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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIdPago = new javax.swing.JTextField();
        cmbEmpleado = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        btnNuevoRegistro = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtSalario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gestión de pagos empleados");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 0, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Pago:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Salario:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 35, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Empleado:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 73, -1, -1));

        txtIdPago.setEditable(false);
        txtIdPago.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jPanel1.add(txtIdPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 32, 111, -1));

        cmbEmpleado.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jPanel1.add(cmbEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 70, 140, -1));

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

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 152, 401, 123));

        btnNuevoRegistro.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnNuevoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/add.png"))); // NOI18N
        btnNuevoRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoRegistroMouseClicked(evt);
            }
        });
        jPanel1.add(btnNuevoRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 108, -1, -1));

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-guardar-24.png"))); // NOI18N
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 108, -1, -1));

        btnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-editar-archivo-24.png"))); // NOI18N
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 108, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-basura-24.png"))); // NOI18N
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 108, -1, -1));

        btnCancelar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-cancelar-24.png"))); // NOI18N
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMouseClicked(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 108, -1, -1));

        txtSalario.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtSalario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalarioKeyTyped(evt);
            }
        });
        jPanel1.add(txtSalario, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 32, 140, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 430, 285));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Fondo.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 330));

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

    private void txtSalarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalarioKeyTyped
        validarCampos.numbersAndPoint(evt, txtSalario);
    }//GEN-LAST:event_txtSalarioKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevoRegistro;
    private javax.swing.JComboBox<ComboItem> cmbEmpleado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtIdPago;
    private javax.swing.JTextField txtSalario;
    // End of variables declaration//GEN-END:variables
}
