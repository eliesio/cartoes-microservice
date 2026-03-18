package br.com.exemplo.cartoes.controller.dto;

import br.com.exemplo.cartoes.domain.entity.Cartao;
import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representacao de um cartao")
public record CartaoResponse(
    @Schema(description = "Identificador do cartao", example = "1")
        Long id,
    @Schema(description = "CPF do portador", example = "12345678901")
        String cpf,
    @Schema(description = "Nome impresso no cartao", example = "JOAO DA SILVA")
        String nomeImpresso,
    @Schema(description = "Produto do cartao", example = "CREDITO")
        String produto,
    @Schema(description = "Subproduto do cartao", example = "PLATINUM")
        String subproduto,
    @Schema(description = "Tipo do cartao", example = "FISICO")
        TipoCartao tipoCartao,
    @Schema(description = "Situacao atual do cartao", example = "ATIVO")
        SituacaoCartao situacao,
    @Schema(description = "Data de criacao do cartao", example = "2026-03-17T10:14:03.059219")
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