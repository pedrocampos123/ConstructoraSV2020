/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
 * Nombre de la clase: Proyecto
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
    @NamedQuery(name = "Proyecto.findAll", query = "SELECT p FROM Proyecto p")
    , @NamedQuery(name = "Proyecto.findByIdProyecto", query = "SELECT p FROM Proyecto p WHERE p.idProyecto = :idProyecto")
    , @NamedQuery(name = "Proyecto.findByNombreProyecto", query = "SELECT p FROM Proyecto p WHERE p.nombreProyecto = :nombreProyecto")
    , @NamedQuery(name = "Proyecto.findByFechaInicio", query = "SELECT p FROM Proyecto p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Proyecto.findByTiempoEstimado", query = "SELECT p FROM Proyecto p WHERE p.tiempoEstimado = :tiempoEstimado")
    , @NamedQuery(name = "Proyecto.findByPrecioTotal", query = "SELECT p FROM Proyecto p WHERE p.precioTotal = :precioTotal")
    , @NamedQuery(name = "Proyecto.findByEstado", query = "SELECT p FROM Proyecto p WHERE p.estado = :estado")})
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idProyecto;
    @Column(length = 50)
    private String nombreProyecto;
    @Column(length = 50)
    private String fechaInicio;
    @Column(length = 30)
    private String tiempoEstimado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 22)
    private Double precioTotal;
    @Column(length = 50)
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProyecto")
    private Collection<Empleado> empleadoCollection;
    @JoinColumn(name = "idUbicacion", referencedColumnName = "idUbicacion")
    @ManyToOne
    private Ubicacion idUbicacion;
    @OneToMany(mappedBy = "idProyecto")
    private Collection<Detalleproyectomaquina> detalleproyectomaquinaCollection;
    @OneToMany(mappedBy = "idProyecto")
    private Collection<Maquinaria> maquinariaCollection;

    public Proyecto() {
    }

    public Proyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<Empleado> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<Empleado> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

    public Ubicacion getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Ubicacion idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    @XmlTransient
    public Collection<Detalleproyectomaquina> getDetalleproyectomaquinaCollection() {
        return detalleproyectomaquinaCollection;
    }

    public void setDetalleproyectomaquinaCollection(Collection<Detalleproyectomaquina> detalleproyectomaquinaCollection) {
        this.detalleproyectomaquinaCollection = detalleproyectomaquinaCollection;
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
        hash += (idProyecto != null ? idProyecto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proyecto)) {
            return false;
        }
        Proyecto other = (Proyecto) object;
        if ((this.idProyecto == null && other.idProyecto != null) || (this.idProyecto != null && !this.idProyecto.equals(other.idProyecto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Proyecto[ idProyecto=" + idProyecto + " ]";
    }
    
}
