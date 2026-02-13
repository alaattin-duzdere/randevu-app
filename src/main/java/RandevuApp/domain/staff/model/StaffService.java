package RandevuApp.domain.staff.model;

import RandevuApp.domain.service_offering.model.ServiceOffering;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staff_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering serviceOffering;
}
