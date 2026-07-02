package RandevuApp.domain.service_offering.model;

import RandevuApp.commons.model.BaseEntity;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.staff.model.StaffService;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "service_offering")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceOffering extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "is_active")
    private boolean active = true;

    // Bu hizmet hangi işletmeye ait?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // Bu hizmeti veren personeller (Ara tablo ilişkisi)
    @OneToMany(mappedBy = "serviceOffering", cascade = CascadeType.ALL)
    private List<StaffService> staffServices;
}