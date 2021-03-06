package br.uece.sisdoc.specification;

import java.util.Date;

import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.UsuarioDocumento;

public class DocumentoSpecification {

	public Specification<Documento> filterByUserId(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("usuario").get("id"), id);
		
	}
	
	public Specification<Documento> filterToUserId(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) -> {
			Root<UsuarioDocumento> usuarioDocRoot = criteriaQuery.from(UsuarioDocumento.class);
			
			criteriaQuery.select(usuarioDocRoot.get("documento"));
			criteriaQuery.where(
					criteriaBuilder.and(criteriaBuilder.equal(
							usuarioDocRoot.get("usuarioDestino").get("id"), id), 
							criteriaBuilder.equal(usuarioDocRoot.get("documento").get("id"), root.get("id"))
					)
			);
			return criteriaQuery.distinct(true).getRestriction();
//			Join<Documento,Usuario> docUser = root.join("usuario");
//			Join<UsuarioDocumento,Usuario> usuerDoc = docUser.join("usuarioDestino");
//			return criteriaBuilder.equal(usuerDoc.get("usuarioDestino").get("id"), id);
			
		};
	}
	
	public Specification<Documento> orderById(){
		
		return (root, criteriaQuery, criteriaBuilder) -> {
			return criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id"))).getRestriction();
//			Join<Documento,Usuario> docUser = root.join("usuario");
//			Join<UsuarioDocumento,Usuario> usuerDoc = docUser.join("usuarioDestino");
//			return criteriaBuilder.equal(usuerDoc.get("usuarioDestino").get("id"), id);
			
		};
	}
	
	public Specification<Documento> filterById(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("id"), id);
		
	}
	
	public Specification<Documento> filterSendeds(){
		
		return (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("enviada"), true);
		
	}
	
	public Specification<Documento> filterByIdentificador(String identificador){
		
		return (identificador == null || identificador.equals("")) ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.like(root.get("identificador"), "%".concat(identificador).concat("%"));
		
	}
	
	public Specification<Documento> filterByTipo(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("tipoDocumento").get("id"), id);
		
	}
	
	public Specification<Documento> filterByDate(Date dataInicial, Date dataFinal){
		
		return (dataInicial == null || dataFinal == null) ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.between(root.get("dataCriacao"), dataInicial, dataFinal);
		
	}
	
}
