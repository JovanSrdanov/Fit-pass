package services;

import java.security.Key;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Customer;
import beans.Product;
import dao.CustomerDao;
import dao.ProductDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/customers")
public class CustomerService {
	
	@Context
	ServletContext ctx;
	
	public CustomerService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("CustomerDao") == null) {
			ctx.setAttribute("CustomerDao", new CustomerDao());
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer> getAll() {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		return dao.getAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getById(@PathParam("id") int id) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		return dao.getById(id);
	}
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNew(Customer customer) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		
		for(Customer cust : dao.getAll()) {
			if(cust.getUsername().equals(customer.getUsername())) {
				return Response.status(409).build();
			}
		}
		dao.addNew(customer);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void getProducts(@PathParam("id") int id) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		dao.removeById(id);
	}
	
	@GET
	@Path("/info")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getInfo(@Context HttpHeaders headers) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String token = authHeaders.get(0).substring("Bearer".length()).trim();
		
		Jws<Claims> jws;
		String username = "";

		try {
			SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
	        Key key = keyGenerator.generateKey();

            jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            username = jws.getBody().getSubject();
		}
		     
		catch (JwtException ex) {
		    
		}
		
		return dao.getByUsername(username);
	}

}
