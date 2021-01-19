package pulse.spe.model.util;

import javax.validation.constraints.NotBlank;

public class OcorrenciasUsuario {
	
	@NotBlank
	private String cpf;
	
	@NotBlank
	private long qntOcorrencias;
	
	
	
	public OcorrenciasUsuario(String cpf, long qntOcorrencias) {
		super();
		this.cpf = cpf;
		this.qntOcorrencias = qntOcorrencias;
	}
	
	public String getCpf() {
		return cpf;
	}
	public long getQntOcorrencias() {
		return qntOcorrencias;
	}
	
	
}
