package com.ar.tdp2fiuba.hoycomo.utils;

import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoreDeserializer implements JsonDeserializer<Store> {

    @Override
    public Store deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject storeJson = json.getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Store store = gson.fromJson(storeJson, Store.class);
        List<String> foodTypes = new ArrayList<>();
        if (storeJson.get("foodTypes").isJsonArray()) {
            foodTypes = gson.fromJson(storeJson.get("foodTypes"), new TypeToken<List<String>>() {}.getType());
        }
        store.setFoodTypes(foodTypes);
        return store;
    }
}
