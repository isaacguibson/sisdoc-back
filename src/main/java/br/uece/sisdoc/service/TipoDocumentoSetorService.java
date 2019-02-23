package br.uece.sisdoc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.dto.TipoDocumentoSetorDTO;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.model.TipoDocumentoSetor;
import br.uece.sisdoc.repository.SetorRepository;
import br.uece.sisdoc.repository.TipoDocumentoRepository;
import br.uece.sisdoc.repository.TipoDocumentoSetorRepository;

@Service
public class TipoDocumentoSetorService {

	@Autowired
	TipoDocumentoSetorRepository tipoDocumentoSetorRepository;
	
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	SetorRepository setorRepository;
	
	public TipoDocumentoSetor create(TipoDocumentoSetorDTO tipoDocumentoSetorDTO) {
		
		TipoDocumentoSetor tipoDocumentoSetor = dtoToTipoDocumentoSetorDTO(tipoDocumentoSetorDTO);
		
		return tipoDocumentoSetorRepository.save(tipoDocumentoSetor);
	}
	
	public TipoDocumentoSetor update(TipoDocumentoSetorDTO tipoDocumentoSetorDTO) {
		
		if(tipoDocumentoSetorDTO.getId() == null) {
			return null;
		}
		
		TipoDocumentoSetor tipoDocumentoSetor = dtoToTipoDocumentoSetorDTO(tipoDocumentoSetorDTO);
		
		tipoDocumentoSetor.setId(tipoDocumentoSetorDTO.getId());
		
		return tipoDocumentoSetorRepository.save(tipoDocumentoSetor);
	}

	public void delete(Long id) {
		
		tipoDocumentoSetorRepository.deleteById(id);
	}
	
	public TipoDocumentoSetor findById(Long id) {
		
		Optional<TipoDocumentoSetor> optionalTipoDocumentoSetor =
				tipoDocumentoSetorRepository.findById(id);
		
		if(optionalTipoDocumentoSetor.isPresent()) {
			return optionalTipoDocumentoSetor.get();
		} else {
			return null;
		}
	}
	
	public Page<TipoDocumentoSetor> findAll(Pageable pageable, TipoDocumentoSetorDTO tipoDocumentoSetorDTO) {
		
		return tipoDocumentoSetorRepository.findAll(pageable);
	}
	
	
	private TipoDocumentoSetor dtoToTipoDocumentoSetorDTO(TipoDocumentoSetorDTO tipoDocumentoSetorDTO) {
		
		TipoDocumentoSetor tipoDocumentoSetor = new TipoDocumentoSetor();
		
		Optional<Setor> optionalSetor = setorRepository.findById(tipoDocumentoSetorDTO.getSetorId());
		Setor setor = null;
		
		if(optionalSetor.isPresent()) {
			setor = optionalSetor.get();
		}
		
		tipoDocumentoSetor.setSetor(setor);
		
		Optional<TipoDocumento> optionalTipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoSetorDTO.getTipoDocumentoId());
		TipoDocumento tipoDocumento = null;
		
		if(optionalTipoDocumento.isPresent()) {
			tipoDocumento = optionalTipoDocumento.get();
		}
		
		tipoDocumentoSetor.setTipoDocumento(tipoDocumento);
		
		return tipoDocumentoSetor;
		
	}
}
