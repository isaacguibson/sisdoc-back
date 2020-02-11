package br.uece.sisdoc.dto;

import java.util.List;

public class DocumentoDTO {

	private Long id;
	
	private String identificador;
	
	private Long usuarioId;
	
	private Long tipoDocumentoId;
	
	private String assunto;
	
	private String conteudo;
	
	private String dataInicial;
	
	private String dataFinal;
	
	private List<Long> destinatariosIds;
	
	private List<Long> setoresDestinatariosIds;
	
	private Boolean mensagemGeral;
	
	private Boolean mensagemSetor;
	
	private Integer requerido; //para requerimentos
	
	private Integer vinculo; //para requerimentos
	
	private List<Long> rotinas; //para requerimentos
	
	private List<GenericListObject> outrasRotinas; //para requerimentos
	
	private List<GenericListObject> informacoes; //para requerimentos

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

	public Boolean getMensagemGeral() {
		return mensagemGeral;
	}

	public void setMensagemGeral(Boolean mensagemGeral) {
		this.mensagemGeral = mensagemGeral;
	}

	public Boolean getMensagemSetor() {
		return mensagemSetor;
	}

	public void setMensagemSetor(Boolean mensagemSetor) {
		this.mensagemSetor = mensagemSetor;
	}

	public List<Long> getSetoresDestinatariosIds() {
		return setoresDestinatariosIds;
	}

	public void setSetoresDestinatariosIds(List<Long> setoresDestinatariosIds) {
		this.setoresDestinatariosIds = setoresDestinatariosIds;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public Integer getRequerido() {
		return requerido;
	}

	public void setRequerido(Integer requerido) {
		this.requerido = requerido;
	}

	public Integer getVinculo() {
		return vinculo;
	}

	public void setVinculo(Integer vinculo) {
		this.vinculo = vinculo;
	}

	public List<Long> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Long> rotinas) {
		this.rotinas = rotinas;
	}

	public List<GenericListObject> getInformacoes() {
		return informacoes;
	}

	public void setInformacoes(List<GenericListObject> informacoes) {
		this.informacoes = informacoes;
	}

	public List<GenericListObject> getOutrasRotinas() {
		return outrasRotinas;
	}

	public void setOutrasRotinas(List<GenericListObject> outrasRotinas) {
		this.outrasRotinas = outrasRotinas;
	}
	
}

