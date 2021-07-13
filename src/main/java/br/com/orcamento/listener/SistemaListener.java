package br.com.orcamento.listener;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import br.com.orcamento.model.Usuario;


/** 
 * @author Renan Celso 
 * 
 */
@WebListener
public class SistemaListener implements PhaseListener, HttpSessionListener {

	private static final long serialVersionUID = 1L;

	public static final String INITIAL_PAGE = "/";

	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		
		String currentPage = facesContext.getExternalContext().getRequestServletPath();
		boolean paginaPublica = (currentPage.lastIndexOf("publico/") > -1);
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

		Usuario usuarioLogado = null;			

		if (session != null) {
			if(session.getAttribute("usuarioLogado") != null) {
				usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");				
			}
		}

		if (!paginaPublica && usuarioLogado == null) {			
			redirect(INITIAL_PAGE);			
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	private void redirect(String page) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().toLowerCase() + page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// 		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		//		
	}

}