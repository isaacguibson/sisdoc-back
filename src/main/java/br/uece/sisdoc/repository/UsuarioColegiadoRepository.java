package br.uece.sisdoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.UsuarioColegiado;

@Repository
public interface UsuarioColegiadoRepository extends JpaRepository<UsuarioColegiado, Long>, JpaSpecificationExecutor<UsuarioColegiado> {

	@Query(value = "SELECT usuarioColegiado FROM UsuarioColegiado usuarioColegiado WHERE usuarioColegiado.usuario.id = :usuarioId")
	public List<UsuarioColegiado> obterUsuarioColegiadoPeloUsuario(@Param("usuarioId") Long usuarioId);
	
	@Query(value = "DELETE FROM UsuarioColegiado usuarioColegiado WHERE usuarioColegiado.usuario.id = :usuarioId")
	public void deletarUsuarioColegiadoPeloUsuario(@Param("usuarioId") Long usuarioId);
	
}
