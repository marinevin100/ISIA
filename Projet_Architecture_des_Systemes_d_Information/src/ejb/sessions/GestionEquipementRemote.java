package ejb.sessions;
 
import ejb.entites.*;
import jakarta.ejb.Remote;
import java.sql.Date;
import java.util.List;
 
@Remote
public interface GestionEquipementRemote {

    Particulier creerParticulier(String email, String adresse, double latitude, double longitude)
    throws ParticulierDejaExistantException, ParticulierIntrouvableException ;
 
    Equipement proposerEquipement(String emailLoueur, String nom, double valeur,
                                  int idCategorie, boolean estElectrique, Boolean estFilaire)
                                  throws ParticulierIntrouvableException, CategorieIntrouvableException;
 
    List<Categorie> listerCategories();
 
    List<Emprunt> listerEmpruntsDesMesEquipements(String emailLoueur);
 
    List<Equipement> listerEquipementsProches(String emailEmprunteur, int idCategorie)
    throws ParticulierIntrouvableException;
 
    List<Emprunt> listerMesEmprunts(String emailEmprunteur);
 
    Emprunt faireDemandeDEmprunt(String emailEmprunteur, int idEquipement, Date dateEmprunt)
    throws ParticulierIntrouvableException, EquipementIntrouvableException,
    ProprietaireNePeutPasEmprunterException, DateEmpruntInvalideException,
    CreditsInsuffisantsException, AucunEquipementProposException,
    EmpruntEnRetardException, EquipementDejaDemandeException;
 
    void modifierEtatEmprunt(String email, int idEmprunt, String nouvelEtat)
    throws EmpruntIntrouvableException, ChangementEtatNonAutoriseException;
}