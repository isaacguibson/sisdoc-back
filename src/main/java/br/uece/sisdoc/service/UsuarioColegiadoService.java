package br.uece.sisdoc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Colegiado;
import br.uece.sisdoc.model.Reuniao;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.repository.UsuarioColegiadoRepository;
import br.uece.sisdoc.specification.UsuarioColegiadoSpecification;

@Service
public class UsuarioColegiadoService {
	
	@Autowired
	UsuarioColegiadoRepository usuarioColegiadoRepository;
	
	@Autowired
	UsuarioService usuarioService;

	public List<UsuarioColegiado> getUsuariosColegiadosByColegiadoId(Long id) {
		if(id == null) {
			return null;
		}
		
		UsuarioColegiadoSpecification usuarioColSpec = new UsuarioColegiadoSpecification();
		
		return usuarioColegiadoRepository.findAll(Specification.where(
				usuarioColSpec.filterByColegiado(id)));
		
	}
	
	public void salvarUsuariosColegiados(Colegiado colegiado, List<Long> membrosIds) {
		
		UsuarioColegiado usuarioColegiado = null;
		Usuario membro = null;
		for (Long membroId : membrosIds) {
			usuarioColegiado = new UsuarioColegiado();
			usuarioColegiado.setColegiado(colegiado);
			
			membro = usuarioService.findById(membroId);
			
			if(membro!=null) {
				usuarioColegiado.setUsuario(membro);
				usuarioColegiadoRepository.save(usuarioColegiado);
			}
			
		}
		
	}
	
	public void removerUsuariosColegiados(Colegiado colegiado) {
		
		List<UsuarioColegiado> usuariosColegiados = getUsuariosColegiadosByColegiadoId(colegiado.getId());
		
		for (UsuarioColegiado usuarioColegiado : usuariosColegiados) {
			usuarioColegiadoRepository.delete(usuarioColegiado);
		}
		
	}
	
	public List<UsuarioColegiado> getUsuariosColegiadosByUsuarioId(Long id) {
		if(id == null) {
			return null;
		}
		
		UsuarioColegiadoSpecification usuarioColSpec = new UsuarioColegiadoSpecification();
		
		return usuarioColegiadoRepository.findAll(Specification.where(
				usuarioColSpec.filterByUsuario(id)));
		
	}
	
}
