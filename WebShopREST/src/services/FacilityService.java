package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.CustomerDao;
import dao.FacilityDao;
import dto.FacilityDto;

@Path("/facilitys")
public class FacilityService {
	@Context
	ServletContext ctx;
	
	public FacilityService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("FacilityDao") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("FacilityDao", new FacilityDao(contextPath));
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FacilityDto> getAll() {
		FacilityDao dao = (FacilityDao) ctx.getAttribute("FacilityDao");
		return dao.getAllTable(ctx.getRealPath(""));
	}

}
