package br.uece.sisdoc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.uece.sisdoc.dto.CargoDTO;
import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.repository.CargoRepository;
import br.uece.sisdoc.repository.SetorRepository;
import br.uece.sisdoc.specification.CargoSpecification;

@Service
public class CargoService {
	
	@Autowired
	CargoRepository cargoRepository;
	
	@Autowired
	SetorRepository setorRepository;
	
	
	public Cargo create(CargoDTO cargoDto) {
		
		Cargo cargo = dtoToCargo(cargoDto);
		
		return cargoRepository.save(cargo);
	}
	
	public Cargo update(CargoDTO cargoDto) {
		
		if(cargoDto.getId() == null) {
			return null;
		}
		
		Cargo cargo = dtoToCargo(cargoDto);
		
		cargo.setId(cargoDto.getId());
		
		return cargoRepository.save(cargo);
	}

	public void delete(Long id) {
		
		cargoRepository.deleteById(id);
		
	}

	public Cargo findById(Long id) {
		
		Optional<Cargo> optionalCargo = cargoRepository.findById(id);
		
		if(optionalCargo.isPresent()) {
			return optionalCargo.get();
		} else {
			return null;
		}
		
	}
	
	public List<Cargo> findBySetor(Long setorId) {
		CargoSpecification cargoSpecification = new CargoSpecification();
		
		return cargoRepository.findAll(Specification.where(
				cargoSpecification.filterBySetor(setorId)
		));
	}
	
	public Page<Cargo> findAll(Pageable pageable, CargoDTO cargoDto) {
		
		CargoSpecification cargoSpecification = new CargoSpecification();
		
		return cargoRepository.findAll(Specification.where(
					cargoSpecification.filterById(cargoDto.getId())
				).and(cargoSpecification.filterByName(cargoDto.getNome()))
				.and(cargoSpecification.filterBySetor(cargoDto.getSetorId())),
				pageable);
		
	}
	
	
	private Cargo dtoToCargo(CargoDTO cargoDTO) {
		Cargo cargo = new Cargo();
		
		cargo.setNome(cargoDTO.getNome());
		
		Optional<Setor> optionalSetor = setorRepository.findById(cargoDTO.getSetorId());
		Setor setor = null;
		
		if(optionalSetor.isPresent()) {
			setor =  optionalSetor.get();
		}
		
		cargo.setSetor(setor);
		
		return cargo;
	}

}
