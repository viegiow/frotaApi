package com.example.frota.percurso;

import java.time.LocalDateTime;

import com.example.frota.caminhao.Caminhao;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "percursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Percurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origem;
    private String destino;
    private Double kmSaida;
    private Double kmChegada;
    private LocalDateTime dataSaida;
    private LocalDateTime dataChegada;
    @Enumerated(EnumType.STRING)
    private StatusPercurso status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminhao_id")
    private Caminhao caminhao;
    
}
