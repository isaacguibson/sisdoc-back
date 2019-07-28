package br.uece.sisdoc.controller;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		
		Page<Documento> pageReturn = documentoService.findAll(pageable);
		
		return pageReturn;
	}
	
	@GetMapping("/{id}")
	public Documento findById(@PathVariable Long id) {
		
		return documentoService.findById(id);
	}
	
	
	@GetMapping("/oficio/{id}")
	public ResponseEntity<byte[]> createDocumentFile(@PathVariable Long id) {
		
		try {
			String path = documentoService.generateOficio(id);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	@PostMapping("/oficio")
	public Documento create(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PutMapping
	public Documento update(@RequestBody DocumentoDTO documento) {
		
		return documentoService.update(documento);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		
		documentoService.delete(id);
	}
	
}
