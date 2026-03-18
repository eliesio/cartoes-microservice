package br.com.exemplo.cartoes.exception;

public class CartaoNaoEncontradoException extends RuntimeException {

    public CartaoNaoEncontradoException(Long id) {
        super("Cartao com id " + id + " nao encontrado.");
    }
}