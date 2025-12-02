package org.example.ghostnet.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "ghostnet",
    indexes = {
        @Index(name = "idx_ghostnet_status", columnList = "status"),
        @Index(name = "idx_ghostnet_createdAt", columnList = "createdAt")
    }
)
public class GhostNet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Breitengrad: -90 .. 90 */
    @NotNull
    @Column(nullable = false, precision = 9, scale = 6)
    private Double latitude;

    /** Längengrad: -180 .. 180 */
    @NotNull
    @Column(nullable = false, precision = 10, scale = 6)
    private Double longitude;

    /** Größe in Quadratmetern */
    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Long size; // <-- Long statt Integer

    /** Status als Enum, String in DB speichern */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Status status = Status.GEMELDET;

    /** Optional: Name und Telefon der bergenden Person */
    @Size(max = 255)
    private String retrieverName;

    @Size(max = 255)
    private String retrieverPhone;

    /** Erstellzeitpunkt */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /* --- Sicherheits-Validierungen für Double-Werte --- */
    @AssertTrue(message = "Latitude muss zwischen -90 und 90 liegen")
    public boolean isLatitudeValid() {
        if (latitude == null) return false;
        if (latitude.isNaN() || latitude.isInfinite()) return false;
        return latitude >= -90.0 && latitude <= 90.0;
    }

    @AssertTrue(message = "Longitude muss zwischen -180 und 180 liegen")
    public boolean isLongitudeValid() {
        if (longitude == null) return false;
        if (longitude.isNaN() || longitude.isInfinite()) return false;
        return longitude >= -180.0 && longitude <= 180.0;
    }

    /* --- Lifecycle Hooks --- */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.GEMELDET;
        }
    }

    /* --- Getter/Setter --- */
    public Long getId() { return id; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getRetrieverName() { return retrieverName; }
    public void setRetrieverName(String retrieverName) { this.retrieverName = retrieverName; }

    public String getRetrieverPhone() { return retrieverPhone; }
    public void setRetrieverPhone(String retrieverPhone) { this.retrieverPhone = retrieverPhone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
