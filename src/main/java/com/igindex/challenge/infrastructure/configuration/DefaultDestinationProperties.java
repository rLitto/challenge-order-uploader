package com.igindex.challenge.infrastructure.configuration;

import com.igindex.challenge.application.DestinationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "messaging.default.destination")
public class DefaultDestinationProperties extends DestinationProperties {

}
