package org.example.ghostnet.web;

import org.example.ghostnet.model.GhostNet;
import org.example.ghostnet.model.Status;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ListController implements Serializable {

    @Inject
    private EntityManager em;

    private List<GhostNet> openNets;

    // Felder für den "Bergung übernehmen"-Dialog
    private Long claimNetId;
    private String claimName;
    private String claimPhone;

    @PostConstruct
    public void init() {
        reload();
    }

    private void reload() {
        openNets = em.createQuery(
                        "SELECT g FROM GhostNet g " +
                        "WHERE g.status NOT IN (:done, :lost) " +
                        "ORDER BY g.createdAt DESC",
                        GhostNet.class)
                .setParameter("done", Status.GEBORGEN)
                .setParameter("lost", Status.VERSCHOLLEN)
                .getResultList();
    }

    // Getter für Tabelle
    public List<GhostNet> getOpenNets() {
        return openNets;
    }

    /* =======================
       Claim-Dialog-Logik
       ======================= */

    // Wird von "Bergung übernehmen" aufgerufen
    public void openClaimDialog(Long netId) {
        this.claimNetId = netId;
        this.claimName = null;
        this.claimPhone = null;
    }

    // Speichern aus dem Dialog
    public void saveClaim() {
        if (claimNetId == null) {
            return;
        }

        GhostNet net = em.find(GhostNet.class, claimNetId);
        if (net == null) {
            resetClaim();
            return;
        }

        em.getTransaction().begin();
        try {
            net.setRetrieverName(claimName);
            net.setRetrieverPhone(claimPhone);
            // GEÄNDERT: Status beim Übernehmen der Bergung
            // HIER muss ein Wert aus dem Status-Enum stehen.
            // Erwartet: BERGUNG_BEVORSTEHEND existiert im Enum Status.
            net.setStatus(Status.BERGUNG_BEVORSTEHEND);
            em.merge(net);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        }

        resetClaim();
        reload();
    }

    public void cancelClaim() {
        resetClaim();
    }

    private void resetClaim() {
        this.claimNetId = null;
        this.claimName = null;
        this.claimPhone = null;
    }

    /* =======================
       Bestehende Status-Methoden
       ======================= */

    public void markRecovered(Long id) {
        GhostNet net = em.find(GhostNet.class, id);
        if (net == null) return;

        em.getTransaction().begin();
        try {
            net.setStatus(Status.GEBORGEN);
            em.merge(net);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        }
        reload();
    }

    public void markLost(Long id) {
        GhostNet net = em.find(GhostNet.class, id);
        if (net == null) return;

        em.getTransaction().begin();
        try {
            net.setStatus(Status.VERSCHOLLEN);
            em.merge(net);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        }
        reload();
    }

    /* =======================
       Getter/Setter für Dialog-Felder
       ======================= */

    public Long getClaimNetId() {
        return claimNetId;
    }

    public String getClaimName() {
        return claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    public String getClaimPhone() {
        return claimPhone;
    }

    public void setClaimPhone(String claimPhone) {
        this.claimPhone = claimPhone;
    }
}
