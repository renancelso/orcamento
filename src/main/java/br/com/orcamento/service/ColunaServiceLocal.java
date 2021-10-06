package br.com.orcamento.service;

import java.util.List;

import javax.ejb.Local;

import br.com.orcamento.model.Coluna;
import br.com.orcamento.padrao.GenericServiceInterface;

/**
 * @author Renan Celso
 */
@Local
public interface ColunaServiceLocal extends GenericServiceInterface{

	public List<Coluna> buscarColunaPorDescricao(String descricao);	


}
