package ejb.sessions;
 
import ejb.entites.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
 


@Stateless
public class GestionEquipementBean implements GestionEquipementRemote, GestionEquipementLocal {
 
    @PersistenceContext(unitName = "SolidarTool")
    private EntityManager em;
 
    @Override
    public Particulier creerParticulier(String email, String adresse,
                                        double latitude, double longitude) throws ParticulierDejaExistantException {
        if (em.find(Particulier.class, email) != null) {
            throw new ParticulierDejaExistantException("Email déjà utilisé : " + email);
        }
        Particulier p = new Particulier();
        p.setEmail(email);
        p.setAdresse(adresse);
        p.setLatitude(latitude);
        p.setLongitude(longitude);
        p.setCredits(50);
        em.persist(p);
        return p;
    }
 
    @Override
    public Equipement proposerEquipement(String emailLoueur, String nom, double valeur,
                                          int idCategorie, boolean estElectrique, Boolean estFilaire)
                                          throws ParticulierIntrouvableException, CategorieIntrouvableException {
        Particulier loueur = em.find(Particulier.class, emailLoueur);
        if (loueur == null) throw new ParticulierIntrouvableException("Particulier introuvable : " + emailLoueur);
 
        Categorie categorie = em.find(Categorie.class, idCategorie);
        if (categorie == null) throw new CategorieIntrouvableException("Catégorie introuvable : " + idCategorie);
 
        Equipement equipement;
        if (estElectrique) {
            EquipementElectrique ee = new EquipementElectrique();
            ee.setEstFilaire(estFilaire != null && estFilaire);
            equipement = ee;
        } else {
            equipement = new Equipement();
        }
        equipement.setNom(nom);
        equipement.setValeur(valeur);
        equipement.setLoueur(loueur);
        equipement.setCategorie(categorie);
        em.persist(equipement);
 
        loueur.setCredits(loueur.getCredits() + 2);
        em.merge(loueur);
 
        return equipement;
    }
 
    @Override
    public List<Categorie> listerCategories() {
        return em.createQuery("SELECT c FROM Categorie c ORDER BY c.nom", Categorie.class)
                 .getResultList();
    }
 
    @Override
    public List<Emprunt> listerEmpruntsDesMesEquipements(String emailLoueur) {
        return em.createQuery(
            "SELECT e FROM Emprunt e WHERE e.equipement.loueur.email = :email",
            Emprunt.class
        ).setParameter("email", emailLoueur).getResultList();
    }
 
    @Override
    public List<Equipement> listerEquipementsProches(String emailEmprunteur, int idCategorie)
    throws ParticulierIntrouvableException{
        Particulier emprunteur = em.find(Particulier.class, emailEmprunteur);
        if (emprunteur == null) throw new ParticulierIntrouvableException("Particulier introuvable");
 
        List<Equipement> tous = em.createQuery(
            "SELECT eq FROM Equipement eq WHERE eq.categorie.id = :idCat " +
            "AND eq.loueur.email != :email",
            Equipement.class
        ).setParameter("idCat", idCategorie)
         .setParameter("email", emailEmprunteur)
         .getResultList();
 
        return tous.stream()
            .filter(eq -> Utils.calculerDistance(
                emprunteur.getLatitude(), emprunteur.getLongitude(),
                eq.getLoueur().getLatitude(), eq.getLoueur().getLongitude()
            ) <= 5.0)
            .collect(Collectors.toList());
    }
 
    @Override
    public List<Emprunt> listerMesEmprunts(String emailEmprunteur) {
        return em.createQuery(
            "SELECT e FROM Emprunt e WHERE e.emprunteur.email = :email",
            Emprunt.class
        ).setParameter("email", emailEmprunteur).getResultList();
    }
 
    @Override
    public Emprunt faireDemandeDEmprunt(String emailEmprunteur, int idEquipement, Date dateEmprunt)
    throws ParticulierIntrouvableException, EquipementIntrouvableException,
    ProprietaireNePeutPasEmprunterException, DateEmpruntInvalideException,
    CreditsInsuffisantsException, AucunEquipementProposException,
    EmpruntEnRetardException, EquipementDejaDemandeException{
        Particulier emprunteur = em.find(Particulier.class, emailEmprunteur);
        if (emprunteur == null) throw new ParticulierIntrouvableException("Particulier introuvable");
 
        Equipement equipement = em.find(Equipement.class, idEquipement);
        if (equipement == null) throw new EquipementIntrouvableException("Équipement introuvable");
 
        Date today = Utils.today;
 
        if (equipement.getLoueur().getEmail().equals(emailEmprunteur)) {
            throw new ProprietaireNePeutPasEmprunterException("Vous ne pouvez pas emprunter votre propre équipement");
        }
 
        if (!Utils.isBefore(today, dateEmprunt)) {
            throw new DateEmpruntInvalideException("La date d'emprunt doit être au moins 1 jour après aujourd'hui");
        }
 
        if (!emprunteur.isPeutEmprunter()) {
            throw new CreditsInsuffisantsException("Vous n'avez pas de crédits suffisants");
        }
 
        Long nbEquipsProposes = em.createQuery(
            "SELECT COUNT(eq) FROM Equipement eq WHERE eq.loueur.email = :email",
            Long.class
        ).setParameter("email", emailEmprunteur).getSingleResult();
        if (nbEquipsProposes == 0) {
            throw new AucunEquipementProposException("Vous devez proposer au moins un équipement pour pouvoir emprunter");
        }
 
        Long nbEnRetard = em.createQuery(
            "SELECT COUNT(e) FROM Emprunt e WHERE e.emprunteur.email = :email " +
            "AND e.etat = :etat AND e.date < :today",
            Long.class
        ).setParameter("email", emailEmprunteur)
         .setParameter("etat", EtatEmprunt.equipementEmprunte)
         .setParameter("today", today)
         .getSingleResult();
        if (nbEnRetard > 0) {
            throw new EmpruntEnRetardException("Vous avez un équipement emprunté en retard non retourné");
        }

        Long dejaDemandeJour = em.createQuery(
            "SELECT COUNT(e) FROM Emprunt e WHERE e.equipement.id = :idEquip " +
            "AND e.date = :date AND e.etat != :etatRefuse",
            Long.class
        ).setParameter("idEquip", idEquipement)
         .setParameter("date", dateEmprunt)
         .setParameter("etatRefuse", EtatEmprunt.demandeRefusee)
         .getSingleResult();
        if (dejaDemandeJour > 0) {
            throw new EquipementDejaDemandeException("Cet équipement a déjà été demandé pour ce jour");
        }
 
        Emprunt emprunt = new Emprunt();
        emprunt.setEmprunteur(emprunteur);
        emprunt.setEquipement(equipement);
        emprunt.setDate(dateEmprunt);
        emprunt.setEtat(EtatEmprunt.demandeEmprunt);
        em.persist(emprunt);
        return emprunt;
    }
 
    @Override
    public void modifierEtatEmprunt(String email, int idEmprunt, String nouvelEtatStr)
    throws EmpruntIntrouvableException, ChangementEtatNonAutoriseException {
 
        Emprunt emprunt = em.find(Emprunt.class, idEmprunt);
        if (emprunt == null)
            throw new EmpruntIntrouvableException("Emprunt introuvable : " + idEmprunt);
 
        Particulier loueur     = emprunt.getEquipement().getLoueur();
        Particulier emprunteur = emprunt.getEmprunteur();
        Equipement  equipement = emprunt.getEquipement();
        Date today             = Utils.today;
        Date dateEmprunt       = emprunt.getDate();
 
        boolean isLoueur     = loueur.getEmail().equals(email);
        boolean isEmprunteur = emprunteur.getEmail().equals(email);
 
        if (!isLoueur && !isEmprunteur)
            throw new ChangementEtatNonAutoriseException(
                "L'utilisateur '" + email + "' n'est ni le loueur ni l'emprunteur de cet emprunt");
 
        EtatEmprunt etatActuel = emprunt.getEtat();
 
        // ---- Passage à l'état FINAL (suppression) ----
        if (nouvelEtatStr == null || nouvelEtatStr.equalsIgnoreCase("FINAL")) {
            if (!Utils.isAfter(today, dateEmprunt))
                throw new ChangementEtatNonAutoriseException(
                    "La suppression n'est possible que le lendemain de la date d'emprunt");
 
            switch (etatActuel) {
                case demandeRefusee:
                    break;
                case demandeEmprunt:
                    loueur.setCredits(Math.max(0, loueur.getCredits() - 2));
                    em.merge(loueur);
                    break;
                case demandeAcceptee:
                    emprunteur.setCredits(Math.max(0, emprunteur.getCredits() - 2));
                    em.merge(emprunteur);
                    break;
                case equipementRetourne:
                    int cout = 2;
                    if (equipement.getValeur() > 100) cout++;
                    if (equipement instanceof EquipementElectrique) {
                        cout++;
                        if (!((EquipementElectrique) equipement).isEstFilaire()) cout++;
                    }
                    emprunteur.setCredits(Math.max(0, emprunteur.getCredits() - cout));
                    em.merge(emprunteur);
                    break;
                default:
                    throw new ChangementEtatNonAutoriseException(
                        "L'état '" + etatActuel + "' ne peut pas passer à l'état final");
            }
            em.remove(emprunt);
            return;
        }
 
        // ---- Transitions normales ----
        EtatEmprunt nouvelEtat;
        try {
            nouvelEtat = EtatEmprunt.valueOf(nouvelEtatStr);
        } catch (IllegalArgumentException e) {
            throw new ChangementEtatNonAutoriseException("État inconnu : '" + nouvelEtatStr + "'");
        }
 
        // demandeEmprunt → demandeRefusee  (loueur, dj < de)
        if (etatActuel == EtatEmprunt.demandeEmprunt
                && nouvelEtat == EtatEmprunt.demandeRefusee) {
            if (!isLoueur)
                throw new ChangementEtatNonAutoriseException("Seul le loueur peut refuser une demande d'emprunt");
            if (!Utils.isBefore(today, dateEmprunt))
                throw new ChangementEtatNonAutoriseException("Le refus n'est possible qu'avant la date d'emprunt");
            loueur.setCredits(Math.max(0, loueur.getCredits() - 1));
            em.merge(loueur);
        }
 
        // demandeEmprunt → demandeAcceptee  (loueur, dj < de)
        else if (etatActuel == EtatEmprunt.demandeEmprunt
                && nouvelEtat == EtatEmprunt.demandeAcceptee) {
            if (!isLoueur)
                throw new ChangementEtatNonAutoriseException("Seul le loueur peut accepter une demande d'emprunt");
            if (!Utils.isBefore(today, dateEmprunt))
                throw new ChangementEtatNonAutoriseException("L'acceptation n'est possible qu'avant la date d'emprunt");
            loueur.setCredits(loueur.getCredits() + 2);
            em.merge(loueur);
        }
 
        // demandeAcceptee → equipementEmprunte  (loueur, dj = de)
        else if (etatActuel == EtatEmprunt.demandeAcceptee
                && nouvelEtat == EtatEmprunt.equipementEmprunte) {
            if (!isLoueur)
                throw new ChangementEtatNonAutoriseException("Seul le loueur peut confirmer la remise de l'équipement");
            if (!Utils.isEqual(today, dateEmprunt))
                throw new ChangementEtatNonAutoriseException("La remise n'est possible que le jour de l'emprunt");
            emprunteur.setCredits(emprunteur.getCredits() + 1);
            em.merge(emprunteur);
        }
 
        // equipementEmprunte → equipementRetourne  (loueur, dj >= de)
        else if (etatActuel == EtatEmprunt.equipementEmprunte
                && nouvelEtat == EtatEmprunt.equipementRetourne) {
            if (!isLoueur)
                throw new ChangementEtatNonAutoriseException("Seul le loueur peut confirmer le retour de l'équipement");
            if (Utils.isBefore(today, dateEmprunt))
                throw new ChangementEtatNonAutoriseException("Le retour n'est possible qu'à partir du jour de l'emprunt");
            if (Utils.isEqual(today, dateEmprunt)) {
                emprunteur.setCredits(emprunteur.getCredits() + 1);
                em.merge(emprunteur);
            }
        }
 
        else {
            throw new ChangementEtatNonAutoriseException(
                "Transition invalide : '" + etatActuel + "' → '" + nouvelEtat + "'");
        }
 
        emprunt.setEtat(nouvelEtat);
        em.merge(emprunt);
    }
}