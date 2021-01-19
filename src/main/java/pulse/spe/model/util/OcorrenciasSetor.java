package pulse.spe.model.util;

import javax.validation.constraints.NotBlank;

public class OcorrenciasSetor {
	
	@NotBlank
	private String setor;
	
	@NotBlank
	private long qntOcorrencias;
	
	
	
	public OcorrenciasSetor(String setor, long qntOcorrencias) {
		super();
		this.setor = setor;
		this.qntOcorrencias = qntOcorrencias;
	}
	
	
	public String getSetor() {
		return setor;
	}
	
	public long getQntOcorrencias() {
		return qntOcorrencias;
	}
	
	
}
