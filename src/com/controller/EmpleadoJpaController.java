/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.exceptions.NonexistentEntityException;
import com.entities.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Proyecto;
import com.entities.Pagoempleado;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Nombre del controller: EmpleadoJpaController
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public EmpleadoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ConstructoraSVPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) {
        if (empleado.getPagoempleadoCollection() == null) {
            empleado.setPagoempleadoCollection(new ArrayList<Pagoempleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto idProyecto = empleado.getIdProyecto();
            if (idProyecto != null) {
                idProyecto = em.getReference(idProyecto.getClass(), idProyecto.getIdProyecto());
                empleado.setIdProyecto(idProyecto);
            }
            Collection<Pagoempleado> attachedPagoempleadoCollection = new ArrayList<Pagoempleado>();
            for (Pagoempleado pagoempleadoCollectionPagoempleadoToAttach : empleado.getPagoempleadoCollection()) {
                pagoempleadoCollectionPagoempleadoToAttach = em.getReference(pagoempleadoCollectionPagoempleadoToAttach.getClass(), pagoempleadoCollectionPagoempleadoToAttach.getIdPago());
                attachedPagoempleadoCollection.add(pagoempleadoCollectionPagoempleadoToAttach);
            }
            empleado.setPagoempleadoCollection(attachedPagoempleadoCollection);
            em.persist(empleado);
            if (idProyecto != null) {
                idProyecto.getEmpleadoCollection().add(empleado);
                idProyecto = em.merge(idProyecto);
            }
            for (Pagoempleado pagoempleadoCollectionPagoempleado : empleado.getPagoempleadoCollection()) {
                Empleado oldIdEmpleadoOfPagoempleadoCollectionPagoempleado = pagoempleadoCollectionPagoempleado.getIdEmpleado();
                pagoempleadoCollectionPagoempleado.setIdEmpleado(empleado);
                pagoempleadoCollectionPagoempleado = em.merge(pagoempleadoCollectionPagoempleado);
                if (oldIdEmpleadoOfPagoempleadoCollectionPagoempleado != null) {
                    oldIdEmpleadoOfPagoempleadoCollectionPagoempleado.getPagoempleadoCollection().remove(pagoempleadoCollectionPagoempleado);
                    oldIdEmpleadoOfPagoempleadoCollectionPagoempleado = em.merge(oldIdEmpleadoOfPagoempleadoCollectionPagoempleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            Proyecto idProyectoOld = persistentEmpleado.getIdProyecto();
            Proyecto idProyectoNew = empleado.getIdProyecto();
            Collection<Pagoempleado> pagoempleadoCollectionOld = persistentEmpleado.getPagoempleadoCollection();
            Collection<Pagoempleado> pagoempleadoCollectionNew = empleado.getPagoempleadoCollection();
            if (idProyectoNew != null) {
                idProyectoNew = em.getReference(idProyectoNew.getClass(), idProyectoNew.getIdProyecto());
                empleado.setIdProyecto(idProyectoNew);
            }
            Collection<Pagoempleado> attachedPagoempleadoCollectionNew = new ArrayList<Pagoempleado>();
            for (Pagoempleado pagoempleadoCollectionNewPagoempleadoToAttach : pagoempleadoCollectionNew) {
                pagoempleadoCollectionNewPagoempleadoToAttach = em.getReference(pagoempleadoCollectionNewPagoempleadoToAttach.getClass(), pagoempleadoCollectionNewPagoempleadoToAttach.getIdPago());
                attachedPagoempleadoCollectionNew.add(pagoempleadoCollectionNewPagoempleadoToAttach);
            }
            pagoempleadoCollectionNew = attachedPagoempleadoCollectionNew;
            empleado.setPagoempleadoCollection(pagoempleadoCollectionNew);
            empleado = em.merge(empleado);
            if (idProyectoOld != null && !idProyectoOld.equals(idProyectoNew)) {
                idProyectoOld.getEmpleadoCollection().remove(empleado);
                idProyectoOld = em.merge(idProyectoOld);
            }
            if (idProyectoNew != null && !idProyectoNew.equals(idProyectoOld)) {
                idProyectoNew.getEmpleadoCollection().add(empleado);
                idProyectoNew = em.merge(idProyectoNew);
            }
            for (Pagoempleado pagoempleadoCollectionOldPagoempleado : pagoempleadoCollectionOld) {
                if (!pagoempleadoCollectionNew.contains(pagoempleadoCollectionOldPagoempleado)) {
                    pagoempleadoCollectionOldPagoempleado.setIdEmpleado(null);
                    pagoempleadoCollectionOldPagoempleado = em.merge(pagoempleadoCollectionOldPagoempleado);
                }
            }
            for (Pagoempleado pagoempleadoCollectionNewPagoempleado : pagoempleadoCollectionNew) {
                if (!pagoempleadoCollectionOld.contains(pagoempleadoCollectionNewPagoempleado)) {
                    Empleado oldIdEmpleadoOfPagoempleadoCollectionNewPagoempleado = pagoempleadoCollectionNewPagoempleado.getIdEmpleado();
                    pagoempleadoCollectionNewPagoempleado.setIdEmpleado(empleado);
                    pagoempleadoCollectionNewPagoempleado = em.merge(pagoempleadoCollectionNewPagoempleado);
                    if (oldIdEmpleadoOfPagoempleadoCollectionNewPagoempleado != null && !oldIdEmpleadoOfPagoempleadoCollectionNewPagoempleado.equals(empleado)) {
                        oldIdEmpleadoOfPagoempleadoCollectionNewPagoempleado.getPagoempleadoCollection().remove(pagoempleadoCollectionNewPagoempleado);
                        oldIdEmpleadoOfPagoempleadoCollectionNewPagoempleado = em.merge(oldIdEmpleadoOfPagoempleadoCollectionNewPagoempleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            Proyecto idProyecto = empleado.getIdProyecto();
            if (idProyecto != null) {
                idProyecto.getEmpleadoCollection().remove(empleado);
                idProyecto = em.merge(idProyecto);
            }
            Collection<Pagoempleado> pagoempleadoCollection = empleado.getPagoempleadoCollection();
            for (Pagoempleado pagoempleadoCollectionPagoempleado : pagoempleadoCollection) {
                pagoempleadoCollectionPagoempleado.setIdEmpleado(null);
                pagoempleadoCollectionPagoempleado = em.merge(pagoempleadoCollectionPagoempleado);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Empleado> getEmpleado(int idEmpleado){
        List<Empleado> resultado = null;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT p FROM Empleado p where p.idEmpleado = :idEmpleado");
            query.setParameter("idEmpleado", idEmpleado);
            
            resultado = query.getResultList();
            
        } catch (Exception e) {
            
        }
        return resultado;
    }
    
}
