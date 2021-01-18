package pulse.spe.service;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pulse.spe.builder.PontoBuilder;
import pulse.spe.model.Usuario;
import pulse.spe.model.Ponto;
import pulse.spe.model.util.BancoHoras;
import pulse.spe.model.util.OcorrenciasSetor;
import pulse.spe.model.util.OcorrenciasUsuario;
import pulse.spe.repository.PontosRepo;

@Service
public class PontoService {

	@Autowired
	private PontosRepo pontoRepo;

	@Autowired
	private UsuarioService usuarioService;

	private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

	private int qntRegistrosPraSerConsideradoOcorrencia=1;

	public Ponto createPontoByFuncionario(Usuario funcionario) {
		Ponto ponto = PontoBuilder.umRegistroPonto().comData(LocalDate.now()).comHora(LocalTime.now())
				.comFuncionario(funcionario).constroi();
		return ponto;
	}

	public Ponto registraBatidaDePonto(Ponto ponto) {
		return pontoRepo.save(ponto);
	}

	public List<Ponto> findRegistroPontoDentroIntervalo(LocalDate inicio, LocalDate fim) {
		return pontoRepo.findAllByDataBetween(inicio, fim);
	}

	public List<Ponto> findRegistroPontoByCPFAndIntervaloDeData(Usuario funcionario, LocalDate inicio,
			LocalDate fim) {
		return pontoRepo.findAllByFuncionarioAndDataBetween(funcionario, inicio, fim);
	}

	public List<String> retrieveHorariosBatidaDePontoPorUsuarioEData(String cpf, LocalDate data) {
		Usuario usuario = usuarioService.findBy(cpf);
		List<Ponto> pontosRegistrados = this.pontoRepo.findAllByFuncionarioAndData(usuario, data);
		List<String> horarios = new ArrayList<>();

		pontosRegistrados.forEach(ponto -> horarios
				.add(formatterDate.format(ponto.getData()) + " " + formatterHour.format(ponto.getHora())));

		return horarios;
	}

	public BancoHoras retrieveBancoHorasDentroIntervalo(String cpf, LocalDate inicio, LocalDate fim) {
		Usuario funcionario = usuarioService.findBy(cpf);
		List<Ponto> registroPontos = findRegistroPontoByCPFAndIntervaloDeData(funcionario, inicio, fim);

		Map<String, List<Ponto>> registroDePontosPorDia = this.agrupaRegistroDePontosPorData(registroPontos);

		Map<String, BancoHoras> bancoDeHorasPorDia = this.computaBancoDeHoras(registroDePontosPorDia);

		BancoHoras bancoHorasGlobal = computaBancoDeHorasGlobal(bancoDeHorasPorDia);
		return bancoHorasGlobal;
	}

	public BancoHoras retrieveHorariosBatidaNoMesAtual(String cpf) {
		LocalDate inicio = LocalDate.now().withDayOfMonth(1);
		LocalDate fim = LocalDate.now().with(lastDayOfMonth());
		return retrieveBancoHorasDentroIntervalo(cpf, inicio, fim);
	}

	public BancoHoras getBancoHorasDentroIntervalo(LocalDate inicio, LocalDate fim) {
		List<Ponto> registroPontos = findRegistroPontoDentroIntervalo(inicio, fim);

		Map<String, List<Ponto>> registroDePontosPorDia = this.agrupaRegistroDePontosPorData(registroPontos);

		agrupaRegistroDePontosPorData(registroPontos);
		Map<String, BancoHoras> bancoDeHorasPorDia = this.computaBancoDeHoras(registroDePontosPorDia);
		BancoHoras bancoHorasGlobal = computaBancoDeHorasGlobal(bancoDeHorasPorDia);
		return bancoHorasGlobal;
	}

	public List<OcorrenciasUsuario> retrieveOcorrenciasEmpresaDentroIntervalo(LocalDate inicio, LocalDate fim) {
		return pontoRepo.countTotalOcorrenciasPorUsuarioDentroIntevalo(inicio, fim, qntRegistrosPraSerConsideradoOcorrencia);
	}

	public List<List<? extends Object>> retrieveOcorrenciasConsolidadasNoPeriodo(LocalDate inicio, LocalDate fim,
			Integer diaSemana) {
		List<OcorrenciasUsuario> ocorrenciasPorUsuario = pontoRepo
				.countTotalOcorrenciasDentroIntervaloByFuncionarioAndDiaSemana(inicio, fim, qntRegistrosPraSerConsideradoOcorrencia ,diaSemana);
		List<OcorrenciasSetor> ocorrenciasPorSetor = pontoRepo
				.countTotalOcorrenciasDentroIntervaloBySetorAndDiaSemana(inicio, fim, qntRegistrosPraSerConsideradoOcorrencia ,diaSemana);
		
		return Arrays.asList(ocorrenciasPorUsuario, ocorrenciasPorSetor);
	}

	public List<Ponto> retrieveOcorrenciasUsuarioDentroIntervalo(String cpf, LocalDate inicio,
			LocalDate fim) {
		
	    return  pontoRepo.findBatidasPontoDeTodosOsDiasEmQueHouveOcorrencia(cpf, inicio, fim, qntRegistrosPraSerConsideradoOcorrencia);
	}

	private Map<String, List<Ponto>> agrupaRegistroDePontosPorData(List<Ponto> registroPontos) {

		Map<String, List<Ponto>> registroDePontosPorDia = new HashMap<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		LocalDate dataAtual = null;
		for (Ponto ponto : registroPontos) {
			LocalDate data = ponto.getData();
			String date = formatter.format(data);

			if (data != dataAtual) {
				dataAtual = data;
				if (!registroDePontosPorDia.containsKey(date)) {
					registroDePontosPorDia.put(date, new ArrayList<>());
				}
			}
			List<Ponto> registros = registroDePontosPorDia.get(date);
			registros.add(ponto);
			registroDePontosPorDia.put(date, registros);
		}
		return registroDePontosPorDia;
	}

	private Map<String, BancoHoras> computaBancoDeHoras(Map<String, List<Ponto>> registroDePontosPorDia) {
		Map<String, BancoHoras> bancoHorasPorDia = new HashMap<String, BancoHoras>();

		int cargaHorariaMinimaSabado = 4;
		int cargaHorariaMaximaOutrosDias = 8;
		int cargaHorariaMaximaHoraExtra = 2;

		Set<Entry<String, List<Ponto>>> entrySet = registroDePontosPorDia.entrySet();
		for (Entry<String, List<Ponto>> entry : entrySet) {
			BancoHoras bancoHorasDiario = new BancoHoras();
			int qntHorasTrabalhadaDia = 0;
			int qntHorasNaoTrabalhadaDia = 0;
			int qntHorasExtraDia = 0;

			String data = entry.getKey();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate instance = LocalDate.parse(data, formatter);

			int dayOfWeek = instance.getDayOfWeek().getValue();
			int cargaHorariaMininaDia = dayOfWeek == 6 ? cargaHorariaMinimaSabado : cargaHorariaMaximaOutrosDias;

			List<Ponto> registrosPonto = entry.getValue();
//			System.out.println(data + " " + registrosPonto.size());
			if (registrosPonto.size() <= 1) {
				qntHorasNaoTrabalhadaDia = cargaHorariaMininaDia;
			} else {

				for (int i = 1; i < registrosPonto.size(); i++) {
					if (i == 4) {
						break;
					}

//					if (registroDePontosPorDia.size() == 3) {
//						System.out.println(data);
//					}
//					
					if ((i + 1) % 2 == 0) {
						Long diffHours = HOURS.between(registrosPonto.get(i - 1).getHora(),
								registrosPonto.get(i).getHora());
						qntHorasTrabalhadaDia += diffHours;

					} else {
						if (i == 2 && registrosPonto.size() == 3) {
							qntHorasTrabalhadaDia = cargaHorariaMininaDia;
						}
					}
				}

				if (qntHorasTrabalhadaDia > cargaHorariaMininaDia) {
					qntHorasExtraDia = qntHorasTrabalhadaDia - cargaHorariaMininaDia;
					if (qntHorasExtraDia > cargaHorariaMaximaHoraExtra) {
						qntHorasExtraDia = cargaHorariaMaximaHoraExtra;
					}
					qntHorasTrabalhadaDia = cargaHorariaMininaDia;

				} else if (qntHorasTrabalhadaDia < cargaHorariaMininaDia) {
					qntHorasNaoTrabalhadaDia = cargaHorariaMininaDia - qntHorasTrabalhadaDia;

				}

			}

			bancoHorasDiario.setHorasTrabalhadas(qntHorasTrabalhadaDia);
			bancoHorasDiario.setHorasNaoTrabalhadas(qntHorasNaoTrabalhadaDia);
			bancoHorasDiario.setHorasExtrasTrabalhadas(qntHorasExtraDia);
			bancoHorasPorDia.put(data, bancoHorasDiario);

		}
		;
		return bancoHorasPorDia;
	}

	private BancoHoras computaBancoDeHorasGlobal(Map<String, BancoHoras> bancoDeHorasPorDia) {
		BancoHoras bancoHorasGlobal = new BancoHoras();
		bancoDeHorasPorDia.entrySet().forEach(entry -> {
			BancoHoras bancoHorasDiario = entry.getValue();
			bancoHorasGlobal.addHorasTrabalhada(bancoHorasDiario.getHorasTrabalhadas());
			bancoHorasGlobal.addHorasNaoTrabalhadas(bancoHorasDiario.getHorasNaoTrabalhadas());
			bancoHorasGlobal.addHorasExtrasTrabalhadas(bancoHorasDiario.getHorasExtrasTrabalhadas());
			// System.out.println(entry.getKey() + " " +
			// bancoHorasDiario.getHorasNaoTrabalhadas() + " " +
			// bancoHorasGlobal.getHorasNaoTrabalhadas());

		});
		return bancoHorasGlobal;
	}

}
