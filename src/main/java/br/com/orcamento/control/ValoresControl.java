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
import br.com.orcamento.model.Taxa;
import br.com.orcamento.model.Valores;
import br.com.orcamento.padrao.BaseControl;
import br.com.orcamento.service.ServicoServiceLocal;
import br.com.orcamento.service.TaxaServiceLocal;
import br.com.orcamento.service.ValoresServiceLocal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Renan Celso
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ManagedBean(name = "valoresControl")
@ViewScoped
public class ValoresControl extends BaseControl {

	private static final long serialVersionUID = 1474502888288593320L;

	private transient Logger log = Logger.getLogger(ValoresControl.class.getName());

	@EJB
	private ValoresServiceLocal valoresService;
	
	
	@EJB
	private ServicoServiceLocal servicoService;
	
	@EJB
	private TaxaServiceLocal taxaService;

	private Valores novoValor;
	
	private Valores valorSelecionado;
	
	private List<Valores> listaValores;
	
	private List<Servico> listaServicos;	
	private List<Taxa> listaTaxas;
	
	private Integer idServico;
	private Integer idTaxa;
	
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		novoValor = new Valores();	
		valorSelecionado = null;	
		listaValores = new ArrayList<Valores>();
		listaValores = (List<Valores>) valoresService.consultarTodos(Valores.class, " order by o.escopo ");		
		listaServicos = (List<Servico>) servicoService.consultarTodos(Servico.class, " order by o.descricao ");
		listaTaxas = (List<Taxa>) taxaService.consultarTodos(Taxa.class, " order by o.descricao ");
		
		
		
	}
	
	public void onRowSelect(SelectEvent event) {
		novoValor = null;
		valorSelecionado = new Valores();
		valorSelecionado = (Valores) event.getObject();  	
    }

	public String cadastrarValor() {
		try {

			if (novoValor.getEscopo() == null || "".equalsIgnoreCase(novoValor.getEscopo())) {
				addErrorMessage("É necessário informar a descricao do escopo.");			
				return null;
			}			
		
			List<Valores> listaValores = new ArrayList<Valores>();
			
			listaValores = valoresService.buscarValorPorEscopo(novoValor.getEscopo());

			if (listaValores != null && !listaValores.isEmpty()) {
				addErrorMessage("O escopo '" + novoValor.getEscopo() + "' já foi cadastrado anteriormente.");				
				return null;
			}

			novoValor.setDhAtu(LocalDateTime.now());
			novoValor = (Valores) valoresService.atualizar(novoValor);

			addInfoMessage("Escopo '" + novoValor.getEscopo() + "' cadastrado com sucesso!");
			init();

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar nova escopo. " + e.getMessage());
			return null;
		}
	}
	
	
	public String alterarValor() {
		try {		
			
			if(valorSelecionado != null) {
			
				if (valorSelecionado.getServico() == null) {
					addErrorMessage("É necessário informar o servico relaciondo.");			
					return null;
				}
				
				List<Valores> listaValores = new ArrayList<>();
				listaValores = valoresService.buscarValorPorEscopo(valorSelecionado.getEscopo());
	
				if (listaValores == null || listaValores.isEmpty()) {
					addErrorMessage("Escopo '" + valorSelecionado.getEscopo() + "' nao encontrado.");				
					return null;
				}
	
				valorSelecionado.setDhAtu(LocalDateTime.now());
				valorSelecionado = (Valores) valoresService.atualizar(valorSelecionado);
	
				addInfoMessage("Escopo '" + valorSelecionado.getEscopo() + "' alterado com sucesso!");			
				
				init();
			
			} else {
				addErrorMessage("Erro ao alterar escopo.");
			}

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar novo escopo. " + e.getMessage());
			return null;
		}
	}
	
	
	public String excluirValor() {
		try {	
			
			String descricao = valorSelecionado.getEscopo();			
			valoresService.remover(valorSelecionado);
			addInfoMessage("Escopo " + descricao + " excluído com sucesso!");
			init();
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao excluir o escopo. " + e.getMessage());
			return null;
		}
	}

	

}
