package com.ar.tdp2fiuba.hoycomo.service;

import com.ar.tdp2fiuba.hoycomo.model.Address;
import com.ar.tdp2fiuba.hoycomo.model.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreService {

    private final List<Store> stores;
    private int count = 0;

    // TODO: 31/03/18 Use static methods.
    public StoreService() {
        Address addressStub = new Address("Av. Paseo Colón 850", -34.617614, -58.367931);
        String imageUrlExample = "http://i.imgur.com/DvpvklR.png";

        stores = new ArrayList<>();
        stores.add(new Store("55501", "La Birra", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55502", "La Birra Night", addressStub, imageUrlExample, null, 60));
        stores.add(new Store("55503", "Los Orientales", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55504", "Tierra de Nadie", addressStub, imageUrlExample, 30, 40));
        stores.add(new Store("55505", "Dellepiane Bar", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55506", "Heisenburger", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55507", "La Fragua", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55508", "Hobb's", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55509", "Delú", addressStub, imageUrlExample, null, 20));
        stores.add(new Store("55510", "San Carlos", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55511", "San Antonio", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55512", "La Rey", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55513", "La Riojana", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55514", "L'Spiagge Di Napoli", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55515", "La Mezzetta", addressStub, imageUrlExample, 50, 60));
        stores.add(new Store("55516", "El Imperio", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55517", "Pin Pun", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55518", "Lo de Darío", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55519", "Siga la Vaca", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55520", "Los Maizales", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55521", "Güerrín", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55522", "Rodizzio", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55523", "Friday's Puerto Madero", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55524", "Clé", addressStub, imageUrlExample, 40, 60));
        stores.add(new Store("55525", "Prosciutto Caballito", addressStub, imageUrlExample, 40, 60));
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
