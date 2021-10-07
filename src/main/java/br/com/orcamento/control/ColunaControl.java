package br.com.orcamento.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import br.com.orcamento.model.Coluna;
import br.com.orcamento.model.Servico;
import br.com.orcamento.padrao.BaseControl;
import br.com.orcamento.service.ColunaServiceLocal;

/**
 * @author Renan Celso
 */

@ManagedBean(name = "colunaControl")
@ViewScoped
public class ColunaControl extends BaseControl {

	private static final long serialVersionUID = 2181300953185023148L;

	private transient Logger log = Logger.getLogger(ColunaControl.class.getName());
	
	@EJB
	private ColunaServiceLocal colunaService;

	private Coluna novaColuna;
	
	private Coluna colunaSelecionada;
	
	private List<Coluna> listaColunas;
	
	private List<Servico> listaServicos;
	
	private Servico servicoSelecionado;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {		
		
		novaColuna = new Coluna();	
		novaColuna.setServico(new Servico());		
		colunaSelecionada = null;	
		
		listaColunas = new ArrayList<Coluna>();
		listaColunas = (List<Coluna>) colunaService.consultarTodos(Coluna.class, " order by o.servico.descricao, o.descricao ");		
		
		listaServicos = new ArrayList<Servico>();
		listaServicos = (List<Servico>) colunaService.consultarTodos(Servico.class, " order by o.descricao ");				
		
		if(servicoSelecionado == null) {
			servicoSelecionado = new Servico();
		}
	}
	
	public void onRowSelect(SelectEvent event) {
		novaColuna = null;
		colunaSelecionada = new Coluna();
		colunaSelecionada = (Coluna) event.getObject();  	
		servicoSelecionado = colunaSelecionada.getServico();
    }

	public String btnCadastrarNovaColuna() {
		try {
			
			if(servicoSelecionado == null || servicoSelecionado.getId() == null) {
				addErrorMessage("Informe umn servico");
				return null;				
			}
			
			novaColuna.setServico(servicoSelecionado);
			
			//novaColuna.setTipo(servicoSelecionado.getTipoServico());
			
			if (novaColuna.getDescricao() == null || "".equalsIgnoreCase(novaColuna.getDescricao())) {
				addErrorMessage("É necessário informar a descricao.");				
				return null;
			}

			List<Coluna> listaColunas = new ArrayList<Coluna>();
			listaColunas = colunaService.buscarColunaPorDescricao(novaColuna.getDescricao());

			if (listaColunas != null && !listaColunas.isEmpty()) {
				addErrorMessage("Coluna '" + novaColuna.getDescricao() + "' já é cadastrada.");			
				return null;
			}
			
			novaColuna = (Coluna) colunaService.atualizar(novaColuna);

			addInfoMessage("Coluna " + novaColuna.getDescricao() + " cadastrada com sucesso");
			init();

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar nova coluna. " + e.getMessage());
			return null;
		}
	}
	
	
	public String btnAlterarColuna() {
		try {
			
			if(servicoSelecionado == null || servicoSelecionado.getId() == null) {
				addErrorMessage("Informe umn servico");
				return null;				
			}
			
			if(colunaSelecionada.getId() != null) {
				
				colunaSelecionada.setServico(servicoSelecionado);
				
				//colunaSelecionada.setTipo(servicoSelecionado.getTipoServico());
				
				if (colunaSelecionada.getDescricao() == null || "".equalsIgnoreCase(colunaSelecionada.getDescricao())) {
					addErrorMessage("É necessário informar a descricao.");				
					return null;					
				}			
				
				colunaSelecionada = (Coluna) colunaService.atualizar(colunaSelecionada);
	
				addInfoMessage("Coluna " + colunaSelecionada.getDescricao() + " alterada com sucesso");
				
				init();
				
			} else {
				addErrorMessage("Erro ao alterar usuario");				
			}
			
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar nova coluna. " + e.getMessage());
			return null;
		}
	}
	
	
	public String btnExcluirColuna() {
		try {	
			
			String colunaRemove = colunaSelecionada.getDescricao();			
			colunaService.remover(colunaSelecionada);
			addInfoMessage("Coluna " + colunaRemove + " excluída com sucesso");
			init();
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao excluir usuário. " + e.getMessage());
			return null;
		}
	}

	public List<Coluna> getListaColunas() {
		return listaColunas;
	}

	public void setListaColunas(List<Coluna> listaColunas) {
		this.listaColunas = listaColunas;
	}

	public ColunaServiceLocal getColunaService() {
		return colunaService;
	}

	public void setColunaService(ColunaServiceLocal colunaService) {
		this.colunaService = colunaService;
	}

	public Coluna getNovaColuna() {
		return novaColuna;
	}

	public void setNovaColuna(Coluna novaColuna) {
		this.novaColuna = novaColuna;
	}

	public Coluna getColunaSelecionada() {
		return colunaSelecionada;
	}

	public void setColunaSelecionada(Coluna colunaSelecionada) {
		this.colunaSelecionada = colunaSelecionada;
	}

	public List<Servico> getListaServicos() {
		return listaServicos;
	}

	public void setListaServicos(List<Servico> listaServicos) {
		this.listaServicos = listaServicos;
	}

	public Servico getServicoSelecionado() {
		return servicoSelecionado;
	}

	public void setServicoSelecionado(Servico servicoSelecionado) {
		this.servicoSelecionado = servicoSelecionado;
	}
		
}
