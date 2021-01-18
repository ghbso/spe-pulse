package pulse.spe.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import pulse.spe.builder.UsuarioBuilder;
import pulse.spe.model.Usuario;
import pulse.spe.service.PontoService;
import pulse.spe.service.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
class PontoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PontoService pontoService;
	
	@MockBean
	private UsuarioService usuarioService;

	private Usuario usuarioComPerfilUser;

	private Usuario usuarioComPerfilAdmin;

	private Usuario usuarioComPerfilManager;

	
	@BeforeEach
	public void setup() {
		usuarioComPerfilUser = UsuarioBuilder
				.umUsuario()
				.comCpf("872.112.321-10")
				.comNome("Usuario")
				.comSobrenome("A")
				.comSenha("Setor A")
				.comFoto("some photo".getBytes())
				.comEmail("usuarioa@email.com.br")
				.comSenha("usuarioasenha")
				.comPerfil("USER")
				.constroi();
		
		usuarioComPerfilAdmin = UsuarioBuilder
				.umUsuario()
				.comCpf("217.323.213-01")
				.comNome("Usuario")
				.comSobrenome("A")
				.comSenha("Setor A")
				.comFoto("some photo".getBytes())
				.comEmail("usuarioa@email.com.br")
				.comSenha("usuarioasenha")
				.comPerfil("ADMIN")
				.constroi();
		

		usuarioComPerfilManager = UsuarioBuilder
				.umUsuario()
				.comCpf("212.213.014-10")
				.comNome("Usuario")
				.comSobrenome("A")
				.comSenha("Setor A")
				.comFoto("some photo".getBytes())
				.comEmail("usuarioa@email.com.br")
				.comSenha("usuarioasenha")
				.comPerfil("MANAGER")
				.constroi();
	 	
		Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
      
	}

	@Test
	@WithMockUser(authorities = { "USER" })
	void deveRetornarForbiddenPorqueNaoEAdminECPFPesquisadoNaoEDoUsuarioLogado() throws Exception {
		
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioComPerfilUser);
		when(usuarioService.findByEmail(Mockito.any(String.class))).thenReturn(usuarioComPerfilUser);
	
		
		this.mockMvc.perform(post("/frequencia")
				.param("cpf", "cpf")
		)
		.andExpect(status().isForbidden());

		this.mockMvc.perform(get("/frequencia")
				.param("cpf", "cpf")
				.param("data", "31/01/2020"))
		.andExpect(status().isForbidden());

		this.mockMvc.perform(get("/frequencia/hoje")
				.param("cpf", "cpf")
		)
		.andExpect(status().isForbidden());

		this.mockMvc.perform(get("/frequencia/historico")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isForbidden());

		this.mockMvc.perform(get("/frequencia/historico/mes")
				.param("cpf", "cpf")
		)
		.andExpect(status().isForbidden());
		
		this.mockMvc.perform(get("/frequencia/historico/ocorrencias/usuario")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isForbidden());
	}
	

	@Test
	@WithMockUser(authorities = { "USER" })
	void deveRetornarForbiddenPorqueNaoPossiPerfilNecessario() throws Exception {
		
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioComPerfilUser);
		when(usuarioService.findByEmail(Mockito.any(String.class))).thenReturn(usuarioComPerfilUser);
	

		this.mockMvc.perform(get("/frequencia/historico/ocorrencias")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isForbidden());

		this.mockMvc.perform(get("/frequencia/historico/consolidado")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
				.param("diaSemana", "1")
		)
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = { "ADMIN" })
	void deveRetornarOkPorqueEAdmin() throws Exception {
		
	    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioComPerfilAdmin);
		when(usuarioService.findByEmail(Mockito.any(String.class))).thenReturn(usuarioComPerfilAdmin);
	
		
		this.mockMvc.perform(post("/frequencia")
				.param("cpf", "cpf")
		)
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia")
				.param("cpf", "cpf")
				.param("data", "31/01/2020"))
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia/hoje")
				.param("cpf", "cpf")
		)
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia/historico")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia/historico/mes")
				.param("cpf", "cpf")
		)
		.andExpect(status().isOk());
		
		this.mockMvc.perform(get("/frequencia/historico/ocorrencias/usuario")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = { "USER" })
	void deveRetornarOkPorqueNaoEAdminMasCPFPesquisadoEDoUsuarioLogado() throws Exception {
		
	    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioComPerfilUser);
		when(usuarioService.findByEmail(Mockito.any(String.class))).thenReturn(usuarioComPerfilUser);
	
		
		this.mockMvc.perform(post("/frequencia")
				.param("cpf", usuarioComPerfilUser.getCpf())
		)
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia")
				.param("cpf", usuarioComPerfilUser.getCpf())
				.param("data", "31/01/2020"))
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia/hoje")
			.param("cpf", usuarioComPerfilUser.getCpf())
		)
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia/historico")
				.param("cpf", usuarioComPerfilUser.getCpf())
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isOk());

		this.mockMvc.perform(get("/frequencia/historico/mes")
				.param("cpf", usuarioComPerfilUser.getCpf())
		)
		.andExpect(status().isOk());
		
		this.mockMvc.perform(get("/frequencia/historico/ocorrencias/usuario")
				.param("cpf", usuarioComPerfilUser.getCpf())
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(authorities = { "ADMIN" })
	void deveRetornarOkPorquePossiPerfilAdminNecessario() throws Exception {
		
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioComPerfilAdmin);
		when(usuarioService.findByEmail(Mockito.any(String.class))).thenReturn(usuarioComPerfilAdmin);
	

		this.mockMvc.perform(get("/frequencia/historico/ocorrencias")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
		)
		.andExpect(status().isOk());

	}

	@Test
	@WithMockUser(authorities = { "MANAGER" })
	void deveRetornarOkPorquePossiPerfilManagerNecessario() throws Exception {
		
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioComPerfilManager);
		when(usuarioService.findByEmail(Mockito.any(String.class))).thenReturn(usuarioComPerfilManager);
	

		this.mockMvc.perform(get("/frequencia/historico/consolidado")
				.param("cpf", "cpf")
				.param("inicio", "01/01/2020")
				.param("fim", "31/01/2020")
				.param("diaSemana", "1")
		)
		.andExpect(status().isOk());
	}
}
