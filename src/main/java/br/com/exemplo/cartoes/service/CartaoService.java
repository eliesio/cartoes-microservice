package br.com.exemplo.cartoes.service;

import br.com.exemplo.cartoes.controller.dto.CartaoCreateRequest;
import br.com.exemplo.cartoes.domain.entity.Cartao;
import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import br.com.exemplo.cartoes.domain.event.CartaoCriadoEvent;
import br.com.exemplo.cartoes.repository.CartaoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final CartaoEventProducer cartaoEventProducer;

    @Transactional
    public List<Cartao> criarCartoes(CartaoCreateRequest request) {
        List<Cartao> cartoes = List.of(
                criarCartao(request, TipoCartao.FISICO, SituacaoCartao.PENDENTE_ATIVACAO),
                criarCartao(request, TipoCartao.ONLINE, SituacaoCartao.ATIVO)
        );

        List<Cartao> cartoesSalvos = cartaoRepository.saveAll(cartoes);
        cartoesSalvos.forEach(cartao -> cartaoEventProducer.publicarCartaoCriado(CartaoCriadoEvent.from(cartao)));
        return cartoesSalvos;
    }

    private Cartao criarCartao(CartaoCreateRequest request, TipoCartao tipoCartao, SituacaoCartao situacao) {
        return Cartao.builder()
                .cpf(request.cpf())
                .nomeImpresso(request.nomeImpresso())
                .produto(request.produto())
                .subproduto(request.subproduto())
                .tipoCartao(tipoCartao)
                .situacao(situacao)
                .build();
    }
}