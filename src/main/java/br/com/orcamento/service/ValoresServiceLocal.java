package br.com.orcamento.service;

import java.util.List;

import javax.ejb.Local;

import br.com.orcamento.model.Valores;
import br.com.orcamento.padrao.GenericServiceInterface;

/**
 * @author Renan Celso
 */
@Local
public interface ValoresServiceLocal extends GenericServiceInterface{

	public List<Valores> buscarValorPorEscopo(String escopo);	


}
