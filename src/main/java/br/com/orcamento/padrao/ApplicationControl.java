package br.com.orcamento.padrao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/** 
 * @author Renan Celso
 */
@ManagedBean(name = "applicationControl")
@ApplicationScoped
public class ApplicationControl implements Serializable {

	private static final long serialVersionUID = 3102335277951787470L;
	
	protected transient Logger log = Logger.getLogger(ApplicationControl.class.getName());	
	private Date dataHoraInicio;
	
	@PostConstruct
	public void init() {	
	
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);		
		PropertyConfigurator.configure(session.getServletContext().getRealPath("/") +File.separator+"WEB-INF"+File.separator+"log4j.properties");
		Locale.setDefault(new Locale("pt", "BR"));		
		
		dataHoraInicio = new Date();
		
	}	
	
	public String getIniciarAplicacao(){
		
		return "";
	}

	public Date getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(Date dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}
	
	public String getDataHoraInicioStr() {
		String formato = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(formato);
        return formatter.format(dataHoraInicio);
	}
	
	public String getVersao() {
		InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
		Properties prop = new Properties();
		try {
			prop.load(input);	
			
			return "Versao "+prop.getProperty("versao")+" - "+getDataHoraInicioStr();
			
		} catch (IOException e) {
			return "";
		}    	
    }
	
}
