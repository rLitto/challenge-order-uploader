package com.igindex.challenge.infrastructure;

import com.igindex.challenge.application.DestinationProperties;
import io.vavr.control.Try;

import java.io.Serializable;

public interface MessagingService {

    Try<Void> sendMessage(DestinationProperties configuration, Serializable payload);
}
