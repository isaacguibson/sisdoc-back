package br.uece.sisdoc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.dto.ColegiadoDTO;
import br.uece.sisdoc.model.Colegiado;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.service.ColegiadoService;
import br.uece.sisdoc.specification.ColegiadoSpecification;
import br.uece.sisdoc.specification.SetorSpecification;

@RestController
@RequestMapping("/colegiado")
public class ColegiadoController {
	
	@Autowired
	ColegiadoService colegiadoService;

	@GetMapping("/{id}")
	public Colegiado findById(@PathVariable Long id) {
		
		return colegiadoService.findById(id);
	}
	
	@GetMapping("/dto/{id}")
	public ColegiadoDTO findDTOById(@PathVariable Long id) {
		
		return colegiadoService.getColegiadoDTO(id);
	}
	
	@GetMapping("/all")
	public List<Colegiado> get() {
		
		return colegiadoService.findAll();
	}
	
	@GetMapping
	public Page<Colegiado> findAll(Pageable pageable, Colegiado colegiado) {
		
		return colegiadoService.findAll(pageable, colegiado);
	}
	
	@GetMapping("/membros/{id}")
	public List<Usuario> getMembros(@PathVariable Long id) {
		
		return colegiadoService.getMembros(id);
	}
	
	@PostMapping
	public Colegiado create(@RequestBody ColegiadoDTO colegiadoDTO) {
		
		return colegiadoService.create(colegiadoDTO);
	}
	
}
