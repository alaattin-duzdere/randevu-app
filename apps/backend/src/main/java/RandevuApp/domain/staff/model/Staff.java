package RandevuApp.domain.staff.model;

import RandevuApp.commons.model.BaseEntity;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.time_off.model.TimeOff;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "staffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff extends BaseEntity {

    @Column(name = "staff_name", nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "color_code", length = 7)
    private String colorCode;

    private String photo;

    @Column(nullable = false)
    private boolean active = true;

    // --- RELATIONS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeOff> timeOffs;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<StaffService> staffServices;
}