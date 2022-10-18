/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pabdnew.learnmigratedb;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author acer
 */
@Entity
@Table(name = "admin_cakepedia")
@NamedQueries({
    @NamedQuery(name = "AdminCakepedia.findAll", query = "SELECT a FROM AdminCakepedia a"),
    @NamedQuery(name = "AdminCakepedia.findByUsername", query = "SELECT a FROM AdminCakepedia a WHERE a.username = :username"),
    @NamedQuery(name = "AdminCakepedia.findByPasswordAdmin", query = "SELECT a FROM AdminCakepedia a WHERE a.passwordAdmin = :passwordAdmin")})
public class AdminCakepedia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Username")
    private String username;
    @Basic(optional = false)
    @Column(name = "Password_Admin")
    private String passwordAdmin;
    @OneToMany(mappedBy = "username")
    private Collection<Transaksi> transaksiCollection;

    public AdminCakepedia() {
    }

    public AdminCakepedia(String username) {
        this.username = username;
    }

    public AdminCakepedia(String username, String passwordAdmin) {
        this.username = username;
        this.passwordAdmin = passwordAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public Collection<Transaksi> getTransaksiCollection() {
        return transaksiCollection;
    }

    public void setTransaksiCollection(Collection<Transaksi> transaksiCollection) {
        this.transaksiCollection = transaksiCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminCakepedia)) {
            return false;
        }
        AdminCakepedia other = (AdminCakepedia) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pabdnew.learnmigratedb.AdminCakepedia[ username=" + username + " ]";
    }
    
}
