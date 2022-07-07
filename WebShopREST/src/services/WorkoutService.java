package services;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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

import beans.Admin;
import beans.Workout;
import beans.Workout;
import beans.Manager;
import beans.Product;
import beans.Role;
import beans.Trainer;
import beans.User;
import dao.AdminDao;
import dao.WorkoutDao;
import dao.FacilityDao;
import dao.WorkoutDao;
import dao.ManagerDao;
import dao.ProductDAO;
import dao.TrainerDao;
import dao.WorkoutDao;
import dto.BigDaddy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/workout")
public class WorkoutService {
	
	@Context
	ServletContext ctx;
	
	public WorkoutService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("WorkoutDao") == null) {
			ctx.setAttribute("WorkoutDao", new WorkoutDao());
		}
	}
	
	@PUT
	@Path("/new/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNew(@PathParam("id") int id ,Workout workout) {
		WorkoutDao dao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		
		for(Workout cust : dao.getAll()) {
			if(cust.getname().equals(workout.getname())) {
				return Response.status(409).build();
			}
		}
		workout.setDeleted(false);
		workout.setFacilityId(id);
		dao.addNew(workout);
		
		FacilityDao facilityDao = new FacilityDao();
		facilityDao.getById(id).addWorkout(id);
		facilityDao.writeFile();
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("/update")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Workout update(Workout updatedWorkout, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.manager.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		ManagerDao managerDao = new ManagerDao();
		int managerId = managerDao.getByUsername(username).getId();
		if(managerId != updatedWorkout.getFacilityId()) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		WorkoutDao dao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		//int id = dao.getById(updatedWorkout.getId()).getId();
		return dao.update(updatedWorkout.getId(), updatedWorkout);
	}
}
