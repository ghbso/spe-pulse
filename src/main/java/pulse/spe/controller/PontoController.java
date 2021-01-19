package pulse.spe.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import pulse.spe.model.Usuario;
import pulse.spe.model.Ponto;
import pulse.spe.model.util.BancoHoras;
import pulse.spe.model.util.OcorrenciasUsuario;
import pulse.spe.service.PontoService;
import pulse.spe.service.UsuarioService;

@RestController
@RequestMapping("/frequencia")
public class PontoController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PontoService pontoService;

	@Operation(summary = "Registra batida de ponto do usuário", 
			description = "Registra a batida de ponto do usuário", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro de ponto realizado!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
	})
	@PostMapping
	@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
	public ResponseEntity<?> addRegistroPonto(@RequestParam("cpf") String cpf) {
		if (!checkSeTemPermissao(cpf)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado!");
		}

		Usuario funcionario = usuarioService.findBy(cpf);
		Ponto ponto = pontoService.createPontoByFuncionario(funcionario);
		ponto = pontoService.registraBatidaDePonto(ponto);
		return ResponseEntity.ok(ponto);
	}

	@Operation(summary = "Retorna registros de frequência de um dado usuário em um dado dia", 
			description = "Retorna a listagem com registros de frequência de um dado usuário em um dado dia ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@GetMapping
	@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
	public ResponseEntity<?> getRegistrosPontoEmUmaDataEspecifica(@RequestParam("cpf") String cpf,
			@RequestParam("data") String data) {

		if (!checkSeTemPermissao(cpf)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado!");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate inicio = LocalDate.parse(data, formatter);

		List<String> horariosBatidaDePonto = pontoService.retrieveHorariosBatidaDePontoPorUsuarioEData(cpf, inicio);
		return ResponseEntity.ok(horariosBatidaDePonto);

	}

	@Operation(summary = "Retorna registros de frequência de um dado usuário no dia atual", 
			description = "Retorna a listagem com registros de frequência de um dado usuário no dia atual ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
	@GetMapping(value = "/hoje")
	public ResponseEntity<?> getRegistroPontoHoje(@RequestParam("cpf") String cpf) {
		if (!checkSeTemPermissao(cpf)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado!");
		}

		LocalDate inicio = LocalDate.now();

		List<String> horariosBatidaDePonto = pontoService.retrieveHorariosBatidaDePontoPorUsuarioEData(cpf, inicio);
		return ResponseEntity.ok(horariosBatidaDePonto);

	}

	@Operation(summary = "Retorna registros de frequência de um dado usuário em um dado intervalo", 
			description = "Retorna a listagem com registros de frequência de um dado usuário em um dado intervalo ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
	@GetMapping(value = "/historico")
	public ResponseEntity<?> getRegistrosPontoDeUmUsuarioEmUmIntervalo(@RequestParam("cpf") String cpf,
			@RequestParam("inicio") String dInicio, @RequestParam("fim") String dFim) {

		if (!checkSeTemPermissao(cpf)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado!");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate inicio = LocalDate.parse(dInicio, formatter);
		LocalDate fim = LocalDate.parse(dFim, formatter);

		BancoHoras bancoHoras = pontoService.retrieveBancoHorasDentroIntervalo(cpf, inicio, fim);
		return ResponseEntity.ok(bancoHoras);

	}

	@Operation(summary = "Retorna registros de frequência de um dado usuário no mês atual", 
			description = "Retorna a listagem com registros de frequência de um dado usuário no mês atual ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
	@GetMapping(value = "/historico/mes")
	public ResponseEntity<?> getRegistrosPontoDeUmUsuarioNoMesAtual(@RequestParam("cpf") String cpf) {

		if (!checkSeTemPermissao(cpf)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado!");
		}

		BancoHoras bancoHoras = pontoService.retrieveHorariosBatidaNoMesAtual(cpf);
		return ResponseEntity.ok(bancoHoras);
	}

	@Operation(summary = "Retorna registros o total de ocorrências para cada usuário dentro de um intervalo", 
			description = "Retorna a listagem com total de ocorrências de um cada usuário dentro de um intervalo ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/historico/ocorrencias")
	public ResponseEntity<List<OcorrenciasUsuario>> getTotalOcorrenciasDeCasaUsuarioDentroDeUmIntervalo(
			@RequestParam("inicio") String dInicio, @RequestParam("fim") String dFim) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate inicio = LocalDate.parse(dInicio, formatter);
		LocalDate fim = LocalDate.parse(dFim, formatter);

		List<OcorrenciasUsuario> historicoEmpresaDentroIntervalo = pontoService
				.retrieveOcorrenciasEmpresaDentroIntervalo(inicio, fim);
		return ResponseEntity.ok(historicoEmpresaDentroIntervalo);

	}

	@Operation(summary = "Retorna registros das ocorrências de um dado usuário dentro de um intervalo", 
			description = "Retorna a listagem das ocorrências de um dado usuário dentro de um intervalo ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@GetMapping(value = "/historico/ocorrencias/usuario")
	@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
	public ResponseEntity<?> getOcorrenciasDeUmUsuarioDentroDeUmIntervalo(@RequestParam("cpf") String cpf,
			@RequestParam("inicio") String dInicio, @RequestParam("fim") String dFim) {

		if (!checkSeTemPermissao(cpf)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado!");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate inicio = LocalDate.parse(dInicio, formatter);
		LocalDate fim = LocalDate.parse(dFim, formatter);

		List<Ponto> list = pontoService.retrieveOcorrenciasUsuarioDentroIntervalo(cpf, inicio, fim);
		return ResponseEntity.ok(list);

	}

	@Operation(summary = "Retorna relatório de ocorrências consolidade de um um intervalo", 
			description = "Retorna a listagem de ocorrências consolidadas de um intervalo ", 
			tags = {"Frequência" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listagem com registros retornada!"),
			@ApiResponse(responseCode = "401", description = "Acesso não autorizado!"), 
			@ApiResponse(responseCode = "403", description = "Acesso negado!")
			
	})
	@GetMapping(value = "/historico/consolidado")
	@PreAuthorize("hasAuthority('MANAGER')")
	public ResponseEntity<List<List<? extends Object>>> getRegistroConsolidadoDeUmIntervalo(
			@RequestParam("inicio") String dInicio, @RequestParam("fim") String dFim,
			@RequestParam("diaSemana") Integer diaSemana) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate inicio = LocalDate.parse(dInicio, formatter);
		LocalDate fim = LocalDate.parse(dFim, formatter);

		List<List<? extends Object>> relatorio = pontoService.retrieveOcorrenciasConsolidadasNoPeriodo(inicio, fim,
				diaSemana);
		return ResponseEntity.ok(relatorio);

	}

	private boolean checkSeTemPermissao(String cpf) {
		boolean hasPermissao = true;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Usuario usuario = usuarioService.findByEmail(username);
		if (!usuario.getPerfil().equalsIgnoreCase("ADMIN")) {
			if (!cpf.equalsIgnoreCase(usuario.getCpf())) {
				hasPermissao = false;
			}
		}
		return hasPermissao;
	}
}
