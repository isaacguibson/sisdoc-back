package br.uece.sisdoc.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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

import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.service.SetorService;

@RestController
@RequestMapping("/setor")
public class SetorController {

	@Autowired
	SetorService setorService;
	
	@GetMapping
	public Page<Setor> get(Pageable pageable, Setor setor) {
		
		return setorService.findAll(pageable, setor);
	}
	
	@GetMapping("/listaSetores")
	public String getAll() {
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		
		List<Setor> setores = setorService.findAll();
		
		for(Setor setor : setores) {
			jsonObject = new JSONObject();
			
			jsonObject.put("id", setor.getId());
			jsonObject.put("nome", setor.getNome());
			jsonArray.put(jsonObject);
		}
		
		return jsonArray.toString();
	}
	
	@GetMapping("/{id}")
	public Setor findById(@PathVariable Long id) {
		
		return setorService.findById(id);
	}
	
	
	@PostMapping
	public Setor create(@RequestBody Setor setor) {
		
		return setorService.create(setor);
	}
	
	@PutMapping
	public Setor update(@RequestBody Setor setor) {
		
		return setorService.update(setor);
	}
	
}
