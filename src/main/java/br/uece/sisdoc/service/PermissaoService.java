package br.uece.sisdoc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.repository.CargoPermissaoRepository;

@Service
public class PermissaoService {

	@Autowired
	CargoPermissaoRepository cargoPermissaoRepository;
	
	public List<Long> obterPermissoesPeloCargo(Long cargoId) {
		if(cargoId==null) {
			return new ArrayList<Long>();
		}
		
		return cargoPermissaoRepository.getPermissoesDoCargo(cargoId);
	}
	
}
