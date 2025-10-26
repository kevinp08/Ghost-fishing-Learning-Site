package org.example.ghostnet.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Repräsentiert ein gemeldetes Geisternetz.
 */
@Entity
public class GhostNet {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal latitude;   // Breitengrad
    private BigDecimal longitude;  // Längengrad
    private BigDecimal approxSize; // geschätzte Größe in m²
    private String status;         // Gemeldet, Geborgen etc.

    public GhostNet() {}

    public Long getId() { return id; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public BigDecimal getApproxSize() { return approxSize; }
    public void setApproxSize(BigDecimal approxSize) { this.approxSize = approxSize; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
