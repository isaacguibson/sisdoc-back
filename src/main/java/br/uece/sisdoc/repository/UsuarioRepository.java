package br.uece.sisdoc.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario>{

	Usuario findByEmail(String email);
	
	@Query(value = "SELECT usuario.id, usuario.nome FROM Usuario usuario")
	public List<Object[]> allUsersForList();
	
	@Query(value = "SELECT usuarioCargo.usuario FROM UsuarioCargo usuarioCargo WHERE usuarioCargo.cargo.isCargoPrincipal = TRUE")
	public List<Usuario> allPrincipalUsers();
	
	@Query(value = "SELECT usuarioCargo.usuario FROM UsuarioCargo usuarioCargo WHERE usuarioCargo.cargo.isCargoPrincipal = TRUE AND usuarioCargo.cargo.setor.id = :idSetor")
	public List<Usuario> getPrincipalUsersFromSetor(@Param("idSetor") Long idSetor);
}
