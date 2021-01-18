package pulse.spe.builder;

import java.time.LocalDate;
import java.time.LocalTime;

import pulse.spe.model.Usuario;
import pulse.spe.model.Ponto;

public class PontoBuilder {
	private Ponto ponto;
	
	private PontoBuilder () {}
	
	public static PontoBuilder umRegistroPonto() {

		PontoBuilder builder = new PontoBuilder();

		builder.ponto = new Ponto();
		builder.ponto.setData(LocalDate.now());
		builder.ponto.setHora(LocalTime.now());
		builder.ponto.setId(null);
		builder.ponto.setFuncionario(null);
		
		return builder;
	}

	public PontoBuilder comID(Integer id) {
		ponto.setId(id);
		return this;
	}
	
	public PontoBuilder comData(LocalDate data) {
		ponto.setData(data);
		return this;
	}
	
	public PontoBuilder comHora(LocalTime hora) {
		ponto.setHora(hora);
		return this;
	}
	
	public PontoBuilder comFuncionario(Usuario funcionario) {
		ponto.setFuncionario(funcionario);
		return this;
	}
	

	public Ponto constroi() {
		return ponto;
	}
	
	
	
}
