package br.com.exemplo.cartoes.controller;

import br.com.exemplo.cartoes.controller.dto.CartaoCreateRequest;
import br.com.exemplo.cartoes.controller.dto.CartaoResponse;
import br.com.exemplo.cartoes.service.CartaoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<List<CartaoResponse>> criar(@Valid @RequestBody CartaoCreateRequest request) {
        List<CartaoResponse> response = cartaoService.criarCartoes(request)
                .stream()
                .map(CartaoResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}