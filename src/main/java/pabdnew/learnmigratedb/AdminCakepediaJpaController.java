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
import pabdnew.learnmigratedb.exceptions.NonexistentEntityException;
import pabdnew.learnmigratedb.exceptions.PreexistingEntityException;

/**
 *
 * @author acer
 */
public class AdminCakepediaJpaController implements Serializable {

    public AdminCakepediaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AdminCakepedia adminCakepedia) throws PreexistingEntityException, Exception {
        if (adminCakepedia.getTransaksiCollection() == null) {
            adminCakepedia.setTransaksiCollection(new ArrayList<Transaksi>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Transaksi> attachedTransaksiCollection = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionTransaksiToAttach : adminCakepedia.getTransaksiCollection()) {
                transaksiCollectionTransaksiToAttach = em.getReference(transaksiCollectionTransaksiToAttach.getClass(), transaksiCollectionTransaksiToAttach.getKodeTransaksi());
                attachedTransaksiCollection.add(transaksiCollectionTransaksiToAttach);
            }
            adminCakepedia.setTransaksiCollection(attachedTransaksiCollection);
            em.persist(adminCakepedia);
            for (Transaksi transaksiCollectionTransaksi : adminCakepedia.getTransaksiCollection()) {
                AdminCakepedia oldUsernameOfTransaksiCollectionTransaksi = transaksiCollectionTransaksi.getUsername();
                transaksiCollectionTransaksi.setUsername(adminCakepedia);
                transaksiCollectionTransaksi = em.merge(transaksiCollectionTransaksi);
                if (oldUsernameOfTransaksiCollectionTransaksi != null) {
                    oldUsernameOfTransaksiCollectionTransaksi.getTransaksiCollection().remove(transaksiCollectionTransaksi);
                    oldUsernameOfTransaksiCollectionTransaksi = em.merge(oldUsernameOfTransaksiCollectionTransaksi);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdminCakepedia(adminCakepedia.getUsername()) != null) {
                throw new PreexistingEntityException("AdminCakepedia " + adminCakepedia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AdminCakepedia adminCakepedia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdminCakepedia persistentAdminCakepedia = em.find(AdminCakepedia.class, adminCakepedia.getUsername());
            Collection<Transaksi> transaksiCollectionOld = persistentAdminCakepedia.getTransaksiCollection();
            Collection<Transaksi> transaksiCollectionNew = adminCakepedia.getTransaksiCollection();
            Collection<Transaksi> attachedTransaksiCollectionNew = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionNewTransaksiToAttach : transaksiCollectionNew) {
                transaksiCollectionNewTransaksiToAttach = em.getReference(transaksiCollectionNewTransaksiToAttach.getClass(), transaksiCollectionNewTransaksiToAttach.getKodeTransaksi());
                attachedTransaksiCollectionNew.add(transaksiCollectionNewTransaksiToAttach);
            }
            transaksiCollectionNew = attachedTransaksiCollectionNew;
            adminCakepedia.setTransaksiCollection(transaksiCollectionNew);
            adminCakepedia = em.merge(adminCakepedia);
            for (Transaksi transaksiCollectionOldTransaksi : transaksiCollectionOld) {
                if (!transaksiCollectionNew.contains(transaksiCollectionOldTransaksi)) {
                    transaksiCollectionOldTransaksi.setUsername(null);
                    transaksiCollectionOldTransaksi = em.merge(transaksiCollectionOldTransaksi);
                }
            }
            for (Transaksi transaksiCollectionNewTransaksi : transaksiCollectionNew) {
                if (!transaksiCollectionOld.contains(transaksiCollectionNewTransaksi)) {
                    AdminCakepedia oldUsernameOfTransaksiCollectionNewTransaksi = transaksiCollectionNewTransaksi.getUsername();
                    transaksiCollectionNewTransaksi.setUsername(adminCakepedia);
                    transaksiCollectionNewTransaksi = em.merge(transaksiCollectionNewTransaksi);
                    if (oldUsernameOfTransaksiCollectionNewTransaksi != null && !oldUsernameOfTransaksiCollectionNewTransaksi.equals(adminCakepedia)) {
                        oldUsernameOfTransaksiCollectionNewTransaksi.getTransaksiCollection().remove(transaksiCollectionNewTransaksi);
                        oldUsernameOfTransaksiCollectionNewTransaksi = em.merge(oldUsernameOfTransaksiCollectionNewTransaksi);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = adminCakepedia.getUsername();
                if (findAdminCakepedia(id) == null) {
                    throw new NonexistentEntityException("The adminCakepedia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdminCakepedia adminCakepedia;
            try {
                adminCakepedia = em.getReference(AdminCakepedia.class, id);
                adminCakepedia.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adminCakepedia with id " + id + " no longer exists.", enfe);
            }
            Collection<Transaksi> transaksiCollection = adminCakepedia.getTransaksiCollection();
            for (Transaksi transaksiCollectionTransaksi : transaksiCollection) {
                transaksiCollectionTransaksi.setUsername(null);
                transaksiCollectionTransaksi = em.merge(transaksiCollectionTransaksi);
            }
            em.remove(adminCakepedia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AdminCakepedia> findAdminCakepediaEntities() {
        return findAdminCakepediaEntities(true, -1, -1);
    }

    public List<AdminCakepedia> findAdminCakepediaEntities(int maxResults, int firstResult) {
        return findAdminCakepediaEntities(false, maxResults, firstResult);
    }

    private List<AdminCakepedia> findAdminCakepediaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AdminCakepedia.class));
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

    public AdminCakepedia findAdminCakepedia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AdminCakepedia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdminCakepediaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AdminCakepedia> rt = cq.from(AdminCakepedia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
