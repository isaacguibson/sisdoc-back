package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Setor;

public class SetorSpecification {

	
	public Specification<Setor> filterByName(String nome){
		
		return nome == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.like(root.get("nome"), "%".concat(nome).concat("%"));
		
	}
	
}
