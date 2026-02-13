package RandevuApp.domain.staff.model;

import RandevuApp.commons.model.BaseEntity;
import RandevuApp.domain.business.model.Business;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff extends BaseEntity {

    @Column(name = "staff_name", nullable = false)
    private String name;

    private String photo; // Fotoğraf URL'i veya dosya yolu

    @Column(nullable = false)
    private boolean active = true;

    // --- İLİŞKİLER ---

    // 1. Hangi işletmeye ait?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // 2. Personelin İzinleri (TimeOff)
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeOff> timeOffs;

    // 3. Verdiği Hizmetler (StaffService tablosu üzerinden)
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<StaffService> staffServices;

    // (İleride Appointment eklendiğinde buraya @OneToMany eklenecek)
}
