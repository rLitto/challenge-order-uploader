package com.igindex.challenge.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.igindex.challenge.domain.order.Order;
import com.igindex.challenge.error.ParserException;

import java.util.List;

public class JacksonXmlParser implements XmlParser {
    private static final TypeReference<List<Order>> TYPE_REFERENCE = new TypeReference<List<Order>>() {
    };

    private final XmlMapper mapper;

    public JacksonXmlParser(XmlMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Order> parseString(String input) {
        try {
            return mapper.readValue(input, TYPE_REFERENCE);
        } catch (Exception e) {
            throw new ParserException("Unable to parse string", e);
        }
    }
}
