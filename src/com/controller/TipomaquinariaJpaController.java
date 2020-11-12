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
import com.entities.Maquinaria;
import com.entities.Tipomaquinaria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: TipomaquinariaJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class TipomaquinariaJpaController implements Serializable {

    public TipomaquinariaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public TipomaquinariaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipomaquinaria tipomaquinaria) {
        if (tipomaquinaria.getMaquinariaCollection() == null) {
            tipomaquinaria.setMaquinariaCollection(new ArrayList<Maquinaria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Maquinaria> attachedMaquinariaCollection = new ArrayList<Maquinaria>();
            for (Maquinaria maquinariaCollectionMaquinariaToAttach : tipomaquinaria.getMaquinariaCollection()) {
                maquinariaCollectionMaquinariaToAttach = em.getReference(maquinariaCollectionMaquinariaToAttach.getClass(), maquinariaCollectionMaquinariaToAttach.getIdMaquinaria());
                attachedMaquinariaCollection.add(maquinariaCollectionMaquinariaToAttach);
            }
            tipomaquinaria.setMaquinariaCollection(attachedMaquinariaCollection);
            em.persist(tipomaquinaria);
            for (Maquinaria maquinariaCollectionMaquinaria : tipomaquinaria.getMaquinariaCollection()) {
                Tipomaquinaria oldIdTipoOfMaquinariaCollectionMaquinaria = maquinariaCollectionMaquinaria.getIdTipo();
                maquinariaCollectionMaquinaria.setIdTipo(tipomaquinaria);
                maquinariaCollectionMaquinaria = em.merge(maquinariaCollectionMaquinaria);
                if (oldIdTipoOfMaquinariaCollectionMaquinaria != null) {
                    oldIdTipoOfMaquinariaCollectionMaquinaria.getMaquinariaCollection().remove(maquinariaCollectionMaquinaria);
                    oldIdTipoOfMaquinariaCollectionMaquinaria = em.merge(oldIdTipoOfMaquinariaCollectionMaquinaria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipomaquinaria tipomaquinaria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipomaquinaria persistentTipomaquinaria = em.find(Tipomaquinaria.class, tipomaquinaria.getIdTipo());
            Collection<Maquinaria> maquinariaCollectionOld = persistentTipomaquinaria.getMaquinariaCollection();
            Collection<Maquinaria> maquinariaCollectionNew = tipomaquinaria.getMaquinariaCollection();
            Collection<Maquinaria> attachedMaquinariaCollectionNew = new ArrayList<Maquinaria>();
            for (Maquinaria maquinariaCollectionNewMaquinariaToAttach : maquinariaCollectionNew) {
                maquinariaCollectionNewMaquinariaToAttach = em.getReference(maquinariaCollectionNewMaquinariaToAttach.getClass(), maquinariaCollectionNewMaquinariaToAttach.getIdMaquinaria());
                attachedMaquinariaCollectionNew.add(maquinariaCollectionNewMaquinariaToAttach);
            }
            maquinariaCollectionNew = attachedMaquinariaCollectionNew;
            tipomaquinaria.setMaquinariaCollection(maquinariaCollectionNew);
            tipomaquinaria = em.merge(tipomaquinaria);
            for (Maquinaria maquinariaCollectionOldMaquinaria : maquinariaCollectionOld) {
                if (!maquinariaCollectionNew.contains(maquinariaCollectionOldMaquinaria)) {
                    maquinariaCollectionOldMaquinaria.setIdTipo(null);
                    maquinariaCollectionOldMaquinaria = em.merge(maquinariaCollectionOldMaquinaria);
                }
            }
            for (Maquinaria maquinariaCollectionNewMaquinaria : maquinariaCollectionNew) {
                if (!maquinariaCollectionOld.contains(maquinariaCollectionNewMaquinaria)) {
                    Tipomaquinaria oldIdTipoOfMaquinariaCollectionNewMaquinaria = maquinariaCollectionNewMaquinaria.getIdTipo();
                    maquinariaCollectionNewMaquinaria.setIdTipo(tipomaquinaria);
                    maquinariaCollectionNewMaquinaria = em.merge(maquinariaCollectionNewMaquinaria);
                    if (oldIdTipoOfMaquinariaCollectionNewMaquinaria != null && !oldIdTipoOfMaquinariaCollectionNewMaquinaria.equals(tipomaquinaria)) {
                        oldIdTipoOfMaquinariaCollectionNewMaquinaria.getMaquinariaCollection().remove(maquinariaCollectionNewMaquinaria);
                        oldIdTipoOfMaquinariaCollectionNewMaquinaria = em.merge(oldIdTipoOfMaquinariaCollectionNewMaquinaria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipomaquinaria.getIdTipo();
                if (findTipomaquinaria(id) == null) {
                    throw new NonexistentEntityException("The tipomaquinaria with id " + id + " no longer exists.");
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
            Tipomaquinaria tipomaquinaria;
            try {
                tipomaquinaria = em.getReference(Tipomaquinaria.class, id);
                tipomaquinaria.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipomaquinaria with id " + id + " no longer exists.", enfe);
            }
            Collection<Maquinaria> maquinariaCollection = tipomaquinaria.getMaquinariaCollection();
            for (Maquinaria maquinariaCollectionMaquinaria : maquinariaCollection) {
                maquinariaCollectionMaquinaria.setIdTipo(null);
                maquinariaCollectionMaquinaria = em.merge(maquinariaCollectionMaquinaria);
            }
            em.remove(tipomaquinaria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipomaquinaria> findTipomaquinariaEntities() {
        return findTipomaquinariaEntities(true, -1, -1);
    }

    public List<Tipomaquinaria> findTipomaquinariaEntities(int maxResults, int firstResult) {
        return findTipomaquinariaEntities(false, maxResults, firstResult);
    }

    private List<Tipomaquinaria> findTipomaquinariaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipomaquinaria.class));
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

    public Tipomaquinaria findTipomaquinaria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipomaquinaria.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipomaquinariaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipomaquinaria> rt = cq.from(Tipomaquinaria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Tipomaquinaria> getTipoMaquinariaSeleccionada(int tipo) {
        List<Tipomaquinaria> tipos = null;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT t FROM Tipomaquinaria t WHERE t.idTipo = :tipo");
            query.setParameter("tipo", tipo);
            tipos = query.getResultList();
        } catch (Exception e) {
        }
        return tipos;
    }
    
}
