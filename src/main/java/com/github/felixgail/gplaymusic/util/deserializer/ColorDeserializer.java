package com.github.felixgail.gplaymusic.util.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.awt.Color;
import java.lang.reflect.Type;

public class ColorDeserializer implements JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        int red = object.get("red").getAsInt();
        int green = object.get("green").getAsInt();
        int blue = object.get("blue").getAsInt();
        return new Color(red, green, blue);
    }
}
