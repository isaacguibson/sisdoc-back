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
@Table(name="TIPO_DOCUMENTO_SETOR")
public class TipoDocumentoSetor {
	
	/* Entidade auxiliar para relacionamento muitos para muitos
	 * entre Setor e TipoDocumento  
	 */
	
	@Id
	@SequenceGenerator(sequenceName = "SEQ_TIPO_DOCUMENTO_SETOR", name = "SEQ_TIPO_DOCUMENTO_SETOR", allocationSize = 1)
	@GeneratedValue(generator="SEQ_TIPO_DOCUMENTO_SETOR", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_tipo_documento_setor", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "setor_id", referencedColumnName = "id_setor")
	private Setor setor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_documento_id", referencedColumnName = "id_tipo_documento")
	private TipoDocumento tipoDocumento;

	
	//Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	
	
}
