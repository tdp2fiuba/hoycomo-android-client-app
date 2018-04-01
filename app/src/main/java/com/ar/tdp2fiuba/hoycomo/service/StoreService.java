package com.ar.tdp2fiuba.hoycomo.service;

import com.ar.tdp2fiuba.hoycomo.model.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreService {

    private final List<Store> stores;
    private int count = 0;

    // TODO: 31/03/18 Use static methods.
    public StoreService() {
        stores = new ArrayList<>();
        stores.add(new Store("55501", "La Birra", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55502", "La Birra Night", "http://i.imgur.com/DvpvklR.png", null, 60));
        stores.add(new Store("55503", "Los Orientales", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55504", "Tierra de Nadie", "http://i.imgur.com/DvpvklR.png", 30, 40));
        stores.add(new Store("55505", "Dellepiane Bar", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55506", "Heisenburger", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55507", "La Fragua", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55508", "Hobb's", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55509", "Delú", "http://i.imgur.com/DvpvklR.png", null, 20));
        stores.add(new Store("55510", "San Carlos", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55511", "San Antonio", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55512", "La Rey", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55513", "La Riojana", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55514", "L'Spiagge Di Napoli", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55515", "La Mezzetta", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55516", "El Imperio", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55517", "Pin Pun", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55518", "Lo de Darío", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55519", "Siga la Vaca", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55520", "Los Maizales", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55521", "Güerrín", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55522", "Rodizzio", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55523", "Friday's Puerto Madero", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55524", "Clé", "http://i.imgur.com/DvpvklR.png", 40, 60));
        stores.add(new Store("55525", "Prosciutto Caballito", "http://i.imgur.com/DvpvklR.png", 40, 60));
    }

    // TODO: 30/03/18 Implement.
    public List<Store> getStores(final int count) {
        int toIndex = this.count + count > this.stores.size() ?
                this.stores.size() : this.count + count;
        int fromIndex = this.count < toIndex ?
                this.count : toIndex;
        List<Store> stores = this.stores.subList(fromIndex, toIndex);
        this.count += count;
        return stores;
    }

    // TODO: 31/03/18 Remove.
    public void resetCount() {
        this.count = 0;
    }
}
