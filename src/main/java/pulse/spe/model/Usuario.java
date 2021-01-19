package pulse.spe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Usuario {

	@Id
	@Column
	@NotNull
	@NotBlank
	private String cpf;

	@Column
	@NotNull
	@NotBlank
	private String nome;

	@Column
	@NotNull
	@NotBlank
	private String sobrenome;

	@Column
	@NotNull
	@NotBlank
	private String email;

	@Column
	@NotNull
	@NotBlank
	private String senha;

	@Column
	@NotNull
	@NotBlank
	private String setor;

	@Column
	@NotNull
	@NotBlank
	private String perfil;

	@Column
	@NotNull
	@NotBlank
	private byte[] foto;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	
	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getSetor() {
		return setor;
	}

}
