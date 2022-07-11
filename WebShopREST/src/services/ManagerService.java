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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Manager;
import beans.Manager;
import beans.Manager;
import beans.Product;
import beans.Role;
import dao.ManagerDao;
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
		
		if(RegistrationService.usernameExists(manager.getUsername())) {
			return Response.status(409).build();
		}
		
		manager.setFacilityId(-1);
		manager.setRole(Role.manager);
		
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
	
	@PUT
	@Path("/update")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manager update(Manager updatedManager, @Context HttpHeaders headers) {
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
		int id = dao.getByUsername(username).getId();
		updatedManager.setRole(Role.manager);
		return dao.update(id, updatedManager);
	}
	
	@GET
	@Path("/hasFacility/{id}")
	@JWTTokenNeeded
	public boolean hasFacility(@PathParam("id") int id, @Context HttpHeaders headers) {
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
		Manager manager = dao.getById(id);
		if(manager == null) {
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Ne postoji manager sa ovim id").build());
		}
		
		if(manager.getFacilityId() != -1) {
			return true;
		}
		return false;
	}
	
	@POST
	@Path("/swap/{oldId}/{newId}")
	@JWTTokenNeeded
	public Response swapManagers(@PathParam("oldId") int firstId, 
								@PathParam("newId") int secondId,
								@Context HttpHeaders headers) {
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
		Manager firstManager = dao.getById(firstId);
		Manager secondManager = dao.getById(secondId);
		
		if(firstManager == null || secondManager == null) {
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Jedan ili oba mangera ne postoje").build());
		}
		
		secondManager.setFacilityId(firstManager.getFacilityId());
		firstManager.setFacilityId(-1);
		dao.writeFile();
			
		return Response.ok().build();
	}
	
	@DELETE
    @Path("/delete/{id}")
    @JWTTokenNeeded
    public Response deleteComment(@PathParam("id") int id, @Context HttpHeaders headers) {
        String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
        if(!role.equals(Role.admin.toString())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
        dao.removeById(id);
        
        return Response.ok().build();
    }
}
