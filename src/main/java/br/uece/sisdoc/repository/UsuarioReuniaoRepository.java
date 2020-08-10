package br.uece.sisdoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.model.UsuarioReuniao;

@Repository
public interface UsuarioReuniaoRepository extends JpaRepository<UsuarioReuniao, Long>, JpaSpecificationExecutor<UsuarioReuniao> {

	@Query(value = "SELECT usuarioReuniao FROM UsuarioReuniao usuarioReuniao WHERE usuarioReuniao.usuario.id = :usuarioId")
	public List<UsuarioReuniao> obterUsuarioReuniaoPeloUsuario(@Param("usuarioId") Long usuarioId);
	
	@Query(value = "DELETE FROM UsuarioReuniao usuarioReuniao WHERE usuarioReuniao.usuario.id = :usuarioId")
	public void deletarUsuarioReuniaoPeloUsuario(@Param("usuarioId") Long usuarioId);
	
}
