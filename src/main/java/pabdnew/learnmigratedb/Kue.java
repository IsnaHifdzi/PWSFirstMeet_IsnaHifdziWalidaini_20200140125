/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pabdnew.learnmigratedb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "kue")
@NamedQueries({
    @NamedQuery(name = "Kue.findAll", query = "SELECT k FROM Kue k"),
    @NamedQuery(name = "Kue.findByKodeKue", query = "SELECT k FROM Kue k WHERE k.kodeKue = :kodeKue"),
    @NamedQuery(name = "Kue.findByNamaKue", query = "SELECT k FROM Kue k WHERE k.namaKue = :namaKue"),
    @NamedQuery(name = "Kue.findByHarga", query = "SELECT k FROM Kue k WHERE k.harga = :harga")})
public class Kue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Kue")
    private String kodeKue;
    @Basic(optional = false)
    @Column(name = "Nama_Kue")
    private String namaKue;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "Harga")
    private BigDecimal harga;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kodeKue")
    private Collection<Transaksi> transaksiCollection;

    public Kue() {
    }

    public Kue(String kodeKue) {
        this.kodeKue = kodeKue;
    }

    public Kue(String kodeKue, String namaKue, BigDecimal harga) {
        this.kodeKue = kodeKue;
        this.namaKue = namaKue;
        this.harga = harga;
    }

    public String getKodeKue() {
        return kodeKue;
    }

    public void setKodeKue(String kodeKue) {
        this.kodeKue = kodeKue;
    }

    public String getNamaKue() {
        return namaKue;
    }

    public void setNamaKue(String namaKue) {
        this.namaKue = namaKue;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
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
        hash += (kodeKue != null ? kodeKue.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kue)) {
            return false;
        }
        Kue other = (Kue) object;
        if ((this.kodeKue == null && other.kodeKue != null) || (this.kodeKue != null && !this.kodeKue.equals(other.kodeKue))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pabdnew.learnmigratedb.Kue[ kodeKue=" + kodeKue + " ]";
    }
    
}
