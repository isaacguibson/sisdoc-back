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
@Table(name="REUNIAO")
public class Reuniao {
	
	@Id
	@SequenceGenerator(sequenceName = "SEQ_REUNIAO", name = "SEQ_REUNIAO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_REUNIAO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_reuniao", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "colegiado_id", referencedColumnName = "id_colegiado")
	private Colegiado colegiado;
	
	@Column(name = "tipo", nullable = true)
	private String tipo;
	
	@Column(name = "numero", nullable = true)
	private Integer numero;
	
	@Column(name = "hora", nullable = true)
	private String hora;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Colegiado getColegiado() {
		return colegiado;
	}

	public void setColegiado(Colegiado colegiado) {
		this.colegiado = colegiado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}
}
