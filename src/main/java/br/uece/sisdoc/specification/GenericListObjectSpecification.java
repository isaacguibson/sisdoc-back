package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.dto.GenericListObject;
import br.uece.sisdoc.model.Documento;

public class GenericListObjectSpecification {

	public Specification<GenericListObject> findByDocumento(Documento documento){
		if(documento == null) {
			return null;
		}
		
		if(documento.getId() == null) {
			return null;
		}
		
		return (root, criteriaQuery, criteriaBuilder) -> 
					criteriaBuilder.equal(root.get("documento").get("id"), documento.getId());
	}
	
	public Specification<GenericListObject> findByType(String type){
		
		if(type==null || type.equals("")) {
			return null;
		}
		
		return (root, criteriaQuery, criteriaBuilder) -> 
					criteriaBuilder.equal(root.get("type"), type);
	}
	
}
