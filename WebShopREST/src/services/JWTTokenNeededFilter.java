package services;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {
	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if(authorizationHeader != null && authorizationHeader.length() > 0) {  
        	// Extract the token from the HTTP Authorization header
        	String token = authorizationHeader.substring("Bearer".length()).trim();

	        try {
	
	            // Validate the token
	        	SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
	            Key key = keyGenerator.generateKey();
	            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
	            //logger.info("#### valid token : " + token);
	            System.out.println("Validan token: " + token);
	            return;
	
	        } catch (Exception e) {
	            //logger.severe("#### invalid token : " + token);
	        	System.out.println("Invalid token: " + token);
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
	        }
        }
        else {
        	System.out.println("Nema header");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
