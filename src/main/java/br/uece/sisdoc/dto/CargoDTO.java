package br.uece.sisdoc.dto;

public class CargoDTO {

	Long id;
	
	String nome;
	
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

	public Long getSetorId() {
		return setorId;
	}

	public void setSetorId(Long setorId) {
		this.setorId = setorId;
	}
	
	
	
}
