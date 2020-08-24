package br.uece.sisdoc.dto;

public class ReuniaoDTO {

	private Long id;
	
	private Long colegiadoId;
	
	private String tipo;
	
	private String hora;
	
	private Integer numero;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getColegiadoId() {
		return colegiadoId;
	}

	public void setColegiadoId(Long colegiadoId) {
		this.colegiadoId = colegiadoId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}
