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

import beans.Admin;
import beans.Trainer;
import beans.Trainer;
import beans.Product;
import beans.Role;
import dao.AdminDao;
import dao.TrainerDao;
import dao.TrainerDao;
import dao.ProductDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/trainer")
public class TrainerService {
	
	@Context
	ServletContext ctx;
	
	public TrainerService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("TrainerDao") == null) {
			ctx.setAttribute("TrainerDao", new TrainerDao());
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Trainer> getAll() {
		TrainerDao dao = (TrainerDao) ctx.getAttribute("TrainerDao");
		return dao.getAll();
	}
	
	@POST
	@Path("/reg")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNew(Trainer trainer, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		
		TrainerDao dao = (TrainerDao) ctx.getAttribute("TrainerDao");
		
		for(Trainer train : dao.getAll()) {
			if(train.getUsername().equals(trainer.getUsername())) {
				return Response.status(409).build();
			}
		}
		dao.addNew(trainer);
		return Response.ok().build();
	}

}

