package RandevuApp.domain.business.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "business_schedule_overrides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessScheduleOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Column(nullable = false)
    private boolean isClosed;

    private LocalTime openTime;
    private LocalTime closeTime;

    private String reason;
}
