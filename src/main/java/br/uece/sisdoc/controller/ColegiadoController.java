package br.uece.sisdoc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.model.Colegiado;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.service.ColegiadoService;

@RestController
@RequestMapping("/colegiado")
public class ColegiadoController {
	
	@Autowired
	ColegiadoService colegiadoService;

	@GetMapping("/{id}")
	public Colegiado findById(@PathVariable Long id) {
		
		return colegiadoService.findById(id);
	}
	
	@GetMapping
	public List<Colegiado> get() {
		
		return colegiadoService.findAll();
	}
	
	@GetMapping("/membros/{id}")
	public List<Usuario> getMembros(@PathVariable Long id) {
		
		return colegiadoService.getMembros(id);
	}
	
}
