package br.uece.sisdoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="DOCUMENTO_STATUS")
public class DocumentoStatus {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_DOCUMENTO_STATUS", name = "SEQ_DOCUMENTO_STATUS", allocationSize = 1)
	@GeneratedValue(generator="SEQ_DOCUMENTO_STATUS", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_documento_status", nullable = false)
	private Long id;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
}
