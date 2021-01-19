package pulse.spe.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import pulse.spe.builder.UsuarioBuilder;
import pulse.spe.model.Usuario;
import pulse.spe.security.UsuarioManager;
import pulse.spe.service.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private UsuarioService usuarioService;

	@MockBean
	private UsuarioManager userDetailsService;
	
	private MockMultipartFile jsonFile;

	private MockMultipartFile file;

	private Usuario usuarioA;



	@BeforeEach
	public void setup() {

		usuarioA = UsuarioBuilder
				.umUsuario()
				.comCpf("872.112.321-10")
				.comNome("Usuario")
				.comSobrenome("A")
				.comSenha("Setor A")
				.comFoto("some photo".getBytes())
				.comEmail("usuarioa@email.com.br")
				.comSenha("usuarioasenha")
				.constroi();
		
		when(usuarioService.addFuncionario(Mockito.any(Usuario.class))).thenReturn(new Usuario());
		
	    when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(new UserDetails() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getUsername() {
				// TODO Auto-generated method stub
				return usuarioA.getEmail();
			}
			
			@Override
			public String getPassword() {
				// TODO Auto-generated method stub
				return usuarioA.getSenha();
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				// TODO Auto-generated method stub
				return null;
			}
		}
	    );

		jsonFile = new MockMultipartFile("usuario", "", "application/json", 
				"{ \"cpf\": \"cpf\",\"nome\": \"nome\", \"sobrenome\": \"sobrenome\",	\"senha\": \"senha\", \"email\": \"email\", \"setor\": \"setor\"}".getBytes());
		file = new MockMultipartFile("foto", "test", "text/plain", "some xml".getBytes());
        

	}
	
	@Test
	void deveRetornarOkPoisEPermitidoParaTodos() throws Exception {
		
		this.mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"usuario\": \"" + usuarioA.getEmail() + "\" , \"senha\": \""+ usuarioA.getSenha() + "\" }")
		)
		.andExpect(status().isOk());
		
	}

	@Test
	void deveRetornarUnauthorizedDevidoCredenciaisIncorretas() throws Exception {
		
		this.mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"usuario\": \"" + usuarioA.getEmail() + "\" , \"senha\": \""+ "senha incorreta" + "\" }")
		)
		.andExpect(status().isUnauthorized());
		
	}

	@Test
	void deveUnauthorizedPoisNaoHaUsuarioLogado() throws Exception {
		
		this.mockMvc.perform(multipart("/usuario")
				.file(jsonFile)
				.file(file))
		.andExpect(status().isUnauthorized());
		
		
	}

	@Test
	@WithMockUser(authorities = { "USER" })
	void deveRetornarForbiddenDevidoNaoPossuirRoleDeADM() throws Exception {
		
		this.mockMvc.perform(multipart("/usuario")
				.file(jsonFile)
				.file(file))
		.andExpect(status().isForbidden());
		
		
	}

	@Test
	@WithMockUser(authorities = { "ADMIN" })
	void deveRetornarOk() throws Exception {
		
		
		this.mockMvc.perform(multipart("/usuario")
				.file(jsonFile)
				.file(file))
		.andExpect(status().isOk());
		
		
	}

}
