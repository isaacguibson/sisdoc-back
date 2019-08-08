package br.uece.sisdoc.model;

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

@Entity
@Table(name="USUARIO_DOCUMENTO")
public class UsuarioDocumento {
	
	@Id
	@SequenceGenerator(sequenceName = "SEQ_USUARIO_DOCUMENTO", name = "SEQ_USUARIO_DOCUMENTO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_USUARIO_DOCUMENTO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_usuario_documento", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuarioDestino;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "documento_id", referencedColumnName = "id_documento", nullable = false)
	private Documento documento;
	
	/*Indica se o usuario destino ja baixou o documento*/
	@Column(name = "aberta_pelo_usuario", nullable = false)
	private Boolean abertaPeloUsuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Boolean getAbertaPeloUsuario() {
		return abertaPeloUsuario;
	}

	public void setAbertaPeloUsuario(Boolean abertaPeloUsuario) {
		this.abertaPeloUsuario = abertaPeloUsuario;
	}

}
