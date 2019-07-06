package br.uece.sisdoc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.repository.TipoDocumentoRepository;

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
		
		return tipoDocumentoRepository.findAll(pageable);
	}
	
	public List<TipoDocumento> listAll() {
		
		return tipoDocumentoRepository.findAll();
	}
	
}
