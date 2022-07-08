package services;

import java.security.Key;
import java.util.ArrayList;
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

import beans.Trainer;
import beans.Workout;
import beans.Trainer;
import beans.Trainer;
import beans.Facility;
import beans.Product;
import beans.Role;
import dao.TrainerDao;
import dao.WorkoutDao;
import dao.TrainerDao;
import dao.TrainerDao;
import dao.FacilityDao;
import dao.ManagerDao;
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
		
		if(RegistrationService.usernameExists(trainer.getUsername())) {
			return Response.status(409).build();
		}
		
		dao.addNew(trainer);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/update")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Trainer update(Trainer updatedTrainer, @Context HttpHeaders headers) {
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		TrainerDao dao = (TrainerDao) ctx.getAttribute("TrainerDao");
		int id = dao.getByUsername(username).getId();
		updatedTrainer.setRole(Role.trainer);
		return dao.update(id, updatedTrainer);
	}
	
	@GET
	@Path("/dig/{id}")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Trainer> getAll(@PathParam("id") int id, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		ManagerDao managerDao = new ManagerDao();
		int managersFacilityId = managerDao.getByUsername(username).getFacilityId();
		
		if(!role.equals(Role.manager.toString()) || managersFacilityId != id) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		ArrayList<Trainer> trainersInFacility = new ArrayList<Trainer>();
		
		TrainerDao trainerDao = (TrainerDao) ctx.getAttribute("TrainerDao");
		FacilityDao facilityDao = new FacilityDao();
		WorkoutDao workoutDao = new WorkoutDao();
		
		Facility facility = facilityDao.getById(id);
		
		for(int workoutId : facility.getWorkoutIds()) {
			Workout workout = workoutDao.getById(workoutId);
			
			if(workout.getTrainerId() != -1) {
				Trainer trainer = trainerDao.getById(workout.getTrainerId());
				if(!trainersInFacility.contains(trainer)) {		
					trainersInFacility.add(trainer);
				}
			}
		}
			
		return trainersInFacility;
	}

}

