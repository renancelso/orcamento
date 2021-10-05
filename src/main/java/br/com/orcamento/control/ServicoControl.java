package br.com.orcamento.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import br.com.orcamento.model.Servico;
import br.com.orcamento.padrao.BaseControl;
import br.com.orcamento.service.ServicoServiceLocal;

/**
 * @author Renan Celso
 */

@ManagedBean(name = "servicoControl")
@ViewScoped
public class ServicoControl extends BaseControl {

	private static final long serialVersionUID = 1474502888288593320L;

	private transient Logger log = Logger.getLogger(ServicoControl.class.getName());

	@EJB
	private ServicoServiceLocal servicoService;

	private Servico novoServico;
	
	private Servico servicoSelecionado;
	
	private List<Servico> listaServicos;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		novoServico = new Servico();	
		servicoSelecionado = null;	
		listaServicos = new ArrayList<Servico>();
		listaServicos = (List<Servico>) servicoService.consultarTodos(Servico.class, " order by o.descricao ");		
	}
	
	public void onRowSelect(SelectEvent event) {
		novoServico = null;
		servicoSelecionado = new Servico();
		servicoSelecionado = (Servico) event.getObject();  	
    }

	public String btnCadastrarNovoServico() {
		try {

			if (novoServico.getDescricao() == null || "".equalsIgnoreCase(novoServico.getDescricao())) {
				addErrorMessage("É necessário informar a descricao do servico.");			
				return null;
			}			
		
			List<Servico> listaServicos = new ArrayList<Servico>();
			listaServicos = servicoService.buscarServicoPorDescricao(novoServico.getDescricao());

			if (listaServicos != null && !listaServicos.isEmpty()) {
				addErrorMessage("Servico '" + novoServico.getDescricao() + "' já é cadastrado.");				
				return null;
			}

			novoServico.setDhAtu(LocalDateTime.now());
			novoServico = (Servico) servicoService.atualizar(novoServico);

			addInfoMessage("Servico '" + novoServico.getDescricao() + "' cadastrado com sucesso");
			init();

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar novo servico. " + e.getMessage());
			return null;
		}
	}
	
	
	public String btnAlterarServico() {
		try {		
			
			if(servicoSelecionado != null) {
			
				if (servicoSelecionado.getDescricao() == null || "".equalsIgnoreCase(servicoSelecionado.getDescricao())) {
					addErrorMessage("É necessário informar a descricao do servico.");			
					return null;
				}
				
				List<Servico> listaServicos = new ArrayList<Servico>();
				listaServicos = servicoService.buscarServicoPorDescricao(servicoSelecionado.getDescricao());
	
				if (listaServicos != null && !listaServicos.isEmpty()) {
					addErrorMessage("Servico '" + servicoSelecionado.getDescricao() + "' já é cadastrado.");				
					return null;
				}
	
				servicoSelecionado.setDhAtu(LocalDateTime.now());
				servicoSelecionado = (Servico) servicoService.atualizar(servicoSelecionado);
	
				addInfoMessage("Servico '" + servicoSelecionado.getDescricao() + "' alterado com sucesso");			
				
				init();
			
			} else {
				addErrorMessage("Erro ao alterar servico");
			}

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar novo servico. " + e.getMessage());
			return null;
		}
	}
	
	
	public String btnExcluirServico() {
		try {	
			
			String userRemove = servicoSelecionado.getDescricao();			
			servicoService.remover(servicoSelecionado);
			addInfoMessage("Servico " + userRemove + " excluído com sucesso");
			init();
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao excluir servico. " + e.getMessage());
			return null;
		}
	}

	public Servico getNovoServico() {
		return novoServico;
	}

	public void setNovoServico(Servico novoServico) {
		this.novoServico = novoServico;
	}

	public Servico getServicoSelecionado() {
		return servicoSelecionado;
	}

	public void setServicoSelecionado(Servico servicoSelecionado) {
		this.servicoSelecionado = servicoSelecionado;
	}

	public List<Servico> getListaServicos() {
		return listaServicos;
	}

	public void setListaServicos(List<Servico> listaServicos) {
		this.listaServicos = listaServicos;
	}	

}
