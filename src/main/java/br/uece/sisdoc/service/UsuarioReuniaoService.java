package br.uece.sisdoc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Reuniao;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioReuniao;
import br.uece.sisdoc.repository.UsuarioReuniaoRepository;
import br.uece.sisdoc.specification.UsuarioReuniaoSpecification;

@Service
public class UsuarioReuniaoService {

	@Autowired
	UsuarioReuniaoRepository usuarioReuniaoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	public List<UsuarioReuniao> getUsuariosReuniaoByReuniaoId(Long id) {
		if(id == null) {
			return null;
		}
		
		UsuarioReuniaoSpecification usuarioReuniaoSpec = new UsuarioReuniaoSpecification();
		
		return usuarioReuniaoRepository.findAll(Specification.where(
				usuarioReuniaoSpec.filterByReuniao(id)));
		
	}
	
	public List<UsuarioReuniao> getUsuariosReuniaoByUsuarioId(Long id) {
		if(id == null) {
			return null;
		}
		
		UsuarioReuniaoSpecification usuarioReuniaoSpec = new UsuarioReuniaoSpecification();
		
		return usuarioReuniaoRepository.findAll(Specification.where(
				usuarioReuniaoSpec.filterByUsuario(id)));
		
	}
	
	public boolean saveByReuniao(Reuniao reuniao, List<Long> usuariosIds) {
		
		if(usuariosIds == null) {
			return false;
		}
		
		if(usuariosIds == null || usuariosIds.size() == 0) {
			return false;
		}
		
		UsuarioReuniao usuarioReuniao = null;
		List<Usuario> usuarios = usuarioService.findByListId(usuariosIds);
		if(usuarios != null && usuarios.size() > 0) {
			int count = 0;
			for(Usuario usuario : usuarios) {
				usuarioReuniao = new UsuarioReuniao();
				usuarioReuniao.setReuniao(reuniao);
				usuarioReuniao.setUsuario(usuario);
				
				usuarioReuniaoRepository.save(usuarioReuniao);
				count++;
			}
			
			return count > 0;
		}
		
		return false;
	}
	
}
