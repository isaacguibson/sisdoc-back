package br.uece.sisdoc.specification;

import org.springframework.data.jpa.domain.Specification;

import br.uece.sisdoc.model.TipoDocumentoSetor;


public class TipoDocumentoSetorSpecification {

	//	Consulta pelo tipo de documento
	public Specification<TipoDocumentoSetor> filterTipoDocumento(Long tipoDocumentoId){
		
		return tipoDocumentoId == null ? null : (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("tipoDocumento").get("id"), tipoDocumentoId);
		
	}
	
}
