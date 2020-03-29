package br.uece.sisdoc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.model.DocumentoRotina;
import br.uece.sisdoc.repository.DocumentoRotinaRepository;
import br.uece.sisdoc.specification.DocumentoRotinaSpecification;

@Service
public class DocumentoRotinaService {

	@Autowired
	private DocumentoRotinaRepository documentoRotinaRepository;
	
	public boolean deletByDocumentoId(Long documentoId) {
		int count = 0, total = 0;
		DocumentoRotinaSpecification docRotSpec = new DocumentoRotinaSpecification();
		
		List<DocumentoRotina> docRotinas = documentoRotinaRepository.findAll(Specification.where(
				docRotSpec.getByDocumentoId(documentoId)
		));
		
		if(docRotinas != null && docRotinas.size()>0) {
			total = docRotinas.size();
			
			for(DocumentoRotina docRotina : docRotinas) {
				documentoRotinaRepository.delete(docRotina);
				count++;
			}
		}
		
		return total != 0 && count == total;
	}
	
}
