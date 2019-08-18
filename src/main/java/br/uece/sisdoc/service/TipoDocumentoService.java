package br.uece.sisdoc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.repository.TipoDocumentoRepository;
import br.uece.sisdoc.specification.TipoDocumentoSpecification;

@Service
public class TipoDocumentoService {

	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	public TipoDocumento create(TipoDocumento tipoDocumento) {
		
		return tipoDocumentoRepository.save(tipoDocumento);
	}
	
	public TipoDocumento update(TipoDocumento tipoDocumento) {
		
		if(tipoDocumento.getId()==null) {
			return null;
		}
		
		return tipoDocumentoRepository.save(tipoDocumento);
	}

	public void delete(Long id) {
	
		tipoDocumentoRepository.deleteById(id);
	}

	public TipoDocumento findById(Long id) {
	
		Optional<TipoDocumento> optionalTipoDocumento =
				tipoDocumentoRepository.findById(id);
		
		if(optionalTipoDocumento.isPresent()) {
			return optionalTipoDocumento.get();
		} else {
			return null;
		}
		
	}

	public Page<TipoDocumento> findAll(Pageable pageable, TipoDocumento tipoDocumento) {
		
		TipoDocumentoSpecification tipoDocSpecification = new TipoDocumentoSpecification();
		
		return tipoDocumentoRepository.findAll(Specification.where(
				tipoDocSpecification.filterById(tipoDocumento.getId())
				).and(tipoDocSpecification.filterByName(tipoDocumento.getNome()))
				, pageable);
	}
	
	public List<TipoDocumento> listAll() {
		
		return tipoDocumentoRepository.findAll();
	}
	
}
