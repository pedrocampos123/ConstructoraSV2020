package com.vistas;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.controller.ProyectoJpaController;
import com.controller.UbicacionJpaController;
import com.entities.Proyecto;
import com.entities.Ubicacion;
import com.toedter.calendar.JCalendar;
import com.utilidades.Mensajeria;
import com.utilidades.ValidarCampos;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTextField;

/**
 * Nombre de la clase: FrmProyecto 
 * Fecha: 01/11/2020 CopyRight: 
 * Pedro Campos
 * modificación: 12/11/2020 
 * Version: 1.2
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
        ((JTextField) this.jchFecha.getDateEditor()).setEditable(false);         
        this.setTitle("Proyectos");
        //setResizable(false);
        jPanel1.setOpaque(false);
        deshabilitar();
        mostrarDatos();
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Proyecto", "Nombre del proyecto", "Fecha Inicio", "Tiempo estimado",
            "Presupuesto total", "Ubicacion", "Estado"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[7];
        try {
            List lista;
            lista = daoProy.getAllProyects();
            
            if(lista != null)
                for (int i = 0; i < lista.size(); i++) {
                    proy = (Proyecto) lista.get(i);
                    datos[0] = proy.getIdProyecto();
                    datos[1] = proy.getNombreProyecto();
                    datos[2] = proy.getFechaInicio();
                    datos[3] = proy.getTiempoEstimado();
                    datos[4] = proy.getPrecioTotal();
                    datos[5] = proy.getIdUbicacion().getIdUbicacion();
                    datos[6] = proy.getEstado();
                    tabla.addRow(datos);
                }
            this.TablaProyecto.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deshabilitar() {
        txtProyectoName.setEnabled(false);
        jchFecha.setEnabled(false);
        txtTiempo.setEnabled(false);
        txtPrecio.setEnabled(false);
        this.cmbEstado.setEnabled(false);
        this.btnInsertar.setEnabled(false);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
        this.btnLimpiar.setEnabled(false);
    }

    public void habilitar() {
        txtProyectoName.setEnabled(true);
        jchFecha.setEnabled(true);
        txtTiempo.setEnabled(true);
        txtPrecio.setEnabled(true);
        this.cmbEstado.setEnabled(true);
        this.btnInsertar.setEnabled(true);
        this.btnModificar.setEnabled(true);
        this.btnEliminar.setEnabled(true);
        this.btnLimpiar.setEnabled(true);
    }

    public void limpiarCampos() {
        try {
            this.txtCodigo.setText("");
            this.txtProyectoName.setText("");
            this.jchFecha.setCalendar(null);
            this.txtTiempo.setText("");
            this.txtPrecio.setText("");
            this.cmbEstado.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

    public void llenarTabla() {
        try {
            int fila = this.TablaProyecto.getSelectedRow();
            this.txtCodigo.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 0)));
            this.txtProyectoName.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 1)));
            
            try {
                String sDate1 = String.valueOf(this.TablaProyecto.getValueAt(fila, 2));  
                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                this.jchFecha.setDate(date1);
            } catch (Exception e) {
            }
            
            //this.jchFecha.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 2)));
            this.txtTiempo.setText(String.valueOf(this.TablaProyecto.getValueAt(fila, 3)));
            this.txtPrecio.setText(validarCampos.numberFormat(String.valueOf(this.TablaProyecto.getValueAt(fila, 4))));
            this.cmbEstado.setSelectedItem(String.valueOf(this.TablaProyecto.getValueAt(fila, 6)));
        } catch (Exception e) {
        }
    }

    public void setearValores() {
        try {
            //se coloca cero por el autoincrement de la base de datos
            proy.setIdProyecto(0);
            proy.setNombreProyecto(this.txtProyectoName.getText());
            
            try {
                int year;
                int month;
                int day;
                
                year = jchFecha.getCalendar().get(Calendar.YEAR);
                month = jchFecha.getCalendar().get(Calendar.MONTH) + 1;
                day = jchFecha.getCalendar().get(Calendar.DAY_OF_MONTH);
                
                proy.setFechaInicio(year+ "-" + month + "-"+ day);
            } catch (Exception e) {
            }
            
            //proy.setFechaInicio(this.txtFechaInicio.getText());
            proy.setTiempoEstimado(this.txtTiempo.getText());
            String precio = this.txtPrecio.getText().replace("$", "").replace(",", "");
            proy.setPrecioTotal(Double.parseDouble(precio));
            proy.setEstado(this.cmbEstado.getSelectedItem().toString());
        } catch (Exception e) {
        }
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

            //setear valores de ubicación
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
            
            if(locacion.newLocation.getLatitud() != null)
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
        jLabel8 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaProyecto = new javax.swing.JTable();
        jchFecha = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();

        setClosable(true);
        setMaximumSize(new java.awt.Dimension(800, 800));
        setPreferredSize(new java.awt.Dimension(696, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtProyectoName.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtProyectoName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtProyectoNameKeyTyped(evt);
            }
        });
        jPanel1.add(txtProyectoName, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 60, 143, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Fecha Inicio:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(346, 43, -1, -1));

        btnMapa.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/mapa.png"))); // NOI18N
        btnMapa.setMargin(new java.awt.Insets(0, 4, 0, 4));
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
        jPanel1.add(btnMapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(346, 104, 84, 40));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gestión del proyecto.");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 0, 207, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Código proyecto:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 40, -1, -1));

        txtCodigo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtCodigo.setEnabled(false);
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });
        jPanel1.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 37, 143, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Proyecto: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 63, -1, -1));

        btnLimpiar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-cancelar-24.png"))); // NOI18N
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, -1, -1));

        txtTiempo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtTiempo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTiempoKeyTyped(evt);
            }
        });
        jPanel1.add(txtTiempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 93, 144, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tiempo estimado en meses:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 96, -1, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Presupuesto Total: ");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 122, -1, -1));

        txtPrecio.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });
        jPanel1.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 119, 144, -1));

        btnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/add.png"))); // NOI18N
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoMouseClicked(evt);
            }
        });
        jPanel1.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, -1, -1));

        btnInsertar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnInsertar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-guardar-24.png"))); // NOI18N
        btnInsertar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInsertarMouseClicked(evt);
            }
        });
        jPanel1.add(btnInsertar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-basura-24.png"))); // NOI18N
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, -1, -1));

        btnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-editar-archivo-24.png"))); // NOI18N
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, -1, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Estado:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(346, 76, -1, -1));

        cmbEstado.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "En Proceso", "Terminado" }));
        jPanel1.add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 73, 168, -1));

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
        jScrollPane2.setViewportView(TablaProyecto);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 570, 120));

        jchFecha.setDateFormatString("yyyy-MM-dd");
        jchFecha.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jchFecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jchFechaKeyTyped(evt);
            }
        });
        jPanel1.add(jchFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 170, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 640, 350));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Fondo.jpg"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 400));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        validarCampos.numbersAndPointAndComa(evt, txtPrecio);
    }//GEN-LAST:event_txtPrecioKeyTyped

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        validarCampos.onlyNumbres(evt);
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void txtProyectoNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProyectoNameKeyTyped
        validarCampos.spaceAndWords(evt, txtProyectoName);
    }//GEN-LAST:event_txtProyectoNameKeyTyped

    private void txtTiempoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTiempoKeyTyped
        
    }//GEN-LAST:event_txtTiempoKeyTyped

    private void btnMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapaActionPerformed

    }//GEN-LAST:event_btnMapaActionPerformed

    private void btnMapaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseReleased

    }//GEN-LAST:event_btnMapaMouseReleased

    private void btnMapaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMousePressed

    }//GEN-LAST:event_btnMapaMousePressed

    private void btnMapaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseExited

    }//GEN-LAST:event_btnMapaMouseExited

    private void btnMapaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMapaMouseClicked
        FrmGenerarMapa mapa = new FrmGenerarMapa(false, 0, 0, "");
        mapa.setVisible(true);
    }//GEN-LAST:event_btnMapaMouseClicked

    private void TablaProyectoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaProyectoMouseClicked
        llenarTabla();
    }//GEN-LAST:event_TablaProyectoMouseClicked

    private void jchFechaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jchFechaKeyTyped
       validarCampos.NoLetters(evt, jchFecha);
    }//GEN-LAST:event_jchFechaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaProyecto;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMapa;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jchFecha;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtProyectoName;
    private javax.swing.JTextField txtTiempo;
    // End of variables declaration//GEN-END:variables
}
