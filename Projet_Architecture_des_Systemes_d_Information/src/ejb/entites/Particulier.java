package ejb.entites;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Particulier implements Serializable {

    @Id
    private String email;
    private String adresse;
    private double latitude;
    private double longitude;
    private int credits;

    @OneToMany(mappedBy = "loueur")
    private List<Equipement> equipementsProposes;

    @OneToMany(mappedBy = "emprunteur")
    private List<Emprunt> emprunts;

    public Particulier() {}

    @Transient
    public boolean isPeutEmprunter() {
        return this.credits > 0; 
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public List<Equipement> getEquipementsProposes() {
        return equipementsProposes;
    }

    public void setEquipementsProposes(List<Equipement> equipementsProposes) {
        this.equipementsProposes = equipementsProposes;
    }

    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    
}