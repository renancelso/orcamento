package br.com.orcamento.padrao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import br.com.orcamento.model.Usuario;

/**
 *
 * @author Renan Celso
 *
 */

@ManagedBean(name = "sessionControl")
@SessionScoped
public class SessionControl extends BaseControl {

	private static final long serialVersionUID = 7314625690695764468L;

	private transient Logger log = Logger.getLogger(SessionControl.class.getName());
	

	/**
	 *
	 * @return Retorna o tempo em milisegundos necessario para realizar o logout da
	 *         aplicacao.
	 */
	public int getSessionTimeout() {
		return (FacesContext.getCurrentInstance().getExternalContext().getSessionMaxInactiveInterval()) * 1000;
	}

	public Object getSessionAttribute(String name) {
		Object result = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session != null) {
			result = session.getAttribute(name);
		}
		return result;
	}

	public Usuario usuarioLogado() {
		Object o = getSessionAttribute("usuarioLogado");
		return o != null ? (Usuario) o : null;
	}

	/**
	 * Invalida a sessao e redireciona para pagina de login.
	 *
	 * @return
	 */
	public String invalidateSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		log.info("Invalidando Sessăo e fazendo logout!");
		// redirect("/cadastro-perito");
		session.invalidate();
		PrimeFaces.current().executeScript("window.location.reload();");
		return null;
	}

	public String deslogar() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		// redirect("/cadastro-perito");
		session.invalidate();
		PrimeFaces.current().executeScript("window.location.reload();");
		return null;
	}

	public String refresh() {
		PrimeFaces.current().executeScript("window.location.reload();");
		return null;
	}

	/**
	 * Renova a sessao.
	 *
	 */
	public String renovarSessao() {
		log.info("Renovando Sessăo!");
		return null;
	}

	public String getContexto() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return request.getSession().getServletContext().getContextPath();
	}

	public String getUrlAtual() {
		StringBuilder url = new StringBuilder();
		url.append(FacesContext.getCurrentInstance().getExternalContext().getRequestServerName()).append(":");
		url.append(FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort()).append("/");
		url.append(FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath());
		return url.toString();
	}		

	public Logger getLogger() {
		return log;
	}

	public void init() {
		log.info("Init SessionControl");
	}
}
