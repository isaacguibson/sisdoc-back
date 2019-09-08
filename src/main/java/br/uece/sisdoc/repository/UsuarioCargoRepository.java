package br.uece.sisdoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.UsuarioCargo;
import br.uece.sisdoc.model.Cargo;

@Repository
public interface UsuarioCargoRepository extends JpaRepository<UsuarioCargo, Long> , JpaSpecificationExecutor<UsuarioCargo>{
	
	@Query(value = "SELECT usuarioCargo.cargo FROM UsuarioCargo usuarioCargo WHERE usuarioCargo.usuario.id = :usuarioId")
	public List<Cargo> getUserCargos(@Param("usuarioId") Long usuarioId);

}
