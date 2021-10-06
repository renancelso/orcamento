package br.com.orcamento.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.orcamento.model.Coluna;
import br.com.orcamento.padrao.GenericService;

/**
 * @author Renan Celso
 */
@Stateless
public class ColunaService extends GenericService implements ColunaServiceLocal {

	private static final long serialVersionUID = 4447180900924473878L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Coluna> buscarColunaPorDescricao(String descricao) {		
    	try {
    		
    		List<Coluna> listaColunas = new ArrayList<>();    		
    		
    		StringBuilder sql = new StringBuilder();
    		sql.append("select o from ").append(Coluna.class.getSimpleName()).append(" o ");
    		sql.append(" where o.descricao = '").append(descricao).append("'");    		    		
    		
    		listaColunas = (List<Coluna>) consultarPorQuery(sql.toString(), 0, 0);
	    
    		return listaColunas; 	 
    		
    	} catch(Exception e) {
    		log.error(e);
    		return null;
    	}		
	}

}
