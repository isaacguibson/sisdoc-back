package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.configuration.CustomUserDetailService;
import br.uece.sisdoc.configuration.CustomUserPrincipal;
import br.uece.sisdoc.dto.UsuarioDocumentoDTO;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioDocumento;
import br.uece.sisdoc.repository.DocumentoRepository;
import br.uece.sisdoc.repository.UsuarioDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;
import br.uece.sisdoc.specification.UsuarioDocumentoSpecification;

@Service
public class UsuarioDocumentoService {

	@Autowired
	UsuarioDocumentoRepository usuarioDocumentoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	DocumentoRepository documentoRepository;
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	public List<UsuarioDocumento> create(UsuarioDocumentoDTO usuarioDocumentoDTO, Authentication authentication) {
		
		if(!validaEnvio(usuarioDocumentoDTO, authentication)) {
			return null;
		}
		
		List<UsuarioDocumento> usuariosDocumentos = dtoToUsuarioDocumento(usuarioDocumentoDTO);
		List<UsuarioDocumento> usuariosDocumentosReturn = new ArrayList<UsuarioDocumento>();
		
		for(UsuarioDocumento usuarioDocumento : usuariosDocumentos) {
			usuariosDocumentosReturn.add(usuarioDocumentoRepository.save(usuarioDocumento));
		}
		
		return usuariosDocumentosReturn;
	}
	
	public List<UsuarioDocumento> update(UsuarioDocumentoDTO usuarioDocumentoDTO) {
		
		List<UsuarioDocumento> usuariosDocumentos = dtoToUsuarioDocumento(usuarioDocumentoDTO);
		List<UsuarioDocumento> usuariosDocumentosReturn = new ArrayList<UsuarioDocumento>();
		
		for(UsuarioDocumento usuarioDocumento : usuariosDocumentos) {
			if(usuarioDocumento.getId() == null) {
				return null;
			}
			
			usuariosDocumentosReturn.add(usuarioDocumentoRepository.save(usuarioDocumento));
		}
		
		return usuariosDocumentosReturn;
		
	}
	
	public void delete(Long id) {
		
		usuarioDocumentoRepository.deleteById(id);
	}
	
	public List<UsuarioDocumento> getUsuarioDocumentoFromDocumentoId(Long documentoId) {
		UsuarioDocumentoSpecification userDocSpec = new UsuarioDocumentoSpecification();
		
		List<UsuarioDocumento> usuariosDocumento = usuarioDocumentoRepository.findAll(Specification.where(
				userDocSpec.filterByDocumento(documentoId)
		));
		
		return usuariosDocumento;
	}
	
	public boolean deleteByDocumento(Long documentoId) {
		
		int count = 0;
		 
		List<UsuarioDocumento> usuariosDocumento = getUsuarioDocumentoFromDocumentoId(documentoId);
		int total = 0;
		if(usuariosDocumento != null && usuariosDocumento.size() > 0) {
			total = usuariosDocumento.size();
			for (UsuarioDocumento usuDoc : usuariosDocumento) {
				usuarioDocumentoRepository.delete(usuDoc);
				count++;
			}
		}
		
		return total != 0 && count == total;
	}
	
	public UsuarioDocumento findById(Long id) {
		
		Optional<UsuarioDocumento> optionalUsuarioDocumento = usuarioDocumentoRepository.findById(id);
		
		if(optionalUsuarioDocumento.isPresent()) {
			return optionalUsuarioDocumento.get();
		} else {
			return null;
		}
	}
	
	
	private List<UsuarioDocumento> dtoToUsuarioDocumento(UsuarioDocumentoDTO usuarioDocumentoDTO) {
		
		List<UsuarioDocumento> usuariosDocumentos = new ArrayList<UsuarioDocumento>();
		
		UsuarioDocumento usuarioDocumento = new UsuarioDocumento();
		
		Optional<Usuario> optionalUsuario = null;
		Usuario usuario = null;
		
		Optional<Documento> optionalDocumento = documentoRepository.findById(usuarioDocumentoDTO.getDocumentoId());
		Documento documento = null;
		
		if(optionalDocumento.isPresent()) {
			documento = optionalDocumento.get();
		}
		
		boolean abertaPeloUsuario = usuarioDocumentoDTO.getAbertaPeloUsuario() == null ?
									false : usuarioDocumentoDTO.getAbertaPeloUsuario();
		
		for(Long usuarioId : usuarioDocumentoDTO.getUsuariosIds()) {
			
			usuarioDocumento = new UsuarioDocumento();
			
			optionalUsuario = usuarioRepository.findById(usuarioId);
			
			usuario = null;
			
			if(optionalUsuario.isPresent()) {
				usuario = optionalUsuario.get();
			}
			
			usuarioDocumento.setUsuarioDestino(usuario);
			
			usuarioDocumento.setDocumento(documento);
			
			usuarioDocumento.setAbertaPeloUsuario(abertaPeloUsuario);
			
			usuariosDocumentos.add(usuarioDocumento);
		}
		
		
		return usuariosDocumentos;
		
	}
	
	private boolean validaEnvio(UsuarioDocumentoDTO usuarioDocumentoDTO, Authentication authentication) {
		
		CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) customUserDetailService.loadUserByUsername(authentication.getName());
		Optional<Documento> optionalDocumento = documentoRepository.findById(usuarioDocumentoDTO.getDocumentoId());
		Documento documento = null;
		
		if(optionalDocumento.isPresent()) {
			documento = optionalDocumento.get();
		}
		if(documento == null || !customUserPrincipal.getUsuario().getId().equals(documento.getUsuario().getId())) {
			return false;
		}
		
		return true;
		
	}
}
