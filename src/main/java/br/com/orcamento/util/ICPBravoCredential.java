package br.com.orcamento.util;

public enum ICPBravoCredential{
	
		
	USUARIO("de160d74-d517-4b93-ad9e-dd5823cecb6b"),
	SENHA("C2FE14DEB6EC0FB33ACD905DEDBB70D4A23B05B3DC09EFB65FDC43512832F2D6");
		
     
	  private String valor;

    private ICPBravoCredential(String valor) {
		
		this.valor = valor;
    }

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
	
	
