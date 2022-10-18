/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pabdnew.learnmigratedb;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pabdnew.learnmigratedb.exceptions.IllegalOrphanException;
import pabdnew.learnmigratedb.exceptions.NonexistentEntityException;
import pabdnew.learnmigratedb.exceptions.PreexistingEntityException;

/**
 *
 * @author acer
 */
public class KueJpaController implements Serializable {

    public KueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("pabdnew_learnmigratedb_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kue kue) throws PreexistingEntityException, Exception {
        if (kue.getTransaksiCollection() == null) {
            kue.setTransaksiCollection(new ArrayList<Transaksi>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Transaksi> attachedTransaksiCollection = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionTransaksiToAttach : kue.getTransaksiCollection()) {
                transaksiCollectionTransaksiToAttach = em.getReference(transaksiCollectionTransaksiToAttach.getClass(), transaksiCollectionTransaksiToAttach.getKodeTransaksi());
                attachedTransaksiCollection.add(transaksiCollectionTransaksiToAttach);
            }
            kue.setTransaksiCollection(attachedTransaksiCollection);
            em.persist(kue);
            for (Transaksi transaksiCollectionTransaksi : kue.getTransaksiCollection()) {
                Kue oldKodeKueOfTransaksiCollectionTransaksi = transaksiCollectionTransaksi.getKodeKue();
                transaksiCollectionTransaksi.setKodeKue(kue);
                transaksiCollectionTransaksi = em.merge(transaksiCollectionTransaksi);
                if (oldKodeKueOfTransaksiCollectionTransaksi != null) {
                    oldKodeKueOfTransaksiCollectionTransaksi.getTransaksiCollection().remove(transaksiCollectionTransaksi);
                    oldKodeKueOfTransaksiCollectionTransaksi = em.merge(oldKodeKueOfTransaksiCollectionTransaksi);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKue(kue.getKodeKue()) != null) {
                throw new PreexistingEntityException("Kue " + kue + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kue kue) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kue persistentKue = em.find(Kue.class, kue.getKodeKue());
            Collection<Transaksi> transaksiCollectionOld = persistentKue.getTransaksiCollection();
            Collection<Transaksi> transaksiCollectionNew = kue.getTransaksiCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaksi transaksiCollectionOldTransaksi : transaksiCollectionOld) {
                if (!transaksiCollectionNew.contains(transaksiCollectionOldTransaksi)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaksi " + transaksiCollectionOldTransaksi + " since its kodeKue field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Transaksi> attachedTransaksiCollectionNew = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionNewTransaksiToAttach : transaksiCollectionNew) {
                transaksiCollectionNewTransaksiToAttach = em.getReference(transaksiCollectionNewTransaksiToAttach.getClass(), transaksiCollectionNewTransaksiToAttach.getKodeTransaksi());
                attachedTransaksiCollectionNew.add(transaksiCollectionNewTransaksiToAttach);
            }
            transaksiCollectionNew = attachedTransaksiCollectionNew;
            kue.setTransaksiCollection(transaksiCollectionNew);
            kue = em.merge(kue);
            for (Transaksi transaksiCollectionNewTransaksi : transaksiCollectionNew) {
                if (!transaksiCollectionOld.contains(transaksiCollectionNewTransaksi)) {
                    Kue oldKodeKueOfTransaksiCollectionNewTransaksi = transaksiCollectionNewTransaksi.getKodeKue();
                    transaksiCollectionNewTransaksi.setKodeKue(kue);
                    transaksiCollectionNewTransaksi = em.merge(transaksiCollectionNewTransaksi);
                    if (oldKodeKueOfTransaksiCollectionNewTransaksi != null && !oldKodeKueOfTransaksiCollectionNewTransaksi.equals(kue)) {
                        oldKodeKueOfTransaksiCollectionNewTransaksi.getTransaksiCollection().remove(transaksiCollectionNewTransaksi);
                        oldKodeKueOfTransaksiCollectionNewTransaksi = em.merge(oldKodeKueOfTransaksiCollectionNewTransaksi);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = kue.getKodeKue();
                if (findKue(id) == null) {
                    throw new NonexistentEntityException("The kue with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kue kue;
            try {
                kue = em.getReference(Kue.class, id);
                kue.getKodeKue();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kue with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaksi> transaksiCollectionOrphanCheck = kue.getTransaksiCollection();
            for (Transaksi transaksiCollectionOrphanCheckTransaksi : transaksiCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Kue (" + kue + ") cannot be destroyed since the Transaksi " + transaksiCollectionOrphanCheckTransaksi + " in its transaksiCollection field has a non-nullable kodeKue field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(kue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kue> findKueEntities() {
        return findKueEntities(true, -1, -1);
    }

    public List<Kue> findKueEntities(int maxResults, int firstResult) {
        return findKueEntities(false, maxResults, firstResult);
    }

    private List<Kue> findKueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kue.class));
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

    public Kue findKue(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kue.class, id);
        } finally {
            em.close();
        }
    }

    public int getKueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kue> rt = cq.from(Kue.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
