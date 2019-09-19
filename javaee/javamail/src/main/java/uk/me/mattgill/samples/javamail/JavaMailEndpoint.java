package uk.me.mattgill.samples.javamail;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.ok;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/mail")
@RequestScoped
public class JavaMailEndpoint {

	@Inject
	private JavaMailClient client;

	@POST
	@Consumes(TEXT_PLAIN)
	public Response sendMail(String content) throws AddressException, MessagingException {
		client.sendMessageToSelf("Auto generated message", content);
		return ok().build();
	}

}