package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Cargo;

public class CargoSpecification {

	public Specification<Cargo> filterByName(String nome){
		
		return nome == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.like(root.get("nome"), "%".concat(nome).concat("%"));
		
	}
	
	public Specification<Cargo> filterById(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("id"), id);
		
	}
	
	public Specification<Cargo> filterBySetor(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("setor").get("id"), id);
		
	}
	
}
