package es.ucm.fdi.tieryourlikes.model.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.Category;

public class CategorySerializer implements JsonSerializer<Category> {
    @Override
    public JsonElement serialize(Category src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add(AppConstants.DB_ID_KEY, new JsonPrimitive(src.getId()));
        result.add(AppConstants.DB_CATEGORY_NAME, new JsonPrimitive(src.getName()));
        result.add(AppConstants.DB_CREATION_TIME, new JsonPrimitive(src.getCreationTime()));
        return result;
    }
}
