package br.uece.sisdoc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.dto.ReuniaoDTO;
import br.uece.sisdoc.model.Colegiado;
import br.uece.sisdoc.model.Reuniao;
import br.uece.sisdoc.repository.ReuniaoRepository;

@Service
public class ReuniaoService {

	@Autowired
	ReuniaoRepository reuniaoRepository;
	
	@Autowired
	ColegiadoService colegiadoService;
	
	public Reuniao create(ReuniaoDTO reuniaoDTO) {
		
		Reuniao reuniao = dtoToReuniao(reuniaoDTO);
		
		if(reuniao != null) {
			return reuniaoRepository.save(reuniao);
		}
		
		return reuniao;
		
	}
	
	public Reuniao save(Reuniao reuniao) {
		
		if(reuniao != null) {
			return reuniaoRepository.save(reuniao);
		}
		
		return reuniao;
	}
	
	public Reuniao update(ReuniaoDTO reuniaoDTO) {
		
		if(reuniaoDTO.getId() == null) {
			return null;
		}
		
		Reuniao reuniao = dtoToReuniao(reuniaoDTO);
		
		if(reuniao != null) {
			return reuniaoRepository.save(reuniao);
		}
		return reuniao;
	}
	
	public Reuniao update(Reuniao reuniao) {
		
		if(reuniao != null && reuniao.getId() != null) {
			return reuniaoRepository.save(reuniao);
		}
		
		return reuniao;
	}

	public void delete(Long id) {
		
		reuniaoRepository.deleteById(id);
		
	}

	public Reuniao findById(Long id) {
		
		Optional<Reuniao> optionalReuniao = reuniaoRepository.findById(id);
		
		if(optionalReuniao.isPresent()) {
			return optionalReuniao.get();
		} else {
			return null;
		}
		
	}
	
	private Reuniao dtoToReuniao(ReuniaoDTO reuniaoDTO) {
		Reuniao reuniao = new Reuniao();
		
		Colegiado colegiado = colegiadoService.findById(reuniaoDTO.getColegiadoId());
		if(colegiado != null) {
			reuniao.setColegiado(colegiado);
			
			return reuniao;
		}
		
		return null;
	}
	
}
