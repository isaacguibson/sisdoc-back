package br.uece.sisdoc.controller;

import java.util.List;

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

import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.service.TipoDocumentoService;

@RestController
@RequestMapping("/tipo_documento")
public class TipoDocumentoController {

	@Autowired
	TipoDocumentoService tipoDocumentoService;
	
	@GetMapping
	public Page<TipoDocumento> get(Pageable pageable, TipoDocumento tipoDocumento) {
		
		return tipoDocumentoService.findAll(pageable, tipoDocumento);
		
	}
	
	@GetMapping("/listAll")
	public List<TipoDocumento> get() {
		
		return tipoDocumentoService.listAll();
		
	}
	
	@GetMapping("/{id}")
	public TipoDocumento get(@PathVariable Long id) {
		
		return tipoDocumentoService.findById(id);
		
	}
	
	@PostMapping
	public TipoDocumento create(@RequestBody TipoDocumento tipoDocumento) {
		
		return tipoDocumentoService.create(tipoDocumento);
		
	}
	
	@PutMapping
	public TipoDocumento update(@RequestBody TipoDocumento tipoDocumento) {
		
		return tipoDocumentoService.update(tipoDocumento);
		
	}
	
}
