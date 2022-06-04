package neusoft.pensioncommunity.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class GsonUtil {
    @Getter
    private final static Gson gson;
    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,new TypeAdapter<LocalDate>(){

                    @Override
                    public void write(JsonWriter out, LocalDate value) throws IOException {
                        out.jsonValue("\""+value.toString()+"\"");
                    }

                    @Override
                    public LocalDate read(JsonReader in) throws IOException {
                        return LocalDate.parse(in.nextString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                })

                .registerTypeAdapter(LocalTime.class, new TypeAdapter<LocalTime>() {

                    @Override
                    public void write(JsonWriter out, LocalTime value) throws IOException {
                        out.jsonValue("\""+ value.toString()+"\"");
                    }

                    @Override
                    public LocalTime read(JsonReader in) throws IOException {
                        return LocalTime.parse(in.nextString(),DateTimeFormatter.ofPattern("HH:mm"));
                    }
                })

                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {

                    @Override
                    public void write(JsonWriter out, LocalDateTime value) throws IOException {
                        out.jsonValue("\""+value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"\"");
                    }

                    @Override
                    public LocalDateTime read(JsonReader in) throws IOException {
                        return LocalDateTime.parse(in.nextString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                })
                .create();
    }

    private GsonUtil() {}

}
