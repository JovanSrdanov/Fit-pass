package services;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.ws.rs.core.Response.Status;

import beans.Admin;
import beans.BaseMembership;
import beans.Customer;
import beans.CustomerType;
import beans.Manager;
import beans.Membership;
import beans.Admin;
import beans.Product;
import beans.PromoCode;
import beans.Role;
import beans.Trainer;
import beans.Workout;
import beans.WorkoutHistory;
import dao.AdminDao;
import dao.BaseMembershipDao;
import dao.CustomerDao;
import dao.CustomerTypeDao;
import dao.ManagerDao;
import dao.MembershipDao;
import dao.AdminDao;
import dao.ProductDAO;
import dao.PromoCodeDao;
import dao.TrainerDao;
import dao.WorkoutDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import main.Startup;

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
		Customer customer = customerDao.getByUsername(username);
		int tokenCustomerId = customer.getId();
		if(tokenCustomerId != customerId) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		
		MembershipDao dao = (MembershipDao) ctx.getAttribute("MembershipDao");
		Membership membership = dao.getById(customer.getMembershipId());
		if(membership == null) {
			throw new WebApplicationException(Response.Status.NO_CONTENT);
		}

		return membership;
	}
	
	@POST
	@Path("/newMembership")
	@JWTTokenNeeded
	public Response addNew(@FormParam("code") String code,
						   @FormParam("perDay") int perDay,
						   @FormParam("promoCode") String promoCode,
						   @Context HttpHeaders headers) {
		
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		CustomerDao customerDao = new CustomerDao();
		Customer customer = customerDao.getByUsername(username);
		if(customer == null) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		BaseMembershipDao baseMembershipDao = new BaseMembershipDao();
		BaseMembership baseMembership = baseMembershipDao.getByCode(code);
		if(baseMembership == null) {
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Base clanarian ne postoji").build());
		}
		
		double discount = 1;
		PromoCodeDao promoCodeDao = new PromoCodeDao();
		System.out.println("Promo kod je:" + promoCode);
		if(!promoCode.equals("")) {
			PromoCode existingPromoCode = promoCodeDao.getByCode(promoCode);
			if(existingPromoCode == null) {
				throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Promo kod ne vazi").build());
			}
			if(existingPromoCode.getUsageCount() > 0 && existingPromoCode.getValidDate().compareTo(new Date()) >= 0) {
				discount = existingPromoCode.getDiscountPercentage() / 100.0;			
				existingPromoCode.reduceUsgeCount();
				promoCodeDao.writeFile();
			}
			else
			{
				throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Promo kod ne moze vise da se koristi").build());
			}
		}
		
		double discount2 = 1;
		CustomerTypeDao customerTypeDao = new CustomerTypeDao();   
		CustomerType customerType = customerTypeDao.getById(customer.getCustomerTypeId());
		if(customerType != null) {
			discount2 = customerType.getDiscount();			
		}
		
		double multiplicator = baseMembership.getPriceMultiplicator();
		if(perDay == 1) {
			multiplicator = 1;
		}
		
		Date todayDay = new Date();
		Calendar cal = Calendar.getInstance();
        cal.setTime(todayDay);
 
        cal.add(Calendar.DATE, baseMembership.getDurationDays());
 
        Date endDate = cal.getTime();
        
        int perDayForPrice = perDay;
        if(perDay == -1) {
        	perDayForPrice = 11;
        }
		
        Membership membership = new Membership();
		membership.setPrice(perDayForPrice * baseMembership.getPrice() * multiplicator * discount * discount2);
		membership.setCustomerId(customer.getId());
		membership.setDeleted(false);
		membership.setCode(code);
		membership.setStartDate(todayDay);
		membership.setEndDate(endDate);
		membership.setActive(true);
		membership.setNumberOfTrainings(perDay);
		membership.setNumberOfChechkins(0);
		
		MembershipDao membershipDao = new MembershipDao();
		membershipDao.addNew(membership);
		
		int oldMemId = customer.getMembershipId();
		customer.setMembershipId(membership.getId());
		customerDao.writeFile();
		
		Membership oldMembership = membershipDao.getById(oldMemId);
		if(oldMembership != null) {
			Startup.calculatePointForMembership(oldMembership);
			oldMembership.setActive(false);
			membershipDao.writeFile();
		}
		
		
		return Response.ok().build();
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
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Nepostoje trening ili kupac sa ovim id/username").build());
		}
		if(workout.getFacilityId() != manager.getFacilityId()) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Niste manager ovog objekta").build());
		}
		if(customer.getMembershipId() == -1) {
			System.out.println("nema clanarinu uopste");
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Kupac nema uplacenu clanarinu").build());
		}
		MembershipDao membershipDao = (MembershipDao) ctx.getAttribute("MembershipDao");
		Membership membership = membershipDao.getById(customer.getMembershipId());
		
		if(membership == null) {
			System.out.println("nema clanarine u bazi");
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Kupac ima fejk clanarinu").build());
		}
		
		Date todayDay = new Date();
		
		if(membership.getStartDate().compareTo(todayDay) > 0 || membership.getEndDate().compareTo(todayDay) < 0) {
			System.out.println("datum ne valja");
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Istekla clanarina").build());
		}
		
		//Provera da li je ispucao broj puta u danu
		int todayCheckins = 0;
		for(WorkoutHistory workoutHistory : customer.getWorkoutHistory()) {
			if(		workoutHistory.getCheckDate().getYear() == todayDay.getYear() &&
					workoutHistory.getCheckDate().getDay() == todayDay.getDay() &&
					workoutHistory.getCheckDate().getMonth() == todayDay.getMonth()) {
				
				todayCheckins += 1;
			}
		}
		
		if(membership.getNumberOfTrainings() != -1 && todayCheckins >= membership.getNumberOfTrainings()) {
			System.out.println("vec se cekiro");
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Kupac se vec cekirao max broj puta").build());
		}
		
		//*****************************************************************************************************
		TrainerDao trainerDao = new TrainerDao();
		if(workout.getTrainerId() != -1) {
			Trainer trainer = trainerDao.getById(workout.getTrainerId());
			trainer.getWorkoutHistory().add(new WorkoutHistory(workoutId, todayDay, customer.getId(), workout.getTrainerId()));
			trainerDao.writeFile();
		} 
		
		membership.setNumberOfChechkins(membership.getNumberOfChechkins() + 1);
		membershipDao.writeFile();
		
		customer.getWorkoutHistory().add(new WorkoutHistory(workoutId, todayDay, customer.getId(), workout.getTrainerId()));
		customer.addVistedFacility(workout.getFacilityId());
		customerDao.writeFile();
		System.out.println("DDoso do kraja");
		return Response.status(200).build();
		
		
	}
}
