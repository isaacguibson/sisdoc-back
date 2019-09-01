package br.uece.sisdoc.dto;

import java.util.List;

public class DocumentoDTO {

	private Long id;
	
	private String identificador;
	
	private Long usuarioId;
	
	private Long tipoDocumentoId;
	
	private String conteudo;
	
	private String dataInicial;
	
	private String dataFinal;
	
	private List<Long> destinatariosIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	public void setTipoDocumentoId(Long tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<Long> getDestinatariosIds() {
		return destinatariosIds;
	}

	public void setDestinatariosIds(List<Long> destinatariosIds) {
		this.destinatariosIds = destinatariosIds;
	}
	
}

