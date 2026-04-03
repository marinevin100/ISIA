package ejb.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ELECTRIQUE")
public class EquipementElectrique extends Equipement {

    private boolean estFilaire;

    public EquipementElectrique() {}

    public boolean isEstFilaire() {
        return estFilaire;
    }

    public void setEstFilaire(boolean estFilaire) {
        this.estFilaire = estFilaire;
    }
}