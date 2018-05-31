package com.igindex.challenge.infrastructure;

import com.igindex.challenge.application.ConnectionProperties;
import com.igindex.challenge.application.DestinationProperties;
import io.vavr.control.Try;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
class ConnectionCachingMessagingService implements MessagingService {
    private static final Logger log = LoggerFactory.getLogger(ConnectionCachingMessagingService.class);
    private final ApplicationContext context;
    private final ConcurrentHashMap<ConnectionProperties, ConnectionFactory> connectionsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ConnectionProperties, JmsTemplate> jmsTemplateMap = new ConcurrentHashMap<>();

    public ConnectionCachingMessagingService(ApplicationContext context) {
        this.context = context;
        final Map<String, DestinationProperties> defaultDestinationProperties = context.getBeansOfType(DestinationProperties.class);
        defaultDestinationProperties.forEach((k, v) -> getConnectionFactory(v));
    }

    @Override
    public Try<Void> sendMessage(DestinationProperties configuration, Serializable payload) {
        JmsTemplate template = getJmsTemplate(configuration);
        final Destination destination = makeDestination(configuration);
        return Try.run(() -> template.convertAndSend(destination, payload));
    }

    private Destination makeDestination(DestinationProperties configuration) {
        switch (configuration.getType()) {
            case Topic:
                return new ActiveMQTopic(configuration.getName());
            default:
                return new ActiveMQQueue(configuration.getName());
        }
    }

    private ConnectionFactory getConnectionFactory(DestinationProperties configuration) {
        return connectionsMap
                .computeIfAbsent(configuration.getConnection(),
                        properties -> context.getBean(ConnectionFactory.class, configuration));
    }

    private JmsTemplate getJmsTemplate(DestinationProperties configuration) {
        return jmsTemplateMap
                .computeIfAbsent(configuration.getConnection(),
                        properties -> context.getBean(JmsTemplate.class, getConnectionFactory(configuration)));
    }
}
