package ejb.sessions;
 
import jakarta.ejb.Local;
 
@Local
public interface GestionEquipementLocal {
 
    void modifierEtatEmprunt(String email, int idEmprunt, String nouvelEtat)
    throws EmpruntIntrouvableException, ChangementEtatNonAutoriseException;
}