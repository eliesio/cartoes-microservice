package br.com.exemplo.cartoes.controller.dto;

import br.com.exemplo.cartoes.domain.entity.Cartao;
import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import java.time.LocalDateTime;

public record CartaoResponse(
        Long id,
        String cpf,
        String nomeImpresso,
        String produto,
        String subproduto,
        TipoCartao tipoCartao,
        SituacaoCartao situacao,
        LocalDateTime dataCriacao
) {

    public static CartaoResponse from(Cartao cartao) {
        return new CartaoResponse(
                cartao.getId(),
                cartao.getCpf(),
                cartao.getNomeImpresso(),
                cartao.getProduto(),
                cartao.getSubproduto(),
                cartao.getTipoCartao(),
                cartao.getSituacao(),
                cartao.getDataCriacao()
        );
    }
}