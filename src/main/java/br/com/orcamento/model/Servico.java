package br.com.orcamento.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name="servico")
public class Servico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "tipo_servico")
	private String tipoServico;
	
	@Column(name = "descricao", unique = true, nullable = false, length=255)
	private String descricao;
	
	@Type(type = "yes_no")
	@Column(name = "situacao")
	private Boolean situacao;
	
	@Column(name = "dh_atu")
	private LocalDateTime dhAtu;

	@Transient
	private List<Coluna> listaColuna;
	
}
