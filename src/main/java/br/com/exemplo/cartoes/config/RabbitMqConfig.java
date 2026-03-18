package br.com.exemplo.cartoes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange cartaoExchange(RabbitMqProperties properties) {
        return new DirectExchange(properties.exchange());
    }

    @Bean
    public Queue cartaoCriadoQueue(RabbitMqProperties properties) {
        return new Queue(properties.criadoQueue(), true);
    }

    @Bean
    public Queue cartaoAtivadoQueue(RabbitMqProperties properties) {
        return new Queue(properties.ativadoQueue(), true);
    }

    @Bean
    public Queue cartaoCanceladoQueue(RabbitMqProperties properties) {
        return new Queue(properties.canceladoQueue(), true);
    }

    @Bean
    public Binding cartaoCriadoBinding(Queue cartaoCriadoQueue, DirectExchange cartaoExchange, RabbitMqProperties properties) {
        return BindingBuilder.bind(cartaoCriadoQueue)
                .to(cartaoExchange)
                .with(properties.criadoRoutingKey());
    }

    @Bean
    public Binding cartaoAtivadoBinding(Queue cartaoAtivadoQueue, DirectExchange cartaoExchange, RabbitMqProperties properties) {
        return BindingBuilder.bind(cartaoAtivadoQueue)
                .to(cartaoExchange)
                .with(properties.ativadoRoutingKey());
    }

    @Bean
    public Binding cartaoCanceladoBinding(Queue cartaoCanceladoQueue, DirectExchange cartaoExchange, RabbitMqProperties properties) {
        return BindingBuilder.bind(cartaoCanceladoQueue)
                .to(cartaoExchange)
                .with(properties.canceladoRoutingKey());
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}