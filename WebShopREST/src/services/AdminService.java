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
import beans.Customer;
import beans.Product;
import dao.AdminDao;
import dao.CustomerDao;
import dao.ProductDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/admin")
public class AdminService {
	
	@Context
	ServletContext ctx;
	
	public AdminService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("AdminDao") == null) {
			ctx.setAttribute("AdminDao", new AdminDao());
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Admin> getAll() {
		AdminDao dao = (AdminDao) ctx.getAttribute("AdminDao");
		return dao.getAll();
	}

}
