/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Proyecto;
import com.entities.Ubicacion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: UbicacionJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 05/11/2020
 * Version: 1.0
 * @author pedro
 */
public class UbicacionJpaController implements Serializable {

    public UbicacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public UbicacionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ubicacion ubicacion) {
        if (ubicacion.getProyectoCollection() == null) {
            ubicacion.setProyectoCollection(new ArrayList<Proyecto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Proyecto> attachedProyectoCollection = new ArrayList<Proyecto>();
            for (Proyecto proyectoCollectionProyectoToAttach : ubicacion.getProyectoCollection()) {
                proyectoCollectionProyectoToAttach = em.getReference(proyectoCollectionProyectoToAttach.getClass(), proyectoCollectionProyectoToAttach.getIdProyecto());
                attachedProyectoCollection.add(proyectoCollectionProyectoToAttach);
            }
            ubicacion.setProyectoCollection(attachedProyectoCollection);
            em.persist(ubicacion);
            for (Proyecto proyectoCollectionProyecto : ubicacion.getProyectoCollection()) {
                Ubicacion oldIdUbicacionOfProyectoCollectionProyecto = proyectoCollectionProyecto.getIdUbicacion();
                proyectoCollectionProyecto.setIdUbicacion(ubicacion);
                proyectoCollectionProyecto = em.merge(proyectoCollectionProyecto);
                if (oldIdUbicacionOfProyectoCollectionProyecto != null) {
                    oldIdUbicacionOfProyectoCollectionProyecto.getProyectoCollection().remove(proyectoCollectionProyecto);
                    oldIdUbicacionOfProyectoCollectionProyecto = em.merge(oldIdUbicacionOfProyectoCollectionProyecto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ubicacion ubicacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion persistentUbicacion = em.find(Ubicacion.class, ubicacion.getIdUbicacion());
            Collection<Proyecto> proyectoCollectionOld = persistentUbicacion.getProyectoCollection();
            Collection<Proyecto> proyectoCollectionNew = ubicacion.getProyectoCollection();
            Collection<Proyecto> attachedProyectoCollectionNew = new ArrayList<Proyecto>();
            for (Proyecto proyectoCollectionNewProyectoToAttach : proyectoCollectionNew) {
                proyectoCollectionNewProyectoToAttach = em.getReference(proyectoCollectionNewProyectoToAttach.getClass(), proyectoCollectionNewProyectoToAttach.getIdProyecto());
                attachedProyectoCollectionNew.add(proyectoCollectionNewProyectoToAttach);
            }
            proyectoCollectionNew = attachedProyectoCollectionNew;
            ubicacion.setProyectoCollection(proyectoCollectionNew);
            ubicacion = em.merge(ubicacion);
            for (Proyecto proyectoCollectionOldProyecto : proyectoCollectionOld) {
                if (!proyectoCollectionNew.contains(proyectoCollectionOldProyecto)) {
                    proyectoCollectionOldProyecto.setIdUbicacion(null);
                    proyectoCollectionOldProyecto = em.merge(proyectoCollectionOldProyecto);
                }
            }
            for (Proyecto proyectoCollectionNewProyecto : proyectoCollectionNew) {
                if (!proyectoCollectionOld.contains(proyectoCollectionNewProyecto)) {
                    Ubicacion oldIdUbicacionOfProyectoCollectionNewProyecto = proyectoCollectionNewProyecto.getIdUbicacion();
                    proyectoCollectionNewProyecto.setIdUbicacion(ubicacion);
                    proyectoCollectionNewProyecto = em.merge(proyectoCollectionNewProyecto);
                    if (oldIdUbicacionOfProyectoCollectionNewProyecto != null && !oldIdUbicacionOfProyectoCollectionNewProyecto.equals(ubicacion)) {
                        oldIdUbicacionOfProyectoCollectionNewProyecto.getProyectoCollection().remove(proyectoCollectionNewProyecto);
                        oldIdUbicacionOfProyectoCollectionNewProyecto = em.merge(oldIdUbicacionOfProyectoCollectionNewProyecto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ubicacion.getIdUbicacion();
                if (findUbicacion(id) == null) {
                    throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion ubicacion;
            try {
                ubicacion = em.getReference(Ubicacion.class, id);
                ubicacion.getIdUbicacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.", enfe);
            }
            Collection<Proyecto> proyectoCollection = ubicacion.getProyectoCollection();
            for (Proyecto proyectoCollectionProyecto : proyectoCollection) {
                proyectoCollectionProyecto.setIdUbicacion(null);
                proyectoCollectionProyecto = em.merge(proyectoCollectionProyecto);
            }
            em.remove(ubicacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ubicacion> findUbicacionEntities() {
        return findUbicacionEntities(true, -1, -1);
    }

    public List<Ubicacion> findUbicacionEntities(int maxResults, int firstResult) {
        return findUbicacionEntities(false, maxResults, firstResult);
    }

    private List<Ubicacion> findUbicacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ubicacion.class));
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

    public Ubicacion findUbicacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ubicacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getUbicacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ubicacion> rt = cq.from(Ubicacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Ubicacion getLastUbication(){
        Ubicacion ubicacion = new Ubicacion();
        List<Ubicacion> resultado;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT u FROM Ubicacion u");
            resultado = query.getResultList();
            
            if(!resultado.isEmpty()){
                for (Ubicacion item : resultado) {
                    ubicacion.setIdUbicacion(Integer.parseInt(item.getIdUbicacion().toString()));
                    ubicacion.setLatitud(item.getLatitud());
                    ubicacion.setLongitud(item.getLongitud());
                    ubicacion.setNombre(item.getNombre());
                }
                return ubicacion;
            }else 
                return new Ubicacion();
        } catch (Exception e) {
            return new Ubicacion();
        }
    }
    
}
