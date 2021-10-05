package br.com.orcamento.service;

import java.util.List;

import javax.ejb.Local;

import br.com.orcamento.model.Servico;
import br.com.orcamento.padrao.GenericServiceInterface;

/**
 * @author Renan Celso
 */
@Local
public interface ServicoServiceLocal extends GenericServiceInterface{

	public List<Servico> buscarServicoPorDescricao(String descricao);	


}
