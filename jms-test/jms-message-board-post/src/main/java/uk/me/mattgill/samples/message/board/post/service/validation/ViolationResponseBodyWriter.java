package uk.me.mattgill.samples.message.board.post.service.validation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.validation.ConstraintViolation;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ViolationResponseBodyWriter implements MessageBodyWriter<Set<ConstraintViolation<?>>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == HashSet.class;
    }

    @Override
    public long getSize(Set<ConstraintViolation<?>> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // depracated by JAX-RS 2.0
        return 0;
    }

    @Override
    public void writeTo(Set<ConstraintViolation<?>> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try (Writer writer = new PrintWriter(entityStream)) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            t.stream().map((v) -> {
                JsonObjectBuilder objBuilder = Json.createObjectBuilder();
                objBuilder.add("field", v.getPropertyPath().toString());
                objBuilder.add("message", v.getMessage());
                objBuilder.add("passed-value", v.getInvalidValue().toString());
                return objBuilder;
            }).forEachOrdered((objBuilder) -> {
                arrayBuilder.add(objBuilder);
            });
            JsonArray errors = arrayBuilder.build();
            writer.write(errors.toString());
        }
    }

}
