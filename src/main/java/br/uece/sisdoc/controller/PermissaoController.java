package br.uece.sisdoc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.service.PermissaoService;

@RestController
@RequestMapping("/permissao")
public class PermissaoController {
	
	@Autowired
	PermissaoService permissaoService;

	@GetMapping("/obterPermissoesPeloCargo/{cargoId}")
	public List<Long> obterPermissoesPeloCargo(@PathVariable Long cargoId) {
		
		return permissaoService.obterPermissoesPeloCargo(cargoId);
	}
	
}
