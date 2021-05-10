package es.ucm.fdi.tieryourlikes.model.serializers;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.TierRow;

public class TierRowSerializer implements JsonSerializer<TierRow> {
    @Override
    public JsonElement serialize(TierRow src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add(AppConstants.DB_ROW_NAME_KEY, new JsonPrimitive(src.getRowName()));
        result.add(AppConstants.DB_ROW_COLOR_KEY, new JsonPrimitive(src.getRowName()));

        JsonArray image_urls = new JsonArray();
        List<String> stringUrls = src.getImageUrls();
        for(String s : stringUrls){
            image_urls.add(s);
        }

        result.add(AppConstants.DB_IMAGE_URL_KEY, image_urls);

        return result;
    }
}
