package br.uece.sisdoc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Reuniao;
import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.repository.UsuarioColegiadoRepository;
import br.uece.sisdoc.specification.UsuarioColegiadoSpecification;

@Service
public class UsuarioColegiadoService {
	
	@Autowired
	UsuarioColegiadoRepository usuarioColegiadoRepository;

	public List<UsuarioColegiado> getUsuariosColegiadosByColegiadoId(Long id) {
		if(id == null) {
			return null;
		}
		
		UsuarioColegiadoSpecification usuarioColSpec = new UsuarioColegiadoSpecification();
		
		return usuarioColegiadoRepository.findAll(Specification.where(
				usuarioColSpec.filterByColegiado(id)));
		
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
