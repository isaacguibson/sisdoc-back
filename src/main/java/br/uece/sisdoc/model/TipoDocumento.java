package br.uece.sisdoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TIPO_DOCUMENTO")
public class TipoDocumento {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_TIPO_DOCUMENTO", name = "SEQ_TIPO_DOCUMENTO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_TIPO_DOCUMENTO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_tipo_documento", nullable = false)
	private Long id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "permissao_id", referencedColumnName = "id_permissao", nullable = false)
	private Permissao permissao;

	
	//Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
}
