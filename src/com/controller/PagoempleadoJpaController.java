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
import com.entities.Empleado;
import com.entities.Pagoempleado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: PagoempleadoJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class PagoempleadoJpaController implements Serializable {

    public PagoempleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public PagoempleadoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pagoempleado pagoempleado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = pagoempleado.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                pagoempleado.setIdEmpleado(idEmpleado);
            }
            em.persist(pagoempleado);
            if (idEmpleado != null) {
                idEmpleado.getPagoempleadoCollection().add(pagoempleado);
                idEmpleado = em.merge(idEmpleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pagoempleado pagoempleado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagoempleado persistentPagoempleado = em.find(Pagoempleado.class, pagoempleado.getIdPago());
            Empleado idEmpleadoOld = persistentPagoempleado.getIdEmpleado();
            Empleado idEmpleadoNew = pagoempleado.getIdEmpleado();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                pagoempleado.setIdEmpleado(idEmpleadoNew);
            }
            pagoempleado = em.merge(pagoempleado);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getPagoempleadoCollection().remove(pagoempleado);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getPagoempleadoCollection().add(pagoempleado);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagoempleado.getIdPago();
                if (findPagoempleado(id) == null) {
                    throw new NonexistentEntityException("The pagoempleado with id " + id + " no longer exists.");
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
            Pagoempleado pagoempleado;
            try {
                pagoempleado = em.getReference(Pagoempleado.class, id);
                pagoempleado.getIdPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagoempleado with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = pagoempleado.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getPagoempleadoCollection().remove(pagoempleado);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(pagoempleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pagoempleado> findPagoempleadoEntities() {
        return findPagoempleadoEntities(true, -1, -1);
    }

    public List<Pagoempleado> findPagoempleadoEntities(int maxResults, int firstResult) {
        return findPagoempleadoEntities(false, maxResults, firstResult);
    }

    private List<Pagoempleado> findPagoempleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pagoempleado.class));
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

    public Pagoempleado findPagoempleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pagoempleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoempleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pagoempleado> rt = cq.from(Pagoempleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
