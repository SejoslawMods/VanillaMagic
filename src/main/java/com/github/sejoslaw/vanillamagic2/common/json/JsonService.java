package com.github.sejoslaw.vanillamagic2.common.json;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class JsonService implements IJsonService {
    private final JsonObject json;

    public JsonService(JsonObject json) {
        this.json = json;
    }

    public String getString(String key) {
        return this.getValue(key, JsonElement::getAsString, "");
    }

    public byte getByte(String key) {
        return this.getValue(key, JsonElement::getAsByte, Byte.MIN_VALUE);
    }

    public int getInt(String key) {
        return this.getValue(key, JsonElement::getAsInt, Integer.MIN_VALUE);
    }

    public JsonItemStack getItemStack(String key) {
        JsonObject jsonObjectStack = this.json.getAsJsonObject(key);
        IJsonService service = new JsonService(jsonObjectStack);
        return new JsonItemStack(service);
    }

    public List<IJsonService> getList(String key) {
        List<IJsonService> list = new ArrayList<>();
        JsonArray jsonArray = this.getValue(key, JsonElement::getAsJsonArray, this.json.getAsJsonArray());

        for (JsonElement jsonElement : jsonArray) {
            list.add(new JsonService(jsonElement.getAsJsonObject()));
        }

        return list;
    }

    public Set<String> getEntries() {
        return this.json.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    private <T> T getValue(String key, Function<JsonElement, T> desiredOutput, T defaultIfNotFound) {
        JsonElement element = this.json.get(key);
        return element != null ? desiredOutput.apply(element) : defaultIfNotFound;
    }

    public static void parseArray(Reader reader, Consumer<IJsonService> consumer) {
        parseInternal(reader, rootElement -> rootElement.getAsJsonArray().forEach(jsonElement -> call(jsonElement, consumer)));
    }

    public static void parse(Reader reader, Consumer<IJsonService> consumer) {
        parseInternal(reader, rootElement -> call(rootElement, consumer));
    }

    public static void writePlayerQuestProgress(Writer writer, Set<String> playerQuests) {
        JsonWriter jsonWriter = new JsonWriter(writer);

        try {
            jsonWriter.beginObject();

            for (String questUniqueName : playerQuests) {
                jsonWriter.name(questUniqueName).value(1);
            }

            jsonWriter.endObject();
            jsonWriter.flush();
            jsonWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void parseInternal(Reader reader, Consumer<JsonElement> consumer) {
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(reader);
        consumer.accept(rootElement);
    }

    private static void call(JsonElement rootElement, Consumer<IJsonService> consumer) {
        if (rootElement == null || rootElement.isJsonNull()) {
            rootElement = new JsonObject();
        }

        IJsonService service = new JsonService(rootElement.getAsJsonObject());
        consumer.accept(service);
    }
}
