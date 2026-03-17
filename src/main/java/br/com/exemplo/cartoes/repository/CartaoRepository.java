package br.com.exemplo.cartoes.repository;

import br.com.exemplo.cartoes.domain.entity.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}