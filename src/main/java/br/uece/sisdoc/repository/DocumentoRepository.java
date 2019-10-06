package br.uece.sisdoc.repository;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long>, JpaSpecificationExecutor<Documento>{

	/**
	 * Indica o numero de cod ate determinada data
	 * */
	@Query(value = "SELECT MAX(doc.codigo) FROM Documento doc WHERE doc.dataCriacao < :dataFinal")
	public Long ultimoCodDocumento(@Param("dataFinal") Date dataFinal);
	
	@Query(value = "SELECT MAX(doc.codigo) FROM Documento doc WHERE doc.tipoDocumento.id = :tipoDocumentoId AND doc.dataCriacao < :dataFinal")
	public Long ultimoCodDocumentoByDoc(@Param("tipoDocumentoId") Long tipoDocumentoId, @Param("dataFinal") Date dataFinal);
	
	/**
	 * Pega todos os documentos do usuario do parametro
	 * */
	@Query(value = "SELECT doc FROM Documento doc WHERE doc.usuario.id = :idUsuario")
	public List<Documento> docsFromUser(@Param("idUsuario") Long idUsuario);
	
}
