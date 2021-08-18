package br.com.orcamento.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;


/**
 * @author Renan Celso
 */
@Entity
@Table(name="servico")
public class Servico implements Serializable {
	
	private static final long serialVersionUID = 1136670534894789845L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "descricao", unique = true, nullable = false, length=255)
	private String descricao;		
			
	@Column(name = "dh_atu")   
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dhAtu;	
	
	@Transient
	private List<Coluna> listaColuna;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDhAtu() {
		return dhAtu;
	}

	public void setDhAtu(Date dhAtu) {
		this.dhAtu = dhAtu;
	}		

	public List<Coluna> getListaColuna() {
		return listaColuna;
	}

	public void setListaColuna(List<Coluna> listaColuna) {
		this.listaColuna = listaColuna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servico other = (Servico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
		 
}
