package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.TipoDocumento;;

public class TipoDocumentoSpecification {
	
public Specification<TipoDocumento> filterByName(String nome){
		
		return nome == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.like(root.get("nome"), "%".concat(nome).concat("%"));
		
	}
	
	public Specification<TipoDocumento> filterById(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("id"), id);
		
	}

}
