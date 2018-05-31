package com.igindex.challenge.application;

import com.igindex.challenge.domain.order.Order;

public interface OrderSubmitApplication {
    void submit(DestinationProperties destination, Order... orders);
}
