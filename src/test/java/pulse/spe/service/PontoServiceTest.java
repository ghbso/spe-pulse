package pulse.spe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import pulse.spe.builder.PontoBuilder;
import pulse.spe.builder.UsuarioBuilder;
import pulse.spe.model.Usuario;
import pulse.spe.model.Ponto;
import pulse.spe.model.util.BancoHoras;
import pulse.spe.repository.PontosRepo;


@SpringBootTest
@AutoConfigureMockMvc
class PontoServiceTest {

	@Autowired
	private PontoService pontoService;

	@MockBean
	private UsuarioService usuarioService;

	@MockBean
	private PontosRepo pontosRepo;

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
		
	}
	
	@Test
	void deveTerRegistroApenasHorasTrabalhadas() {
		Ponto registroPonto01 = PontoBuilder
				.umRegistroPonto()
				.comID(1)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto02 = PontoBuilder
				.umRegistroPonto()
				.comID(2)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto03 = PontoBuilder
				.umRegistroPonto()
				.comID(3)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto04 = PontoBuilder
				.umRegistroPonto()
				.comID(4)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(17, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto05 = PontoBuilder
			.umRegistroPonto()
			.comID(5)
			.comData(LocalDate.of(2021, 01, 16))
			.comHora(LocalTime.of(8, 0))
			.comFuncionario(usuarioA)
			.constroi();
		
		Ponto registroPonto06 = PontoBuilder
				.umRegistroPonto()
				.comID(6)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		List<Ponto> registrosPonto = List.of(registroPonto01, registroPonto02, registroPonto03, registroPonto04, registroPonto05, registroPonto06);
		Mockito.when(usuarioService.findBy(Mockito.any(String.class))).thenReturn(usuarioA);
		Mockito.when(pontosRepo.findAllByFuncionarioAndDataBetween(Mockito.any(Usuario.class), 
				Mockito.any(LocalDate.class),
				Mockito.any(LocalDate.class)
				)).thenReturn(registrosPonto);
		
		BancoHoras bancoHoras = pontoService.retrieveBancoHorasDentroIntervalo(usuarioA.getCpf(),registroPonto01.getData(), registroPonto06.getData());
		
		assertEquals(bancoHoras.getHorasTrabalhadas(), 12);
		assertEquals(bancoHoras.getHorasExtrasTrabalhadas(), 0);
		assertEquals(bancoHoras.getHorasNaoTrabalhadas(), 0);
		
		
	}

	@Test
	void deveTerRegistroApenasHorasTrabalhadasEHorasExtras() {
		Ponto registroPonto01 = PontoBuilder
				.umRegistroPonto()
				.comID(1)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto02 = PontoBuilder
				.umRegistroPonto()
				.comID(2)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto03 = PontoBuilder
				.umRegistroPonto()
				.comID(3)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto04 = PontoBuilder
				.umRegistroPonto()
				.comID(4)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(17, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto05 = PontoBuilder
			.umRegistroPonto()
			.comID(5)
			.comData(LocalDate.of(2021, 01, 16))
			.comHora(LocalTime.of(8, 0))
			.comFuncionario(usuarioA)
			.constroi();
		
		Ponto registroPonto06 = PontoBuilder
				.umRegistroPonto()
				.comID(6)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto07 = PontoBuilder
				.umRegistroPonto()
				.comID(7)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		Ponto registroPonto08 = PontoBuilder
				.umRegistroPonto()
				.comID(8)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(14, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto09 = PontoBuilder
				.umRegistroPonto()
				.comID(9)
				.comData(LocalDate.of(2021, 01, 23))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		Ponto registroPonto10 = PontoBuilder
				.umRegistroPonto()
				.comID(10)
				.comData(LocalDate.of(2021, 01, 23 ))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto11 = PontoBuilder
				.umRegistroPonto()
				.comID(11)
				.comData(LocalDate.of(2021, 01, 23))
				.comHora(LocalTime.of(15, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		Ponto registroPonto12 = PontoBuilder
				.umRegistroPonto()
				.comID(12)
				.comData(LocalDate.of(2021, 01, 23 ))
				.comHora(LocalTime.of(19, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		
		List<Ponto> registrosPonto = List.of(registroPonto01, registroPonto02, registroPonto03, registroPonto04, registroPonto05, registroPonto06, 
				registroPonto07, registroPonto08, registroPonto09, registroPonto10, registroPonto11, registroPonto12);
		Mockito.when(usuarioService.findBy(Mockito.any(String.class))).thenReturn(usuarioA);
		Mockito.when(pontosRepo.findAllByFuncionarioAndDataBetween(Mockito.any(Usuario.class), 
				Mockito.any(LocalDate.class),
				Mockito.any(LocalDate.class)
				)).thenReturn(registrosPonto);
		
		BancoHoras bancoHoras = pontoService.retrieveBancoHorasDentroIntervalo(usuarioA.getCpf(),registroPonto01.getData(), registroPonto12.getData());
		
		assertEquals(bancoHoras.getHorasTrabalhadas(), 16);
		assertEquals(bancoHoras.getHorasExtrasTrabalhadas(), 3);
		assertEquals(bancoHoras.getHorasNaoTrabalhadas(), 0);
		
		
	}

	@Test
	void deveTerRegistrosHorasTrabalhadasHorasExtrasEHorasNaoTrabalhadas() {
		Ponto registroPonto01 = PontoBuilder
				.umRegistroPonto()
				.comID(1)
				.comData(LocalDate.of(2021, 01, 11))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto02 = PontoBuilder
				.umRegistroPonto()
				.comID(2)
				.comData(LocalDate.of(2021, 01, 11))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto03 = PontoBuilder
				.umRegistroPonto()
				.comID(3)
				.comData(LocalDate.of(2021, 01, 11))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto04 = PontoBuilder
				.umRegistroPonto()
				.comID(4)
				.comData(LocalDate.of(2021, 01, 11))
				.comHora(LocalTime.of(17, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto05 = PontoBuilder
			.umRegistroPonto()
			.comID(5)
			.comData(LocalDate.of(2021, 01, 12))
			.comHora(LocalTime.of(8, 0))
			.comFuncionario(usuarioA)
			.constroi();
		
		Ponto registroPonto06 = PontoBuilder
				.umRegistroPonto()
				.comID(6)
				.comData(LocalDate.of(2021, 01, 12))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto07 = PontoBuilder
				.umRegistroPonto()
				.comID(7)
				.comData(LocalDate.of(2021, 01, 13))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		Ponto registroPonto08 = PontoBuilder
				.umRegistroPonto()
				.comID(8)
				.comData(LocalDate.of(2021, 01, 14))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto09 = PontoBuilder
				.umRegistroPonto()
				.comID(9)
				.comData(LocalDate.of(2021, 01, 14))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		Ponto registroPonto10 = PontoBuilder
				.umRegistroPonto()
				.comID(10)
				.comData(LocalDate.of(2021, 01, 14 ))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto11 = PontoBuilder
				.umRegistroPonto()
				.comID(11)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		Ponto registroPonto12 = PontoBuilder
				.umRegistroPonto()
				.comID(12)
				.comData(LocalDate.of(2021, 01, 16 ))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto13 = PontoBuilder
				.umRegistroPonto()
				.comID(13)
				.comData(LocalDate.of(2021, 01, 16 ))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto14 = PontoBuilder
				.umRegistroPonto()
				.comID(14)
				.comData(LocalDate.of(2021, 01, 23 ))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto15 = PontoBuilder
				.umRegistroPonto()
				.comID(15)
				.comData(LocalDate.of(2021, 01, 23 ))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		

		Ponto registroPonto16 = PontoBuilder
				.umRegistroPonto()
				.comID(16)
				.comData(LocalDate.of(2021, 01, 23 ))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto17 = PontoBuilder
				.umRegistroPonto()
				.comID(17)
				.comData(LocalDate.of(2021, 01, 23 ))
				.comHora(LocalTime.of(15, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		List<Ponto> registrosPonto = List.of(registroPonto01, registroPonto02, registroPonto03, registroPonto04, registroPonto05, registroPonto06, 
				registroPonto07, registroPonto08, registroPonto09, registroPonto10, registroPonto11, registroPonto12, registroPonto13, registroPonto14,
				registroPonto15, registroPonto16, registroPonto17);
		
		Mockito.when(usuarioService.findBy(Mockito.any(String.class))).thenReturn(usuarioA);
		Mockito.when(pontosRepo.findAllByFuncionarioAndDataBetween(Mockito.any(Usuario.class), 
				Mockito.any(LocalDate.class),
				Mockito.any(LocalDate.class)
				)).thenReturn(registrosPonto);
		
		BancoHoras bancoHoras = pontoService.retrieveBancoHorasDentroIntervalo(usuarioA.getCpf(),registroPonto01.getData(), registroPonto17.getData());
		
		assertEquals(bancoHoras.getHorasTrabalhadas(), 28);
		assertEquals(bancoHoras.getHorasExtrasTrabalhadas(), 2);
		assertEquals(bancoHoras.getHorasNaoTrabalhadas(), 12);
		
		
	}
	
	@Test
	void deveTerRegistroHorasTrabalhadasMesAtual() {
		Ponto registroPonto01 = PontoBuilder
				.umRegistroPonto()
				.comID(1)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(8, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto02 = PontoBuilder
				.umRegistroPonto()
				.comID(2)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto03 = PontoBuilder
				.umRegistroPonto()
				.comID(3)
				.comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(13, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto04 = PontoBuilder
				.umRegistroPonto()
				.comID(4)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(17, 0))
				.comFuncionario(usuarioA)
				.constroi();
		
		Ponto registroPonto05 = PontoBuilder
			.umRegistroPonto()
			.comID(5)
			.comData(LocalDate.of(2021, 01, 16))
			.comHora(LocalTime.of(8, 0))
			.comFuncionario(usuarioA)
			.constroi();
		
		Ponto registroPonto06 = PontoBuilder
				.umRegistroPonto()
				.comID(6)
				.comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(12, 0))
				.comFuncionario(usuarioA)
				.constroi();
			
		List<Ponto> registrosPonto = List.of(registroPonto01, registroPonto02, registroPonto03, registroPonto04, registroPonto05, registroPonto06);
		Mockito.when(usuarioService.findBy(Mockito.any(String.class))).thenReturn(usuarioA);
		Mockito.when(pontosRepo.findAllByFuncionarioAndDataBetween(Mockito.any(Usuario.class), 
				Mockito.any(LocalDate.class),
				Mockito.any(LocalDate.class)
				)).thenReturn(registrosPonto);
		
		BancoHoras bancoHoras = pontoService.retrieveHorariosBatidaNoMesAtual(usuarioA.getCpf());
		
		assertEquals(bancoHoras.getHorasTrabalhadas(), 12);
		assertEquals(bancoHoras.getHorasExtrasTrabalhadas(), 0);
		assertEquals(bancoHoras.getHorasNaoTrabalhadas(), 0);
		
		
	}

}
