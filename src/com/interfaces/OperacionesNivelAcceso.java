/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interfaces;

import com.utilidades.ValidarAccesos;

/**
 * Nombre de la interface: OperacionesNivelAcceso
 * Fecha: 06/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 06/11/2020
 * Version: 1.0
 * @author pedro
 */
public interface OperacionesNivelAcceso {
    public void setearNivelAcceso(int nivel, String tipoAcceso);
    public ValidarAccesos getAcceso();
}
