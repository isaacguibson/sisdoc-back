package br.uece.sisdoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.DocumentoRotina;

@Repository
public interface DocumentoRotinaRepository extends JpaRepository<DocumentoRotina, Long>, JpaSpecificationExecutor<DocumentoRotina>{

}
