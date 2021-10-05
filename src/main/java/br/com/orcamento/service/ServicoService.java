package br.com.orcamento.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.orcamento.model.Servico;
import br.com.orcamento.padrao.GenericService;

/**
 * @author Renan Celso
 */
@Stateless
public class ServicoService extends GenericService implements ServicoServiceLocal {

	private static final long serialVersionUID = -8503299493613295875L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Servico> buscarServicoPorDescricao(String descricao) {		
    	try {
    		
    		List<Servico> listaServicos = new ArrayList<>();    		
    		
    		StringBuilder sql = new StringBuilder();
    		sql.append("select o from ").append(Servico.class.getSimpleName()).append(" o ");
    		sql.append(" where o.descricao = '").append(descricao).append("'");    		    		
    		
    		listaServicos = (List<Servico>) consultarPorQuery(sql.toString(), 0, 0);
	    
    		return listaServicos; 	 
    		
    	} catch(Exception e) {
    		log.error(e);
    		return null;
    	}		
	}

}
