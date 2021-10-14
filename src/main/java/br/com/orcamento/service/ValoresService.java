package br.com.orcamento.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.orcamento.model.Valores;
import br.com.orcamento.padrao.GenericService;

/**
 * @author Renan Celso
 */
@Stateless
public class ValoresService extends GenericService implements ValoresServiceLocal {

	private static final long serialVersionUID = -8503299493613295875L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Valores> buscarValorPorEscopo(String escopo) {		
    	try {
    		
    		List<Valores> listaServicos = new ArrayList<>();    		
    		
    		StringBuilder sql = new StringBuilder();
    		sql.append("select o from ").append(Valores.class.getSimpleName()).append(" o ");
    		sql.append(" where o.escopo = '").append(escopo).append("'");
    		
    		listaServicos = (List<Valores>) consultarPorQuery(sql.toString(), 0, 0);
	    
    		return listaServicos;
    		
    	} catch(Exception e) {
    		log.error(e);
    		return null;
    	}		
	}

}
