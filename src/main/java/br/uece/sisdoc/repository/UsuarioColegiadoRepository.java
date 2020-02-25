package br.uece.sisdoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.UsuarioColegiado;

@Repository
public interface UsuarioColegiadoRepository extends JpaRepository<UsuarioColegiado, Long>, JpaSpecificationExecutor<UsuarioColegiado> {

}
