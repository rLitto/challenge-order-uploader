package com.igindex.challenge.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igindex.challenge.application.ConnectionProperties;
import com.igindex.challenge.application.DestinationProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
@EnableConfigurationProperties(DefaultDestinationProperties.class)
public class MessagingConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ConnectionFactory connectionFactory(DestinationProperties properties) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        final ConnectionProperties connection = properties.getConnection();
        connectionFactory.setBrokerURL(connection.getBrokerUrl());
        connectionFactory.setUserName(connection.getUsername());
        connectionFactory.setPassword(connection.getPassword());
        return connectionFactory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
