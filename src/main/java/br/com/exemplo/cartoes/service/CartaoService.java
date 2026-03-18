package br.com.exemplo.cartoes.service;

import br.com.exemplo.cartoes.controller.dto.CartaoCreateRequest;
import br.com.exemplo.cartoes.domain.entity.Cartao;
import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import br.com.exemplo.cartoes.domain.event.CartaoAtivadoEvent;
import br.com.exemplo.cartoes.domain.event.CartaoCanceladoEvent;
import br.com.exemplo.cartoes.domain.event.CartaoCriadoEvent;
import br.com.exemplo.cartoes.exception.CartaoNaoEncontradoException;
import br.com.exemplo.cartoes.exception.RegraNegocioException;
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
    private final TransactionDateTimeProvider transactionDateTimeProvider;

    @Transactional
    public List<Cartao> criarCartoes(CartaoCreateRequest request) {
        var dataCriacao = transactionDateTimeProvider.now();

        List<Cartao> cartoes = List.of(
                criarCartao(request, TipoCartao.FISICO, SituacaoCartao.PENDENTE_ATIVACAO, dataCriacao),
                criarCartao(request, TipoCartao.ONLINE, SituacaoCartao.ATIVO, dataCriacao)
        );

        List<Cartao> cartoesSalvos = cartaoRepository.saveAll(cartoes);
        cartoesSalvos.forEach(cartao -> cartaoEventProducer.publicarCartaoCriado(CartaoCriadoEvent.from(cartao)));
        return cartoesSalvos;
    }

    @Transactional
    public Cartao ativarCartao(Long id) {
        Cartao cartao = buscarPorId(id);

        if (cartao.getTipoCartao() != TipoCartao.FISICO) {
            throw new RegraNegocioException("Apenas cartoes fisicos podem ser ativados.");
        }

        if (cartao.getSituacao() != SituacaoCartao.PENDENTE_ATIVACAO) {
            throw new RegraNegocioException("O cartao precisa estar em PENDENTE_ATIVACAO para ser ativado.");
        }

        cartao.setSituacao(SituacaoCartao.ATIVO);
        cartao.setDataAtivacao(transactionDateTimeProvider.now());
        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        cartaoEventProducer.publicarCartaoAtivado(CartaoAtivadoEvent.from(cartaoAtualizado));
        return cartaoAtualizado;
    }

    @Transactional
    public Cartao cancelarCartao(Long id) {
        Cartao cartao = buscarPorId(id);

        if (cartao.getSituacao() != SituacaoCartao.ATIVO) {
            throw new RegraNegocioException("Apenas cartoes ativos podem ser cancelados.");
        }

        cartao.setSituacao(SituacaoCartao.CANCELADO);
        cartao.setDataCancelamento(transactionDateTimeProvider.now());
        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        cartaoEventProducer.publicarCartaoCancelado(CartaoCanceladoEvent.from(cartaoAtualizado));
        return cartaoAtualizado;
    }

    private Cartao criarCartao(CartaoCreateRequest request, TipoCartao tipoCartao, SituacaoCartao situacao,
                               java.time.LocalDateTime dataCriacao) {
        return Cartao.builder()
                .cpf(request.cpf())
                .nomeImpresso(request.nomeImpresso())
                .produto(request.produto())
                .subproduto(request.subproduto())
                .tipoCartao(tipoCartao)
                .situacao(situacao)
                .dataCriacao(dataCriacao)
                .build();
    }

    private Cartao buscarPorId(Long id) {
        return cartaoRepository.findById(id)
                .orElseThrow(() -> new CartaoNaoEncontradoException(id));
    }
}