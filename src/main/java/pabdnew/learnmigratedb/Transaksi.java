/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pabdnew.learnmigratedb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author acer
 */
@Entity
@Table(name = "transaksi")
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByKodeTransaksi", query = "SELECT t FROM Transaksi t WHERE t.kodeTransaksi = :kodeTransaksi"),
    @NamedQuery(name = "Transaksi.findByJumlah", query = "SELECT t FROM Transaksi t WHERE t.jumlah = :jumlah")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Kode_Transaksi")
    private String kodeTransaksi;
    @Basic(optional = false)
    @Column(name = "Jumlah")
    private String jumlah;
    @JoinColumn(name = "Id_Pembeli", referencedColumnName = "Id_Pembeli")
    @ManyToOne(optional = false)
    private Pembeli idPembeli;
    @JoinColumn(name = "Kode_Kue", referencedColumnName = "Kode_Kue")
    @ManyToOne(optional = false)
    private Kue kodeKue;
    @JoinColumn(name = "Username", referencedColumnName = "Username")
    @ManyToOne
    private AdminCakepedia username;

    public Transaksi() {
    }

    public Transaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public Transaksi(String kodeTransaksi, String jumlah) {
        this.kodeTransaksi = kodeTransaksi;
        this.jumlah = jumlah;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public Pembeli getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(Pembeli idPembeli) {
        this.idPembeli = idPembeli;
    }

    public Kue getKodeKue() {
        return kodeKue;
    }

    public void setKodeKue(Kue kodeKue) {
        this.kodeKue = kodeKue;
    }

    public AdminCakepedia getUsername() {
        return username;
    }

    public void setUsername(AdminCakepedia username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeTransaksi != null ? kodeTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.kodeTransaksi == null && other.kodeTransaksi != null) || (this.kodeTransaksi != null && !this.kodeTransaksi.equals(other.kodeTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pabdnew.learnmigratedb.Transaksi[ kodeTransaksi=" + kodeTransaksi + " ]";
    }
    
}
