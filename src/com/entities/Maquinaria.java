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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Nombre de la clase: Maquinaria
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
    @NamedQuery(name = "Maquinaria.findAll", query = "SELECT m FROM Maquinaria m")
    , @NamedQuery(name = "Maquinaria.findByIdMaquinaria", query = "SELECT m FROM Maquinaria m WHERE m.idMaquinaria = :idMaquinaria")
    , @NamedQuery(name = "Maquinaria.findByNombreMaquina", query = "SELECT m FROM Maquinaria m WHERE m.nombreMaquina = :nombreMaquina")
    , @NamedQuery(name = "Maquinaria.findByPeso", query = "SELECT m FROM Maquinaria m WHERE m.peso = :peso")
    , @NamedQuery(name = "Maquinaria.findByAnioAdquisicion", query = "SELECT m FROM Maquinaria m WHERE m.anioAdquisicion = :anioAdquisicion")
    , @NamedQuery(name = "Maquinaria.findByPrecio", query = "SELECT m FROM Maquinaria m WHERE m.precio = :precio")
    , @NamedQuery(name = "Maquinaria.findByLargo", query = "SELECT m FROM Maquinaria m WHERE m.largo = :largo")
    , @NamedQuery(name = "Maquinaria.findByAncho", query = "SELECT m FROM Maquinaria m WHERE m.ancho = :ancho")})
public class Maquinaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idMaquinaria;
    @Column(length = 50)
    private String nombreMaquina;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 22)
    private Double peso;
    @Column(length = 30)
    private String anioAdquisicion;
    @Column(precision = 22)
    private Double precio;
    @Column(precision = 22)
    private Double largo;
    @Column(precision = 22)
    private Double ancho;
    @OneToMany(mappedBy = "idMaquinaria")
    private Collection<Detalleproyectomaquina> detalleproyectomaquinaCollection;
    @JoinColumn(name = "idTipo", referencedColumnName = "idTipo")
    @ManyToOne
    private Tipomaquinaria idTipo;
    @JoinColumn(name = "idProyecto", referencedColumnName = "idProyecto")
    @ManyToOne
    private Proyecto idProyecto;

    public Maquinaria() {
    }

    public Maquinaria(Integer idMaquinaria) {
        this.idMaquinaria = idMaquinaria;
    }

    public Integer getIdMaquinaria() {
        return idMaquinaria;
    }

    public void setIdMaquinaria(Integer idMaquinaria) {
        this.idMaquinaria = idMaquinaria;
    }

    public String getNombreMaquina() {
        return nombreMaquina;
    }

    public void setNombreMaquina(String nombreMaquina) {
        this.nombreMaquina = nombreMaquina;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getAnioAdquisicion() {
        return anioAdquisicion;
    }

    public void setAnioAdquisicion(String anioAdquisicion) {
        this.anioAdquisicion = anioAdquisicion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getLargo() {
        return largo;
    }

    public void setLargo(Double largo) {
        this.largo = largo;
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

    @XmlTransient
    public Collection<Detalleproyectomaquina> getDetalleproyectomaquinaCollection() {
        return detalleproyectomaquinaCollection;
    }

    public void setDetalleproyectomaquinaCollection(Collection<Detalleproyectomaquina> detalleproyectomaquinaCollection) {
        this.detalleproyectomaquinaCollection = detalleproyectomaquinaCollection;
    }

    public Tipomaquinaria getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Tipomaquinaria idTipo) {
        this.idTipo = idTipo;
    }

    public Proyecto getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Proyecto idProyecto) {
        this.idProyecto = idProyecto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMaquinaria != null ? idMaquinaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maquinaria)) {
            return false;
        }
        Maquinaria other = (Maquinaria) object;
        if ((this.idMaquinaria == null && other.idMaquinaria != null) || (this.idMaquinaria != null && !this.idMaquinaria.equals(other.idMaquinaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Maquinaria[ idMaquinaria=" + idMaquinaria + " ]";
    }
    
}
