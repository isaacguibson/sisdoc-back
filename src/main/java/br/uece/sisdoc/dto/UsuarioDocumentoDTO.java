package br.uece.sisdoc.dto;

import java.util.List;

public class UsuarioDocumentoDTO {

	private Long id;
	
	private List<Long> usuariosIds;
	
	private Long documentoId;
	
	private Boolean abertaPeloUsuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public Boolean getAbertaPeloUsuario() {
		return abertaPeloUsuario;
	}

	public void setAbertaPeloUsuario(Boolean abertaPeloUsuario) {
		this.abertaPeloUsuario = abertaPeloUsuario;
	}

	public List<Long> getUsuariosIds() {
		return usuariosIds;
	}

	public void setUsuariosIds(List<Long> usuariosIds) {
		this.usuariosIds = usuariosIds;
	}
	
}
