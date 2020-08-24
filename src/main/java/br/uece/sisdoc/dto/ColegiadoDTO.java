package br.uece.sisdoc.dto;

import java.util.List;

public class ColegiadoDTO {

	Long id;
	
	String nome;
	
	String descricao;
	
	List<Long> membrosIds;
	
	Long setorId;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Long> getMembrosIds() {
		return membrosIds;
	}

	public void setMembrosIds(List<Long> membrosIds) {
		this.membrosIds = membrosIds;
	}

	public Long getSetorId() {
		return setorId;
	}

	public void setSetorId(Long setorId) {
		this.setorId = setorId;
	}
	
}
