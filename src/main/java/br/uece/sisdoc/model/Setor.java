package br.uece.sisdoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="SETOR")
public class Setor {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_SETOR", name = "SEQ_SETOR", allocationSize = 1)
	@GeneratedValue(generator="SEQ_SETOR", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_setor", nullable = false)
	private Long id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "sigla", nullable = true)
	private String sigla;
	
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
}
