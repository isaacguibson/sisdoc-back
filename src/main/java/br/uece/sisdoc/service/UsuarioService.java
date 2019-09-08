package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.dto.UsuarioDTO;
import br.uece.sisdoc.dto.UsuarioForListDTO;
import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.repository.CargoRepository;
import br.uece.sisdoc.repository.SetorRepository;
import br.uece.sisdoc.repository.UsuarioCargoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;

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
	private PasswordEncoder passwordEncoder;
	
	public Usuario create(UsuarioDTO usuarioDTO) {
		
		Usuario usuario = dtoToUsuario(usuarioDTO);
		
		return usuarioRepository.save(usuario);
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
		
		usuarioRepository.deleteById(id);
	}

	public Usuario findById(Long id) {
		
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
		
		if(optionalUsuario.isPresent()) {
			return optionalUsuario.get();
		} else {
			return null;
		}
	}

	public Page<Usuario> findAll(Pageable pageable, UsuarioDTO usuarioDTO) {
		
		return usuarioRepository.findAll(pageable);
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
		
		Optional<Setor> optionalSetor = setorRepository.findById(usuarioDTO.getSetorId());
		if(optionalSetor.isPresent()) {
//			usuario.setSetor(optionalSetor.get());
		}
		
		Optional<Cargo> optionalCargo = cargoRepository.findById(usuarioDTO.getCargoId());
		if(optionalCargo.isPresent()) {
//			usuario.setCargo(optionalCargo.get());
		}
		
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
		usuario.setTratamento(usuarioDTO.getTratamento());
		
		return usuario;
	}
	
}
