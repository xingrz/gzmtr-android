package us.xingrz.gzmtr.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class EnumTypeAdapter<E extends Enum> extends TypeAdapter<E> {

    private final E enumeration;
    private final boolean convertCase;

    public EnumTypeAdapter(E enumeration) {
        this(enumeration, true);
    }

    public EnumTypeAdapter(E enumeration, boolean convertCase) {
        this.enumeration = enumeration;
        this.convertCase = convertCase;
    }

    @Override
    public void write(JsonWriter out, E value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(convertCase ? value.name().toLowerCase() : value.name());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        try {
            return (E) Enum.valueOf(
                    enumeration.getDeclaringClass(),
                    convertCase ? in.nextString().toUpperCase() : in.nextString());
        } catch (Exception e) {
            return null;
        }
    }

}
