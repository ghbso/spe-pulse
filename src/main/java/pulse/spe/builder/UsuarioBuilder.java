package pulse.spe.builder;

import pulse.spe.model.Usuario;

public class UsuarioBuilder {
	private Usuario usuario;
	
	private UsuarioBuilder () {}
	
	public static UsuarioBuilder umUsuario() {

		UsuarioBuilder builder = new UsuarioBuilder();

		builder.usuario = new Usuario();
		builder.usuario.setCpf("");
		builder.usuario.setNome("");
		builder.usuario.setSobrenome("");
		builder.usuario.setEmail("");
		builder.usuario.setSenha("");
		builder.usuario.setSetor("");
		builder.usuario.setFoto(null);
		builder.usuario.setPerfil("");
		
		return builder;
	}

	public UsuarioBuilder comCpf(String cpf) {
		usuario.setCpf(cpf);
		return this;
	}
	
	public UsuarioBuilder comNome(String nome) {
		usuario.setNome(nome);
		return this;
	}
	
	public UsuarioBuilder comSobrenome(String sobrenome) {
		usuario.setSobrenome(sobrenome);;
		return this;
	}	

	public UsuarioBuilder comSetor(String setor) {
		usuario.setSetor(setor);
		return this;
	}
	
	public UsuarioBuilder comEmail(String email) {
		usuario.setEmail(email);
		return this;
	}
	
	public UsuarioBuilder comSenha(String senha) {
		usuario.setSenha(senha);
		return this;
	}
	
	public UsuarioBuilder comPerfil(String perfil) {
		usuario.setPerfil(perfil);
		return this;
	}
	
	public UsuarioBuilder comFoto(byte[] foto) {
		usuario.setFoto(foto);
		return this;
	}
	
	public Usuario constroi() {
		return usuario;
	}
	
	
	
}
