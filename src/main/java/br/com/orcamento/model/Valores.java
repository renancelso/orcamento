package br.com.orcamento.model;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.orcamento.model.enums.MoMat;
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
	
	@Basic
    private int momatValue;

    @Transient
	private MoMat momat;
    
    private Double custo;
    
    private String unidade;
    
	private Boolean situacao;
	
	private LocalDateTime dhAtu;
	
	@PostLoad
    void fillTransient() {
        if (momatValue > 0) {
            this.momat = MoMat.of(momatValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (momat != null) {
            this.momatValue = momat.getCodigo();
        }
    }

}
