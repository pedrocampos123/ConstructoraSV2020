/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.IllegalOrphanException;
import com.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Ubicacion;
import com.entities.Empleado;
import java.util.ArrayList;
import java.util.Collection;
import com.entities.Detalleproyectomaquina;
import com.entities.Maquinaria;
import com.entities.Proyecto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: ProyectoJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 09/11/2020
 * Version: 1.1
 * @author pedro
 */
public class ProyectoJpaController implements Serializable {

    public ProyectoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public ProyectoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proyecto proyecto) {
        if (proyecto.getEmpleadoCollection() == null) {
            proyecto.setEmpleadoCollection(new ArrayList<Empleado>());
        }
        if (proyecto.getDetalleproyectomaquinaCollection() == null) {
            proyecto.setDetalleproyectomaquinaCollection(new ArrayList<Detalleproyectomaquina>());
        }
        if (proyecto.getMaquinariaCollection() == null) {
            proyecto.setMaquinariaCollection(new ArrayList<Maquinaria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion idUbicacion = proyecto.getIdUbicacion();
            if (idUbicacion != null) {
                idUbicacion = em.getReference(idUbicacion.getClass(), idUbicacion.getIdUbicacion());
                proyecto.setIdUbicacion(idUbicacion);
            }
            Collection<Empleado> attachedEmpleadoCollection = new ArrayList<Empleado>();
            for (Empleado empleadoCollectionEmpleadoToAttach : proyecto.getEmpleadoCollection()) {
                empleadoCollectionEmpleadoToAttach = em.getReference(empleadoCollectionEmpleadoToAttach.getClass(), empleadoCollectionEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoCollection.add(empleadoCollectionEmpleadoToAttach);
            }
            proyecto.setEmpleadoCollection(attachedEmpleadoCollection);
            Collection<Detalleproyectomaquina> attachedDetalleproyectomaquinaCollection = new ArrayList<Detalleproyectomaquina>();
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach : proyecto.getDetalleproyectomaquinaCollection()) {
                detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach = em.getReference(detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach.getClass(), detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach.getIdDetalle());
                attachedDetalleproyectomaquinaCollection.add(detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach);
            }
            proyecto.setDetalleproyectomaquinaCollection(attachedDetalleproyectomaquinaCollection);
            Collection<Maquinaria> attachedMaquinariaCollection = new ArrayList<Maquinaria>();
            for (Maquinaria maquinariaCollectionMaquinariaToAttach : proyecto.getMaquinariaCollection()) {
                maquinariaCollectionMaquinariaToAttach = em.getReference(maquinariaCollectionMaquinariaToAttach.getClass(), maquinariaCollectionMaquinariaToAttach.getIdMaquinaria());
                attachedMaquinariaCollection.add(maquinariaCollectionMaquinariaToAttach);
            }
            proyecto.setMaquinariaCollection(attachedMaquinariaCollection);
            em.persist(proyecto);
            if (idUbicacion != null) {
                idUbicacion.getProyectoCollection().add(proyecto);
                idUbicacion = em.merge(idUbicacion);
            }
            for (Empleado empleadoCollectionEmpleado : proyecto.getEmpleadoCollection()) {
                Proyecto oldIdProyectoOfEmpleadoCollectionEmpleado = empleadoCollectionEmpleado.getIdProyecto();
                empleadoCollectionEmpleado.setIdProyecto(proyecto);
                empleadoCollectionEmpleado = em.merge(empleadoCollectionEmpleado);
                if (oldIdProyectoOfEmpleadoCollectionEmpleado != null) {
                    oldIdProyectoOfEmpleadoCollectionEmpleado.getEmpleadoCollection().remove(empleadoCollectionEmpleado);
                    oldIdProyectoOfEmpleadoCollectionEmpleado = em.merge(oldIdProyectoOfEmpleadoCollectionEmpleado);
                }
            }
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionDetalleproyectomaquina : proyecto.getDetalleproyectomaquinaCollection()) {
                Proyecto oldIdProyectoOfDetalleproyectomaquinaCollectionDetalleproyectomaquina = detalleproyectomaquinaCollectionDetalleproyectomaquina.getIdProyecto();
                detalleproyectomaquinaCollectionDetalleproyectomaquina.setIdProyecto(proyecto);
                detalleproyectomaquinaCollectionDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionDetalleproyectomaquina);
                if (oldIdProyectoOfDetalleproyectomaquinaCollectionDetalleproyectomaquina != null) {
                    oldIdProyectoOfDetalleproyectomaquinaCollectionDetalleproyectomaquina.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquinaCollectionDetalleproyectomaquina);
                    oldIdProyectoOfDetalleproyectomaquinaCollectionDetalleproyectomaquina = em.merge(oldIdProyectoOfDetalleproyectomaquinaCollectionDetalleproyectomaquina);
                }
            }
            for (Maquinaria maquinariaCollectionMaquinaria : proyecto.getMaquinariaCollection()) {
                Proyecto oldIdProyectoOfMaquinariaCollectionMaquinaria = maquinariaCollectionMaquinaria.getIdProyecto();
                maquinariaCollectionMaquinaria.setIdProyecto(proyecto);
                maquinariaCollectionMaquinaria = em.merge(maquinariaCollectionMaquinaria);
                if (oldIdProyectoOfMaquinariaCollectionMaquinaria != null) {
                    oldIdProyectoOfMaquinariaCollectionMaquinaria.getMaquinariaCollection().remove(maquinariaCollectionMaquinaria);
                    oldIdProyectoOfMaquinariaCollectionMaquinaria = em.merge(oldIdProyectoOfMaquinariaCollectionMaquinaria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proyecto proyecto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto persistentProyecto = em.find(Proyecto.class, proyecto.getIdProyecto());
            Ubicacion idUbicacionOld = persistentProyecto.getIdUbicacion();
            Ubicacion idUbicacionNew = proyecto.getIdUbicacion();
            Collection<Empleado> empleadoCollectionOld = persistentProyecto.getEmpleadoCollection();
            Collection<Empleado> empleadoCollectionNew = proyecto.getEmpleadoCollection();
            Collection<Detalleproyectomaquina> detalleproyectomaquinaCollectionOld = persistentProyecto.getDetalleproyectomaquinaCollection();
            Collection<Detalleproyectomaquina> detalleproyectomaquinaCollectionNew = proyecto.getDetalleproyectomaquinaCollection();
            Collection<Maquinaria> maquinariaCollectionOld = persistentProyecto.getMaquinariaCollection();
            Collection<Maquinaria> maquinariaCollectionNew = proyecto.getMaquinariaCollection();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoCollectionOldEmpleado : empleadoCollectionOld) {
                if (!empleadoCollectionNew.contains(empleadoCollectionOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoCollectionOldEmpleado + " since its idProyecto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUbicacionNew != null) {
                idUbicacionNew = em.getReference(idUbicacionNew.getClass(), idUbicacionNew.getIdUbicacion());
                proyecto.setIdUbicacion(idUbicacionNew);
            }
            Collection<Empleado> attachedEmpleadoCollectionNew = new ArrayList<Empleado>();
            for (Empleado empleadoCollectionNewEmpleadoToAttach : empleadoCollectionNew) {
                empleadoCollectionNewEmpleadoToAttach = em.getReference(empleadoCollectionNewEmpleadoToAttach.getClass(), empleadoCollectionNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoCollectionNew.add(empleadoCollectionNewEmpleadoToAttach);
            }
            empleadoCollectionNew = attachedEmpleadoCollectionNew;
            proyecto.setEmpleadoCollection(empleadoCollectionNew);
            Collection<Detalleproyectomaquina> attachedDetalleproyectomaquinaCollectionNew = new ArrayList<Detalleproyectomaquina>();
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach : detalleproyectomaquinaCollectionNew) {
                detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach = em.getReference(detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach.getClass(), detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach.getIdDetalle());
                attachedDetalleproyectomaquinaCollectionNew.add(detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach);
            }
            detalleproyectomaquinaCollectionNew = attachedDetalleproyectomaquinaCollectionNew;
            proyecto.setDetalleproyectomaquinaCollection(detalleproyectomaquinaCollectionNew);
            Collection<Maquinaria> attachedMaquinariaCollectionNew = new ArrayList<Maquinaria>();
            for (Maquinaria maquinariaCollectionNewMaquinariaToAttach : maquinariaCollectionNew) {
                maquinariaCollectionNewMaquinariaToAttach = em.getReference(maquinariaCollectionNewMaquinariaToAttach.getClass(), maquinariaCollectionNewMaquinariaToAttach.getIdMaquinaria());
                attachedMaquinariaCollectionNew.add(maquinariaCollectionNewMaquinariaToAttach);
            }
            maquinariaCollectionNew = attachedMaquinariaCollectionNew;
            proyecto.setMaquinariaCollection(maquinariaCollectionNew);
            proyecto = em.merge(proyecto);
            if (idUbicacionOld != null && !idUbicacionOld.equals(idUbicacionNew)) {
                idUbicacionOld.getProyectoCollection().remove(proyecto);
                idUbicacionOld = em.merge(idUbicacionOld);
            }
            if (idUbicacionNew != null && !idUbicacionNew.equals(idUbicacionOld)) {
                idUbicacionNew.getProyectoCollection().add(proyecto);
                idUbicacionNew = em.merge(idUbicacionNew);
            }
            for (Empleado empleadoCollectionNewEmpleado : empleadoCollectionNew) {
                if (!empleadoCollectionOld.contains(empleadoCollectionNewEmpleado)) {
                    Proyecto oldIdProyectoOfEmpleadoCollectionNewEmpleado = empleadoCollectionNewEmpleado.getIdProyecto();
                    empleadoCollectionNewEmpleado.setIdProyecto(proyecto);
                    empleadoCollectionNewEmpleado = em.merge(empleadoCollectionNewEmpleado);
                    if (oldIdProyectoOfEmpleadoCollectionNewEmpleado != null && !oldIdProyectoOfEmpleadoCollectionNewEmpleado.equals(proyecto)) {
                        oldIdProyectoOfEmpleadoCollectionNewEmpleado.getEmpleadoCollection().remove(empleadoCollectionNewEmpleado);
                        oldIdProyectoOfEmpleadoCollectionNewEmpleado = em.merge(oldIdProyectoOfEmpleadoCollectionNewEmpleado);
                    }
                }
            }
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionOldDetalleproyectomaquina : detalleproyectomaquinaCollectionOld) {
                if (!detalleproyectomaquinaCollectionNew.contains(detalleproyectomaquinaCollectionOldDetalleproyectomaquina)) {
                    detalleproyectomaquinaCollectionOldDetalleproyectomaquina.setIdProyecto(null);
                    detalleproyectomaquinaCollectionOldDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionOldDetalleproyectomaquina);
                }
            }
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionNewDetalleproyectomaquina : detalleproyectomaquinaCollectionNew) {
                if (!detalleproyectomaquinaCollectionOld.contains(detalleproyectomaquinaCollectionNewDetalleproyectomaquina)) {
                    Proyecto oldIdProyectoOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina = detalleproyectomaquinaCollectionNewDetalleproyectomaquina.getIdProyecto();
                    detalleproyectomaquinaCollectionNewDetalleproyectomaquina.setIdProyecto(proyecto);
                    detalleproyectomaquinaCollectionNewDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionNewDetalleproyectomaquina);
                    if (oldIdProyectoOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina != null && !oldIdProyectoOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina.equals(proyecto)) {
                        oldIdProyectoOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquinaCollectionNewDetalleproyectomaquina);
                        oldIdProyectoOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina = em.merge(oldIdProyectoOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina);
                    }
                }
            }
            for (Maquinaria maquinariaCollectionOldMaquinaria : maquinariaCollectionOld) {
                if (!maquinariaCollectionNew.contains(maquinariaCollectionOldMaquinaria)) {
                    maquinariaCollectionOldMaquinaria.setIdProyecto(null);
                    maquinariaCollectionOldMaquinaria = em.merge(maquinariaCollectionOldMaquinaria);
                }
            }
            for (Maquinaria maquinariaCollectionNewMaquinaria : maquinariaCollectionNew) {
                if (!maquinariaCollectionOld.contains(maquinariaCollectionNewMaquinaria)) {
                    Proyecto oldIdProyectoOfMaquinariaCollectionNewMaquinaria = maquinariaCollectionNewMaquinaria.getIdProyecto();
                    maquinariaCollectionNewMaquinaria.setIdProyecto(proyecto);
                    maquinariaCollectionNewMaquinaria = em.merge(maquinariaCollectionNewMaquinaria);
                    if (oldIdProyectoOfMaquinariaCollectionNewMaquinaria != null && !oldIdProyectoOfMaquinariaCollectionNewMaquinaria.equals(proyecto)) {
                        oldIdProyectoOfMaquinariaCollectionNewMaquinaria.getMaquinariaCollection().remove(maquinariaCollectionNewMaquinaria);
                        oldIdProyectoOfMaquinariaCollectionNewMaquinaria = em.merge(oldIdProyectoOfMaquinariaCollectionNewMaquinaria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proyecto.getIdProyecto();
                if (findProyecto(id) == null) {
                    throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto proyecto;
            try {
                proyecto = em.getReference(Proyecto.class, id);
                proyecto.getIdProyecto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Empleado> empleadoCollectionOrphanCheck = proyecto.getEmpleadoCollection();
            for (Empleado empleadoCollectionOrphanCheckEmpleado : empleadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proyecto (" + proyecto + ") cannot be destroyed since the Empleado " + empleadoCollectionOrphanCheckEmpleado + " in its empleadoCollection field has a non-nullable idProyecto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ubicacion idUbicacion = proyecto.getIdUbicacion();
            if (idUbicacion != null) {
                idUbicacion.getProyectoCollection().remove(proyecto);
                idUbicacion = em.merge(idUbicacion);
            }
            Collection<Detalleproyectomaquina> detalleproyectomaquinaCollection = proyecto.getDetalleproyectomaquinaCollection();
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionDetalleproyectomaquina : detalleproyectomaquinaCollection) {
                detalleproyectomaquinaCollectionDetalleproyectomaquina.setIdProyecto(null);
                detalleproyectomaquinaCollectionDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionDetalleproyectomaquina);
            }
            Collection<Maquinaria> maquinariaCollection = proyecto.getMaquinariaCollection();
            for (Maquinaria maquinariaCollectionMaquinaria : maquinariaCollection) {
                maquinariaCollectionMaquinaria.setIdProyecto(null);
                maquinariaCollectionMaquinaria = em.merge(maquinariaCollectionMaquinaria);
            }
            em.remove(proyecto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proyecto> findProyectoEntities() {
        return findProyectoEntities(true, -1, -1);
    }

    public List<Proyecto> findProyectoEntities(int maxResults, int firstResult) {
        return findProyectoEntities(false, maxResults, firstResult);
    }

    private List<Proyecto> findProyectoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proyecto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Proyecto findProyecto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proyecto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proyecto> rt = cq.from(Proyecto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Proyecto> getAllProyects(){
        List<Proyecto> resultado = null;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT p FROM Proyecto p");
            
            resultado = query.getResultList();
            
        } catch (Exception e) {
            String ee ="";
        }
        return resultado;
    }
    
    public Proyecto getProyectoUbicacion(int idUbicacion){
        List<Proyecto> resultado = null;
        Proyecto proyecto = new Proyecto();
        Ubicacion ubi = new Ubicacion();
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT p FROM Proyecto p where p.idUbicacion.idUbicacion = :idUbicacion");
            query.setParameter("idUbicacion", idUbicacion);
            
            resultado = query.getResultList();
            
            if(!resultado.isEmpty()){
                for (Proyecto obj : resultado) {
                    proyecto.setIdProyecto(obj.getIdProyecto());
                    proyecto.setNombreProyecto(obj.getNombreProyecto());
                    proyecto.setFechaInicio(obj.getFechaInicio());
                    proyecto.setTiempoEstimado(obj.getTiempoEstimado());
                    proyecto.setPrecioTotal(obj.getPrecioTotal());
                    
                    ubi.setIdUbicacion(obj.getIdUbicacion().getIdUbicacion());
                    ubi.setNombre(obj.getIdUbicacion().getNombre());
                    ubi.setLatitud(obj.getIdUbicacion().getLatitud());
                    ubi.setLongitud(obj.getIdUbicacion().getLongitud());
                    
                    proyecto.setIdUbicacion(ubi);
                }
            }
            
        } catch (Exception e) {
            return new Proyecto();
        }
        return proyecto;
    }
    
    public List<Proyecto> getProyecto(int idProyecto){
        List<Proyecto> resultado = null;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT p FROM Proyecto p where p.idProyecto = :idProyecto");
            query.setParameter("idProyecto", idProyecto);
            
            resultado = query.getResultList();
            
        } catch (Exception e) {
            
        }
        return resultado;
    }
    
}
