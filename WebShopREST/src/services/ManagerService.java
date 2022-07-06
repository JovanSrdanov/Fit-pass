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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Admin;
import beans.Manager;
import beans.Manager;
import beans.Product;
import beans.Role;
import dao.AdminDao;
import dao.ManagerDao;
import dao.ManagerDao;
import dao.ProductDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/manager")
public class ManagerService {
	
	@Context
	ServletContext ctx;
	
	public ManagerService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("ManagerDao") == null) {
			ctx.setAttribute("ManagerDao", new ManagerDao());
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manager> getAll() {
		ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
		return dao.getAll();
	}
	
	@POST
	@Path("/reg")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNew(Manager manager, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
		
		for(Manager man : dao.getAll()) {
			if(man.getUsername().equals(manager.getUsername())) {
				return Response.status(409).build();
			}
		}
		dao.addNew(manager);
		return Response.ok().build();
	}
	
	@GET
	@Path("/available")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manager> getAvailable(@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
		return dao.getAvailable();
	}
}
