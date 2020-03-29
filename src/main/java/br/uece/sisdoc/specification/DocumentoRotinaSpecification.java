package br.uece.sisdoc.specification;

import br.uece.sisdoc.model.DocumentoRotina;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.Documento;

public class DocumentoRotinaSpecification {

	public Specification<DocumentoRotina> getByDocumento(Documento documento) {
		if(documento == null) {
			return null;
		}
		
		if(documento.getId() == null) {
			return null;
		}
		
		return (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("documento").get("id"), documento.getId());
	}
	
	public Specification<DocumentoRotina> getByDocumentoId(Long documentoId) {
		
		if(documentoId == null) {
			return null;
		}
		
		return (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("documento").get("id"), documentoId);
	}
	
}
