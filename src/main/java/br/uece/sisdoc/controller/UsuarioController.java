package br.uece.sisdoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.dto.UsuarioDTO;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping
	public Page<Usuario> get(Pageable pageable, UsuarioDTO usuarioDTO) {
		
		return usuarioService.findAll(pageable, usuarioDTO);
	
	}
	
	@GetMapping("/{id}")
	public Usuario get(@PathVariable Long id) {
		
		return usuarioService.findById(id);
	
	}
	
	
	@PostMapping
	public Usuario create(@RequestBody UsuarioDTO usuarioDTO) {
		
		return usuarioService.create(usuarioDTO);
	}
	
	@PutMapping
	public Usuario update(@RequestBody UsuarioDTO usuarioDTO) {
		
		return usuarioService.update(usuarioDTO);
		
	}
	
	@DeleteMapping
	public String delete(String texto) {
		
		System.out.println(texto);
		
		return texto;
	}
	
}
