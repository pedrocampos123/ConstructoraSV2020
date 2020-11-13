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
import com.utilidades.ValidarCampos;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre de Formulario: FrmUbicacion
 * Fecha: 07/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 12/11/2020
 * Version: 1.1
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
        this.setTitle("Ubicaciones");
        //setResizable(false);
        jPanel1.setOpaque(false);
        mostrarDatos();
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Ubicacion", "Nombre", "Latitud", "Longitud"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[4];
        try {
            List lista;
            lista = daoUbicacion.findUbicacionEntities();
            
            if(lista != null)
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
        try {
            this.txtCodigo.setText("");
            latitud = "";
            longitud = "";
            nombre = "";
            modificacion = false;
        } catch (Exception e) {
        }
    }

    public void llenarTabla() {
        try {
            int fila = this.TablaUbicaciones.getSelectedRow();
            this.txtCodigo.setText(String.valueOf(this.TablaUbicaciones.getValueAt(fila, 0)));
            nombre = (String.valueOf(this.TablaUbicaciones.getValueAt(fila, 1)));
            latitud = (String.valueOf(this.TablaUbicaciones.getValueAt(fila, 2)));
            longitud = (String.valueOf(this.TablaUbicaciones.getValueAt(fila, 3)));
            modificacion = true;
        } catch (Exception e) {
        }
    }

    public void setearValores() {
        try {
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
        } catch (Exception e) {
        }
    
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
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMapa.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/mapa.png"))); // NOI18N
        btnMapa.setMargin(new java.awt.Insets(0, 4, 0, 4));
        btnMapa.setMaximumSize(new java.awt.Dimension(29, 29));
        btnMapa.setMinimumSize(new java.awt.Dimension(29, 29));
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
        jPanel1.add(btnMapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 76, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gestión del ubicaciones");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 190, -1));
        jLabel1.getAccessibleContext().setAccessibleName("Gestión del ubicaciones.");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Código:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        txtCodigo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtCodigo.setEnabled(false);
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });
        jPanel1.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 143, -1));

        btnLimpiar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-cancelar-24.png"))); // NOI18N
        btnLimpiar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        btnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/add.png"))); // NOI18N
        btnNuevo.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoMouseClicked(evt);
            }
        });
        jPanel1.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        btnInsertar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnInsertar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-guardar-24.png"))); // NOI18N
        btnInsertar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnInsertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInsertarMouseClicked(evt);
            }
        });
        jPanel1.add(btnInsertar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-basura-24.png"))); // NOI18N
        btnEliminar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, -1, -1));

        btnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-editar-archivo-24.png"))); // NOI18N
        btnModificar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Seleccionar las coordenadas por medio del mapa");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, -1));

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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 510, 117));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 530, 290));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Fondo.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 330));

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
        modificacion = false;
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
        }else{
            FrmGenerarMapa mapa = new FrmGenerarMapa(false, 0, 0, "");
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCodigo;
    // End of variables declaration//GEN-END:variables
}
