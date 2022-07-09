package services;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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
import beans.BaseMembership;
import beans.Customer;
import beans.Manager;
import beans.Membership;
import beans.Admin;
import beans.Product;
import beans.Role;
import beans.Workout;
import beans.WorkoutHistory;
import dao.AdminDao;
import dao.BaseMembershipDao;
import dao.CustomerDao;
import dao.ManagerDao;
import dao.MembershipDao;
import dao.AdminDao;
import dao.ProductDAO;
import dao.WorkoutDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/membership")
public class MembershipService {
	@Context
	ServletContext ctx;
	
	public MembershipService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("BaseMembershipDao") == null) {
			ctx.setAttribute("BaseMembershipDao", new BaseMembershipDao());
		}
		
		if (ctx.getAttribute("MembershipDao") == null) {
			ctx.setAttribute("MembershipDao", new MembershipDao());
		}
	}
	
	@GET
	@Path("/base")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BaseMembership> getAllBase() {
		BaseMembershipDao dao = (BaseMembershipDao) ctx.getAttribute("BaseMembershipDao");
		return dao.getAll();
	}
	
	//ko ima pristup?
	@GET
	@Path("/all")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Membership> getAll(@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		MembershipDao dao = (MembershipDao) ctx.getAttribute("MembershipDao");
		return dao.getAll();
	}
	
	@GET
	@Path("/customer/{id}")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Membership getMembershipForCustomerId(@PathParam("id") int customerId, @Context HttpHeaders headers) {
		
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		CustomerDao customerDao = new CustomerDao();
		int tokenCustomerId = customerDao.getByUsername(username).getId();
		if(tokenCustomerId != customerId) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		
		MembershipDao dao = (MembershipDao) ctx.getAttribute("MembershipDao");
		Membership membership = dao.getForCustomerId(customerId);
		if(membership == null) {
			throw new WebApplicationException(Response.Status.NO_CONTENT);
		}

		return membership;
	}
	
	@SuppressWarnings("deprecation")
	@POST
	@Path("/check-in")
	@JWTTokenNeeded
	public Response checkIn(@FormParam("customerUsername") String customerUsername,
						@FormParam("workoutId") int workoutId,
						@Context HttpHeaders headers) {
		//Zastita*********************************************************************************************
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.manager.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		WorkoutDao workoutDao = new WorkoutDao();
		Workout workout = workoutDao.getById(workoutId);
		
		CustomerDao customerDao = new CustomerDao();
		Customer customer = customerDao.getByUsername(customerUsername);
		
		ManagerDao managerDao = new ManagerDao();
		Manager manager = managerDao.getByUsername(username);
		
		if(workout == null || customer == null) {
			throw new WebApplicationException(Response.Status.NO_CONTENT);
		}
		if(workout.getFacilityId() != manager.getFacilityId()) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		if(customer.getMembershipId() == -1) {
			System.out.println("nema clanarinu uopste");
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
		MembershipDao membershipDao = (MembershipDao) ctx.getAttribute("MembershipDao");
		Membership membership = membershipDao.getById(customer.getMembershipId());
		
		if(membership == null) {
			System.out.println("nema clanarine u bazi");
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
		
		Date todayDay = new Date();
		
		if(membership.getStartDate().compareTo(todayDay) > 0 || membership.getEndDate().compareTo(todayDay) < 0) {
			System.out.println("datum ne valja");
			System.out.println(membership.getStartDate().compareTo(todayDay));
			System.out.println(membership.getEndDate().compareTo(todayDay));
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
		
		//Provera da li je ispucao broj puta u danu
		int todayCheckins = 0;
		for(WorkoutHistory workoutHistory : customer.getWorkoutHistory()) {
			if(		workoutHistory.getCheckDate().getYear() == todayDay.getYear() ||
					workoutHistory.getCheckDate().getDay() == todayDay.getDay() ||
					workoutHistory.getCheckDate().getMonth() == todayDay.getMonth()) {
				
				todayCheckins += 1;
			}
		}
		
		if(todayCheckins >= membership.getNumberOfTrainings()) {
			System.out.println("vec se cekiro");
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
		
		//*****************************************************************************************************
		customer.getWorkoutHistory().add(new WorkoutHistory(workoutId, todayDay));
		customerDao.writeFile();
		System.out.println("DDoso do kraja");
		return Response.status(200).build();
		
		
	}
}
