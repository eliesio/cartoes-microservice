package br.com.exemplo.cartoes.controller;

import br.com.exemplo.cartoes.controller.dto.CartaoCreateRequest;
import br.com.exemplo.cartoes.controller.dto.CartaoResponse;
import br.com.exemplo.cartoes.service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@Tag(name = "Cartoes", description = "Operacoes de criacao, ativacao e cancelamento de cartoes")
public class CartaoController {

    private final CartaoService cartaoService;

    @PostMapping
    @Operation(
        summary = "Criar cartoes",
        description = "Cria automaticamente dois cartoes para o cliente: um FISICO em PENDENTE_ATIVACAO e um ONLINE em ATIVO."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cartoes criados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CartaoResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = br.com.exemplo.cartoes.controller.dto.ApiErrorResponse.class)))
    })
    public ResponseEntity<List<CartaoResponse>> criar(@Valid @RequestBody CartaoCreateRequest request) {
        List<CartaoResponse> response = cartaoService.criarCartoes(request)
                .stream()
                .map(CartaoResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/ativar")
    @Operation(
        summary = "Ativar cartao fisico",
        description = "Ativa apenas cartoes do tipo FISICO que estejam em PENDENTE_ATIVACAO."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cartao ativado com sucesso", content = @Content(schema = @Schema(implementation = CartaoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cartao nao encontrado", content = @Content(schema = @Schema(implementation = br.com.exemplo.cartoes.controller.dto.ApiErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Regra de negocio violada", content = @Content(schema = @Schema(implementation = br.com.exemplo.cartoes.controller.dto.ApiErrorResponse.class)))
    })
    public ResponseEntity<CartaoResponse> ativar(@PathVariable Long id) {
    return ResponseEntity.ok(CartaoResponse.from(cartaoService.ativarCartao(id)));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(
        summary = "Cancelar cartao",
        description = "Cancela apenas cartoes que estejam na situacao ATIVO."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cartao cancelado com sucesso", content = @Content(schema = @Schema(implementation = CartaoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cartao nao encontrado", content = @Content(schema = @Schema(implementation = br.com.exemplo.cartoes.controller.dto.ApiErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Regra de negocio violada", content = @Content(schema = @Schema(implementation = br.com.exemplo.cartoes.controller.dto.ApiErrorResponse.class)))
    })
    public ResponseEntity<CartaoResponse> cancelar(@PathVariable Long id) {
    return ResponseEntity.ok(CartaoResponse.from(cartaoService.cancelarCartao(id)));
    }
}