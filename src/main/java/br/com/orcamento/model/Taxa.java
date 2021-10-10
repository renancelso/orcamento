package br.com.orcamento.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Renan Celso
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="taxa")
public class Taxa implements Serializable {
	
	private static final long serialVersionUID = 7248232731297484566L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "descricao", unique = true, nullable = false, length=255)
	private String descricao;	
	
	@Column(name = "sigla", unique = true, nullable = false, length=255)
	private String sigla;	
	
	@Column(name = "valor", nullable = false, length=255)
	private Double valor;
			
	@Column(name = "dh_atu")   
    private LocalDateTime dhAtu;	
	
	@Transient
	private String valorPercent;
	
	public String getValorPercent() {
		
		if(this.valor != null) {
			return (this.valor*100) + "%";			
		}else {
			return "0%";
		}
		
	}
			 
}
