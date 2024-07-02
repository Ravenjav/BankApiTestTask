package by.vodorod.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "Currency")
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Setter
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bank_currency_id")
    private Long bankCurrencyId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "scale")
    private Long scale;

    @Column(name = "cur_name")
    private String name;

    @Column(name = "rate")
    private Double rate;
}
