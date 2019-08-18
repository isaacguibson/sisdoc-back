package br.uece.sisdoc.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Documento;

public class DocumentoSpecification {

	public Specification<Documento> filterByUserId(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("usuario").get("id"), id);
		
	}
	
	public Specification<Documento> filterById(Long id){
		
		return id == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("id"), id);
		
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
