package br.uece.sisdoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="DOCUMENTO_ROTINA")
public class DocumentoRotina {
	
	@Id
	@SequenceGenerator(sequenceName = "SEQ_DOCUMENTO_ROTINA", name = "SEQ_DOCUMENTO_ROTINA", allocationSize = 1)
	@GeneratedValue(generator="SEQ_DOCUMENTO_ROTINA", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_documento_rotina", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "documento_id", referencedColumnName = "id_documento", nullable = false)
	private Documento documento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rotina_id", referencedColumnName = "id_rotina", nullable = false)
	private Rotina rotina;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Rotina getRotina() {
		return rotina;
	}

	public void setRotina(Rotina rotina) {
		this.rotina = rotina;
	}

}
