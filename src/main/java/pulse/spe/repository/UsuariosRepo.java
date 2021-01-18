package pulse.spe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pulse.spe.model.Usuario;

@Repository
public interface UsuariosRepo extends JpaRepository<Usuario, String>{

	public Usuario findByCpf(String cpf);

	public Usuario findByEmail(String email);

}
