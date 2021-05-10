package es.ucm.fdi.tieryourlikes.model.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.User;

public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add(AppConstants.DB_USERNAME_KEY, new JsonPrimitive(src.getUsername()));
        result.add(AppConstants.DB_PASSWORD_KEY, new JsonPrimitive(src.getPassword()));
        result.add(AppConstants.DB_EMAIL_KEY, new JsonPrimitive(src.getEmail()));
        result.add(AppConstants.DB_ICON_KEY, new JsonPrimitive(src.getIcon()));
        result.add(AppConstants.DB_CREATION_TIME, new JsonPrimitive(src.getCreationTime()));
        result.add(AppConstants.DB_ROL, new JsonPrimitive(src.getRol()));

        return result;
    }
}
