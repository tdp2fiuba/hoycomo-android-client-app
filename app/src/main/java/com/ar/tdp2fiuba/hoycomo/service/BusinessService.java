package com.ar.tdp2fiuba.hoycomo.service;

import com.ar.tdp2fiuba.hoycomo.model.Business;

import java.util.ArrayList;
import java.util.List;

public class BusinessService {

    private final List<Business> business;
    private int count = 0;

    // TODO: 31/03/18 Use static methods.
    public BusinessService() {
        business = new ArrayList<>();
        business.add(new Business("55501", "La Birra", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55502", "La Birra Night", "http://i.imgur.com/DvpvklR.png", null, 60));
        business.add(new Business("55503", "Los Orientales", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55504", "Tierra de Nadie", "http://i.imgur.com/DvpvklR.png", 30, 40));
        business.add(new Business("55505", "Dellepiane Bar", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55506", "Heisenburger", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55507", "La Fragua", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55508", "Hobb's", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55509", "Delú", "http://i.imgur.com/DvpvklR.png", null, 20));
        business.add(new Business("55510", "San Carlos", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55511", "San Antonio", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55512", "La Rey", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55513", "La Riojana", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55514", "L'Spiagge Di Napoli", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55515", "La Mezzetta", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55516", "El Imperio", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55517", "Pin Pun", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55518", "Lo de Darío", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55519", "Siga la Vaca", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55520", "Los Maizales", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55521", "Güerrín", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55522", "Rodizzio", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55523", "Friday's Puerto Madero", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55524", "Clé", "http://i.imgur.com/DvpvklR.png", 40, 60));
        business.add(new Business("55525", "Prosciutto Caballito", "http://i.imgur.com/DvpvklR.png", 40, 60));
    }

    // TODO: 30/03/18 Implement.
    public List<Business> getBusinesses(final int count) {
        int toIndex = this.count + count > this.business.size() ?
                this.business.size() : this.count + count;
        int fromIndex = this.count < toIndex ?
                this.count : toIndex;
        List<Business> businesses = this.business.subList(fromIndex, toIndex);
        this.count += count;
        return businesses;
    }

    // TODO: 31/03/18 Remove.
    public void resetCount() {
        this.count = 0;
    }
}
