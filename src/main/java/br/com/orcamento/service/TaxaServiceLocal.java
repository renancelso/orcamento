package br.com.orcamento.service;

import java.util.List;

import javax.ejb.Local;

import br.com.orcamento.model.Taxa;
import br.com.orcamento.padrao.GenericServiceInterface;

/**
 * @author Renan Celso
 */
@Local
public interface TaxaServiceLocal extends GenericServiceInterface{

	public List<Taxa> buscarTaxaPorSigla(String sigla);	


}
