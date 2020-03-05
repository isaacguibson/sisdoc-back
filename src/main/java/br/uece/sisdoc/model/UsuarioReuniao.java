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
@Table(name="USUARIO_REUNIAO")
public class UsuarioReuniao {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_USUARIO_REUNIAO", name = "SEQ_USUARIO_REUNIAO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_USUARIO_REUNIAO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_usuario_reuniao", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reuniao_id", referencedColumnName = "id_reuniao", nullable = false)
	private Reuniao reuniao;

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

	public Reuniao getReuniao() {
		return reuniao;
	}

	public void setReuniao(Reuniao reuniao) {
		this.reuniao = reuniao;
	}
	
}
