package ejb.entites;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_equipement")
public class Equipement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération automatique de l'ID
    private int id;
    private String nom;
    private double valeur;
    
    @ManyToOne
    private Particulier loueur;

    @ManyToOne
    private Categorie categorie;

    @OneToOne(mappedBy = "equipement")
    private Emprunt emprunt;

    public Equipement() {}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getValeur() {
        return valeur;
    }
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    public Particulier getLoueur() {
        return loueur;
    }
    public void setLoueur(Particulier loueur) {
        this.loueur = loueur;
    }
    public Categorie getCategorie() {
        return categorie;
    }
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
    public Emprunt getEmprunt() {
        return emprunt;
    }
    public void setEmprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
    }
}