package br.uece.sisdoc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.repository.SetorRepository;

@Service
public class SetorService {

	@Autowired
	SetorRepository setorRepository;
	
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
	
	public Page<Setor> findAll(Pageable pageable, Setor setor) {
		
		return setorRepository.findAll(pageable);
	}
	
}
