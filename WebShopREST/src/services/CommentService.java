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
import javax.ws.rs.core.Response.Status;

import beans.Admin;
import beans.Comment;
import beans.CommentStatus;
import beans.Customer;
import beans.Manager;
import beans.Product;
import beans.Role;
import beans.Trainer;
import beans.User;
import beans.WorkoutHistory;
import dao.AdminDao;
import dao.CommentDao;
import dao.CustomerDao;
import dao.FacilityDao;
import dao.ManagerDao;
import dao.ProductDAO;
import dao.TrainerDao;
import dto.BigDaddy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/comment")
public class CommentService {
	
	@Context
	ServletContext ctx;
	
	public CommentService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("CustomerDao") == null) {
			ctx.setAttribute("CustomerDao", new CustomerDao());
		}
		if (ctx.getAttribute("AdminDao") == null) {
			ctx.setAttribute("AdminDao", new AdminDao());
		}
		if (ctx.getAttribute("FacilityDao") == null) {
			ctx.setAttribute("FacilityDao", new FacilityDao());
		}
		if (ctx.getAttribute("CommentDao") == null) {
			ctx.setAttribute("CommentDao", new CommentDao());
		}
		if (ctx.getAttribute("ManagerDao") == null) {
			ctx.setAttribute("ManagerDao", new ManagerDao());
		}
	}
	
	@GET
	@Path("/all")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getById(@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		CommentDao dao = (CommentDao) ctx.getAttribute("CommentDao");
		return dao.getAllWaiting();
	}
	
	@PUT
	@Path("/changeStatus/{id}")
	@JWTTokenNeeded
	public Response changeCommentStatus(CommentStatus status, @PathParam("id") int id,@Context HttpHeaders headers) {
		
		System.out.println("Status  je " + status);
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		CommentDao commentDao = (CommentDao) ctx.getAttribute("CommentDao");
		Comment comm = commentDao.getById(id);
		if(comm == null) {
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Ne postoji komentar sa ovim id").build());
		}
		
		comm.setStatus(status);
		commentDao.writeFile();
		
		if(status == CommentStatus.approved) {
			FacilityDao facilityDao = (FacilityDao) ctx.getAttribute("FacilityDao");
			int numberOfComments = commentDao.getNumberOfApprovedForFacility(comm.getFacilityId());
			facilityDao.recalculateRating(comm.getFacilityId(), comm.getRating(), numberOfComments);
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/new")
	@JWTTokenNeeded
	public Response newComment(Comment comment ,@Context HttpHeaders headers) {
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		CustomerDao customerDao = (CustomerDao) ctx.getAttribute("CustomerDao");
		Customer customer = customerDao.getByUsername(username);
		
		comment.setCustomerId(customer.getId());
		comment.setStatus(CommentStatus.waiting);
		
		if(!customer.getVisitedFacilityIds().contains(comment.getFacilityId())) {
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Niste posetili ovaj objekat ni jednom").build());
		}
		
		CommentDao commentDao = (CommentDao) ctx.getAttribute("CommentDao");
		if(commentDao.exists(comment.getCustomerId(), comment.getFacilityId())) {
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Vec ste ostavili komentar").build());
		}
		commentDao.addNew(comment);
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/can/{id}")
	@JWTTokenNeeded
	public boolean canComment(@PathParam("id") int facilityId, @Context HttpHeaders headers) {
		String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		CustomerDao customerDao = (CustomerDao) ctx.getAttribute("CustomerDao");
		Customer customer = customerDao.getByUsername(username);
		int customerId  = customer.getId();
		
		if(!customer.getVisitedFacilityIds().contains(facilityId)) {
			return false;
		}
		
		CommentDao commentDao = (CommentDao) ctx.getAttribute("CommentDao");
		return commentDao.exists(customerId, facilityId);
	}
	
	@GET
	@Path("/facility/approved/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getCommentfForFacilityApproved(@PathParam("id") int id) {
		CommentDao commentDao = (CommentDao) ctx.getAttribute("CommentDao");
		return commentDao.getForFacilityApproved(id);
	}
	
	@GET
	@Path("facility/all/{id}")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getCommentfForFacilityAll(@PathParam("id") int id,@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		
		if(role.equals(Role.admin.toString())) {
			CommentDao commentDao = (CommentDao) ctx.getAttribute("CommentDao");
			return commentDao.getForFacilityAll(id);
		}
		if(role.equals(Role.manager.toString())) {
			String username = JWTParser.parseUsername(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
			
			ManagerDao managerDao = (ManagerDao) ctx.getAttribute("ManagerDao");
			Manager manager = managerDao.getByUsername(username);
			
			if(manager == null) {
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			}
			
			if(manager.getFacilityId() == id) {
				CommentDao commentDao = (CommentDao) ctx.getAttribute("CommentDao");
				return commentDao.getForFacilityAll(id);
			}
		}
		
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);	
	}
}
