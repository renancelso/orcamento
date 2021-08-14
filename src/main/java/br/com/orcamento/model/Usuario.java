package br.com.orcamento.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.orcamento.util.Md5;
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
@Table(name="usuario")
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 9086640874010485019L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "login", unique = true, nullable = false, length=255)
	private String login;		
	
	@Column(name = "senha", nullable = false, length=255)
	private String senha;
	
	@Column(name = "email", length=255)
	private String email;
	
	@Column(name = "telefone", length=255)
	private String telefone;
		
	/**
	 * 1 - administrador	
	 * 2 - arquiteto
	 */
	@Column(name = "tipo_usuario", nullable = false, length=255)
	private String tipoUsuario;
			
	@Column(name = "dh_atu")   
    private LocalDateTime dhAtu;	
			
	@Column(name = "senha_nova", length=255)
	private String senhaNova;

	public void setSenha(String senha) {
		if(senha != null && !"".equalsIgnoreCase(senha)){
			this.senha = Md5.getMd5Digest(senha);
		} else {
			this.senha = senha;
		}	
	}

 
		 
}
