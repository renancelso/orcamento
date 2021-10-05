package br.com.orcamento.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import br.com.orcamento.model.Usuario;
import br.com.orcamento.padrao.BaseControl;
import br.com.orcamento.service.LoginServiceLocal;
import br.com.orcamento.service.UsuarioServiceLocal;
import br.com.orcamento.util.Md5;

/**
 * @author Renan Celso
 */

@ManagedBean(name = "usuarioControl")
@ViewScoped
public class UsuarioControl extends BaseControl {

	private static final long serialVersionUID = 2181300953185023148L;

	private transient Logger log = Logger.getLogger(UsuarioControl.class.getName());

	@EJB
	private LoginServiceLocal loginService;
	
	@EJB
	private UsuarioServiceLocal usuarioService;

	private Usuario novoUsuario;
	
	private Usuario usuarioSelecionado;

	private String senhaConfirmacao;
	
	private List<Usuario> listaUsuarios;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {		
		
		novoUsuario = new Usuario();	
		usuarioSelecionado = null;		
		senhaConfirmacao = "";	
		
		listaUsuarios = new ArrayList<Usuario>();
		listaUsuarios = (List<Usuario>) usuarioService.consultarTodos(Usuario.class, " order by o.login ");		
	}
	
	public void onRowSelect(SelectEvent event) {
		novoUsuario = null;
		usuarioSelecionado = new Usuario();
		usuarioSelecionado = (Usuario) event.getObject();  	
    }

	public String btnCadastrarNovoUsuario() {
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

			novoUsuario.setDhAtu(new Date());
			novoUsuario = (Usuario) usuarioService.atualizar(novoUsuario);

			addInfoMessage("Usuário " + novoUsuario.getLogin() + " cadastrado com sucesso");
			init();

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar novo usuário. " + e.getMessage());
			return null;
		}
	}
	
	
	public String btnAlterarUsuario() {
		try {
			
			if(usuarioSelecionado.getId() != null) {
				
				if (usuarioSelecionado.getTipoUsuario() == null || "".equalsIgnoreCase(usuarioSelecionado.getTipoUsuario())) {
					addErrorMessage("É necessário informar o tipo de usuário.");
					usuarioSelecionado.setSenha("");
					senhaConfirmacao = "";
					return null;
				}
	
				if ("".equalsIgnoreCase(usuarioSelecionado.getLogin()) || "".equalsIgnoreCase(usuarioSelecionado.getSenha())
						|| "".equalsIgnoreCase(senhaConfirmacao)) {
					addErrorMessage("Os campos login e senha são obrigatórios. Favor preenchê-los.");
					usuarioSelecionado.setSenha("");
					senhaConfirmacao = "";
					return null;
				}
	
				if (!usuarioSelecionado.getSenha().equals(Md5.getMd5Digest(senhaConfirmacao))) {
					addErrorMessage("Senha e confirmação se senha não conferem.");
					usuarioSelecionado.setSenha("");
					senhaConfirmacao = "";
					return null;
				}
			
				usuarioSelecionado.setDhAtu(new Date());
				usuarioSelecionado = (Usuario) usuarioService.atualizar(usuarioSelecionado);
	
				addInfoMessage("Usuário " + usuarioSelecionado.getLogin() + " alterado com sucesso");
				
				init();
				
			} else {
				addErrorMessage("Erro ao aletrar usuario");				
			}
			
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar novo usuário. " + e.getMessage());
			return null;
		}
	}
	
	
	public String btnExcluirUsuario() {
		try {	
			
			String userRemove = usuarioSelecionado.getLogin();			
			usuarioService.remover(usuarioSelecionado);
			addInfoMessage("Usuário " + userRemove + " excluído com sucesso");
			init();
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao excluir usuário. " + e.getMessage());
			return null;
		}
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

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}
	
}
