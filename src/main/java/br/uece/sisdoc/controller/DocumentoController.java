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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.dto.DocumentoDTO;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.service.DocumentoService;

@RestController
@RequestMapping("/documento")
public class DocumentoController {

	@Autowired
	DocumentoService documentoService;
	
	@GetMapping
	public Page<Documento> get(Pageable pageable, Documento documento) {
		
		return documentoService.findAll(pageable);
	}
	
	@GetMapping("/{id}")
	public Documento findById(@PathVariable Long id) {
		
		return documentoService.findById(id);
	}
	
	
	@PostMapping
	public Documento create(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PutMapping
	public Documento update(@RequestBody DocumentoDTO documento) {
		
		return documentoService.update(documento);
	}
	
}
