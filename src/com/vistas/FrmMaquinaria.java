/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vistas;

import com.controller.MaquinariaJpaController;
import com.controller.ProyectoJpaController;
import com.controller.TipomaquinariaJpaController;
import com.entities.Maquinaria;
import com.entities.Proyecto;
import com.entities.Tipomaquinaria;
import com.utilidades.ComboItem;
import com.utilidades.Mensajeria;
import com.utilidades.ValidarCampos;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre del Formulario: FrmMaquinaria 
 * Fecha: 09/11/2020 
 * CopyRigth: ITCA-FEPADE
 * modificación: 12/11/2020 
 * Versión: 1.1
 * @author Jose Marroquin
 */
public class FrmMaquinaria extends javax.swing.JInternalFrame {

    MaquinariaJpaController daoMaquinaria = new MaquinariaJpaController();
    ProyectoJpaController daoProyecto = new ProyectoJpaController();
    TipomaquinariaJpaController daoTipoMaquinaria = new TipomaquinariaJpaController();
    Tipomaquinaria tipoMaquinaria = new Tipomaquinaria();
    Maquinaria maquinaria = new Maquinaria();
    Proyecto proyecto = new Proyecto();
    Mensajeria message = new Mensajeria();
    ValidarCampos validarCampos = new ValidarCampos();

    /**
     * Creates new form FrmMaquinaria
     */
    public FrmMaquinaria() {
        initComponents();
        ((JTextField) this.txtAnioAdquisicion.getDateEditor()).setEditable(false); 
        this.setTitle("Maquinaria");
        //setResizable(false);
        jPanel1.setOpaque(false);
        cargarComboProyecto(cmbProyecto, (List<Proyecto>) daoProyecto.findProyectoEntities());
        cargarComboTipoMaquinaria(cmbTipo, (List<Tipomaquinaria>) daoTipoMaquinaria.findTipomaquinariaEntities());
        mostrarDatos();
        deshabilitar();
    }

    public void deshabilitar() {
        txtNombreMaquinaria.setEnabled(false);
        txtPeso.setEnabled(false);
        txtAnioAdquisicion.setEnabled(false);
        cmbTipo.setEnabled(false);
        cmbProyecto.setEnabled(false);
        txtAncho.setEnabled(false);
        txtLargo.setEnabled(false);
        txtPrecio.setEnabled(false);

        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    public void habilitar() {
        txtNombreMaquinaria.setEnabled(true);
        txtPeso.setEnabled(true);
        txtAnioAdquisicion.setEnabled(true);
        cmbTipo.setEnabled(true);
        cmbProyecto.setEnabled(true);
        txtAncho.setEnabled(true);
        txtLargo.setEnabled(true);
        txtPrecio.setEnabled(true);

        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    public void mostrarDatos() {
        DefaultTableModel tabla;
        String encabezados[] = {"ID Maquinaria", "Nombre Maquinaria", "Peso", "Año de Adquisición",
            "Precio", "Largo", "Ancho", "Tipo", "Proyecto"};
        tabla = new DefaultTableModel(null, encabezados);
        Object datos[] = new Object[9];
        try {
            List lista;
            lista = daoMaquinaria.findMaquinariaEntities();

            if (lista != null) {
                for (int i = 0; i < lista.size(); i++) {
                    maquinaria = (Maquinaria) lista.get(i);
                    datos[0] = maquinaria.getIdMaquinaria();
                    datos[1] = maquinaria.getNombreMaquina();
                    datos[2] = maquinaria.getPeso();
                    datos[3] = maquinaria.getAnioAdquisicion();
                    datos[4] = maquinaria.getPrecio();
                    datos[5] = maquinaria.getLargo();
                    datos[6] = maquinaria.getAncho();
                    datos[7] = maquinaria.getIdTipo().getIdTipo();
                    datos[8] = maquinaria.getIdProyecto().getIdProyecto();
                    tabla.addRow(datos);
                }
            }
            this.TablaDatos.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void llenarTabla() {
        try {
            int fila = this.TablaDatos.getSelectedRow();
            this.txtIdMaquinaria.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 0)));
            this.txtNombreMaquinaria.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 1)));
            this.txtPeso.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 2)));
            
            try {
                String sDate1 = String.valueOf(this.TablaDatos.getValueAt(fila, 2));  
                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                this.txtAnioAdquisicion.setDate(date1);
            } catch (Exception e) {
            }
            
            //this.txtAnioAdquisicion.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 3)));
            this.txtPrecio.setText(validarCampos.numberFormat(String.valueOf(this.TablaDatos.getValueAt(fila, 4))));
            this.txtLargo.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 5)));
            this.txtAncho.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 6)));

            int tipoSeleccionado = Integer.parseInt(String.valueOf(this.TablaDatos.getValueAt(fila, 7)));

            for (Tipomaquinaria obj : daoTipoMaquinaria.getTipoMaquinariaSeleccionada(tipoSeleccionado)) {
                cmbTipo.getModel().setSelectedItem(obj.getNombre());
            }

            int proyectoSeleccionado = Integer.parseInt(String.valueOf(this.TablaDatos.getValueAt(fila, 8)));

            for (Proyecto obj : daoProyecto.getProyecto(proyectoSeleccionado)) {
                cmbProyecto.getModel().setSelectedItem(obj.getNombreProyecto());
            }
        } catch (Exception e) {
        }
    }

    public void setearValores() {
        try {
            maquinaria.setIdMaquinaria(0);
            maquinaria.setNombreMaquina(txtNombreMaquinaria.getText());
            maquinaria.setPeso(Double.parseDouble(txtPeso.getText()));
            
            try {
                int year;
                int month;
                int day;
                
                year = txtAnioAdquisicion.getCalendar().get(Calendar.YEAR);
                month = txtAnioAdquisicion.getCalendar().get(Calendar.MONTH) + 1;
                day = txtAnioAdquisicion.getCalendar().get(Calendar.DAY_OF_MONTH);
                
                maquinaria.setAnioAdquisicion(day + "/" + month + "/"+ year);
            } catch (Exception e) {
            }
            
            //maquinaria.setAnioAdquisicion(txtAnioAdquisicion.getText());
            String precio = txtPrecio.getText().replace("$", "").replace(",", "");
            maquinaria.setPrecio(Double.parseDouble(precio));
            maquinaria.setLargo(Double.parseDouble(txtLargo.getText()));
            maquinaria.setAncho(Double.parseDouble(txtAncho.getText()));

            //recuperar datos cmbTipo
            String tipoMaquinariaSeleccionado = cmbTipo.getSelectedItem().toString();
            ComboItem itemTipo = new ComboItem();

            for (int i = 0; i < cmbTipo.getItemCount(); i++) {
                if (tipoMaquinariaSeleccionado.equals(cmbTipo.getItemAt(i).toString())) {
                    itemTipo = cmbTipo.getModel().getElementAt(i);
                }
            }

            tipoMaquinaria.setIdTipo(itemTipo.getValue());
            maquinaria.setIdTipo(tipoMaquinaria);

            //recuperar datos cmbProyecto
            String proyectoSeleccionado = cmbProyecto.getSelectedItem().toString();
            ComboItem itemProyecto = new ComboItem();

            for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
                if (proyectoSeleccionado.equals(cmbProyecto.getItemAt(i).toString())) {
                    itemProyecto = cmbProyecto.getModel().getElementAt(i);
                }
            }

            proyecto.setIdProyecto(itemProyecto.getValue());
            maquinaria.setIdProyecto(proyecto);
        } catch (Exception e) {
        }
    }

    private void cargarComboTipoMaquinaria(JComboBox combo, List<Tipomaquinaria> list) {
        for (Tipomaquinaria item : list) {
            combo.addItem(new ComboItem(item.getIdTipo(), item.getNombre()));
        }
    }

    private void cargarComboProyecto(JComboBox combo, List<Proyecto> list) {
        for (Proyecto item : list) {
            combo.addItem(new ComboItem(item.getIdProyecto(), item.getNombreProyecto()));
        }
    }

    public void insertar() {
        try {
            setearValores();

            daoMaquinaria.create(maquinaria);

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

                maquinaria.setIdMaquinaria(Integer.parseInt(txtIdMaquinaria.getText()));
                daoMaquinaria.edit(maquinaria);

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

                daoMaquinaria.destroy(Integer.parseInt(txtIdMaquinaria.getText()));

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

    public void limpiarCampos() {
        try {
            txtIdMaquinaria.setText("");
            txtNombreMaquinaria.setText("");
            txtPeso.setText("");
            this.txtAnioAdquisicion.setCalendar(null);
            txtPrecio.setText("");
            txtLargo.setText("");
            txtAncho.setText("");
            cmbTipo.setSelectedIndex(0);
            cmbProyecto.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIdMaquinaria = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        btnNuevoRegistro = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtNombreMaquinaria = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtLargo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAncho = new javax.swing.JTextField();
        cmbTipo = new javax.swing.JComboBox<>();
        txtPrecio = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cmbProyecto = new javax.swing.JComboBox<>();
        txtPeso = new javax.swing.JTextField();
        txtAnioAdquisicion = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();

        setClosable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gestión de Maquinaria");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 0, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Maquinaria:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Peso Maquinaria:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 78, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre Maquinaria");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 40, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Año de adquisicion:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 78, -1, -1));

        txtIdMaquinaria.setEditable(false);
        txtIdMaquinaria.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jPanel1.add(txtIdMaquinaria, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 37, 140, -1));

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

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 271, 545, 123));

        btnNuevoRegistro.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnNuevoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/add.png"))); // NOI18N
        btnNuevoRegistro.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnNuevoRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoRegistroMouseClicked(evt);
            }
        });
        jPanel1.add(btnNuevoRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 230, -1, -1));

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-guardar-24.png"))); // NOI18N
        btnGuardar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, -1, -1));

        btnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-editar-archivo-24.png"))); // NOI18N
        btnModificar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 230, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-basura-24.png"))); // NOI18N
        btnEliminar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 230, -1, -1));

        btnCancelar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-cancelar-24.png"))); // NOI18N
        btnCancelar.setMargin(new java.awt.Insets(2, 6, 2, 6));
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMouseClicked(evt);
            }
        });
        jPanel1.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 230, -1, -1));

        txtNombreMaquinaria.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtNombreMaquinaria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreMaquinariaKeyTyped(evt);
            }
        });
        jPanel1.add(txtNombreMaquinaria, new org.netbeans.lib.awtextra.AbsoluteConstraints(414, 37, 140, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Proyecto:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 116, -1, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Tipo:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 116, -1, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Largo:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 154, -1, -1));

        txtLargo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtLargo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLargoKeyTyped(evt);
            }
        });
        jPanel1.add(txtLargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 151, 140, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Ancho:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 154, -1, -1));

        txtAncho.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtAncho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAnchoKeyTyped(evt);
            }
        });
        jPanel1.add(txtAncho, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 151, 140, -1));

        cmbTipo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jPanel1.add(cmbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(414, 113, 140, -1));

        txtPrecio.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });
        jPanel1.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 189, 140, -1));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Precio:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 192, -1, -1));

        cmbProyecto.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jPanel1.add(cmbProyecto, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 113, 140, -1));

        txtPeso.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        txtPeso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesoKeyTyped(evt);
            }
        });
        jPanel1.add(txtPeso, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 75, 140, -1));

        txtAnioAdquisicion.setDateFormatString("dd-MM-yyyy");
        jPanel1.add(txtAnioAdquisicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 130, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 570, 407));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Fondo.jpg"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 630, 460));

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
        deshabilitar();
    }//GEN-LAST:event_btnCancelarMouseClicked

    private void txtPesoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoKeyTyped
        validarCampos.numbersAndPoint(evt, txtPeso);
    }//GEN-LAST:event_txtPesoKeyTyped

    private void txtLargoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLargoKeyTyped
        validarCampos.numbersAndPoint(evt, txtLargo);
    }//GEN-LAST:event_txtLargoKeyTyped

    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioKeyTyped
        validarCampos.numbersAndPointAndComa(evt, txtPrecio);
    }//GEN-LAST:event_txtPrecioKeyTyped

    private void txtNombreMaquinariaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreMaquinariaKeyTyped
        validarCampos.spaceAndWords(evt, txtNombreMaquinaria);
    }//GEN-LAST:event_txtNombreMaquinariaKeyTyped

    private void txtAnchoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnchoKeyTyped
        validarCampos.numbersAndPoint(evt, txtAncho);
    }//GEN-LAST:event_txtAnchoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevoRegistro;
    private javax.swing.JComboBox<ComboItem> cmbProyecto;
    private javax.swing.JComboBox<ComboItem> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JTextField txtAncho;
    private com.toedter.calendar.JDateChooser txtAnioAdquisicion;
    private javax.swing.JTextField txtIdMaquinaria;
    private javax.swing.JTextField txtLargo;
    private javax.swing.JTextField txtNombreMaquinaria;
    private javax.swing.JTextField txtPeso;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
