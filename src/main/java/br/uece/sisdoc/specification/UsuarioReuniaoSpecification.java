package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.UsuarioReuniao;

public class UsuarioReuniaoSpecification {

	public Specification<UsuarioReuniao> filterByReuniao(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("reuniao").get("id"), id);
		
	}
	
	public Specification<UsuarioReuniao> filterByUsuario(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("usuario").get("id"), id);
		
	}
	
}
