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
import beans.PromoCode;
import beans.Manager;
import beans.Product;
import beans.PromoCode;
import beans.Role;
import beans.Trainer;
import beans.User;
import dao.AdminDao;
import dao.PromoCodeDao;
import dao.FacilityDao;
import dao.ManagerDao;
import dao.ProductDAO;
import dao.PromoCodeDao;
import dao.TrainerDao;
import dto.BigDaddy;
import dto.FacilityDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Path("/promoCode")
public class PromoCodeService {
	@Context
	ServletContext ctx;
	
	public PromoCodeService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("PromoCodeDao") == null) {
			ctx.setAttribute("PromoCodeDao", new PromoCodeDao());
		}
	}
	
	@GET
	@Path("/")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<PromoCode> getAll(@Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		PromoCodeDao dao = (PromoCodeDao) ctx.getAttribute("PromoCodeDao");
		return dao.getAll();
	}
	
	@POST
	@Path("/new")
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNew(PromoCode promoCode, @Context HttpHeaders headers) {
		
		String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
		if(!role.equals(Role.admin.toString())) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		PromoCodeDao dao = (PromoCodeDao) ctx.getAttribute("PromoCodeDao");
		
		for(PromoCode cust : dao.getAll()) {
			if(cust.getCode().equals(promoCode.getCode())) {
				return Response.status(409).build();
			}
		}
		
		dao.addNew(promoCode);
		return Response.ok().build();
	}
	
	@GET
	@Path("/valid/{code}")
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public double getAll(@PathParam("code") String code) {	
		PromoCodeDao dao = (PromoCodeDao) ctx.getAttribute("PromoCodeDao");
		
		return dao.getDiscountValid(code);
	}
	
	@DELETE
    @Path("/delete/{id}")
    @JWTTokenNeeded
    public Response deletePromoCode(@PathParam("id") int id, @Context HttpHeaders headers) {
        String role = JWTParser.parseRole(headers.getRequestHeader(HttpHeaders.AUTHORIZATION));
        if(!role.equals(Role.admin.toString())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        
        PromoCodeDao dao = (PromoCodeDao) ctx.getAttribute("PromoCodeDao");
        if(dao.getById(id) == null) {
        	throw new WebApplicationException(Response.Status.CONFLICT);
        }
        dao.removeById(id);
        
        return Response.ok().build();
    }
}
