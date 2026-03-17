package br.com.exemplo.cartoes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CartaoCreateRequest(
        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "cpf deve conter 11 digitos numericos")
        String cpf,

        @NotBlank
        @Size(max = 100)
        String nomeImpresso,

        @NotBlank
        @Size(max = 50)
        String produto,

        @NotBlank
        @Size(max = 50)
        String subproduto
) {
}