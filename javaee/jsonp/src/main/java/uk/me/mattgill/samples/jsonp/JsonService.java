package uk.me.mattgill.samples.jsonp;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

@Singleton
@Startup
public class JsonService {

    private static final String TEST_FILE = "object.json";

    public JsonService() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEST_FILE)) {
            JsonParser parser = Json.createParser(inputStream);

            parser.next();
            while (parser.hasNext()) {
                Event event = parser.next();

                switch (event) {
                    case KEY_NAME:
                        System.out.println(String.format("key: %s, value: %s", parser.getString())); break;
                    case START_ARRAY:
                        System.out.println("Starting array: " + parser.getArray().toString()); break;
                }
            }
        }
    }

}