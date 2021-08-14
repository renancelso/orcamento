package br.com.orcamento.model.enums;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoMat {
	
	MAO_OBRA(1, "Mão de Obra"), MATERIAL(2, "Material");
	
	private int codigo;
	private String descricao;

	public static MoMat of(int code) {
		return Stream.of(MoMat.values())
          .filter(x -> x.getCodigo() == code)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
	
}
