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
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Nombre de la calse: FrmMaquinaria
 * Fecha: 09/11/2020
 * CopyRigth:ITCA-FEPADE
 * modificación:10/11/2020 
 * Versión:1.1
 * @author Jose Marroquin
 */
public class FrmMaquinaria extends javax.swing.JInternalFrame {

    MaquinariaJpaController daoMaquinaria = new MaquinariaJpaController();
    ProyectoJpaController daoProyecto = new ProyectoJpaController();
    TipomaquinariaJpaController daoTipoMaquinaria= new TipomaquinariaJpaController();
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
            this.TablaDatos.setModel(tabla);
        } catch (Exception e) {
            message.printMessageAlerts("¡Error: " + e.getMessage() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        public void llenarTabla() {
        int fila = this.TablaDatos.getSelectedRow();
        this.txtIdMaquinaria.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 0)));
        this.txtNombreMaquinaria.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 1)));
        this.txtPeso.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 2)));
        this.txtAnioAdquisicion.setText(String.valueOf(this.TablaDatos.getValueAt(fila, 3)));
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
    }

    public void setearValores() {
        maquinaria.setIdMaquinaria(0);
        maquinaria.setNombreMaquina(txtNombreMaquinaria.getText());
        maquinaria.setPeso(Double.parseDouble(txtPeso.getText()));
        maquinaria.setAnioAdquisicion(txtAnioAdquisicion.getText());
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
        txtIdMaquinaria.setText("");
        txtNombreMaquinaria.setText("");
        txtPeso.setText("");
        txtAnioAdquisicion.setText("");
        txtPrecio.setText("");
        txtLargo.setText("");
        txtAncho.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbProyecto.setSelectedIndex(0);
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
        txtAnioAdquisicion = new javax.swing.JTextField();
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

        setClosable(true);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Gestión de Maquinaria");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("ID Maquinaria:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setText("Peso Maquinaria:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setText("Nombre Maquinaria");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setText("Año de adquisicion:");

        txtIdMaquinaria.setEditable(false);
        txtIdMaquinaria.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

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

        txtAnioAdquisicion.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txtNombreMaquinaria.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setText("Proyecto:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setText("Tipo:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel8.setText("Largo:");

        txtLargo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel9.setText("Ancho:");

        txtAncho.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        cmbTipo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txtPrecio.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("Precio:");

        cmbProyecto.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txtPeso.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtPeso, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                            .addComponent(txtIdMaquinaria)
                                            .addComponent(cmbProyecto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtLargo)))
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNombreMaquinaria, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtAnioAdquisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(75, 75, 75)
                                        .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)
                                .addGap(74, 74, 74)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(btnNuevoRegistro)
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEliminar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar)))
                        .addGap(0, 49, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtIdMaquinaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cmbProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtLargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtNombreMaquinaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtAnioAdquisicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevoRegistro)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        deshabilitar();
    }//GEN-LAST:event_btnCancelarMouseClicked


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
    private javax.swing.JTextField txtAnioAdquisicion;
    private javax.swing.JTextField txtIdMaquinaria;
    private javax.swing.JTextField txtLargo;
    private javax.swing.JTextField txtNombreMaquinaria;
    private javax.swing.JTextField txtPeso;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
