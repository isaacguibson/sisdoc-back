package br.uece.sisdoc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Reuniao;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.model.UsuarioReuniao;
import br.uece.sisdoc.repository.UsuarioReuniaoRepository;
import br.uece.sisdoc.specification.UsuarioReuniaoSpecification;

@Service
public class UsuarioReuniaoService {

	@Autowired
	UsuarioReuniaoRepository usuarioReuniaoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioColegiadoService usuarioColegiadoService;
	
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
	
	public boolean excluirUsuariosReuniao(Reuniao reuniao) {
		List<UsuarioReuniao> usuariosReuniao = getUsuariosReuniaoByReuniaoId(reuniao.getId());
		
		if(usuariosReuniao == null || usuariosReuniao.size() == 0) {
			return false;
		}
		
		int count = 0;
		for(UsuarioReuniao usuarioReuniao : usuariosReuniao) {
			usuarioReuniaoRepository.delete(usuarioReuniao);
			count++;
		}
		
		return count > 0;
	}
	
	public boolean saveForWholeColegiado(Reuniao reuniao) {
		
		if(reuniao == null) {
			return false;
		}
		
		if(reuniao.getColegiado() == null || reuniao.getColegiado().getId() == null) {
			return false;
		}
		
		List<UsuarioColegiado> usuariosColegiado = usuarioColegiadoService.getUsuariosColegiadosByColegiadoId(reuniao.getColegiado().getId());
	
		if(usuariosColegiado == null || usuariosColegiado.size() == 0) {
			return false;
		}
		
		UsuarioReuniao usuarioReuniao = null;
		int count = 0;
		for(UsuarioColegiado usuarioColegiado : usuariosColegiado) {
			usuarioReuniao = new UsuarioReuniao();
			usuarioReuniao.setReuniao(reuniao);
			usuarioReuniao.setUsuario(usuarioColegiado.getUsuario());
			usuarioReuniaoRepository.save(usuarioReuniao);
			count++;
		}
		
		return count > 0;
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
