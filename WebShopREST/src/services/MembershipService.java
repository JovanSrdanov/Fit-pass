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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Admin;
import beans.BaseMembership;
import beans.Membership;
import beans.Admin;
import beans.Product;
import beans.Role;
import dao.AdminDao;
import dao.BaseMembershipDao;
import dao.MembershipDao;
import dao.AdminDao;
import dao.ProductDAO;
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
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Membership> getAll() {
		MembershipDao dao = (MembershipDao) ctx.getAttribute("MembershipDao");
		return dao.getAll();
	}
	
	
}
