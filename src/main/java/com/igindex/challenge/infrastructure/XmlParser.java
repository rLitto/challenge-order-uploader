package com.igindex.challenge.infrastructure;

import com.igindex.challenge.domain.order.Order;

import java.util.List;

@FunctionalInterface
public interface XmlParser {

    List<Order> parseString(String input);
}
