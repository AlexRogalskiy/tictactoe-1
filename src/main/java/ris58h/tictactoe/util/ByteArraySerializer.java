package ris58h.tictactoe.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ByteArraySerializer extends JsonSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (byte b : value) {
            gen.writeNumber(unsignedToBytes(b));
        }
        gen.writeEndArray();
    }

    private static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }
}
