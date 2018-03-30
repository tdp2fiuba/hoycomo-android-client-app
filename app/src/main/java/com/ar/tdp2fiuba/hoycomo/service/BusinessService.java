package com.ar.tdp2fiuba.hoycomo.service;

import com.ar.tdp2fiuba.hoycomo.model.Business;

import java.util.ArrayList;
import java.util.List;

public class BusinessService {

    // TODO: 30/03/18 Implement!
    public static List<Business> getBusinesses() {
        List<Business> business = new ArrayList<>();
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
        return business;
    }
}
