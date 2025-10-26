package org.example.ghostnet.web;

import org.example.ghostnet.model.GhostNet;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * JSF-Controller für die Anzeige und Verwaltung offener Geisternetze.
 */
@Named
@SessionScoped
public class ListController implements Serializable {

    private final ReportNetController reportNetController = new ReportNetController();

    public List<GhostNet> getOpenNets() {
        return reportNetController.getNets();
    }

    public String claim(Long id) {
        for (GhostNet net : reportNetController.getNets()) {
            if (net.getId() != null && net.getId().equals(id)) {
                net.setStatus("Bergung bevorstehend");
            }
        }
        return null;
    }

    public String recover(Long id) {
        for (GhostNet net : reportNetController.getNets()) {
            if (net.getId() != null && net.getId().equals(id)) {
                net.setStatus("Geborgen");
            }
        }
        return null;
    }
}
