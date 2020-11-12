/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interfaces;

import com.entities.Ubicacion;


/**
 * Nombre de la interface: OperacionesUbicacion
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public interface OperacionesUbicacion {
    public void setearDatosMapa(double newLatitud, double newLongitud, String newNombre);
    public Ubicacion getLocation();
    public void limpiarLocation();
}
