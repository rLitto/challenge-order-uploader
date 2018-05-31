package com.igindex.challenge.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igindex.challenge.domain.order.Order;
import com.igindex.challenge.error.OrderSubmitException;
import com.igindex.challenge.infrastructure.MessagingService;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class OrderSubmitApplicationService implements OrderSubmitApplication {
    private static final Logger log = LoggerFactory.getLogger(OrderSubmitApplicationService.class);
    private final MessagingService messagingService;
    private final ObjectMapper objectMapper;

    public OrderSubmitApplicationService(MessagingService messagingService, ObjectMapper objectMapper) {
        this.messagingService = messagingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void submit(DestinationProperties destination, Order... orders) {
        final String errors = executeAndCollectErrors(order -> toJsonAndThenSend(destination, order), orders);
        if (!errors.isEmpty()) {
            throw new OrderSubmitException(errors);
        }
    }

    @SafeVarargs
    private final <T> String executeAndCollectErrors(Function<T, Try<?>> function, T... args) {
        return Arrays.stream(args)
                .map(function)
                .filter(Try::isFailure)
                .map(Try::getCause)
                .peek(e -> log.error("Unable to send message", e))
                .map(Throwable::getLocalizedMessage)
                .collect(Collectors.joining("\n"));
    }

    private Try<Void> toJsonAndThenSend(DestinationProperties destination, Order order) {
        return Try.of(() -> objectMapper.writeValueAsString(order))
                .flatMapTry(json -> messagingService.sendMessage(destination, json));
    }

}
