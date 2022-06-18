package services;

import java.security.Key;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;


@Path("/auth")
public class AuthServiceTest {

	@GET
    @Path("/jwt")
    @JWTTokenNeeded
    public Response echoWithJWTToken(@QueryParam("message") String message, @Context HttpHeaders headers) {
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		if(authHeaders == null || authHeaders.size() <= 0) {
			return Response.status(Response.Status.PROXY_AUTHENTICATION_REQUIRED).build();
		}
		String token = authHeaders.get(0).substring("Bearer".length()).trim();
		//System.out.println(token);
		
		Jws<Claims> jws;

		try {
			SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
	        Key key = keyGenerator.generateKey();

            jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			System.out.println(jws.getBody().getSubject());
		}
		     
		catch (JwtException ex) {
		    
		}
		
        return Response.ok().entity(message == null ? "no message" : message).build();
    }
	
	@GET
	@Path("/nojwt")
    public Response echo(@QueryParam("message") String message) {
        return Response.ok().entity(message == null ? "no message" : message).build();
    }
}
