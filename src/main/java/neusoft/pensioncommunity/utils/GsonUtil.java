package neusoft.pensioncommunity.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class GsonUtil {
    @Getter
    private final static Gson gson;
    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,new TypeAdapter<LocalDate>(){

                    @Override
                    public void write(JsonWriter out, LocalDate value) throws IOException {
                        out.jsonValue(value.toString());
                    }

                    @Override
                    public LocalDate read(JsonReader in) throws IOException {
                        return LocalDate.parse(in.nextString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                })

                .registerTypeAdapter(LocalTime.class, new TypeAdapter<LocalDate>() {

                    @Override
                    public void write(JsonWriter out, LocalDate value) throws IOException {

                    }

                    @Override
                    public LocalDate read(JsonReader in) throws IOException {
                        return null;
                    }
                })

                .create();
    }

    private GsonUtil() {}

}
