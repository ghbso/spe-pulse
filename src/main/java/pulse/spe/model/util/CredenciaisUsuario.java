package pulse.spe.model.util;

import javax.validation.constraints.NotBlank;

public class CredenciaisUsuario {
	
	@NotBlank
	public String usuario;
	
	@NotBlank
	public String senha;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
