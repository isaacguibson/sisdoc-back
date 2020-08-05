package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Usuario;


public class UsuarioSpecification {

	public Specification<Usuario> filterByName(String nome){
		
		return (nome == null || nome.equals("")) ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.like(root.get("nome"), "%".concat(nome).concat("%"));
		
	}
	
	public Specification<Usuario> filterByEmail(String email){
		
		return (email == null || email.equals("")) ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("email"), email);
		
	}
	
}
