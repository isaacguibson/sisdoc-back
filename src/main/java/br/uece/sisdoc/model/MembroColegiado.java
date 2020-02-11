package br.uece.sisdoc.model;

public class MembroColegiado {

	private String nome;
	
	private String cargoSetor;
	
	public MembroColegiado() {
		super();
	}

	public MembroColegiado(String nome, String cargoSetor) {
		super();
		this.nome = nome;
		this.cargoSetor = cargoSetor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCargoSetor() {
		return cargoSetor;
	}

	public void setCargoSetor(String cargoSetor) {
		this.cargoSetor = cargoSetor;
	}
}
