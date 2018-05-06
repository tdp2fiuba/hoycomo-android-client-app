package com.ar.tdp2fiuba.hoycomo.service;

import com.ar.tdp2fiuba.hoycomo.model.Order;

public class OrderService {
    private static Order myOrder;

    public static Order getMyCurrentOrder() {
        return myOrder;
    }

    public static void setAsCurrentOrder(Order order) {
        myOrder = order;
    }

    public static boolean isThereCurrentOrder() {
        return myOrder != null;
    }

    public static void clearOrder() {
        myOrder = null;
    }
}
