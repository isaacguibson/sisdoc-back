package br.uece.sisdoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.uece.sisdoc.model.Reuniao;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Long>, JpaSpecificationExecutor<Reuniao> {

}
