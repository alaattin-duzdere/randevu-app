package RandevuApp.domain.business.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "business_settings")
@Getter
@Setter
public class BusinessSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer slotDurationTime;

    private String openingTime;

    private String closingTime;

    // Business ile geri ilişki (Opsiyonel ama önerilir)
    @OneToOne(mappedBy = "businessSettings")
    private Business business;
}
