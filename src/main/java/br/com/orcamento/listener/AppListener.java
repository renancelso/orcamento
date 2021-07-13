package br.com.orcamento.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * @author Renan Celso
 */
@WebListener
public class AppListener implements ServletContextListener {

	protected transient Logger log = Logger.getLogger(AppListener.class.getName());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			PropertyConfigurator.configure(sce.getServletContext().getRealPath("/")+File.separator+"WEB-INF"+File.separator+"log4j.properties");
			init();
		} catch (Exception e) {
			log.error(e);
		}		
	}
	
	public void init() {			
		//		
	}	
		
	@Override
	public void contextDestroyed(ServletContextEvent sce) {	
		log.info("....................Context Destroyed....................");
	}	
}