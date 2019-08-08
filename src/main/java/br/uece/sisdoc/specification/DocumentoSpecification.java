package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Documento;

public class DocumentoSpecification {

	public Specification<Documento> filterByUserId(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("usuario").get("id"), id);
		
	}
	
}
