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
@Table(name="CARGO_PERMISSAO")
public class CargoPermissao {

	@Id
	@SequenceGenerator(sequenceName = "SEQ_CARGO_PERMISSAO", name = "SEQ_CARGO_PERMISSAO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_CARGO_PERMISSAO", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_cargo_permissao", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cargo_id", referencedColumnName = "id_cargo", nullable = false)
	private Cargo cargo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "permissao_id", referencedColumnName = "id_permissao", nullable = false)
	private Permissao permissao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
	
}
