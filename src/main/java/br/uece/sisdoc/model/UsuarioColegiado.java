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
@Table(name="USUARIO_COLEGIADO")
public class UsuarioColegiado {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_USUARIO_COLEGIADO", name = "SEQ_USUARIO_COLEGIADO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_USUARIO_COLEGIADO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_usuario_colegiado", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "colegiado_id", referencedColumnName = "id_colegiado", nullable = false)
	private Colegiado colegiado;

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

	public Colegiado getColegiado() {
		return colegiado;
	}

	public void setColegiado(Colegiado colegiado) {
		this.colegiado = colegiado;
	}
	
}
