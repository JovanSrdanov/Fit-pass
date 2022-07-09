package services;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import beans.Customer;
import beans.Facility;
import beans.Workout;
import beans.WorkoutAppointment;
import beans.WorkoutHistory;
import beans.WorkoutType;
import beans.Workout;
import beans.Manager;
import beans.Product;
import beans.Role;
import beans.Trainer;
import beans.User;
import dao.AdminDao;
import dao.CustomerDao;
import dao.WorkoutDao;
import dao.FacilityDao;
import dao.WorkoutDao;
import dao.ManagerDao;
import dao.ProductDAO;
import dao.TrainerDao;
import dao.WorkoutAppointmentDao;
import dao.WorkoutDao;
import dto.BigDaddy;
import dto.WorkoutDto;
import dto.WorkoutHistoryDto;
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
		
		if (ctx.getAttribute("WorkoutAppointmentDao") == null) {
			ctx.setAttribute("WorkoutAppointmentDao", new WorkoutAppointmentDao());
		}
	}
	
	@PUT
	@Path("/new/{id}")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNew(@PathParam("id") int id ,Workout workout, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		ManagerDao managerDao = new ManagerDao();
		int managersFacilityId = managerDao.getByUsername(username).getFacilityId();
		
		if(!role.equals(Role.manager.toString()) || managersFacilityId != id) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		WorkoutDao dao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		
		for(Workout cust : dao.getAll()) {
			if(cust.getname().equals(workout.getname())) {
				return Response.status(409).build();
			}
		}
		workout.setDeleted(false);
		workout.setFacilityId(id);
		dao.addNew(workout);
		workout.setPicture("WorkoutLogo" + workout.getId() + ".png");
		dao.writeFile();
		
		FacilityDao facilityDao = new FacilityDao();
		facilityDao.getById(id).addWorkout(workout.getId());
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
		Manager manager = managerDao.getByUsername(username);
		int managerId = manager.getId();
		if(managerId != updatedWorkout.getFacilityId()) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		
		WorkoutDao workoutDao = (WorkoutDao) ctx.getAttribute("WorkoutDao");	
		FacilityDao facilityDao = new FacilityDao();
		Facility facility = facilityDao.getById(manager.getFacilityId());
		
		for(int workoutId : facility.getWorkoutIds()) {
			Workout workout = workoutDao.getById(workoutId);
			if(workout.getname().equals(updatedWorkout.getname())) {
				throw new WebApplicationException(Response.Status.CONFLICT);
			}
		}
		
		//int id = dao.getById(updatedWorkout.getId()).getId();
		return workoutDao.update(updatedWorkout.getId(), updatedWorkout);
	}
	
	@GET
	@Path("/inFacility/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WorkoutDto> getWorkoutsInFacility(@PathParam("id") int id) {
		
	
		ArrayList<WorkoutDto> workoutsInFacility = new ArrayList<WorkoutDto>(); 
		
		WorkoutDao workoutDao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		TrainerDao trainerDao = new TrainerDao();
		
		for(Workout workout : workoutDao.getAll()) {
			if(workout.getFacilityId() == id) {
				Trainer trainer = trainerDao.getById(workout.getTrainerId());
				workoutsInFacility.add(new WorkoutDto(workout, trainer));
			}
		}
		
		return workoutsInFacility;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Workout getById(@PathParam("id") int id) {
		WorkoutDao workoutDao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		return workoutDao.getById(id);
	}
	
	
	@GET
	@Path("/appointments")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WorkoutAppointment> getAllAppointments() {
		WorkoutAppointmentDao workoutAppDao = (WorkoutAppointmentDao) ctx.getAttribute("WorkoutAppointmentDao");
		return workoutAppDao.getAll();
	}
	
	@GET
	@Path("/history/customer")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WorkoutHistoryDto> getCustomerHistory(@Context HttpHeaders headers) {
		ArrayList<WorkoutHistoryDto> workoutHistory = new ArrayList<WorkoutHistoryDto>();
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.customer.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		CustomerDao customerDao = new CustomerDao();
		Customer customer = customerDao.getByUsername(username);
		
		if(customer == null) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		//potrebni dao-vi
		WorkoutDao workoutDao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		FacilityDao facilityDao = new FacilityDao();
		TrainerDao trainerDAo = new TrainerDao();
		
		for(WorkoutHistory custWorkout : customer.getWorkoutHistory()) {
			
			Workout workout = workoutDao.getById(custWorkout.getWorkoutId());
			Facility facility = facilityDao.getById(workout.getFacilityId());
			Trainer trainer = trainerDAo.getById(custWorkout.getTrainerId());
			
			String workoutName = workout.getname();
			String facilityName = facility.getName();
			Date workoutDate = custWorkout.getCheckDate();
			WorkoutType workoutType = workout.getWorkoutType();
			String custName = customer.getName() + " " + customer.getSurname();
			String trainName = "";
			if(trainer != null) {
				trainName = trainer.getName() + " " + trainer.getSurname();
			}
			
			workoutHistory.add(new WorkoutHistoryDto(workoutName, facilityName, workoutDate, workoutType, custName, trainName));
		}
		
		return workoutHistory;
	}
	
	@GET
	@Path("/history/trainer")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WorkoutHistoryDto> getTrainerHistory(@Context HttpHeaders headers) {
		ArrayList<WorkoutHistoryDto> workoutHistory = new ArrayList<WorkoutHistoryDto>();
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.trainer.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		TrainerDao trainerDao = new TrainerDao();
		Trainer trainer = trainerDao.getByUsername(username);
		
		if(trainer == null) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		//potrebni dao-vi
		WorkoutDao workoutDao = (WorkoutDao) ctx.getAttribute("WorkoutDao");
		FacilityDao facilityDao = new FacilityDao();
		CustomerDao customerDao = new CustomerDao();
		
		for(WorkoutHistory custWorkout : trainer.getWorkoutHistory()) {
			
			Workout workout = workoutDao.getById(custWorkout.getWorkoutId());
			Facility facility = facilityDao.getById(workout.getFacilityId());
			Customer customer = customerDao.getById(custWorkout.getCustomerId());
			
			String workoutName = workout.getname();
			String facilityName = facility.getName();
			Date workoutDate = custWorkout.getCheckDate();
			WorkoutType workoutType = workout.getWorkoutType();
			String custName = customer.getName() + " " + customer.getSurname();
			String trainerName = trainer.getName() + " " + trainer.getSurname();
			
			workoutHistory.add(new WorkoutHistoryDto(workoutName, facilityName, workoutDate, workoutType, custName, trainerName));
		}
		
		return workoutHistory;
	}
	
	@GET
	@Path("/history/manager")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WorkoutHistoryDto> getManagerHistory(@Context HttpHeaders headers) {
		
		ArrayList<WorkoutHistoryDto> workoutHistory = new ArrayList<WorkoutHistoryDto>();
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.manager.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		ManagerDao managerDao = new ManagerDao();
		Manager manager = managerDao.getByUsername(username);
		
		if(manager == null) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		int facilityId = manager.getFacilityId();
		
		CustomerDao customerDao = new CustomerDao();
		WorkoutDao workoutDao = new WorkoutDao();
		FacilityDao facilityDao = new FacilityDao();
		TrainerDao trainerDao = new TrainerDao();
		
		for(Customer cust : customerDao.getAll()) {
			for(WorkoutHistory custHist : cust.getWorkoutHistory()) {
				Workout workout = workoutDao.getById(custHist.getWorkoutId());
				
				if(workout.getFacilityId() == facilityId) {
					Facility facility = facilityDao.getById(facilityId);
					Trainer trainer = trainerDao.getById(workout.getTrainerId());
					
					String workoutName = workout.getname();
					String facilityName = facility.getName();
					Date workoutDate = custHist.getCheckDate();
					WorkoutType workoutType = workout.getWorkoutType();
					String customerFullName = cust.getName() + " " + cust.getSurname();
					String trainerFullName = "";
					if(trainer != null) {
						trainerFullName = trainer.getName() + " " + trainer.getSurname();
					}
					
					workoutHistory.add(new WorkoutHistoryDto(workoutName, facilityName, 
							workoutDate, workoutType, customerFullName, trainerFullName));
				}
			}
		}
		
		for(Trainer train : trainerDao.getAll()) {
			for(WorkoutHistory trainHist : train.getWorkoutHistory()) {
				Workout workout = workoutDao.getById(trainHist.getWorkoutId());
				
				if(workout.getFacilityId() == facilityId) {
					Facility facility = facilityDao.getById(facilityId);
					Customer customer = customerDao.getById(trainHist.getCustomerId());
					
					String workoutName = workout.getname();
					String facilityName = facility.getName();
					Date workoutDate = trainHist.getCheckDate();
					WorkoutType workoutType = workout.getWorkoutType();
					String trainerFullName = train.getName() + " " + train.getSurname();
					String customerFullName = customer.getName() + " " + customer.getSurname();
					
					
					workoutHistory.add(new WorkoutHistoryDto(workoutName, facilityName, workoutDate, 
							workoutType, customerFullName, trainerFullName));
				}
			}
		}
		
		return workoutHistory;
	}
}
