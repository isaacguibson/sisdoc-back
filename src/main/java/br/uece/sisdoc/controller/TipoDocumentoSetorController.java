package br.uece.sisdoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.dto.TipoDocumentoSetorDTO;
import br.uece.sisdoc.model.TipoDocumentoSetor;
import br.uece.sisdoc.service.TipoDocumentoSetorService;

@RestController
@RequestMapping("/tipo_documento_setor")
public class TipoDocumentoSetorController {

	@Autowired
	TipoDocumentoSetorService tipoDocumentoSertorService;
	
	@GetMapping
	public Page<TipoDocumentoSetor> get(Pageable pageable, TipoDocumentoSetorDTO tipoDocumentoSetor) {
		
		return tipoDocumentoSertorService.findAll(pageable, tipoDocumentoSetor);
	}
	
	@GetMapping("/{id}")
	public TipoDocumentoSetor findById(@PathVariable Long id) {
		
		return tipoDocumentoSertorService.findById(id);
	}
	
	
	@PostMapping
	public TipoDocumentoSetor create(@RequestBody TipoDocumentoSetorDTO tipoDocumentoSetor) {
		
		return tipoDocumentoSertorService.create(tipoDocumentoSetor);
	}
	
	@PutMapping
	public TipoDocumentoSetor update(@RequestBody TipoDocumentoSetorDTO tipoDocumentoSetor) {
		
		return tipoDocumentoSertorService.update(tipoDocumentoSetor);
	}
	
}
