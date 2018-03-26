package ris58h.tictactoe.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ByteArrayDeserializer extends JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (!p.isExpectedStartArrayToken()) {
            throw new JsonParseException("Start array expected", p.getCurrentLocation());
        }

        ByteArrayBuilder bab = new ByteArrayBuilder();

        JsonToken jsonToken;
        while ((jsonToken  = p.nextToken()) == JsonToken.VALUE_NUMBER_INT) {
            int intValue = p.getIntValue();
            bab.append(intValue);
        }

        if (jsonToken != JsonToken.END_ARRAY) {
            throw new JsonParseException("End array expected", p.getCurrentLocation());
        }

        return bab.toByteArray();
    }
}
