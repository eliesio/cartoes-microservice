package br.com.exemplo.cartoes.domain.event;

import br.com.exemplo.cartoes.domain.entity.Cartao;
import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoAtivadoEvent {

    private Long id;
    private String cpf;
    private TipoCartao tipoCartao;
    private SituacaoCartao situacao;
    private LocalDateTime dataAtivacao;

    public static CartaoAtivadoEvent from(Cartao cartao) {
        return CartaoAtivadoEvent.builder()
                .id(cartao.getId())
                .cpf(cartao.getCpf())
                .tipoCartao(cartao.getTipoCartao())
                .situacao(cartao.getSituacao())
                .dataAtivacao(cartao.getDataAtivacao())
                .build();
    }
}