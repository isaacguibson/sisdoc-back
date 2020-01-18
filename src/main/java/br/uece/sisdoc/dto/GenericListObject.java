package br.uece.sisdoc.dto;

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

import br.uece.sisdoc.model.Documento;

@Entity
@Table(name="GENERIC_LIST_OBJECT")
public class GenericListObject {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_GENERIC_OBJECT", name = "SEQ_GENERIC_OBJECT", allocationSize = 1)
	@GeneratedValue(generator="SEQ_GENERIC_OBJECT", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_generic_object", nullable = false)
	private Long id;
	
	@Column(name = "label", nullable = true, columnDefinition = "VARCHAR(255)")
	private String label;
	
	@Column(name = "value", nullable = true)
	private Long value;
	
	@Column(name = "type", nullable = true, columnDefinition = "VARCHAR(255)")
	private String type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "documento_id", referencedColumnName = "id_documento", nullable = false)
	private Documento documento;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
}
