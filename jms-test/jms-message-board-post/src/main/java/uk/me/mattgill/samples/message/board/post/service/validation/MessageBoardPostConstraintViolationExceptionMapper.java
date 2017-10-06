package uk.me.mattgill.samples.message.board.post.service.validation;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MessageBoardPostConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.serverError()
                .entity(exception.getConstraintViolations())
                .build();
    }

}
