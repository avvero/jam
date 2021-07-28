package pw.avvero.jam.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class SerializationUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static <T> T read(String content, Class<T> valueType) throws IOException {
        if (content == null || content.trim().length() == 0) return null;
        return OBJECT_MAPPER.readValue(content, valueType);
    }

    public static String stringify(Object value) throws JsonProcessingException {
        if (value == null) return null;
        return OBJECT_MAPPER.writeValueAsString(value);
    }

}
