package br.com.orcamento.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.orcamento.model.Taxa;
import br.com.orcamento.padrao.GenericService;

/**
 * @author Renan Celso
 */
@Stateless
public class TaxaService extends GenericService implements TaxaServiceLocal {

	private static final long serialVersionUID = -8503299493613295875L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Taxa> buscarTaxaPorSigla(String sigla) {		
    	try {
    		
    		List<Taxa> listaServicos = new ArrayList<>();    		
    		
    		StringBuilder sql = new StringBuilder();
    		sql.append("select o from ").append(Taxa.class.getSimpleName()).append(" o ");
    		sql.append(" where o.sigla = '").append(sigla).append("'");
    		
    		listaServicos = (List<Taxa>) consultarPorQuery(sql.toString(), 0, 0);
	    
    		return listaServicos;
    		
    	} catch(Exception e) {
    		log.error(e);
    		return null;
    	}		
	}

}
