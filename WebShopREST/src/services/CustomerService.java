package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Customer;
import beans.Product;
import dao.CustomerDao;
import dao.ProductDAO;

@Path("/customers")
public class CustomerService {
	
	@Context
	ServletContext ctx;
	
	public CustomerService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("CustomerDao") == null) {
	    	//String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CustomerDao", new CustomerDao());
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer> getAll() {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		return dao.getAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getById(@PathParam("id") int id) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		return dao.getById(id);
	}
	
	@POST
	@Path("/reg/")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer addNew(Customer customer) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		System.out.println(customer.getBirthDate());
		return dao.addNew(customer);
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void getProducts(@PathParam("id") int id) {
		CustomerDao dao = (CustomerDao) ctx.getAttribute("CustomerDao");
		dao.removeById(id);
	}

}
