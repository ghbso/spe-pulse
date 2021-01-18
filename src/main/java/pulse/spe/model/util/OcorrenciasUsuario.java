package pulse.spe.model.util;

public class OcorrenciasUsuario {
	
	private String cpf;
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
