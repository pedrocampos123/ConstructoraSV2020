/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vistas;

import com.controller.ProyectoJpaController;
import com.controller.UbicacionJpaController;
import com.entities.Proyecto;
import com.entities.Ubicacion;
import com.utilidades.Mensajeria;
import com.utilidades.ValidarAccesos;
import com.utilidades.ValidarCampos;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre de Formulario: FrmUbicacion
 * Fecha: 07/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 07/11/2020
 * Version: 1.0
 * @author pedro
 */
public class FrmUbicacion extends javax.swing.JInternalFrame {

    Mensajeria message = new Mensajeria();

    UbicacionJpaController daoUbicacion = new UbicacionJpaController();

    Ubicacion location = new Ubicacion();
    ValidarCampos validarCampos = new ValidarCampos();
    
    String nombre;
    String latitud;
    String longitud;
    boolean modificacion;
    
    public FrmUbicacion() {
        initComponents();
        setResizable(false);
        mostrarDatos();
        nivelAcceso();
    }
    
    public void nivelAcceso() {
        ValidarAccesos obj = new ValidarAccesos();

        try {

            switch (obj.getAcceso().getTipoNivel()) {
                case "Administrador":
                    //desbloquea la accion de los botones del formulario
                    habilitar();
                    break;
                case "Empleado":
                    //bloquea la accion de los botones del formulario
                    deshabilitar();
                    break;
                case "Invitado":
                    //bloquea la accion de los botones del formulario
                    deshabilitar();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Ubicacion", "Nombre", "Latitud", "Longitud"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[4];
        try {
            List lista;
            lista = daoUbicacion.findUbicacionEntities();
            for (int i = 0; i < lista.size(); i++) {
                location = (Ubicacion) lista.get(i);
                datos[0] = location.getIdUbicacion();
                datos[1] = location.getNombre();
                datos[2] = location.getLatitud();
                datos[3] = location.getLongitud();
                tabla.addRow(datos);
            }
            this.TablaUbicaciones.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deshabilitar() {
        this.btnInsertar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
        this.btnLimpiar.setEnabled(true);
    }

    public void habilitar() {
        this.btnInsertar.setEnabled(true);
        this.btnModificar.setEnabled(true);
        this.btnEliminar.setEnabled(true);
        this.btnLimpiar.setEnabled(true);
    }

    public void limpiarCampos() {
        this.txtCodigo.setText("");
        latitud = "";
        longitud = "";
        nombre = "";
        modificacion = false;
    }

    public void llenarTabla() {
        int fila = this.TablaUbicaciones.getSelectedRow();
        this.txtCodigo.setText(String.valueOf(this.TablaUbicaciones.getValueAt(fila, 0)));
        nombre = (String.valueOf(this.TablaUbicaciones.getValueAt(fila, 1)));
        latitud = (String.valueOf(this.TablaUbicaciones.getValueAt(fila, 2)));
        longitud = (String.valueOf(this.TablaUbicaciones.getValueAt(fila, 3)));
        modificacion = true;
    }

    public void setearValores() {
        //se coloca cero por el autoincrement de la base de datos
        Ubicacion newLocation = new Ubicacion();
        
        newLocation = location.getLocation();
        
        if(newLocation.getLatitud() == 0.0){
            message.printMessageAlerts("¡Debe seleccionar las coordenadas\npor medio del mapa!", "Mensaje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        location.setIdUbicacion(0);
        location.setNombre(newLocation.getNombre());
        location.setLatitud(newLocation.getLatitud());
        location.setLongitud(newLocation.getLongitud());    
    }

    public void insertar() {
        try {
            setearValores();

            //ingresar el registro a la base de datos a la tabla de proyectos
            daoUbicacion.create(location);
            message.printMessageAlerts("¡Los datos se han ingresado correctamente!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            mostrarDatos();
            limpiarCampos();
            
            //limpia la location para asignar una nueva
            location.limpiarLocation();
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificar() {
        try {
            setearValores();
            
            location.setIdUbicacion(Integer.parseInt(this.txtCodigo.getText()));
            
            int respuesta = message.printMessageConfirm("¿Desea modificar los datos?", "Mensaje", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                daoUbicacion.edit(location);
                message.printMessageAlerts("!Datos modificados con exito!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                mostrarDatos();
                limpiarCampos();
            } else {
                mostrarDatos();
                limpiarCampos();
            }
            
            //limpia la location para asignar una nueva
            location.limpiarLocation();
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        try {
            location.setIdUbicacion(Integer.parseInt(this.txtCodigo.getText()));
            
            int respuesta = message.printMessageConfirm("¿Desea modificar los datos?", "Mensaje", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                
                //validar si la ubicacion esta asignada a un proyecto
                Proyecto proyecto = new Proyecto();
                proyecto = validarProyectoUbicacion(location.getIdUbicacion());
                
                if(proyecto.getIdProyecto() != null){
                    message.printMessageAlerts("¡Lamentablemente no se puede eliminar.\nDebido a que la ubicacion esta asignada a un proyecto actualmente.!", "Mensaje", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                daoUbicacion.destroy(location.getIdUbicacion());
                message.printMessageAlerts("!Datos eliminados con exito!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                mostrarDatos();
                limpiarCampos();
            } else {
                mostrarDatos();
                limpiarCampos();
            }
            
            //limpia la location para asignar una nueva
            location.limpiarLocation();
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Proyecto validarProyectoUbicacion(int idUbicacion){
        Proyecto proyecto = new Proyecto();
        ProyectoJpaController daoProyecto = new ProyectoJpaController();
        try {
            proyecto = daoProyecto.getProyectoUbicacion(idUbicacion);
            
            if(proyecto.getIdProyecto() != null){
               return proyecto; 
            }
            else
                return new Proyecto();
        } catch (Exception e) {
            return new Proyecto();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnMapa = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnInsertar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaUbicaciones = new javax.swing.JTable();

        setClosable(true);

        btnMapa.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnMapa.setText("Mapa");
        btnMapa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMapaMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMapaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnMapaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMapaMouseReleased(evt);
            }
        });
        btnMapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMapaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Gestión del ubicaciones");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("Código:");

        txtCodigo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtCodigo.setEnabled(false);
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnLimpiar.setText("Cancelar");
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoMouseClicked(evt);
            }
        });

        btnInsertar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnInsertar.setText("Guardar");
        btnInsertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInsertarMouseClicked(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });

        btnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setText("Seleccionar las coordenadas por medio del mapa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNuevo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnInsertar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimpiar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(23, 23, 23)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnMapa))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnInsertar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jLabel1.getAccessibleContext().setAccessibleName("Gestión del ubicaciones.");

        TablaUbicaciones.setModel(new javax.swing.table.DefaultTableModel(
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
        TablaUbicaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaUbicacionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaUbicaciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TablaUbicacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaUbicacionesMouseClicked
        llenarTabla();
    }//GEN-LAST:event_TablaUbicacionesMouseClicked

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        if(modificacion){
            if (latitud.equals("") && longitud.equals("") && nombre.equals("")) {
                message.printMessageAlerts("¡Debe seleccionar un registro a modificar.!", "Mensaje", JOptionPane.ERROR_MESSAGE);
            }else{
                modificar(); 
            }
        }else{
            message.printMessageAlerts("¡Debe seleccionar un registro a modificar.!", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        eliminar();
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnInsertarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInsertarMouseClicked
        insertar();
    }//GEN-LAST:event_btnInsertarMouseClicked

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnNuevoMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        validarCampos.onlyNumbres(evt);
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void btnMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapaActionPerformed

    }//GEN-LAST:event_btnMapaActionPerformed

    private void btnMapaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseReleased

    }//GEN-LAST:event_btnMapaMouseReleased

    private void btnMapaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMousePressed

    }//GEN-LAST:event_btnMapaMousePressed

    private void btnMapaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseExited

    }//GEN-LAST:event_btnMapaMouseExited

    private void btnMapaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseClicked
        if(modificacion){
            FrmGenerarMapa mapa = new FrmGenerarMapa(modificacion, Double.parseDouble(latitud), Double.parseDouble(longitud), nombre);
            mapa.setVisible(true);
        }
    }//GEN-LAST:event_btnMapaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaUbicaciones;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMapa;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCodigo;
    // End of variables declaration//GEN-END:variables
}
