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

import br.com.orcamento.model.Taxa;
import br.com.orcamento.padrao.BaseControl;
import br.com.orcamento.service.TaxaServiceLocal;
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
@ManagedBean(name = "taxaControl")
@ViewScoped
public class TaxaControl extends BaseControl {

	private static final long serialVersionUID = 1474502888288593320L;

	private transient Logger log = Logger.getLogger(TaxaControl.class.getName());

	@EJB
	private TaxaServiceLocal taxaService;

	private Taxa novaTaxa;
	
	private Taxa taxaSelecionada;
	
	private List<Taxa> listaTaxa;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		novaTaxa = new Taxa();	
		taxaSelecionada = null;	
		listaTaxa = new ArrayList<Taxa>();
		listaTaxa = (List<Taxa>) taxaService.consultarTodos(Taxa.class, " order by o.descricao ");		
	}
	
	public void onRowSelect(SelectEvent event) {
		novaTaxa = null;
		taxaSelecionada = new Taxa();
		taxaSelecionada = (Taxa) event.getObject();  	
    }

	public String cadastrarTaxa() {
		try {

			if (novaTaxa.getDescricao() == null || "".equalsIgnoreCase(novaTaxa.getDescricao())) {
				addErrorMessage("É necessário informar a descricao da taxa.");			
				return null;
			}			
		
			List<Taxa> listaTaxas = new ArrayList<Taxa>();
			listaTaxas = taxaService.buscarTaxaPorSigla(novaTaxa.getDescricao());

			if (listaTaxas != null && !listaTaxas.isEmpty()) {
				addErrorMessage("A taxa '" + novaTaxa.getDescricao() + "' já foi cadastrada anteriormente.");				
				return null;
			}

			novaTaxa.setDhAtu(LocalDateTime.now());
			novaTaxa = (Taxa) taxaService.atualizar(novaTaxa);

			addInfoMessage("Taxa '" + novaTaxa.getDescricao() + "' cadastrada com sucesso!");
			init();

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar nova taxa. " + e.getMessage());
			return null;
		}
	}
	
	
	public String alterarTaxa() {
		try {		
			
			if(taxaSelecionada != null) {
			
				if (taxaSelecionada.getSigla() == null || "".equalsIgnoreCase(taxaSelecionada.getSigla())) {
					addErrorMessage("É necessário informar a sigla da taxa.");			
					return null;
				}
				
				List<Taxa> listaTaxas = new ArrayList<Taxa>();
				listaTaxas = taxaService.buscarTaxaPorSigla(taxaSelecionada.getSigla());
	
				if (listaTaxas == null || listaTaxas.isEmpty()) {
					addErrorMessage("Taxa '" + taxaSelecionada.getDescricao() + "' nao encontrada.");				
					return null;
				}
	
				taxaSelecionada.setDhAtu(LocalDateTime.now());
				taxaSelecionada = (Taxa) taxaService.atualizar(taxaSelecionada);
	
				addInfoMessage("Taxa '" + taxaSelecionada.getDescricao() + "' alterada com sucesso!");			
				
				init();
			
			} else {
				addErrorMessage("Erro ao alterar taxa.");
			}

			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao cadastrar nova taxa. " + e.getMessage());
			return null;
		}
	}
	
	
	public String excluirTaxa() {
		try {	
			
			String descricao = taxaSelecionada.getDescricao();			
			taxaService.remover(taxaSelecionada);
			addInfoMessage("Taxa " + descricao + " excluída com sucesso!");
			init();
			return null;

		} catch (Exception e) {
			log.error(e);
			addErrorMessage("Erro ao excluir a taxa. " + e.getMessage());
			return null;
		}
	}

	

}
