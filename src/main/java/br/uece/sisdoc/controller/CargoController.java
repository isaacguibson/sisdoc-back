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

import br.uece.sisdoc.dto.CargoDTO;
import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.service.CargoService;

@RestController
@RequestMapping("/cargo")
public class CargoController {

	@Autowired
	CargoService cargoService;
	
	@GetMapping
	public Page<Cargo> get(Pageable pageable, CargoDTO cargo) {
		
		return cargoService.findAll(pageable, cargo);
	}
	
	@GetMapping("/{id}")
	public Cargo findById(@PathVariable Long id) {
		
		return cargoService.findById(id);
	}
	
	
	@PostMapping
	public Cargo create(@RequestBody CargoDTO cargo) {
		
		return cargoService.create(cargo);
	}
	
	@PutMapping
	public Cargo update(@RequestBody CargoDTO cargo) {
		
		return cargoService.update(cargo);
	}
	
}
