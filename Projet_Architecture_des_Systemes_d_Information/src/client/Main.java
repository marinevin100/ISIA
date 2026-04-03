package client;
 
import ejb.entites.*;
import ejb.sessions.GestionEquipementRemote;
 
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Date;
import java.util.List;
import java.util.Properties;
 
public class Main {
 
    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.load(Main.class.getClassLoader().getResourceAsStream("jndi.properties"));
        Context ctx = new InitialContext(props);
 
        GestionEquipementRemote service = (GestionEquipementRemote)
            ctx.lookup("ejb:solidar_tool/solidar_toolSessions/GestionEquipementBean!ejb.sessions.GestionEquipementRemote");

        service.creerParticulier(
            "Bastien.Cazaux@polytech-lille.fr",
            "Polytech",
            50.60773717826645, 3.1353520973453652
        );
 
        service.creerParticulier(
            "Louis.Roussel@univ-lille.fr",
            "CRIStAL",
            50.60709033783188, 3.13722964358617
        );
        service.creerParticulier(
            "Olivier.Caron@univ-lille.fr",
            "Halle Vallin",
            50.60993636922798, 3.1350302322755135
        );

		List<Categorie> categories = service.listerCategories();
        int idSport = -1, idJardinage = -1;
        for (Categorie c : categories) {
            if ("Sport".equalsIgnoreCase(c.getNom()))      idSport     = c.getId();
            if ("Jardinage".equalsIgnoreCase(c.getNom())) idJardinage = c.getId();
        }
 
        service.proposerEquipement(
            "Bastien.Cazaux@polytech-lille.fr",
            "vélo bleu ciel électrique",
            2200.0,
            idSport,
            true,   
            false   
        );
 
        Equipement tondeuseLouis = service.proposerEquipement(
            "Louis.Roussel@univ-lille.fr",
            "tondeuse électrique",
            450.0,
            idJardinage,
            true,  
            true  
        );

        Equipement raquetteOlivier = service.proposerEquipement(
            "Olivier.Caron@univ-lille.fr",
            "raquette orange",
            120.0,
            idSport,
            false, 
            null
        );

        System.out.println("\n--- Équipements proches de Louis dans la catégorie Sport ---");
        List<Equipement> prochesLouis = service.listerEquipementsProches(
            "Louis.Roussel@univ-lille.fr",
            idSport
        );

        for (Equipement eq : prochesLouis) {
            System.out.println("  [" + eq.getId() + "] " + eq.getNom()
                + " - " + eq.getValeur() + "€");
        }
 
        System.out.println("\n--- Demandes d'emprunt de Bastien ---");
 
        Emprunt emprunt1 = service.faireDemandeDEmprunt(
            "Bastien.Cazaux@polytech-lille.fr",
            raquetteOlivier.getId(),
            Date.valueOf("2027-03-25")
        );
        System.out.println("Demande créée : [" + emprunt1.getId() + "] "
            + emprunt1.getEquipement().getNom()
            + " le " + emprunt1.getDate()
            + " - état : " + emprunt1.getEtat());
 
        Emprunt emprunt2 = service.faireDemandeDEmprunt(
            "Bastien.Cazaux@polytech-lille.fr",
            tondeuseLouis.getId(),
            Date.valueOf("2027-03-27")
        );
        System.out.println("Demande créée : [" + emprunt2.getId() + "] "
            + emprunt2.getEquipement().getNom()
            + " le " + emprunt2.getDate()
            + " - état : " + emprunt2.getEtat());

        System.out.println("\n--- Emprunts des équipements de Bastien (en tant que loueur) ---");
        List<Emprunt> empruntsEquipsBastien = service.listerEmpruntsDesMesEquipements(
            "Bastien.Cazaux@polytech-lille.fr"
        );
        if (empruntsEquipsBastien.isEmpty()) {
            System.out.println("  (aucun emprunt)");
        }
        for (Emprunt e : empruntsEquipsBastien) {
            System.out.println("  [" + e.getId() + "] "
                + e.getDate()
                + " - " + e.getEquipement().getNom()
                + " - état : " + e.getEtat());
        }


        // Bastien ne propose pas de produit, on vérifie sur Olivier

		// System.out.println("\n--- Emprunts des équipements de Olivier (en tant que loueur) ---");
        // List<Emprunt> empruntsEquipsOlivier = service.listerEmpruntsDesMesEquipements(
        //     "Olivier.Caron@univ-lille.fr"
        // );
        // if (empruntsEquipsOlivier.isEmpty()) {
        //     System.out.println("  (aucun emprunt)");
        // }
        // for (Emprunt e : empruntsEquipsOlivier) {
        //     System.out.println("  [" + e.getId() + "] "
        //         + e.getDate()
        //         + " - " + e.getEquipement().getNom()
        //         + " - état : " + e.getEtat());
        // }
    }
}
