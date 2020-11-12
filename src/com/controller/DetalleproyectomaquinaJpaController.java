/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.NonexistentEntityException;
import com.entities.Detalleproyectomaquina;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Proyecto;
import com.entities.Maquinaria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: DetalleproyectomaquinaJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class DetalleproyectomaquinaJpaController implements Serializable {

    public DetalleproyectomaquinaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public DetalleproyectomaquinaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleproyectomaquina detalleproyectomaquina) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto idProyecto = detalleproyectomaquina.getIdProyecto();
            if (idProyecto != null) {
                idProyecto = em.getReference(idProyecto.getClass(), idProyecto.getIdProyecto());
                detalleproyectomaquina.setIdProyecto(idProyecto);
            }
            Maquinaria idMaquinaria = detalleproyectomaquina.getIdMaquinaria();
            if (idMaquinaria != null) {
                idMaquinaria = em.getReference(idMaquinaria.getClass(), idMaquinaria.getIdMaquinaria());
                detalleproyectomaquina.setIdMaquinaria(idMaquinaria);
            }
            em.persist(detalleproyectomaquina);
            if (idProyecto != null) {
                idProyecto.getDetalleproyectomaquinaCollection().add(detalleproyectomaquina);
                idProyecto = em.merge(idProyecto);
            }
            if (idMaquinaria != null) {
                idMaquinaria.getDetalleproyectomaquinaCollection().add(detalleproyectomaquina);
                idMaquinaria = em.merge(idMaquinaria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleproyectomaquina detalleproyectomaquina) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleproyectomaquina persistentDetalleproyectomaquina = em.find(Detalleproyectomaquina.class, detalleproyectomaquina.getIdDetalle());
            Proyecto idProyectoOld = persistentDetalleproyectomaquina.getIdProyecto();
            Proyecto idProyectoNew = detalleproyectomaquina.getIdProyecto();
            Maquinaria idMaquinariaOld = persistentDetalleproyectomaquina.getIdMaquinaria();
            Maquinaria idMaquinariaNew = detalleproyectomaquina.getIdMaquinaria();
            if (idProyectoNew != null) {
                idProyectoNew = em.getReference(idProyectoNew.getClass(), idProyectoNew.getIdProyecto());
                detalleproyectomaquina.setIdProyecto(idProyectoNew);
            }
            if (idMaquinariaNew != null) {
                idMaquinariaNew = em.getReference(idMaquinariaNew.getClass(), idMaquinariaNew.getIdMaquinaria());
                detalleproyectomaquina.setIdMaquinaria(idMaquinariaNew);
            }
            detalleproyectomaquina = em.merge(detalleproyectomaquina);
            if (idProyectoOld != null && !idProyectoOld.equals(idProyectoNew)) {
                idProyectoOld.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquina);
                idProyectoOld = em.merge(idProyectoOld);
            }
            if (idProyectoNew != null && !idProyectoNew.equals(idProyectoOld)) {
                idProyectoNew.getDetalleproyectomaquinaCollection().add(detalleproyectomaquina);
                idProyectoNew = em.merge(idProyectoNew);
            }
            if (idMaquinariaOld != null && !idMaquinariaOld.equals(idMaquinariaNew)) {
                idMaquinariaOld.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquina);
                idMaquinariaOld = em.merge(idMaquinariaOld);
            }
            if (idMaquinariaNew != null && !idMaquinariaNew.equals(idMaquinariaOld)) {
                idMaquinariaNew.getDetalleproyectomaquinaCollection().add(detalleproyectomaquina);
                idMaquinariaNew = em.merge(idMaquinariaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleproyectomaquina.getIdDetalle();
                if (findDetalleproyectomaquina(id) == null) {
                    throw new NonexistentEntityException("The detalleproyectomaquina with id " + id + " no longer exists.");
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
            Detalleproyectomaquina detalleproyectomaquina;
            try {
                detalleproyectomaquina = em.getReference(Detalleproyectomaquina.class, id);
                detalleproyectomaquina.getIdDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleproyectomaquina with id " + id + " no longer exists.", enfe);
            }
            Proyecto idProyecto = detalleproyectomaquina.getIdProyecto();
            if (idProyecto != null) {
                idProyecto.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquina);
                idProyecto = em.merge(idProyecto);
            }
            Maquinaria idMaquinaria = detalleproyectomaquina.getIdMaquinaria();
            if (idMaquinaria != null) {
                idMaquinaria.getDetalleproyectomaquinaCollection().remove(detalleproyectomaquina);
                idMaquinaria = em.merge(idMaquinaria);
            }
            em.remove(detalleproyectomaquina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detalleproyectomaquina> findDetalleproyectomaquinaEntities() {
        return findDetalleproyectomaquinaEntities(true, -1, -1);
    }

    public List<Detalleproyectomaquina> findDetalleproyectomaquinaEntities(int maxResults, int firstResult) {
        return findDetalleproyectomaquinaEntities(false, maxResults, firstResult);
    }

    private List<Detalleproyectomaquina> findDetalleproyectomaquinaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleproyectomaquina.class));
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

    public Detalleproyectomaquina findDetalleproyectomaquina(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleproyectomaquina.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleproyectomaquinaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleproyectomaquina> rt = cq.from(Detalleproyectomaquina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
