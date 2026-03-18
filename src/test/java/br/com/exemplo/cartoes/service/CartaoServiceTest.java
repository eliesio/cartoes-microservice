package br.com.exemplo.cartoes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.exemplo.cartoes.domain.entity.Cartao;
import br.com.exemplo.cartoes.domain.enums.SituacaoCartao;
import br.com.exemplo.cartoes.domain.enums.TipoCartao;
import br.com.exemplo.cartoes.exception.RegraNegocioException;
import br.com.exemplo.cartoes.repository.CartaoRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private CartaoEventProducer cartaoEventProducer;

    @Mock
    private TransactionDateTimeProvider transactionDateTimeProvider;

    @InjectMocks
    private CartaoService cartaoService;

    @Test
    void deveAtivarCartaoFisicoPendente() {
        Cartao cartao = Cartao.builder()
                .id(1L)
                .cpf("12345678901")
                .tipoCartao(TipoCartao.FISICO)
                .situacao(SituacaoCartao.PENDENTE_ATIVACAO)
                .build();
            LocalDateTime dataAtivacao = LocalDateTime.of(2026, 3, 18, 10, 27, 0);

        when(cartaoRepository.findById(1L)).thenReturn(Optional.of(cartao));
            when(transactionDateTimeProvider.now()).thenReturn(dataAtivacao);
        when(cartaoRepository.save(any(Cartao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cartao cartaoAtivado = cartaoService.ativarCartao(1L);

        assertEquals(SituacaoCartao.ATIVO, cartaoAtivado.getSituacao());
            assertEquals(dataAtivacao, cartaoAtivado.getDataAtivacao());
        verify(cartaoRepository).save(cartao);
        verify(cartaoEventProducer).publicarCartaoAtivado(any());
    }

    @Test
    void naoDeveAtivarCartaoOnline() {
        Cartao cartao = Cartao.builder()
                .id(2L)
                .cpf("12345678901")
                .tipoCartao(TipoCartao.ONLINE)
                .situacao(SituacaoCartao.ATIVO)
                .build();

        when(cartaoRepository.findById(2L)).thenReturn(Optional.of(cartao));

        RegraNegocioException exception = assertThrows(RegraNegocioException.class,
                () -> cartaoService.ativarCartao(2L));

        assertEquals("Apenas cartoes fisicos podem ser ativados.", exception.getMessage());
        verify(cartaoRepository, never()).save(any());
        verify(cartaoEventProducer, never()).publicarCartaoAtivado(any());
    }
}