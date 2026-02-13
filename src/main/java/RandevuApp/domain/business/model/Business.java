package RandevuApp.domain.business.model;

import RandevuApp.commons.model.BaseEntity;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "business")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "time_zone", nullable = false)
    private String timeZone;

    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_settings_id", referencedColumnName = "id")
    private BusinessSettings businessSettings;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private List<Staff> staffList;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private List<ServiceOffering> serviceList;
}
