package br.uece.sisdoc.configuration;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.uece.sisdoc.model.Usuario;

/*
 * Usuario usado para login e controle de conta de usuario 
 */
public class CustomUserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Usuario do sistema
	private Usuario usuario;
	
	//variaveis de controle de conta
	private boolean accountNonExpired; //false se conta expirada, true caso contrario
	private boolean accountNonLocked; //false se bloqueada, true caso contrario
	private boolean credentialsNonExpired; //false se credenciais expiradas, true caso contrario
	private boolean userEnabled; //true se habilitado, false caso contrario
	
	public CustomUserPrincipal(Usuario usuario) {
		super();
		this.usuario = usuario;
		
		//Inicializando variaveis
		//Devem ser validadas no CustomUserDetailService loadByUsername...
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.userEnabled = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		
		if(this.getUsuario() == null) {
			return null;
		} else {
			return this.getUsuario().getSenha();
		}
		
		
	}

	@Override
	public String getUsername() {
		
		if(this.getUsuario() == null) {
			return null;
		} else {
			return this.getUsuario().getEmail();
		}
		
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.userEnabled;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
