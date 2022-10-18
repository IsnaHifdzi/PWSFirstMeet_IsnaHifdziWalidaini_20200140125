/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pabdnew.learnmigratedb;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pabdnew.learnmigratedb.exceptions.NonexistentEntityException;
import pabdnew.learnmigratedb.exceptions.PreexistingEntityException;

/**
 *
 * @author acer
 */
public class TransaksiJpaController implements Serializable {

    public TransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaksi transaksi) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli idPembeli = transaksi.getIdPembeli();
            if (idPembeli != null) {
                idPembeli = em.getReference(idPembeli.getClass(), idPembeli.getIdPembeli());
                transaksi.setIdPembeli(idPembeli);
            }
            Kue kodeKue = transaksi.getKodeKue();
            if (kodeKue != null) {
                kodeKue = em.getReference(kodeKue.getClass(), kodeKue.getKodeKue());
                transaksi.setKodeKue(kodeKue);
            }
            AdminCakepedia username = transaksi.getUsername();
            if (username != null) {
                username = em.getReference(username.getClass(), username.getUsername());
                transaksi.setUsername(username);
            }
            em.persist(transaksi);
            if (idPembeli != null) {
                idPembeli.getTransaksiCollection().add(transaksi);
                idPembeli = em.merge(idPembeli);
            }
            if (kodeKue != null) {
                kodeKue.getTransaksiCollection().add(transaksi);
                kodeKue = em.merge(kodeKue);
            }
            if (username != null) {
                username.getTransaksiCollection().add(transaksi);
                username = em.merge(username);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaksi(transaksi.getKodeTransaksi()) != null) {
                throw new PreexistingEntityException("Transaksi " + transaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaksi transaksi) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi persistentTransaksi = em.find(Transaksi.class, transaksi.getKodeTransaksi());
            Pembeli idPembeliOld = persistentTransaksi.getIdPembeli();
            Pembeli idPembeliNew = transaksi.getIdPembeli();
            Kue kodeKueOld = persistentTransaksi.getKodeKue();
            Kue kodeKueNew = transaksi.getKodeKue();
            AdminCakepedia usernameOld = persistentTransaksi.getUsername();
            AdminCakepedia usernameNew = transaksi.getUsername();
            if (idPembeliNew != null) {
                idPembeliNew = em.getReference(idPembeliNew.getClass(), idPembeliNew.getIdPembeli());
                transaksi.setIdPembeli(idPembeliNew);
            }
            if (kodeKueNew != null) {
                kodeKueNew = em.getReference(kodeKueNew.getClass(), kodeKueNew.getKodeKue());
                transaksi.setKodeKue(kodeKueNew);
            }
            if (usernameNew != null) {
                usernameNew = em.getReference(usernameNew.getClass(), usernameNew.getUsername());
                transaksi.setUsername(usernameNew);
            }
            transaksi = em.merge(transaksi);
            if (idPembeliOld != null && !idPembeliOld.equals(idPembeliNew)) {
                idPembeliOld.getTransaksiCollection().remove(transaksi);
                idPembeliOld = em.merge(idPembeliOld);
            }
            if (idPembeliNew != null && !idPembeliNew.equals(idPembeliOld)) {
                idPembeliNew.getTransaksiCollection().add(transaksi);
                idPembeliNew = em.merge(idPembeliNew);
            }
            if (kodeKueOld != null && !kodeKueOld.equals(kodeKueNew)) {
                kodeKueOld.getTransaksiCollection().remove(transaksi);
                kodeKueOld = em.merge(kodeKueOld);
            }
            if (kodeKueNew != null && !kodeKueNew.equals(kodeKueOld)) {
                kodeKueNew.getTransaksiCollection().add(transaksi);
                kodeKueNew = em.merge(kodeKueNew);
            }
            if (usernameOld != null && !usernameOld.equals(usernameNew)) {
                usernameOld.getTransaksiCollection().remove(transaksi);
                usernameOld = em.merge(usernameOld);
            }
            if (usernameNew != null && !usernameNew.equals(usernameOld)) {
                usernameNew.getTransaksiCollection().add(transaksi);
                usernameNew = em.merge(usernameNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = transaksi.getKodeTransaksi();
                if (findTransaksi(id) == null) {
                    throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.");
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
            Transaksi transaksi;
            try {
                transaksi = em.getReference(Transaksi.class, id);
                transaksi.getKodeTransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.", enfe);
            }
            Pembeli idPembeli = transaksi.getIdPembeli();
            if (idPembeli != null) {
                idPembeli.getTransaksiCollection().remove(transaksi);
                idPembeli = em.merge(idPembeli);
            }
            Kue kodeKue = transaksi.getKodeKue();
            if (kodeKue != null) {
                kodeKue.getTransaksiCollection().remove(transaksi);
                kodeKue = em.merge(kodeKue);
            }
            AdminCakepedia username = transaksi.getUsername();
            if (username != null) {
                username.getTransaksiCollection().remove(transaksi);
                username = em.merge(username);
            }
            em.remove(transaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaksi> findTransaksiEntities() {
        return findTransaksiEntities(true, -1, -1);
    }

    public List<Transaksi> findTransaksiEntities(int maxResults, int firstResult) {
        return findTransaksiEntities(false, maxResults, firstResult);
    }

    private List<Transaksi> findTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaksi.class));
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

    public Transaksi findTransaksi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaksi> rt = cq.from(Transaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
