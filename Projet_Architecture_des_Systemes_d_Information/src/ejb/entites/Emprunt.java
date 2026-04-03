package ejb.entites;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Entity
public class Emprunt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private Date date;

    @Enumerated(EnumType.STRING)
    private EtatEmprunt etat;

    @ManyToOne
    private Particulier emprunteur;

    @OneToOne
    private Equipement equipement;

    public Emprunt() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EtatEmprunt getEtat() {
        return etat;
    }

    public void setEtat(EtatEmprunt etat) {
        this.etat = etat;
    }

    public Particulier getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Particulier emprunteur) {
        this.emprunteur = emprunteur;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }
}