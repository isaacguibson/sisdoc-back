package br.uece.sisdoc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.repository.UsuarioRepository;

/*
 * Classe customizada de UserDetailsService
 * Servico usado para validacao de login de usuario
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		if (usuario == null) {
            throw new UsernameNotFoundException(email);
        }
		
		CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(usuario);
		
		return customUserPrincipal;
	}

}
