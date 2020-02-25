package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Colegiado;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.repository.ColegiadoRepository;

@Service
public class ColegiadoService {

	@Autowired
	ColegiadoRepository colegiadoRepository;
	
	@Autowired
	UsuarioColegiadoService usuarioColegiadoService;
	
	public Colegiado findById(Long id) {
		
		Optional<Colegiado> optionalColegiado = colegiadoRepository.findById(id);
		
		if(optionalColegiado.isPresent()) {
			return optionalColegiado.get();
		} else {
			return null;
		}
		
	}
	
	public List<Colegiado> findAll() {
		
		return colegiadoRepository.findAll();
		
	}
	
	public List<Usuario> getMembros(Long id) {
		List<UsuarioColegiado> usuariosColegiados = null;
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		usuariosColegiados = usuarioColegiadoService.getUsuariosColegiadosByColegiadoId(id);
		
		if(usuariosColegiados != null) {
			for(UsuarioColegiado usuarioColegiado : usuariosColegiados) {
				usuarios.add(usuarioColegiado.getUsuario());
			}
		}
		
		return usuarios;
	}
	
}
