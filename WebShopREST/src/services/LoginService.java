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

import beans.Admin;
import beans.Customer;
import beans.Manager;
import beans.Trainer;
import dao.AdminDao;
import dao.CustomerDao;
import dao.ManagerDao;
import dao.TrainerDao;
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
			ctx.setAttribute("CustomerDao", new CustomerDao());
		}
		
		if (ctx.getAttribute("AdminDao") == null) {
			ctx.setAttribute("AdminDao", new AdminDao());
		}
		
		if (ctx.getAttribute("ManagerDao") == null) {
			ctx.setAttribute("ManagerDao", new ManagerDao());
		}
		
		if (ctx.getAttribute("TrainerDao") == null) {
			ctx.setAttribute("TrainerDao", new TrainerDao());
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
        	CustomerDao customerDao = (CustomerDao) ctx.getAttribute("CustomerDao");
        	AdminDao adminDao = (AdminDao) ctx.getAttribute("AdminDao");
        	ManagerDao managerDao = (ManagerDao) ctx.getAttribute("ManagerDao");
        	TrainerDao trainerDao = (TrainerDao) ctx.getAttribute("TrainerDao");
        	
        	boolean naso = false;
        	String role = "";
        	
        	//provera customera
        	for(Customer cust : customerDao.getAll()) {
        		if(cust.getUsername().equals(username) && cust.getPassword().equals(password)) {
        			naso = true;
        			role = cust.getRole().toString();
        			break;
        		}
        	}
        	
        	//provera admina
        	if(!naso) {
	        	for(Admin admin : adminDao.getAll()) {
	        		if(admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
	        			naso = true;
	        			role = admin.getRole().toString();
	        			break;
	        		}
	        	}
        	}
        	
        	if(!naso) {
	        	for(Manager manager : managerDao.getAll()) {
	        		if(manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
	        			naso = true;
	        			role = manager.getRole().toString();
	        			break;
	        		}
	        	}
        	}
        	
        	if(!naso) {
	        	for(Trainer trainer : trainerDao.getAll()) {
	        		if(trainer.getUsername().equals(username) && trainer.getPassword().equals(password)) {
	        			naso = true;
	        			role = trainer.getRole().toString();
	        			break;
	        		}
	        	}
        	}
        	
        	//********************
        	
        	if(!naso) {
        		return Response.status(Response.Status.UNAUTHORIZED).build();
        	}

            String token = issueToken(username, role);

            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
            //return Response.ok().entity("Bearer " + token).build();

        } catch (Exception e) {
        	System.out.println(e);
            return Response.status(UNAUTHORIZED).build();
        }
    }

    private String issueToken(String username, String role) {
    	SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
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