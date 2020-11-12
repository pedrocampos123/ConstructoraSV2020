/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Nombre de la clase: Tipomaquinaria
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
@Entity
@Table(catalog = "pruebas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipomaquinaria.findAll", query = "SELECT t FROM Tipomaquinaria t")
    , @NamedQuery(name = "Tipomaquinaria.findByIdTipo", query = "SELECT t FROM Tipomaquinaria t WHERE t.idTipo = :idTipo")
    , @NamedQuery(name = "Tipomaquinaria.findByNombre", query = "SELECT t FROM Tipomaquinaria t WHERE t.nombre = :nombre")})
public class Tipomaquinaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idTipo;
    @Column(length = 50)
    private String nombre;
    @OneToMany(mappedBy = "idTipo")
    private Collection<Maquinaria> maquinariaCollection;

    public Tipomaquinaria() {
    }

    public Tipomaquinaria(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Maquinaria> getMaquinariaCollection() {
        return maquinariaCollection;
    }

    public void setMaquinariaCollection(Collection<Maquinaria> maquinariaCollection) {
        this.maquinariaCollection = maquinariaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipomaquinaria)) {
            return false;
        }
        Tipomaquinaria other = (Tipomaquinaria) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Tipomaquinaria[ idTipo=" + idTipo + " ]";
    }
    
}
