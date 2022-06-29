package main;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@ApplicationPath("/rest")
public class Startup  extends Application{
	@Context
	ServletContext ctx;
	
	public static String path;
	
	public Startup() {
	}
	
	@PostConstruct
	public void init() {
	    String contextPath = ctx.getRealPath("");
	    path = contextPath.split(".metadata")[0];
	}
}
