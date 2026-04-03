package web.controleurs;

import ejb.entites.EtatEmprunt;
import ejb.sessions.GestionEquipementLocal;
import ejb.sessions.ChangementEtatNonAutoriseException;
import ejb.sessions.EmpruntIntrouvableException;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("Controleur")
public class Controleur extends HttpServlet {

    @EJB
    private GestionEquipementLocal gestionBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("listeEtats", EtatEmprunt.values());
        req.getRequestDispatcher("/vueForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email      = req.getParameter("email");
        String idStr      = req.getParameter("codeEmprunt");
        String nouvelEtat = req.getParameter("newEtat");

        if (email == null || email.isBlank()
                || idStr == null || idStr.isBlank()
                || nouvelEtat == null || nouvelEtat.isBlank()) {
            req.setAttribute("listeEtats", EtatEmprunt.values());
            req.setAttribute("erreur", "Tous les champs sont obligatoires.");
            req.getRequestDispatcher("/vueForm.jsp").forward(req, resp);
            return;
        }

        int idEmprunt;
        try {
            idEmprunt = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            req.setAttribute("listeEtats", EtatEmprunt.values());
            req.setAttribute("erreur", "Le code emprunt doit être un entier.");
            req.getRequestDispatcher("/vueForm.jsp").forward(req, resp);
            return;
        }

        String messageResultat;
        try {
            gestionBean.modifierEtatEmprunt(email.trim(), idEmprunt, nouvelEtat.trim());
            messageResultat = "l'emprunt a été modifié";
        } catch (EmpruntIntrouvableException e) {
            messageResultat = "erreur changement état : " + e.getMessage();
        } catch (ChangementEtatNonAutoriseException e) {
            messageResultat = "erreur changement état : " + e.getMessage();
        }

        req.setAttribute("emailUtilisateur", email.trim());
        req.setAttribute("numEmprunt",       idEmprunt);
        req.setAttribute("etatVise",         nouvelEtat.trim());
        req.setAttribute("messageResultat",  messageResultat);

        req.getRequestDispatcher("/resultatForm.jsp").forward(req, resp);
    }
}

// tester le web : 
// Louis.Roussel@univ-lille.fr, emprunt 2, demandeAcceptee : validé
// Louis.Roussel@univ-lille.fr, emprunt 3, demandeAcceptee : Emprunt introuvable