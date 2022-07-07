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
import beans.Customer;
import beans.Manager;
import beans.Product;
import beans.Role;
import beans.Trainer;
import beans.User;
import dao.AdminDao;
import dao.CustomerDao;
import dao.ManagerDao;
import dao.ProductDAO;
import dao.TrainerDao;
import dto.BigDaddy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/customer")
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
	
	@GET
	@Path("/")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer> getAll(@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		return dao.getAll();
	}
	
	@GET
	@Path("/all")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BigDaddy> getAllUsers(@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		ArrayList<BigDaddy> bigDaddys = (ArrayList<BigDaddy>) getAllUsers();
		
		return bigDaddys;
	}
	
	@POST
	@Path("/search")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BigDaddy> getAllFilter(@FormParam("name") String name,
											@FormParam("surname") String surname,
											@FormParam("username") String username,
											@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		ArrayList<BigDaddy> allBigDaddys = (ArrayList<BigDaddy>) getAllUsers();
		
		ArrayList<BigDaddy> filteredBigDaddys = new ArrayList<BigDaddy>();
		
		name = name.toLowerCase();
		surname = surname.toLowerCase();
		username = username.toLowerCase();
		
		for(BigDaddy curr : allBigDaddys) {
			if(curr.getName().toLowerCase().contains(name) &&
					curr.getSurname().toLowerCase().contains(surname) &&
					curr.getUsername().toLowerCase().contains(username)) {
				filteredBigDaddys.add(curr);
			}
		}
		
		return filteredBigDaddys;
	}
	
	public Collection<BigDaddy> getAllUsers() {
		ArrayList<BigDaddy> bigDaddys = new ArrayList<BigDaddy>();
		
		CustomerDao customerDao = (CustomerDao) ctx.getAttribute("CustomerDao");
		for(Customer cust : customerDao.getAll()) {
			bigDaddys.add(new BigDaddy(cust, cust.getPoints(), cust.getCustomerTypeId()));
		}
		
		ManagerDao managerDao = new ManagerDao();
		for(Manager man : managerDao.getAll()) {
			bigDaddys.add(new BigDaddy(man, 0, 0));
		}
		
		TrainerDao trainerDao = new TrainerDao();
		for(Trainer train : trainerDao.getAll()) {
			bigDaddys.add(new BigDaddy(train, 0, 0));
		}
		
		AdminDao adminDao = new AdminDao();
		for(Admin admin : adminDao.getAll()) {
			bigDaddys.add(new BigDaddy(admin, 0, 0));
		}
		
		return bigDaddys;
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
	public User getInfo(@Context HttpHeaders headers) {
		
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String username = "";
		String role = "";

		try {
			username = JWTParser.parseUsername(authHeaders);
			role = JWTParser.parseRole(authHeaders);
		}		     
		catch (JwtException ex) {
		    
		}
		
		if(role.equals(Role.customer.toString())) {
			CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
			return (User)(dao.getByUsername(username));
		}
		if(role.equals(Role.admin.toString())) {
			AdminDao dao = (AdminDao) ctx.getAttribute("AdminDao");
			return (User)(dao.getByUsername(username));
		}
		if(role.equals(Role.trainer.toString())) {
			TrainerDao dao = (TrainerDao) ctx.getAttribute("TrainerDao");
			return (User)(dao.getByUsername(username));
		}
		if(role.equals(Role.manager.toString())) {
			ManagerDao dao = (ManagerDao) ctx.getAttribute("ManagerDao");
			return (User)(dao.getByUsername(username));
		}
		
		
		return null;
	}
	
	@PUT
	@Path("/update")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer update(Customer updatedCustomer, @Context HttpHeaders headers) {
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		int id = dao.getByUsername(username).getId();
		return dao.update(id, updatedCustomer);
	}
	

}
