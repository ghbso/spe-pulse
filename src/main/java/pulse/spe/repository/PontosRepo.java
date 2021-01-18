package pulse.spe.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pulse.spe.model.Usuario;
import pulse.spe.model.Ponto;
import pulse.spe.model.util.OcorrenciasSetor;
import pulse.spe.model.util.OcorrenciasUsuario;

@Repository
public interface PontosRepo extends JpaRepository<Ponto, Integer> {

	public List<Ponto> findByData(LocalDate data);

	public List<Ponto> findAllByFuncionarioAndData(Usuario funcionario, LocalDate data);

	public List<Ponto> findAllByDataBetween(LocalDate dataHoraInicio, LocalDate dataHoraFim);

	public List<Ponto> findAllByFuncionarioAndDataBetween(Usuario funcionario, LocalDate dataHoraInicio,
			LocalDate dataHoraFim);

	@Query("SELECT cast(p.data as date) " + "FROM Ponto AS p "
			+ "WHERE p.funcionario.cpf =:cpf AND p.data BETWEEN :inicio and :fim " + "GROUP BY p.data")
	public List<Date> findDatasComRegistroPontoDentroIntervalo(@Param("cpf") String cpf,
			@Param("inicio") LocalDate dataHoraInicio, @Param("fim") LocalDate dataHoraFim);

	
	@Query("SELECT new pulse.spe.model.util.OcorrenciasUsuario ( p.funcionario.cpf, "
			+ "( SELECT count(distinct p2.data) FROM Ponto AS p2 WHERE p.funcionario.cpf = p2.funcionario.cpf AND p2.data IN "
			+ "		(SELECT p3.data FROM Ponto AS p3 WHERE p3.data = p2.data GROUP BY p3.data HAVING COUNT(p3) =:qntRegistrosPraSerConsideradoOcorrencia ) ) ) "
			+ "FROM Ponto AS p "
			+ "WHERE p.data >=:inicio AND p.data <=:fim "
			+ "GROUP BY p.funcionario.cpf"
			)
	public List<OcorrenciasUsuario> countTotalOcorrenciasPorUsuarioDentroIntevalo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim,
			@Param("qntRegistrosPraSerConsideradoOcorrencia") long qntRegistrosPraSerConsideradoOcorrencia);


	
	@Query("SELECT p "
			+ "FROM Ponto AS p "
			+ "WHERE p.funcionario.cpf =:cpf AND p.data BETWEEN :inicio and :fim AND p.data IN "
			+ "		(SELECT p2.data FROM Ponto AS p2 WHERE p2.data = p.data GROUP BY p2.data HAVING COUNT(p2.data) =:qntRegistrosPraSerConsideradoOcorrencia)"
			)
	public List<Ponto> findBatidasPontoDeTodosOsDiasEmQueHouveOcorrencia(
			@Param("cpf") String cpf,
			@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim,
			@Param("qntRegistrosPraSerConsideradoOcorrencia") long qntRegistrosPraSerConsideradoOcorrencia
			);
	
	
	@Query("SELECT  new pulse.spe.model.util.OcorrenciasSetor( p.funcionario.setor, "
			+ "( SELECT count(p2.funcionario.cpf) FROM Ponto AS p2 WHERE p.funcionario.setor = p2.funcionario.setor AND p2.data IN "
			+ "		(SELECT p3.data FROM Ponto AS p3 WHERE p3.data = p2.data AND p3.funcionario.cpf=p2.funcionario.cpf "
			+ "			GROUP BY p3.data HAVING COUNT(p3.data) =:qntRegistrosPraSerConsideradoOcorrencia ) ) )"
			+ "	  "
			+ "FROM Ponto AS p " 
			+ "WHERE extract(dow from p.data) =:diaSemana AND p.data BETWEEN :inicio and :fim "
			+ "GROUP BY p.funcionario.setor")
	public List<OcorrenciasSetor> countTotalOcorrenciasDentroIntervaloBySetorAndDiaSemana(
			@Param("inicio") LocalDate dataHoraInicio, @Param("fim") LocalDate dataHoraFim,
			@Param("qntRegistrosPraSerConsideradoOcorrencia") long qntRegistrosPraSerConsideradoOcorrencia,
			@Param("diaSemana") Integer diaSemana
			);

	@Query("SELECT  new pulse.spe.model.util.OcorrenciasUsuario( p.funcionario.cpf, ( SELECT count(p2.funcionario.cpf) FROM Ponto AS p2 WHERE p.funcionario.cpf = p2.funcionario.cpf AND p2.data IN "
			+ "	(SELECT p3.data FROM Ponto AS p3 WHERE p3.data = p2.data AND p3.funcionario.cpf=p2.funcionario.cpf "
			+ "			GROUP BY p3.data HAVING COUNT(p3.data) =:qntRegistrosPraSerConsideradoOcorrencia ) ) )"
			+ "	  "
			+ "FROM Ponto AS p " 
			+ "WHERE extract(dow from p.data) =:diaSemana AND p.data BETWEEN :inicio and :fim "
			+ "GROUP BY p.funcionario.cpf")
	public List<OcorrenciasUsuario> countTotalOcorrenciasDentroIntervaloByFuncionarioAndDiaSemana(
			@Param("inicio") LocalDate dataHoraInicio, @Param("fim") LocalDate dataHoraFim,
			@Param("qntRegistrosPraSerConsideradoOcorrencia") long qntRegistrosPraSerConsideradoOcorrencia,
			@Param("diaSemana") Integer diaSemana);

}
