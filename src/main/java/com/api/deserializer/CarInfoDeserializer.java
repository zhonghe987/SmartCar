package com.api.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.api.model.CarInfo;
import java.lang.reflect.Type;

public class CarInfoDeserializer implements JsonDeserializer<CarInfo> {

    @Override
    public CarInfo deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String carid = json.getAsJsonObject().get("carid").getAsString();
        String info = json.getAsJsonObject().get("info").getAsString();

        CarInfo carinfo = new CarInfo(carid, info);

        return carinfo;
    }

}
