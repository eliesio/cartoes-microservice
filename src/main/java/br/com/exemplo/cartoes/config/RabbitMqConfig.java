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
    public Queue cartaoQueue(RabbitMqProperties properties) {
        return new Queue(properties.queue(), true);
    }

    @Bean
    public Binding cartaoBinding(Queue cartaoQueue, DirectExchange cartaoExchange, RabbitMqProperties properties) {
        return BindingBuilder.bind(cartaoQueue)
                .to(cartaoExchange)
                .with(properties.routingKey());
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}