package com.ar.tdp2fiuba.hoycomo.model;

public class OrderState {
    private OrderStatus state;
    private String timestamp;

    public OrderState() {
        this.state = OrderStatus.TAKEN;
    }

    public OrderState(OrderStatus state, String timestamp) {
        this.state = state;
        this.timestamp = timestamp;
    }

    public OrderStatus getState() {
        return state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "OrderState{" +
                "state=" + state +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
