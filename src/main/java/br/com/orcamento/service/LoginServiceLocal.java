package br.com.orcamento.service;

import java.util.List;

import javax.ejb.Local;

import br.com.orcamento.model.Usuario;
import br.com.orcamento.padrao.GenericServiceInterface;

/**
 * @author Renan Celso
 */
@Local
public interface LoginServiceLocal extends GenericServiceInterface{

	public List<Usuario> buscarUsuarioPorLogin(String login);

}
