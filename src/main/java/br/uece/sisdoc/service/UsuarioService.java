package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.dto.UsuarioDTO;
import br.uece.sisdoc.dto.UsuarioForListDTO;
import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioCargo;
import br.uece.sisdoc.model.UsuarioColegiado;
import br.uece.sisdoc.model.UsuarioReuniao;
import br.uece.sisdoc.repository.CargoRepository;
import br.uece.sisdoc.repository.SetorRepository;
import br.uece.sisdoc.repository.UsuarioCargoRepository;
import br.uece.sisdoc.repository.UsuarioColegiadoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;
import br.uece.sisdoc.repository.UsuarioReuniaoRepository;
import br.uece.sisdoc.specification.UsuarioSpecification;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	SetorRepository setorRepository;
	
	@Autowired
	CargoRepository cargoRepository;
	
	@Autowired
	UsuarioCargoRepository usuarioCargoRepository;
	
	@Autowired
	UsuarioColegiadoRepository usuarioColegiadoRepository;
	
	@Autowired
	UsuarioReuniaoRepository usuarioReuniaoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UsuarioDocumentoService usuarioDocumentoService;
	
	@Autowired
	private DocumentoService documentoService;
	
	public Usuario create(UsuarioDTO usuarioDTO) {
		
		Usuario usuario = dtoToUsuario(usuarioDTO);
		Cargo cargo = null;
		
		Optional<Cargo> optionalCargo = cargoRepository.findById(usuarioDTO.getCargoId());
		if(optionalCargo.isPresent()) {
			cargo = optionalCargo.get();
		}
		
		if(cargo == null) {
			return null;
		}
		
		Usuario usuarioCriado = usuarioRepository.save(usuario);
		
		if(usuarioCriado != null) {
			UsuarioCargo usuarioCargo = new UsuarioCargo();
			usuarioCargo.setUsuario(usuarioCriado);
			usuarioCargo.setCargo(cargo);
			
			usuarioCargoRepository.save(usuarioCargo);
		}
		
		return usuarioCriado;
	}
	
	public Usuario update(UsuarioDTO usuarioDTO) {
		
		if(usuarioDTO.getId()==null) {
			return null;
		}
		
		Usuario usuario = dtoToUsuario(usuarioDTO);
		
		usuario.setId(usuarioDTO.getId());
		
		return usuarioRepository.save(usuario);
	}

	public void delete(Long id) {
		
		Optional<Usuario> optUsuario = usuarioRepository.findById(id);
		Usuario usuario = null;
		if(optUsuario.isPresent()) {
			usuario = optUsuario.get();
		} else {
			return;
		}
		
		deletarDocumentosRecebidos(usuario);
		deletarDocumentosCriados(usuario);
		removerRelacaoUsuarioCargo(usuario.getId());
		removerRelacaoUsuarioColegiado(usuario.getId());
		removerRelacaoUsuarioReuniao(usuario.getId());
		
		usuarioRepository.deleteById(id);
	}
	
	private boolean deletarDocumentosRecebidos(Usuario usuario) {
		return usuarioDocumentoService.deleteByUsuario(usuario);
	}
	
	private boolean deletarDocumentosCriados(Usuario usuario) {
		return documentoService.deletarDocumentosUsuario(usuario);
	}
	
	private void removerRelacaoUsuarioCargo(Long usuarioId) {
		List<UsuarioCargo> usuariosCargo = new ArrayList<UsuarioCargo>();
		usuariosCargo = usuarioCargoRepository.obterUsuarioCargosPeloUsuario(usuarioId);
		for(UsuarioCargo usuarioCargo : usuariosCargo) {
			usuarioCargoRepository.delete(usuarioCargo);
		}
	}
	
	private void removerRelacaoUsuarioColegiado(Long usuarioId) {
		List<UsuarioColegiado> usuarioColegiados = new ArrayList<UsuarioColegiado>();
		usuarioColegiados = usuarioColegiadoRepository.obterUsuarioColegiadoPeloUsuario(usuarioId);
		for(UsuarioColegiado usuarioColegiado : usuarioColegiados) {
			usuarioColegiadoRepository.delete(usuarioColegiado);
		}
	}
	
	private void removerRelacaoUsuarioReuniao(Long usuarioId) {
		List<UsuarioReuniao> usuariosReuniao = new ArrayList<UsuarioReuniao>();
		usuariosReuniao = usuarioReuniaoRepository.obterUsuarioReuniaoPeloUsuario(usuarioId);
		for(UsuarioReuniao usuarioReuniao : usuariosReuniao) {
			usuarioReuniaoRepository.delete(usuarioReuniao);
		}
	}

	public Usuario findById(Long id) {
		
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
		
		if(optionalUsuario.isPresent()) {
			return optionalUsuario.get();
		} else {
			return null;
		}
	}
	
	public List<Usuario> findByListId(List<Long> ids) {
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		if(ids == null || ids.size() == 0) {
			return usuarios;
		}
		
		Optional<Usuario> optionalUsuario = null;
		for(Long id : ids) {
			optionalUsuario = usuarioRepository.findById(id);
			
			if(optionalUsuario != null && optionalUsuario.isPresent()) {
				usuarios.add(optionalUsuario.get());
			} else {
				continue;
			}
		}
		
		return usuarios;
	}

	public Page<Usuario> findAll(Pageable pageable, UsuarioDTO usuarioDTO) {
		
		UsuarioSpecification usuarioSpecificarion = new UsuarioSpecification();
		
		return usuarioRepository.findAll(Specification.where(
				usuarioSpecificarion.filterByName(usuarioDTO.getNome())
			).and(usuarioSpecificarion.filterByEmail(usuarioDTO.getEmail())),
			pageable);
	}
	
	public List<UsuarioForListDTO> findAllForList() {
		
		List<Object[]> list = usuarioRepository.allUsersForList();
		
		List<UsuarioForListDTO> listUsuarioForList = new ArrayList<UsuarioForListDTO>();
		for(Object[] ob : list) {
			listUsuarioForList.add(new UsuarioForListDTO((Long)ob[0], (String)ob[1]));
		}
		
		return listUsuarioForList;
	}
	
	public Usuario findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	public List<Cargo> findCargoByUser(Long usuarioId) {
		return usuarioCargoRepository.getUserCargos(usuarioId);
	}
	
	private Usuario dtoToUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
		usuario.setTratamento(usuarioDTO.getTratamento());
		
		return usuario;
	}
	
}
