package br.uece.sisdoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.dto.GenericListObject;

@Repository
public interface GenericListObjectRepository extends JpaRepository<GenericListObject, Long>, JpaSpecificationExecutor<GenericListObject>{

}
