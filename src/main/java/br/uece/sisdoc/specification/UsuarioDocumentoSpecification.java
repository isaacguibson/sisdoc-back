package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;
import br.uece.sisdoc.model.UsuarioDocumento;

public class UsuarioDocumentoSpecification {

	public Specification<UsuarioDocumento> filterByDocumento(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("documento").get("id"), id);
		
	}
	
	public Specification<UsuarioDocumento> filterByUsuario(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("usuarioDestino").get("id"), id);
		
	}
	
}
