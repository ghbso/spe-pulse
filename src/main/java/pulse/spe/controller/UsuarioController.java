package pulse.spe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import pulse.spe.model.Usuario;
import pulse.spe.model.util.CredenciaisUsuario;
import pulse.spe.security.JWTUtils;
import pulse.spe.security.UsuarioManager;
import pulse.spe.service.UsuarioService;

@RestController
public class UsuarioController {
	
	@Autowired
	private JWTUtils jwtTokenUtil;

	@Autowired
	private UsuarioManager userDetailsService;

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Cadastra um novo usuário", description = "Realiza o cadastro de um novo usuário", tags = { "Usuário" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário cadastrado!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@PostMapping(value = "/usuario", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Usuario> addUsuario(@RequestPart("usuario") String usuario,
			@RequestPart("foto") MultipartFile file) {

		Usuario funcionario = usuarioService.mapToFuncionario(usuario, file);
		usuarioService.addFuncionario(funcionario);
		return ResponseEntity.ok(funcionario);
	}
	

	@Operation(summary = "Autentica usuário", description = "Realiza autenticação a partir das credenciais do usuário", tags = { "Usuário" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login autorizado!"), 
			@ApiResponse(responseCode = "401", description = "Credenciais incorretas!") 
			
	})
	@PostMapping(value="/login")
	public ResponseEntity<?> login(@RequestBody CredenciaisUsuario usuario) {
		UserDetails userByUsername = userDetailsService.loadUserByUsername(usuario.getUsuario());
		if(!userByUsername.getPassword().equals(usuario.getSenha())) {
			return new ResponseEntity<>("credenciais incorretas", HttpStatus.UNAUTHORIZED);
		}
		String token = jwtTokenUtil.generateToken(userByUsername);
		return ResponseEntity.ok(token);
	}

	
}
