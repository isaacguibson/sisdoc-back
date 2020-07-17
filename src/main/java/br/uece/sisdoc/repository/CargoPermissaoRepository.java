package br.uece.sisdoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.CargoPermissao;

@Repository
public interface CargoPermissaoRepository extends JpaRepository<CargoPermissao, Long> , JpaSpecificationExecutor<CargoPermissao>{

	@Query(value = "SELECT cargoPermissao.permissao.id FROM CargoPermissao cargoPermissao WHERE cargoPermissao.cargo.id = :cargoId")
	public List<Long> getPermissoesDoCargo(@Param("cargoId") Long cargoId);
	
}
