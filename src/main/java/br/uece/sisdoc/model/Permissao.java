package br.uece.sisdoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="PERMISSAO")
public class Permissao {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_PERMISSAO", name = "SEQ_PERMISSAO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_PERMISSAO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_permissao", nullable = false)
	private Long id;
	
	@Column(name = "acao", nullable = false)
	private String acao;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
