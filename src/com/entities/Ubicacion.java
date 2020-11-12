/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import com.interfaces.OperacionesUbicacion;
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
 * Nombre de la clase: Ubicacion
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
    @NamedQuery(name = "Ubicacion.findAll", query = "SELECT u FROM Ubicacion u")
    , @NamedQuery(name = "Ubicacion.findByIdUbicacion", query = "SELECT u FROM Ubicacion u WHERE u.idUbicacion = :idUbicacion")
    , @NamedQuery(name = "Ubicacion.findByNombre", query = "SELECT u FROM Ubicacion u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Ubicacion.findByLatitud", query = "SELECT u FROM Ubicacion u WHERE u.latitud = :latitud")
    , @NamedQuery(name = "Ubicacion.findByLongitud", query = "SELECT u FROM Ubicacion u WHERE u.longitud = :longitud")})
public class Ubicacion implements Serializable, OperacionesUbicacion {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idUbicacion;
    @Column(length = 100)
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 22)
    private Double latitud;
    @Column(precision = 22)
    private Double longitud;
    @OneToMany(mappedBy = "idUbicacion")
    private Collection<Proyecto> proyectoCollection;

    public Ubicacion() {
    }

    public Ubicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    @XmlTransient
    public Collection<Proyecto> getProyectoCollection() {
        return proyectoCollection;
    }

    public void setProyectoCollection(Collection<Proyecto> proyectoCollection) {
        this.proyectoCollection = proyectoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUbicacion != null ? idUbicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ubicacion)) {
            return false;
        }
        Ubicacion other = (Ubicacion) object;
        if ((this.idUbicacion == null && other.idUbicacion != null) || (this.idUbicacion != null && !this.idUbicacion.equals(other.idUbicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Ubicacion[ idUbicacion=" + idUbicacion + " ]";
    }
    
    public static Ubicacion newLocation = new Ubicacion();
    
    @Override
    public void setearDatosMapa(double newLatitud, double newLongitud, String newNombre) {
        try {
            newLocation.setLatitud(newLatitud);
            newLocation.setLongitud(newLongitud);
            newLocation.setNombre(newNombre);  
        } catch (Exception e) {
        }
        
    }

    @Override
    public Ubicacion getLocation() {
        return newLocation;
    }

    @Override
    public void limpiarLocation() {
        newLocation = new Ubicacion();
    }
    
}
