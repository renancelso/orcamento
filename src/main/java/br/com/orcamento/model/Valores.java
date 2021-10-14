package br.com.orcamento.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="valores")
public class Valores {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "servico_id")
	private Servico servico;
	
	private String escopo;

	@OneToOne
	@JoinColumn(name = "taxa_id")
	private Taxa taxa;
	
    private Double custo;
    
    private String unidade;
    
    @Type(type = "yes_no")
	@Column(name = "situacao")
	private Boolean situacao;
	
	private LocalDateTime dhAtu;


}
