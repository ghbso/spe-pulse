package pulse.spe.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import pulse.spe.model.Usuario;
import pulse.spe.service.UsuarioService;

@Component
public class UsuarioManager implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByEmail(username);
		if (username!= null && usuario!= null) {
			return new User(usuario.getEmail(), usuario.getSenha(),
					Arrays.asList(new SimpleGrantedAuthority(usuario.getPerfil())));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}
