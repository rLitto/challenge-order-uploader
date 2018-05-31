package com.igindex.challenge.infrastructure.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.igindex.challenge.infrastructure.JacksonXmlParser;
import com.igindex.challenge.infrastructure.XmlParser;
import com.igindex.challenge.io.XmlSanitizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfiguration {


    @Bean
    public XmlMapper xmlMapper() {
        final XmlMapper mapper = new XmlMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return mapper;
    }

    @Bean
    public XmlParser jacksonXmlParser(XmlMapper mapper) {
        return new JacksonXmlParser(mapper);
    }

    @Bean
    public XmlSanitizer sanitizer() {
        return new XmlSanitizer();
    }

}
