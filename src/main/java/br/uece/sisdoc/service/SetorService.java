package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.repository.SetorRepository;
import br.uece.sisdoc.repository.UsuarioRepository;
import br.uece.sisdoc.specification.SetorSpecification;

@Service
public class SetorService {

	@Autowired
	SetorRepository setorRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public Setor create(Setor setor) {
		
		return setorRepository.save(setor);
	}
	
	public Setor update(Setor setor) {
		
		if(setor.getId() == null) {
			return null;
		}
		
		return setorRepository.save(setor);
	}
	
	public void delete(Long id) {
		
		setorRepository.deleteById(id);
	}
	
	public Setor findById(Long id) {
		
		Optional<Setor> optionalSetor = setorRepository.findById(id);
		
		if(optionalSetor.isPresent()) {
			return optionalSetor.get();
		} else {
			return null;
		}
	}
	
	public Boolean existemUsuariosSetor(Long setorId) {
		Optional<Setor> optSetor = setorRepository.findById(setorId);
		if(optSetor.isPresent()) {
			List<Usuario> usuarios = usuarioRepository.getPrincipalUsersFromSetor(setorId);
			if(usuarios == null || usuarios.size() == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public Page<Setor> findAll(Pageable pageable, Setor setor) {
		
		SetorSpecification setorSpecification = new SetorSpecification();
		
		return setorRepository.findAll(Specification.where(
				setorSpecification.filterByName(setor.getNome())
		).and(setorSpecification.filterById(setor.getId())), 
				pageable);
	}
	
	public List<Setor> setorForList(){
		List<Setor> setores = new ArrayList<Setor>();
		
		setores = setorRepository.findAll();
		
		return setores;
		
	}
	
	public List<Setor> findAll() {
		
		return setorRepository.findAll();
	}
	
}
