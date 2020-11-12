package com.vistas;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.controller.ProyectoJpaController;
import com.controller.UbicacionJpaController;
import com.entities.Proyecto;
import com.entities.Ubicacion;
import com.utilidades.Mensajeria;
import com.utilidades.ValidarCampos;

/**
 * Nombre de la clase: FrmProyecto 
 * Fecha: 01/11/2020 CopyRight: 
 * Pedro Campos
 * modificación:05/11/2020 
 * Version: 1.1
 * @author pedro
 */
public class FrmProyecto extends javax.swing.JInternalFrame {

    Mensajeria message = new Mensajeria();

    ProyectoJpaController daoProy = new ProyectoJpaController();
    UbicacionJpaController daoUbicacion = new UbicacionJpaController();

    Proyecto proy = new Proyecto();
    ValidarCampos validarCampos = new ValidarCampos();

    public FrmProyecto() {
        initComponents();
        setResizable(false);
        mostrarDatos();
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Proyecto", "Nombre del proyecto", "Fecha Inicio", "Tiempo estimado",
            "Presupuesto total", "Ubicacion"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[6];
        try {
            List lista;
            lista = daoProy.getAllProyects();
            for (int i = 0; i < lista.size(); i++) {
                proy = (Proyecto) lista.get(i);
                datos[0] = proy.getIdProyecto();
                datos[1] = proy.getNombreProyecto();
                datos[2] = proy.getFechaInicio();
                datos[3] = proy.getTiempoEstimado();
                datos[4] = proy.getPrecioTotal();
                datos[5] = proy.getIdUbicacion().getIdUbicacion();
                tabla.addRow(datos);
            }
            this.TablaProyecto.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deshabilitar() {
        txtProyectoName.setEnabled(false);
        txtFechaInicio.setEnabled(false);
        txtTiempo.setEnabled(false);
        txtPrecio.setEnabled(false);
        this.btnInsertar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
        this.btnLimpiar.setEnabled(true);
    }

    public void habilitar() {
        txtProyectoName.setEnabled(true);
        txtFechaInicio.setEnabled(true);
        txtTiempo.setEnabled(true);
        txtPrecio.setEnabled(true);
        this.btnInsertar.setEnabled(true);
        this.btnModificar.setEnabled(true);
        this.btnEliminar.setEnabled(true);
        this.btnLimpiar.setEnabled(true);
    }

    public void limpiarCampos() {
        this.txtCodigo.setText("");
        this.txtProyectoName.setText("");
        this.txtFechaInicio.setText("");
        this.txtTiempo.setText("");
        this.txtPrecio.setText("");
    }

    public void llenarTabla() {
        int fila = this.TablaProyecto.getSelectedRow();
        this.txtCodigo.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 0)));
        this.txtProyectoName.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 1)));
        this.txtFechaInicio.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 2)));
        this.txtTiempo.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 3)));
        this.txtPrecio.setText(validarCampos.numberFormat(String.valueOf(this.TablaProyecto.getValueAt(fila, 4))));
    }

    public void setearValores() {
        //se coloca cero por el autoincrement de la base de datos
        proy.setIdProyecto(0);
        proy.setNombreProyecto(this.txtProyectoName.getText());
        proy.setFechaInicio(this.txtFechaInicio.getText());
        proy.setTiempoEstimado(this.txtTiempo.getText());
        String precio = this.txtPrecio.getText().replace("$", "").replace(",", "");
        proy.setPrecioTotal(Double.parseDouble(precio));
    }

    public void insertar() {
        try {
            setearValores();
            //guardar ubicacion
            daoUbicacion.create(Ubicacion.newLocation);

            //recuperacion del ultimo registro ingresado
            Ubicacion locacion = new Ubicacion();
            locacion = daoUbicacion.getLastUbication();

            //valida si no retorna vacio
            if (locacion.getIdUbicacion() == 0) {
                throw new Exception();
            }

            //setear valores de ubicacion
            proy.setIdUbicacion(locacion);

            //ingresar el registro a la base de datos a la tabla de proyectos
            daoProy.create(proy);
            message.printMessageAlerts("¡Los datos se han ingresado correctamente!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            mostrarDatos();
            limpiarCampos();
            
            //limpia la location para asignar una nueva
            locacion.limpiarLocation();
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificar() {
        try {
            setearValores();
            
            //modificar location si se cambio la ubicacion
            Ubicacion locacion = new Ubicacion();
            Ubicacion recuperar = new Ubicacion();
            if(locacion.newLocation.getLatitud() != 0){
                locacion.setearDatosMapa(locacion.newLocation.getLatitud(), locacion.newLocation.getLongitud(), locacion.newLocation.getNombre());
                locacion.newLocation.setIdUbicacion(proy.getIdUbicacion().getIdUbicacion());
                
                recuperar = locacion.getLocation();
                daoUbicacion.edit(recuperar);
            }
            
            proy.setIdProyecto(Integer.parseInt(this.txtCodigo.getText()));
            int respuesta = message.printMessageConfirm("¿Desea modificar los datos?", "Mensaje", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                daoProy.edit(proy);
                message.printMessageAlerts("!Datos modificados con exito!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                mostrarDatos();
                limpiarCampos();
            } else {
                mostrarDatos();
                limpiarCampos();
            }
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        try {
            proy.setIdProyecto(Integer.parseInt(this.txtCodigo.getText()));
            int respuesta = message.printMessageConfirm("¿Desea modificar los datos?", "Mensaje", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                daoProy.destroy(proy.getIdProyecto());
                message.printMessageAlerts("!Datos eliminados con exito!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                mostrarDatos();
                limpiarCampos();
            } else {
                mostrarDatos();
                limpiarCampos();
            }
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaProyecto = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtProyectoName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnMapa = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        txtTiempo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnInsertar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        txtFechaInicio = new javax.swing.JFormattedTextField();

        setClosable(true);

        TablaProyecto.setModel(new javax.swing.table.DefaultTableModel(
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
        TablaProyecto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaProyectoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaProyecto);

        txtProyectoName.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtProyectoName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtProyectoNameKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setText("Fecha Inicio:");

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
        jLabel1.setText("Gestión del proyecto.");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("Código proyecto:");

        txtCodigo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtCodigo.setEnabled(false);
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setText("Proyecto: ");

        btnLimpiar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnLimpiar.setText("Cancelar");
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });

        txtTiempo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtTiempo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTiempoKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setText("Tiempo estimado en meses:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setText("Presupuesto Total: ");

        txtPrecio.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
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

        try {
            txtFechaInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFechaInicio.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(72, 72, 72)
                                    .addComponent(btnNuevo)
                                    .addGap(31, 31, 31)
                                    .addComponent(btnInsertar)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnModificar)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnEliminar)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnLimpiar))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addGap(61, 61, 61)
                                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(235, 235, 235)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(76, 76, 76)
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(112, 112, 112)
                                        .addComponent(txtProyectoName, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4))
                    .addComponent(txtProyectoName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMapa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel6))
                    .addComponent(txtTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo)
                    .addComponent(btnInsertar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(620, 620, 620)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TablaProyectoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaProyectoMouseClicked
        llenarTabla();
    }//GEN-LAST:event_TablaProyectoMouseClicked

    private void btnInsertarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInsertarMouseClicked
        insertar();
    }//GEN-LAST:event_btnInsertarMouseClicked

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        modificar();
    }//GEN-LAST:event_btnModificarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        eliminar();
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked
        habilitar();
        limpiarCampos();
    }//GEN-LAST:event_btnNuevoMouseClicked

    private void btnMapaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseClicked
        FrmGenerarMapa mapa = new FrmGenerarMapa(false, 0, 0, "");
        mapa.setVisible(true);
    }//GEN-LAST:event_btnMapaMouseClicked

    private void btnMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapaActionPerformed

    }//GEN-LAST:event_btnMapaActionPerformed

    private void btnMapaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseExited

    }//GEN-LAST:event_btnMapaMouseExited

    private void btnMapaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseReleased

    }//GEN-LAST:event_btnMapaMouseReleased

    private void btnMapaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMousePressed

    }//GEN-LAST:event_btnMapaMousePressed

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        validarCampos.numbersAndPoint(evt, txtTiempo);
    }//GEN-LAST:event_txtPrecioKeyTyped

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        validarCampos.onlyNumbres(evt);
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void txtProyectoNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProyectoNameKeyTyped
        
    }//GEN-LAST:event_txtProyectoNameKeyTyped

    private void txtTiempoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTiempoKeyTyped
        
    }//GEN-LAST:event_txtTiempoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaProyecto;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JFormattedTextField txtFechaInicio;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtProyectoName;
    private javax.swing.JTextField txtTiempo;
    // End of variables declaration//GEN-END:variables
}
