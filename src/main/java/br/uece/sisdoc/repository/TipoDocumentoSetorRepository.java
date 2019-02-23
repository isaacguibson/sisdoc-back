package br.uece.sisdoc.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.TipoDocumentoSetor;

@Repository
public interface TipoDocumentoSetorRepository extends JpaRepository<TipoDocumentoSetor, Long>, JpaSpecificationExecutor<TipoDocumentoSetor>{

}
