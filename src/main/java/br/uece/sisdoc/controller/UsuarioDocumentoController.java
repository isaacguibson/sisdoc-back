package br.uece.sisdoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.service.UsuarioDocumentoService;

@RestController
@RequestMapping("/usuario_documento")
public class UsuarioDocumentoController {
	
	@Autowired
	UsuarioDocumentoService usuarioDocumentoService;
	
//	@PostMapping
//	public List<UsuarioDocumento> create(@RequestBody UsuarioDocumentoDTO usuarioDocumentoDTO) {
//		
//		return usuarioDocumentoService.create(usuarioDocumentoDTO);
//	}
//	
//	@PutMapping
//	public List<UsuarioDocumento> update(@RequestBody UsuarioDocumentoDTO usuarioDocumentoDTO) {
//		
//		return usuarioDocumentoService.update(usuarioDocumentoDTO);
//	}
	
}
