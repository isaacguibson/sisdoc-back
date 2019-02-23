package br.uece.sisdoc.dto;

public class TipoDocumentoSetorDTO {

	private Long id;
	
	private Long setorId;
	
	private Long tipoDocumentoId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSetorId() {
		return setorId;
	}

	public void setSetorId(Long setorId) {
		this.setorId = setorId;
	}

	public Long getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	public void setTipoDocumentoId(Long tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}
	
}
