package pulse.spe.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pulse.spe.model.Usuario;
import pulse.spe.repository.UsuariosRepo;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuariosRepo usuarioRepo;
	
	public Usuario mapToFuncionario(String funcionarioJson, MultipartFile foto) {
		ObjectMapper objectMapper = new ObjectMapper();
		Usuario funcionario = new Usuario();
		try {
			funcionario = objectMapper.readValue(funcionarioJson, Usuario.class);
			funcionario.setFoto(foto.getBytes());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return funcionario;
	}
	
	public Usuario addFuncionario(Usuario funcionario) {
		return usuarioRepo.save(funcionario);
	}

	public Usuario findBy(String cpf) {
		Usuario funcionario = usuarioRepo.findByCpf(cpf);
		return funcionario;
	}
	
	public Usuario findByEmail(String email) {
		Usuario funcionario = usuarioRepo.findByEmail(email);
		return funcionario;
	}
}
