package pulse.spe.model.util;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

@Component
public class BancoHoras {
	
	@NotBlank
	private Integer horasTrabalhadas=0;
	
	@NotBlank
	private Integer horasNaoTrabalhadas=0;
	
	@NotBlank
	private Integer horasExtrasTrabalhadas=0;
	
	public Integer getHorasTrabalhadas() {
		return horasTrabalhadas;
	}
	public void setHorasTrabalhadas(Integer horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}
	public Integer getHorasNaoTrabalhadas() {
		return horasNaoTrabalhadas;
	}
	public void setHorasNaoTrabalhadas(Integer horasNaoTrabalhadas) {
		this.horasNaoTrabalhadas = horasNaoTrabalhadas;
	}
	public Integer getHorasExtrasTrabalhadas() {
		return horasExtrasTrabalhadas;
	}
	public void setHorasExtrasTrabalhadas(Integer horasExtrasTrabalhadas) {
		this.horasExtrasTrabalhadas = horasExtrasTrabalhadas;
	}
	
	public void addHorasTrabalhada(int hours) {
		this.horasTrabalhadas+=hours;
	}
	
	public void addHorasNaoTrabalhadas(int hours) {
		this.horasNaoTrabalhadas+=hours;
	}
	
	public void addHorasExtrasTrabalhadas(int hours) {
		this.horasExtrasTrabalhadas+=hours;
	}
	
}
