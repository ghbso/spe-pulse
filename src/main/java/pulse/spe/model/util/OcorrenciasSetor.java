package pulse.spe.model.util;

public class OcorrenciasSetor {
	
	private String setor;
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
