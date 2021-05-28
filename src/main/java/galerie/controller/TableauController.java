package galerie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import galerie.dao.TableauRepository;
import galerie.entity.Tableau;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Edition des catégories, sans gestion des erreurs
 */
@Controller
@RequestMapping(path = "/tableau")
public class TableauController {

    @Autowired
    private TableauRepository dao;

    @GetMapping(path = "show")
    public String afficheTousLesTableaux(Model model) {
        model.addAttribute("tableaux", dao.findAll());
        return "afficheTableaux";
    }

    @GetMapping(path = "add")
    public String montreLeFormulairePourAjout(@ModelAttribute("tableau") Tableau Tableau) {
        return "formulaireTableau";
    }

    @PostMapping(path = "save")
    public String ajouteLeTableauPuisMontreLaListe(Tableau Tableau, RedirectAttributes redirectInfo) {
        String message;
        try {

            dao.save(Tableau);
            message = "Le Tableau '" + Tableau.getTitre() + "' a été  enregistrée avec la clé: " + Tableau.getId();
        } catch (DataIntegrityViolationException e) {
            message = "Erreur : Le Tableau '" + Tableau.getTitre() + "' existe déjà";
        }
        redirectInfo.addFlashAttribute("message", message);
        return "redirect:show"; 	
    }

    @GetMapping(path = "delete")
    public String supprimeUnTableauPuisMontreLaListe(@RequestParam("id") Tableau Tableau, RedirectAttributes redirectInfo) {
        String message;
        try {
            dao.delete(Tableau); 
            message = "Le Tableau '" + Tableau.getTitre() + "' a bien été supprimée";
        } catch (DataIntegrityViolationException e) {
            message = "Erreur : Impossible de supprimer le Tableau '" + Tableau.getTitre() + "', il faut d'abord supprimer ses expositions";
        }

        redirectInfo.addFlashAttribute("message", message);
        return "redirect:show"; 
    }
}