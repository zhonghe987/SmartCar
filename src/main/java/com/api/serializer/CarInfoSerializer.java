package com.api.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.api.model.CarInfo;
import java.lang.reflect.Type;

public class CarInfoSerializer implements JsonSerializer<CarInfo> {

    @Override
    public JsonElement serialize(CarInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("carid", src.getCarId());
        object.addProperty("info", src.getInfo());
        object.addProperty("created_at", src.getCreatedAt().toString());
        return object;
    }

}
