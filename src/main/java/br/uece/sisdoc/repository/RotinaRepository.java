package br.uece.sisdoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.Rotina;

@Repository
public interface RotinaRepository extends JpaRepository<Rotina, Long>, JpaSpecificationExecutor<Rotina>{

}
