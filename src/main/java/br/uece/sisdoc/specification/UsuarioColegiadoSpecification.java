package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.UsuarioColegiado;

public class UsuarioColegiadoSpecification {
	
	public Specification<UsuarioColegiado> filterByColegiado(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("colegiado").get("id"), id);
		
	}
	
	public Specification<UsuarioColegiado> filterByUsuario(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("usuario").get("id"), id);
		
	}
}
