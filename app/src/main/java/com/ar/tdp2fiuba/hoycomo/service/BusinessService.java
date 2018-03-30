package com.ar.tdp2fiuba.hoycomo.service;

import com.ar.tdp2fiuba.hoycomo.model.Business;

import java.util.ArrayList;
import java.util.List;

public class BusinessService {

    // TODO: 30/03/18 Implement!
    public static List<Business> getBusinesses() {
        List<Business> business = new ArrayList<>();
        business.add(new Business("55501", "La Birra"));
        business.add(new Business("55502", "La Birra Night"));
        business.add(new Business("55503", "Los Orientales"));
        business.add(new Business("55504", "Tierra de Nadie"));
        business.add(new Business("55505", "Dellepiane Bar"));
        business.add(new Business("55506", "Heisenburger"));
        business.add(new Business("55507", "La Fragua"));
        business.add(new Business("55508", "Hobb's"));
        business.add(new Business("55509", "Delú"));
        business.add(new Business("55510", "San Carlos"));
        business.add(new Business("55511", "San Antonio"));
        business.add(new Business("55512", "La Rey"));
        business.add(new Business("55513", "La Riojana"));
        business.add(new Business("55514", "L'Spiagge Di Napoli"));
        business.add(new Business("55515", "La Mezzetta"));
        business.add(new Business("55516", "El Imperio"));
        business.add(new Business("55517", "Pin Pun"));
        return business;
    }
}
