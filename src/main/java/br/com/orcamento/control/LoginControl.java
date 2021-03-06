package br.com.orcamento.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.orcamento.model.Usuario;
import br.com.orcamento.padrao.BaseControl;
import br.com.orcamento.service.LoginServiceLocal;
import br.com.orcamento.util.Md5;

/**
 * @author Renan Celso
 */

@ManagedBean(name = "loginControl")
@ViewScoped
public class LoginControl extends BaseControl {

	private static final long serialVersionUID = -4636428633761387291L;

	private transient Logger log = Logger.getLogger(LoginControl.class.getName());

	@EJB
	private LoginServiceLocal loginService;

	private Usuario novoUsuario;

	private String senhaConfirmacao;

	private String loginLogar;

	private String senhaLogar;

	@PostConstruct
	public void init() {
		novoUsuario = new Usuario();
		senhaConfirmacao = "";
		loginLogar = "";
		senhaLogar = "";
	}

	public String cadastrarNovoUsuario() {
		try {

			if (novoUsuario.getTipoUsuario() == null || "".equalsIgnoreCase(novoUsuario.getTipoUsuario())) {
				addErrorMessage("É necessário informar o tipo de usuário.");
				novoUsuario.setSenha("");
				senhaConfirmacao = "";
				return null;
			}

			if ("".equalsIgnoreCase(novoUsuario.getLogin()) || "".equalsIgnoreCase(novoUsuario.getSenha())
					|| "".equalsIgnoreCase(senhaConfirmacao)) {
				addErrorMessage("Os campos login e senha são obrigatórios. Favor preenchê-los.");
				novoUsuario.setSenha("");
				senhaConfirmacao = "";
				return null;
			}

			if (!novoUsuario.getSenha().equals(Md5.getMd5Digest(senhaConfirmacao))) {
				addErrorMessage("Senha e confirmação se senha não conferem.");
				novoUsuario.setSenha("");
				senhaConfirmacao = "";
				return null;
			}

			List<Usuario> listaUsuarios = new ArrayList<Usuario>();
			listaUsuarios = loginService.buscarUsuarioPorLogin(novoUsuario.getLogin());

			if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
				addErrorMessage("Login '" + novoUsuario.getLogin() + "' já é cadastrado.");
				novoUsuario.setLogin("");
				novoUsuario.setSenha("");
				senhaConfirmacao = "";
				return null;
			}

			novoUsuario.setDhAtu(LocalDateTime.now());
			novoUsuario = (Usuario) loginService.atualizar(novoUsuario);

			addInfoMessage("Usuário " + novoUsuario.getLogin() + " cadastrado com sucesso");
			init();

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar novo usuário. " + e.getMessage());
			return null;
		}
	}

	public String logar() {

		try {

			HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

			if (loginLogar == null || "".equalsIgnoreCase(loginLogar)) {
				addErrorMessage("Informe seu login");
				return null;
			}

			if (senhaLogar == null || "".equalsIgnoreCase(senhaLogar)) {
				addErrorMessage("Informe sua senha");
				return null;
			}

			List<Usuario> listaUsuarios = loginService.buscarUsuarioPorLogin(loginLogar);

			if (listaUsuarios == null || listaUsuarios.isEmpty()) {
				addErrorMessage("Usuário inválido");
				return null;
			}

			Usuario usuarioBase = new Usuario();
			usuarioBase = listaUsuarios.get(0);

			if (usuarioBase != null && usuarioBase.getLogin() != null && usuarioBase.getSenha() != null
					&& !"".equalsIgnoreCase(usuarioBase.getLogin()) && !"".equalsIgnoreCase(usuarioBase.getSenha())) {

				if (!usuarioBase.getSenha().equals(Md5.getMd5Digest(senhaLogar))
						&& !Md5.getMd5Digest(senhaLogar).equalsIgnoreCase(usuarioBase.getSenhaNova())) {

					addErrorMessage("Senha inválida");
					return null;

				} else {

					if (usuarioBase.getSenha().equals(Md5.getMd5Digest(senhaLogar))) {

						sessao.setAttribute("usuarioLogado", usuarioBase);

						log.info("Usuario validado. Vai redirecionar aplicacao para '/admin'");
						redirect("/admin");
						return null;
					}

					if (usuarioBase.getSenhaNova() != null && !"".equalsIgnoreCase(usuarioBase.getSenhaNova())
							&& Md5.getMd5Digest(senhaLogar).equalsIgnoreCase(usuarioBase.getSenhaNova())) {

						usuarioBase.setSenha(senhaLogar);
						usuarioBase.setSenhaNova(null);
						usuarioBase = (Usuario) loginService.atualizar(usuarioBase);

						sessao.setAttribute("usuarioLogado", usuarioBase);

						log.info("Usuario validado. Vai redirecionar aplicacao para /admin");
						redirect("/admin");
						return null;
					}
				}

			}

		} catch (Exception e) {
			log.error("Erro ao efetuar login no sistema" + e.getMessage());
			addErrorMessage("Erro ao efetuar login no sistema");
			return null;
		}

		return null;
	}

	public Usuario getNovoUsuario() {
		return novoUsuario;
	}

	public void setNovoUsuario(Usuario novoUsuario) {
		this.novoUsuario = novoUsuario;
	}

	public String getSenhaConfirmacao() {
		return senhaConfirmacao;
	}

	public void setSenhaConfirmacao(String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
	}

	public String getSenhaLogar() {
		return senhaLogar;
	}

	public void setSenhaLogar(String senhaLogar) {
		this.senhaLogar = senhaLogar;
	}

	public String getLoginLogar() {
		return loginLogar;
	}

	public void setLoginLogar(String loginLogar) {
		this.loginLogar = loginLogar;
	}

}
