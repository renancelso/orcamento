package br.com.orcamento.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.orcamento.model.Usuario;
import br.com.orcamento.padrao.GenericService;

/**
 * @author Renan Celso
 */
@Stateless
public class LoginService extends GenericService implements LoginServiceLocal {
	
	private static final long serialVersionUID = 7243814857333623422L;

	@SuppressWarnings("unchecked")
	@Override
    public List<Usuario> buscarUsuarioPorLogin(String login) {		
    	try {
    		List<Usuario> listaUsuarios = new ArrayList<>();    		
    		StringBuilder sql = new StringBuilder();
    		sql.append("select o from ").append(Usuario.class.getSimpleName()).append(" o ");
    		sql.append(" where o.login = '").append(login).append("'");    		    		
    		listaUsuarios = (List<Usuario>) consultarPorQuery(sql.toString(), 0, 0);
	    	return listaUsuarios; 	 
    	} catch(Exception e) {
    		log.error(e);
    		return null;
    	}
    }	

}
