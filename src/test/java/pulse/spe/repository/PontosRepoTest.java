package pulse.spe.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import pulse.spe.builder.PontoBuilder;
import pulse.spe.builder.UsuarioBuilder;
import pulse.spe.model.Usuario;
import pulse.spe.model.Ponto;
import pulse.spe.model.util.OcorrenciasSetor;
import pulse.spe.model.util.OcorrenciasUsuario;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class PontosRepoTest {

	@Autowired
	private UsuariosRepo usuariosRepo;

	@Autowired
	private PontosRepo pontosRepo;

	private long qntRegistrosPraSerConsideradoOcorrencia = 1;

	private Usuario usuarioA;

	private Usuario usuarioB;

	@BeforeEach
	void steup() {
		usuarioA = UsuarioBuilder.umUsuario().comCpf("872.112.321-10").comNome("Usuario").comSobrenome("A")
				.comSetor("RH").comFoto("some photo".getBytes()).comEmail("usuarioa@email.com.br")
				.comSenha("usuarioasenha").constroi();

		usuarioB = UsuarioBuilder.umUsuario().comCpf("457.872.132.91").comNome("Usuario").comSobrenome("B")
				.comSetor("ADM").comFoto("some photo".getBytes()).comEmail("usuariog@email.com.br")
				.comSenha("usuarioasenha").constroi();
		
		usuariosRepo.save(usuarioA);
		usuariosRepo.save(usuarioB);

	}

	@Test
	void deveRetornarTotalDeOcorrenciasParaTodosUsuarios() {

		Ponto registroPonto01 = PontoBuilder.umRegistroPonto().comID(1).comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto02 = PontoBuilder.umRegistroPonto().comID(2).comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto03 = PontoBuilder.umRegistroPonto().comID(3).comData(LocalDate.of(2021, 01, 16))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto04 = PontoBuilder.umRegistroPonto().comID(4).comData(LocalDate.of(2021, 01, 17))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioB).constroi();

		Ponto registroPonto05 = PontoBuilder.umRegistroPonto().comID(5).comData(LocalDate.of(2021, 01, 17))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioB).constroi();

		pontosRepo.save(registroPonto01);
		pontosRepo.save(registroPonto02);
		pontosRepo.save(registroPonto03);
		pontosRepo.save(registroPonto04);
		pontosRepo.save(registroPonto05);

		List<OcorrenciasUsuario> ocorrencias = pontosRepo.countTotalOcorrenciasPorUsuarioDentroIntevalo(
				registroPonto01.getData(), registroPonto05.getData(), qntRegistrosPraSerConsideradoOcorrencia);

		assertEquals(ocorrencias.size(), 2);

		assertEquals(ocorrencias.get(1).getCpf(), usuarioA.getCpf());
		assertEquals(ocorrencias.get(1).getQntOcorrencias(), 1);

		assertEquals(ocorrencias.get(0).getCpf(), usuarioB.getCpf());
		assertEquals(ocorrencias.get(0).getQntOcorrencias(), 0);

	}

	@Test
	void deveRetornarTodasOcorrenciasParaUmUsuario() {

		Ponto registroPonto01 = PontoBuilder.umRegistroPonto().comID(1).comData(LocalDate.of(2021, 01, 11))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto02 = PontoBuilder.umRegistroPonto().comID(2).comData(LocalDate.of(2021, 01, 11))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto03 = PontoBuilder.umRegistroPonto().comID(3).comData(LocalDate.of(2021, 01, 13))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto04 = PontoBuilder.umRegistroPonto().comID(4).comData(LocalDate.of(2021, 01, 14))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto05 = PontoBuilder.umRegistroPonto().comID(5).comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		pontosRepo.save(registroPonto01);
		pontosRepo.save(registroPonto02);
		pontosRepo.save(registroPonto03);
		pontosRepo.save(registroPonto04);
		pontosRepo.save(registroPonto05);

		List<Ponto> ocorrencias = pontosRepo.findBatidasPontoDeTodosOsDiasEmQueHouveOcorrencia(usuarioA.getCpf(),
				registroPonto01.getData(), registroPonto05.getData(), qntRegistrosPraSerConsideradoOcorrencia);

		assertEquals(ocorrencias.size(), 3);

		for (Ponto ponto : ocorrencias) {
			assertEquals(ponto.getFuncionario().getCpf(), usuarioA.getCpf());

		}

	}

	@Test
	void deveRetornarRelatorioOcorrenciasPorDiaDaSemanaESetor() {

		Ponto registroPonto01 = PontoBuilder.umRegistroPonto().comID(1).comData(LocalDate.of(2021, 01, 01))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto02 = PontoBuilder.umRegistroPonto().comID(2).comData(LocalDate.of(2021, 01, 01))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto03 = PontoBuilder.umRegistroPonto().comID(3).comData(LocalDate.of(2021, 01, 8))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto04 = PontoBuilder.umRegistroPonto().comID(4).comData(LocalDate.of(2021, 01, 8))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioB).constroi();

		Ponto registroPonto05 = PontoBuilder.umRegistroPonto().comID(5).comData(LocalDate.of(2021, 01, 8))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioB).constroi();

		pontosRepo.save(registroPonto01);
		pontosRepo.save(registroPonto02);
		pontosRepo.save(registroPonto03);
		pontosRepo.save(registroPonto04);
		pontosRepo.save(registroPonto05);

		List<OcorrenciasSetor> ocorrenciasBySetor = pontosRepo.countTotalOcorrenciasDentroIntervaloBySetorAndDiaSemana(
				registroPonto01.getData(), 
				registroPonto05.getData(), 
				qntRegistrosPraSerConsideradoOcorrencia,
				registroPonto01.getData().getDayOfWeek().getValue()+1
				);
		
	
		
		assertEquals(ocorrenciasBySetor.size(), 2);

		assertEquals(ocorrenciasBySetor.get(1).getSetor(), usuarioA.getSetor());
		assertEquals(ocorrenciasBySetor.get(1).getQntOcorrencias(), 1);

		assertEquals(ocorrenciasBySetor.get(0).getSetor(), usuarioB.getSetor());
		assertEquals(ocorrenciasBySetor.get(0).getQntOcorrencias(), 0);

	}

	@Test
	void deveRetornarRelatorioOcorrenciasPorDiaDaSemanaEFuncionario() {

		Ponto registroPonto01 = PontoBuilder.umRegistroPonto().comID(1).comData(LocalDate.of(2021, 01, 01))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto02 = PontoBuilder.umRegistroPonto().comID(2).comData(LocalDate.of(2021, 01, 01))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto03 = PontoBuilder.umRegistroPonto().comID(3).comData(LocalDate.of(2021, 01, 8))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioA).constroi();

		Ponto registroPonto04 = PontoBuilder.umRegistroPonto().comID(4).comData(LocalDate.of(2021, 01, 8))
				.comHora(LocalTime.of(8, 0)).comFuncionario(usuarioB).constroi();

		Ponto registroPonto05 = PontoBuilder.umRegistroPonto().comID(5).comData(LocalDate.of(2021, 01, 15))
				.comHora(LocalTime.of(12, 0)).comFuncionario(usuarioB).constroi();

		pontosRepo.save(registroPonto01);
		pontosRepo.save(registroPonto02);
		pontosRepo.save(registroPonto03);
		pontosRepo.save(registroPonto04);
		pontosRepo.save(registroPonto05);

		List<OcorrenciasUsuario> ocorrenciasBySetor = pontosRepo.countTotalOcorrenciasDentroIntervaloByFuncionarioAndDiaSemana(
				registroPonto01.getData(), 
				registroPonto05.getData(), 
				qntRegistrosPraSerConsideradoOcorrencia,
				registroPonto01.getData().getDayOfWeek().getValue()+1
				);
		
	
		
		assertEquals(ocorrenciasBySetor.size(), 2);

		assertEquals(ocorrenciasBySetor.get(1).getCpf(), usuarioA.getCpf());
		assertEquals(ocorrenciasBySetor.get(1).getQntOcorrencias(), 1);

		assertEquals(ocorrenciasBySetor.get(0).getCpf(), usuarioB.getCpf());
		assertEquals(ocorrenciasBySetor.get(0).getQntOcorrencias(), 2);

	}
}
