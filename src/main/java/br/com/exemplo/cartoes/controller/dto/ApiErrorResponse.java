package br.com.exemplo.cartoes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resposta padrao para erros da API")
public record ApiErrorResponse(
        @Schema(description = "Codigo HTTP", example = "400")
        int status,
        @Schema(description = "Descricao resumida do erro", example = "Bad Request")
        String error,
        @Schema(description = "Mensagem detalhada", example = "Apenas cartoes ativos podem ser cancelados.")
        String message,
        @Schema(description = "Caminho da requisicao", example = "/cartoes/1/cancelar")
        String path,
        @Schema(description = "Data e hora do erro", example = "2026-03-17T11:00:00")
        LocalDateTime timestamp
) {
}