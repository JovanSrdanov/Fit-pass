package services;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import beans.Customer;
import dao.CustomerDao;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginService {
	
	@Context
    private UriInfo uriInfo;
	
	@Context
	ServletContext ctx;
	
	public LoginService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("CustomerDao") == null) {
	    	//String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CustomerDao", new CustomerDao());
		}
	}
	
    @POST
    @Path("/token")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {
   
        try {
         	//System.out.println(username + " " + password);
            // Authenticate the user using the credentials provided
            //authenticate(username, password);
        	/*if(!username.equals("user") || !password.equals("pass")) {
        		return Response.status(Response.Status.UNAUTHORIZED).build();
        	}*/
        	CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
        	boolean naso = false;
        	for(Customer cust : dao.getAll()) {
        		if(cust.getUsername().equals(username) && cust.getPassword().equals(password)) {
        			naso = true;
        			break;
        		}
        	}
        	
        	if(!naso) {
        		return Response.status(Response.Status.UNAUTHORIZED).build();
        	}

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
            //return Response.ok().entity("Bearer " + token).build();

        } catch (Exception e) {
        	System.out.println(e);
            return Response.status(UNAUTHORIZED).build();
        }
    }

    private String issueToken(String username) {
    	SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }
    
    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}