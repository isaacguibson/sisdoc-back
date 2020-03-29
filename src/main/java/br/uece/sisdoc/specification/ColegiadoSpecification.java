package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Colegiado;

public class ColegiadoSpecification {

	public Specification<Colegiado> filterByName(String nome){
		
		return nome == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.like(root.get("nome"), "%".concat(nome).concat("%"));
		
	}
	
	public Specification<Colegiado> filterById(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("id"), id);
		
	}
	
}
