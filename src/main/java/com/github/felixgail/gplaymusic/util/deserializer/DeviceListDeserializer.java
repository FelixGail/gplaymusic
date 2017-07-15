package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.shema.DeviceList;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DeviceListDeserializer implements JsonDeserializer<DeviceList>
{
    @Override
    public DeviceList deserialize(JsonElement jsonElement,
                                  Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject content = jsonElement.getAsJsonObject();
        DeviceList deviceList = new DeviceList();
        JsonArray entries = content.getAsJsonObject("data").getAsJsonArray("items");
        for (JsonElement entry : entries) {
            DeviceList.DeviceInfo info =
                    jsonDeserializationContext.deserialize(entry, DeviceList.DeviceInfo.class);
            deviceList.add(info);
        }
        return deviceList;
    }
}
