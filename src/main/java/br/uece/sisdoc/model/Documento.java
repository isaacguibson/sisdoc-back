package br.uece.sisdoc.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import br.uece.sisdoc.dto.GenericListObject;

@Entity
@Table(name="DOCUMENTO")
public class Documento {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_DOCUMENTO", name = "SEQ_DOCUMENTO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_DOCUMENTO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_documento", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario; /*Usuario que criou o documento*/
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tipo_documento_id", referencedColumnName = "id_tipo_documento", nullable = false)
	private TipoDocumento tipoDocumento;
	
	@Column(name = "codigo", nullable = false)
	private Long codigo;
	
	@Column(name = "identificador", nullable = false)
	private String identificador;
	
	@Column(name = "assunto", nullable = true, columnDefinition = "VARCHAR(255)")
	private String assunto;
	
	@Column(name = "conteudo", nullable = true, columnDefinition = "TEXT")
	private String conteudo;
	
	@Column(name = "data_criacao", columnDefinition = "TIMESTAMP(6)", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dataCriacao;
	
	@Column(name = "enviada", columnDefinition = "BOOLEAN default '0'", nullable = false)
	private Boolean enviada;
	
	//Indica que é pra ser enviada para todos os usuarios
	@Column(name = "mensagem_geral", columnDefinition = "BOOLEAN default '0'", nullable = true)
	private Boolean mensagemGeral;
	
	//Indica se mensagem é para determinado(s) setores
	@Column(name = "mensagem_setor", nullable = true)
	private Boolean mensagemSetor;	
	
	@Column(name = "requerido", nullable = true)
	private Integer requerido; //para requerimentos
	
	@Column(name = "vinculo", nullable = true)
	private Integer vinculo; //para requerimentos
	
	@Transient
	private List<Rotina> rotinas;
	
	@Transient
	private List<GenericListObject> outrasRotinas;
	
	@Transient
	private List<GenericListObject> informacoes;
	
	@Transient
	private int totalPages;
	
	//Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public Boolean getEnviada() {
		return enviada;
	}

	public void setEnviada(Boolean enviada) {
		this.enviada = enviada;
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

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}

	public List<GenericListObject> getOutrasRotinas() {
		return outrasRotinas;
	}

	public void setOutrasRotinas(List<GenericListObject> outrasRotinas) {
		this.outrasRotinas = outrasRotinas;
	}

	public List<GenericListObject> getInformacoes() {
		return informacoes;
	}

	public void setInformacoes(List<GenericListObject> informacoes) {
		this.informacoes = informacoes;
	}
	
}
