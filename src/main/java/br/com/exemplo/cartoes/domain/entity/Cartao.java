package br.com.exemplo.cartoes.domain.entity;

import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 100)
    private String nomeImpresso;

    @Column(nullable = false, length = 50)
    private String produto;

    @Column(nullable = false, length = 50)
    private String subproduto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCartao tipoCartao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SituacaoCartao situacao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column
    private LocalDateTime dataAtivacao;

    @Column
    private LocalDateTime dataCancelamento;
}