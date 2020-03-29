package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.dto.ColegiadoDTO;
import br.uece.sisdoc.model.Colegiado;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.repository.ColegiadoRepository;
import br.uece.sisdoc.specification.ColegiadoSpecification;

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
	
	public ColegiadoDTO getColegiadoDTO(Long id) {
		if(id == null) {
			return null;
		}
		
		Colegiado colegiado = findById(id);
		if(colegiado == null) {
			return null;
		}
		
		ColegiadoDTO colegiadoDTO = colegiadoToDTO(colegiado);
		
		List<UsuarioColegiado> usuariosColegiados = usuarioColegiadoService.getUsuariosColegiadosByColegiadoId(colegiado.getId());
		List<Long> membrosIds = new ArrayList<Long>();
		for(UsuarioColegiado usuarioColegiado : usuariosColegiados) {
			membrosIds.add(usuarioColegiado.getUsuario().getId());
		}
		colegiadoDTO.setMembrosIds(membrosIds);
		
		return colegiadoDTO;
	}
	
	private ColegiadoDTO colegiadoToDTO(Colegiado colegiado) {
		ColegiadoDTO colegiadoDTO = new ColegiadoDTO();
		colegiadoDTO.setId(colegiado.getId());
		colegiadoDTO.setNome(colegiado.getNome());
		colegiadoDTO.setDescricao(colegiado.getDescricao());
		return colegiadoDTO;
	}
	
	public List<Colegiado> findAll() {
		
		return colegiadoRepository.findAll();
		
	}
	
	public Page<Colegiado> findAll(Pageable pageable, Colegiado colegiado) {
		
		ColegiadoSpecification colegiadoSpec = new ColegiadoSpecification();
		
		return colegiadoRepository.findAll(Specification.where(
				colegiadoSpec.filterByName(colegiado.getNome())
		).and(colegiadoSpec.filterById(colegiado.getId())), 
				pageable);
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
	
	public Colegiado create(ColegiadoDTO colegiadoDTO) {
		Colegiado colegiado = dtoToColegiado(colegiadoDTO);
		
		colegiado = colegiadoRepository.save(colegiado);
		
		if(colegiado!=null) {
			salvarUsuariosColegiado(colegiado, colegiadoDTO.getMembrosIds());
		}
		
		return colegiado;
	}
	
	public void salvarUsuariosColegiado(Colegiado colegiado, List<Long> membrosIds) {
		usuarioColegiadoService.salvarUsuariosColegiados(colegiado, membrosIds);
	}
	
	private Colegiado dtoToColegiado(ColegiadoDTO colegiadoDTO) {
		Colegiado colegiado = new Colegiado();
		colegiado.setId(colegiadoDTO.getId());
		colegiado.setDescricao(colegiadoDTO.getDescricao());
		colegiado.setNome(colegiadoDTO.getNome().toUpperCase());
		return colegiado;
	}
	
}
