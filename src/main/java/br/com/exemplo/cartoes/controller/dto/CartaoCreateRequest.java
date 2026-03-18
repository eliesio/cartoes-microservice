package br.com.exemplo.cartoes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criacao dos cartoes fisico e online")
public record CartaoCreateRequest(
        @Schema(description = "CPF do portador", example = "12345678901")
        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "cpf deve conter 11 digitos numericos")
        String cpf,

        @Schema(description = "Nome impresso no cartao", example = "JOAO DA SILVA")
        @NotBlank
        @Size(max = 100)
        String nomeImpresso,

        @Schema(description = "Produto do cartao", example = "CREDITO")
        @NotBlank
        @Size(max = 50)
        String produto,

        @Schema(description = "Subproduto do cartao", example = "PLATINUM")
        @NotBlank
        @Size(max = 50)
        String subproduto
) {
}