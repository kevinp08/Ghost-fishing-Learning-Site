package org.example.ghostnet.web;

import org.example.ghostnet.model.GhostNet;
import org.example.ghostnet.model.Status;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@Named("reportNetController")
@RequestScoped
public class ReportNetController {

    @Inject
    private EntityManager em;

    /** Das Formular bindet direkt auf dieses Objekt. */
    private GhostNet net = new GhostNet();

    public String save() {
        em.getTransaction().begin();
        try {

            if (net.getStatus() == null) {
                net.setStatus(Status.GEMELDET);
            }

            em.persist(net);
            em.getTransaction().commit();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Geisternetz gespeichert.", null));

            return "listOpenNets?faces-redirect=true";
        } catch (ConstraintViolationException ve) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            for (ConstraintViolation<?> v : ve.getConstraintViolations()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        v.getPropertyPath() + ": " + v.getMessage(), null));
            }
            return null;
        } catch (ValidationException ve) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validierung fehlgeschlagen: " + ve.getMessage(), null));
            return null;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fehler beim Speichern: " + e.getMessage(), null));
            return null;
        }
    }

    /* --- Getter/Setter --- */
    public GhostNet getNet() { return net; }
    public void setNet(GhostNet net) { this.net = net; }

    // Kompatibilitäts-Getter/Setter
    public Double getLatitude() { return net.getLatitude(); }
    public void setLatitude(Double latitude) { net.setLatitude(latitude); }
    public Double getLongitude() { return net.getLongitude(); }
    public void setLongitude(Double longitude) { net.setLongitude(longitude); }
    public Long getSize() { return net.getSize(); }           // <-- Long
    public void setSize(Long size) { net.setSize(size); }     // <-- Long
}
