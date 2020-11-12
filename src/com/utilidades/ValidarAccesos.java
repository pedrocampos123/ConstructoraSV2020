/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utilidades;

import com.controller.RolJpaController;
import com.entities.Rol;
import com.interfaces.OperacionesNivelAcceso;
import java.util.List;

/**
 * Nombre de la clase: ValidarAccesos Fecha: 06/11/2020 CopyRigth: Pedro Campos
 * Modificacion: 06/11/2020 Version: 1.0
 *
 * @author pedro
 */
public class ValidarAccesos implements OperacionesNivelAcceso {

    private int nivel;
    private String tipoNivel;

    public ValidarAccesos() {
    }

    public ValidarAccesos(int nivel, String tipoNivel) {
        this.nivel = nivel;
        this.tipoNivel = tipoNivel;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getTipoNivel() {
        return tipoNivel;
    }

    public void setTipoNivel(String tipoNivel) {
        this.tipoNivel = tipoNivel;
    }

    public static ValidarAccesos newNivelAcceso = new ValidarAccesos();

    @Override
    public void setearNivelAcceso(int nivel, String tipoAcceso) {
        newNivelAcceso.setNivel(nivel);
        newNivelAcceso.setTipoNivel(tipoAcceso);
    }

    @Override
    public ValidarAccesos getAcceso() {
        return newNivelAcceso;
    }

}
