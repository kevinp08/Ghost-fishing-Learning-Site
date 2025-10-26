package org.example.ghostnet.web;

import org.example.ghostnet.model.GhostNet;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * JSF-Controller für das Melden von Geisternetzen.
 */
@Named
@SessionScoped
public class ReportNetController implements Serializable {

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal approxSize;
    private List<GhostNet> nets = new ArrayList<>();

    public String submit() {
        GhostNet net = new GhostNet();
        net.setLatitude(latitude);
        net.setLongitude(longitude);
        net.setApproxSize(approxSize);
        net.setStatus("Gemeldet");
        nets.add(net);
        return "listOpenNets?faces-redirect=true";
    }

    public List<GhostNet> getNets() { return nets; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getApproxSize() { return approxSize; }
    public void setApproxSize(BigDecimal approxSize) { this.approxSize = approxSize; }
}
