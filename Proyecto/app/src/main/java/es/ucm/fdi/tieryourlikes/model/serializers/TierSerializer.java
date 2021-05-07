package es.ucm.fdi.tieryourlikes.model.serializers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.TierRow;

public class TierSerializer implements JsonSerializer<Tier> {
    @Override
    public JsonElement serialize(Tier src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add(AppConstants.DB_ID_KEY, new JsonPrimitive(src.getId()));
        result.add(AppConstants.DB_TEMPLATE_ID, new JsonPrimitive(src.getTemplateId()));
        result.add(AppConstants.DB_CREATOR_USERNAME_KEY, new JsonPrimitive(src.getCreatorUsername()));
        result.add(AppConstants.DB_CREATION_TIME, new JsonPrimitive(src.getCreationTime()));

        JsonArray container = new JsonArray();
        JsonArray tier_rows = new JsonArray();

        for(String s : src.getContainer()){
            container.add(s);
        }

        String tierRowsString = tierRowsToJsonString(src.getTierRows());
        tier_rows = JsonParser.parseString(tierRowsString).getAsJsonArray();

        result.add(AppConstants.DB_CONTAINER_KEY, container);
        result.add(AppConstants.DB_TIER_ROWS_KEY, tier_rows);

        return result;
    }

    private String tierRowsToJsonString(List<TierRow> tierRows){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TierRow.class, new TierRowSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(tierRows);
    }
}
