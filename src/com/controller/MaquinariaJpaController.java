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
import com.entities.Tipomaquinaria;
import com.entities.Proyecto;
import com.entities.Detalleproyectomaquina;
import com.entities.Maquinaria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: MaquinariaJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class MaquinariaJpaController implements Serializable {

    public MaquinariaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public MaquinariaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Maquinaria maquinaria) {
        if (maquinaria.getDetalleproyectomaquinaCollection() == null) {
            maquinaria.setDetalleproyectomaquinaCollection(new ArrayList<Detalleproyectomaquina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipomaquinaria idTipo = maquinaria.getIdTipo();
            if (idTipo != null) {
                idTipo = em.getReference(idTipo.getClass(), idTipo.getIdTipo());
                maquinaria.setIdTipo(idTipo);
            }
            Proyecto idProyecto = maquinaria.getIdProyecto();
            if (idProyecto != null) {
                idProyecto = em.getReference(idProyecto.getClass(), idProyecto.getIdProyecto());
                maquinaria.setIdProyecto(idProyecto);
            }
            Collection<Detalleproyectomaquina> attachedDetalleproyectomaquinaCollection = new ArrayList<Detalleproyectomaquina>();
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach : maquinaria.getDetalleproyectomaquinaCollection()) {
                detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach = em.getReference(detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach.getClass(), detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach.getIdDetalle());
                attachedDetalleproyectomaquinaCollection.add(detalleproyectomaquinaCollectionDetalleproyectomaquinaToAttach);
            }
            maquinaria.setDetalleproyectomaquinaCollection(attachedDetalleproyectomaquinaCollection);
            em.persist(maquinaria);
            if (idTipo != null) {
                idTipo.getMaquinariaCollection().add(maquinaria);
                idTipo = em.merge(idTipo);
            }
            if (idProyecto != null) {
                idProyecto.getMaquinariaCollection().add(maquinaria);
                idProyecto = em.merge(idProyecto);
            }
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionDetalleproyectomaquina : maquinaria.getDetalleproyectomaquinaCollection()) {
                Maquinaria oldIdMaquinariaOfDetalleproyectomaquinaCollectionDetalleproyectomaquina = detalleproyectomaquinaCollectionDetalleproyectomaquina.getIdMaquinaria();
                detalleproyectomaquinaCollectionDetalleproyectomaquina.setIdMaquinaria(maquinaria);
                detalleproyectomaquinaCollectionDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionDetalleproyectomaquina);
                if (oldIdMaquinariaOfDetalleproyectomaquinaCollectionDetalleproyectomaquina != null) {
                    oldIdMaquinariaOfDetalleproyectomaquinaCollectionDetalleproyectomaquina.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquinaCollectionDetalleproyectomaquina);
                    oldIdMaquinariaOfDetalleproyectomaquinaCollectionDetalleproyectomaquina = em.merge(oldIdMaquinariaOfDetalleproyectomaquinaCollectionDetalleproyectomaquina);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Maquinaria maquinaria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maquinaria persistentMaquinaria = em.find(Maquinaria.class, maquinaria.getIdMaquinaria());
            Tipomaquinaria idTipoOld = persistentMaquinaria.getIdTipo();
            Tipomaquinaria idTipoNew = maquinaria.getIdTipo();
            Proyecto idProyectoOld = persistentMaquinaria.getIdProyecto();
            Proyecto idProyectoNew = maquinaria.getIdProyecto();
            Collection<Detalleproyectomaquina> detalleproyectomaquinaCollectionOld = persistentMaquinaria.getDetalleproyectomaquinaCollection();
            Collection<Detalleproyectomaquina> detalleproyectomaquinaCollectionNew = maquinaria.getDetalleproyectomaquinaCollection();
            if (idTipoNew != null) {
                idTipoNew = em.getReference(idTipoNew.getClass(), idTipoNew.getIdTipo());
                maquinaria.setIdTipo(idTipoNew);
            }
            if (idProyectoNew != null) {
                idProyectoNew = em.getReference(idProyectoNew.getClass(), idProyectoNew.getIdProyecto());
                maquinaria.setIdProyecto(idProyectoNew);
            }
            Collection<Detalleproyectomaquina> attachedDetalleproyectomaquinaCollectionNew = new ArrayList<Detalleproyectomaquina>();
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach : detalleproyectomaquinaCollectionNew) {
                detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach = em.getReference(detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach.getClass(), detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach.getIdDetalle());
                attachedDetalleproyectomaquinaCollectionNew.add(detalleproyectomaquinaCollectionNewDetalleproyectomaquinaToAttach);
            }
            detalleproyectomaquinaCollectionNew = attachedDetalleproyectomaquinaCollectionNew;
            maquinaria.setDetalleproyectomaquinaCollection(detalleproyectomaquinaCollectionNew);
            maquinaria = em.merge(maquinaria);
            if (idTipoOld != null && !idTipoOld.equals(idTipoNew)) {
                idTipoOld.getMaquinariaCollection().remove(maquinaria);
                idTipoOld = em.merge(idTipoOld);
            }
            if (idTipoNew != null && !idTipoNew.equals(idTipoOld)) {
                idTipoNew.getMaquinariaCollection().add(maquinaria);
                idTipoNew = em.merge(idTipoNew);
            }
            if (idProyectoOld != null && !idProyectoOld.equals(idProyectoNew)) {
                idProyectoOld.getMaquinariaCollection().remove(maquinaria);
                idProyectoOld = em.merge(idProyectoOld);
            }
            if (idProyectoNew != null && !idProyectoNew.equals(idProyectoOld)) {
                idProyectoNew.getMaquinariaCollection().add(maquinaria);
                idProyectoNew = em.merge(idProyectoNew);
            }
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionOldDetalleproyectomaquina : detalleproyectomaquinaCollectionOld) {
                if (!detalleproyectomaquinaCollectionNew.contains(detalleproyectomaquinaCollectionOldDetalleproyectomaquina)) {
                    detalleproyectomaquinaCollectionOldDetalleproyectomaquina.setIdMaquinaria(null);
                    detalleproyectomaquinaCollectionOldDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionOldDetalleproyectomaquina);
                }
            }
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionNewDetalleproyectomaquina : detalleproyectomaquinaCollectionNew) {
                if (!detalleproyectomaquinaCollectionOld.contains(detalleproyectomaquinaCollectionNewDetalleproyectomaquina)) {
                    Maquinaria oldIdMaquinariaOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina = detalleproyectomaquinaCollectionNewDetalleproyectomaquina.getIdMaquinaria();
                    detalleproyectomaquinaCollectionNewDetalleproyectomaquina.setIdMaquinaria(maquinaria);
                    detalleproyectomaquinaCollectionNewDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionNewDetalleproyectomaquina);
                    if (oldIdMaquinariaOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina != null && !oldIdMaquinariaOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina.equals(maquinaria)) {
                        oldIdMaquinariaOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquinaCollectionNewDetalleproyectomaquina);
                        oldIdMaquinariaOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina = em.merge(oldIdMaquinariaOfDetalleproyectomaquinaCollectionNewDetalleproyectomaquina);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = maquinaria.getIdMaquinaria();
                if (findMaquinaria(id) == null) {
                    throw new NonexistentEntityException("The maquinaria with id " + id + " no longer exists.");
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
            Maquinaria maquinaria;
            try {
                maquinaria = em.getReference(Maquinaria.class, id);
                maquinaria.getIdMaquinaria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The maquinaria with id " + id + " no longer exists.", enfe);
            }
            Tipomaquinaria idTipo = maquinaria.getIdTipo();
            if (idTipo != null) {
                idTipo.getMaquinariaCollection().remove(maquinaria);
                idTipo = em.merge(idTipo);
            }
            Proyecto idProyecto = maquinaria.getIdProyecto();
            if (idProyecto != null) {
                idProyecto.getMaquinariaCollection().remove(maquinaria);
                idProyecto = em.merge(idProyecto);
            }
            Collection<Detalleproyectomaquina> detalleproyectomaquinaCollection = maquinaria.getDetalleproyectomaquinaCollection();
            for (Detalleproyectomaquina detalleproyectomaquinaCollectionDetalleproyectomaquina : detalleproyectomaquinaCollection) {
                detalleproyectomaquinaCollectionDetalleproyectomaquina.setIdMaquinaria(null);
                detalleproyectomaquinaCollectionDetalleproyectomaquina = em.merge(detalleproyectomaquinaCollectionDetalleproyectomaquina);
            }
            em.remove(maquinaria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Maquinaria> findMaquinariaEntities() {
        return findMaquinariaEntities(true, -1, -1);
    }

    public List<Maquinaria> findMaquinariaEntities(int maxResults, int firstResult) {
        return findMaquinariaEntities(false, maxResults, firstResult);
    }

    private List<Maquinaria> findMaquinariaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Maquinaria.class));
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

    public Maquinaria findMaquinaria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Maquinaria.class, id);
        } finally {
            em.close();
        }
    }

    public int getMaquinariaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Maquinaria> rt = cq.from(Maquinaria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
