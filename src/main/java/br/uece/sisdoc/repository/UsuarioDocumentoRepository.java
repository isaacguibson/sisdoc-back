package br.uece.sisdoc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.UsuarioDocumento;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.Setor;

@Repository
public interface UsuarioDocumentoRepository extends JpaRepository<UsuarioDocumento, Long> , JpaSpecificationExecutor<UsuarioDocumento>{

	
	@Query(value = "SELECT usuarioDocumento.documento FROM UsuarioDocumento usuarioDocumento WHERE usuarioDocumento.usuarioDestino.id = :usuarioId")
	public Page<Documento> getDocumentosToUser(@Param("usuarioId") Long usuarioId, Pageable pageable);
	
	@Query(value = "SELECT usuarioDocumento.usuarioDestino.id FROM UsuarioDocumento usuarioDocumento WHERE usuarioDocumento.documento.id = :documentoId")
	public List<Long> getDestinatariosDoDoc(@Param("documentoId") Long documentoId);
	
	@Query(value = "SELECT usuarioCargo.cargo.setor FROM UsuarioDocumento usuarioDocumento, UsuarioCargo usuarioCargo WHERE usuarioDocumento.documento.id = :documentoId AND usuarioCargo.usuario.id = usuarioDocumento.usuarioDestino.id")
	public List<Setor> getSetoresDoDoc(@Param("documentoId") Long documentoId);
	
	@Query(value = "SELECT usuarioDocumento FROM UsuarioDocumento usuarioDocumento WHERE usuarioDocumento.usuarioDestino.id = :usuarioId AND usuarioDocumento.documento.id = :documentoId")
	public List<UsuarioDocumento> getUserDocByUserDestIdAndDocId(@Param("usuarioId") Long usuarioId, @Param("documentoId") Long documentoId);
	
	@Query(value = "SELECT usuarioDocumento.documento FROM UsuarioDocumento usuarioDocumento WHERE usuarioDocumento.usuarioDestino.id = :usuarioId AND usuarioDocumento.abertaPeloUsuario = 0")
	public Page<Documento> getDocumentosNaoLidos(@Param("usuarioId") Long usuarioId, Pageable pageable);
	
	@Query(value = "DELETE FROM UsuarioDocumento usuarioDocumento WHERE usuarioDocumento.documento.id = :documentoId")
	public void deleteDocumentosEnviadosByDoc(@Param("documentoId") Long documentoId);
	
}
