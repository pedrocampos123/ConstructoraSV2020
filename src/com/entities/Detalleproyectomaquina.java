/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Nombre de la clase: Detalleproyectomaquina
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
@Entity
@Table(catalog = "constructorasv", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalleproyectomaquina.findAll", query = "SELECT d FROM Detalleproyectomaquina d")
    , @NamedQuery(name = "Detalleproyectomaquina.findByIdDetalle", query = "SELECT d FROM Detalleproyectomaquina d WHERE d.idDetalle = :idDetalle")})
public class Detalleproyectomaquina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idDetalle;
    @JoinColumn(name = "idProyecto", referencedColumnName = "idProyecto")
    @ManyToOne
    private Proyecto idProyecto;
    @JoinColumn(name = "idMaquinaria", referencedColumnName = "idMaquinaria")
    @ManyToOne
    private Maquinaria idMaquinaria;

    public Detalleproyectomaquina() {
    }

    public Detalleproyectomaquina(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Proyecto getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Proyecto idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Maquinaria getIdMaquinaria() {
        return idMaquinaria;
    }

    public void setIdMaquinaria(Maquinaria idMaquinaria) {
        this.idMaquinaria = idMaquinaria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalle != null ? idDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleproyectomaquina)) {
            return false;
        }
        Detalleproyectomaquina other = (Detalleproyectomaquina) object;
        if ((this.idDetalle == null && other.idDetalle != null) || (this.idDetalle != null && !this.idDetalle.equals(other.idDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Detalleproyectomaquina[ idDetalle=" + idDetalle + " ]";
    }
    
}
