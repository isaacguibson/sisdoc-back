package br.uece.sisdoc.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uece.sisdoc.dto.DocumentoDTO;
import br.uece.sisdoc.dto.UsuarioDocumentoDTO;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.UsuarioDocumento;
import br.uece.sisdoc.service.DocumentoService;
import br.uece.sisdoc.service.UsuarioDocumentoService;

@RestController
@RequestMapping("/documento")
public class DocumentoController {

	@Autowired
	DocumentoService documentoService;
	
	@Autowired
	UsuarioDocumentoService usuarioDocumentoService;
	
	@GetMapping
	public Page<Documento> get(Pageable pageable, DocumentoDTO documentoDTO) {
		
		Page<Documento> pageReturn = documentoService.findAll(pageable, documentoDTO);
		
		return pageReturn;
	}
	
	@GetMapping("from-user/{id}")
	public Page<Documento> getFromUser(Pageable pageable, @PathVariable Long id, Authentication authentication, DocumentoDTO documentoDTO) {
		
		Page<Documento> pageReturn = documentoService.findAllFromUser(pageable, id, authentication, documentoDTO);
		
		return pageReturn;
	}
	
	@GetMapping("to-user/{id}")
	public Page<Documento> getToUser(Pageable pageable, @PathVariable Long id, Authentication authentication, DocumentoDTO documentoDTO) {
		
		Page<Documento> pageReturn = documentoService.findAllToUser(pageable, id, authentication, documentoDTO);
		
		return pageReturn;
	}
	
	@GetMapping("/{id}")
	public DocumentoDTO findById(@PathVariable Long id) {
		
		return documentoService.findFullDocById(id);
	}

	@GetMapping("/word/{id}")
	public ResponseEntity<byte[]> createWordFile(@PathVariable Long id, @RequestParam Long cargoId, @RequestParam Integer tipoDocumento) {
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = null;
			
			switch (tipoDocumento) {
				case 1:
					path = documentoService.generateOficio(id, cargoId);
					break;
				case 3:
					path = documentoService.generatePortaria(id, cargoId);
					break;
				case 4:
					path = documentoService.generateRequerimento(id, cargoId);
					break;
				case 5:
					path = documentoService.generateDespacho(id, cargoId);
					break;
				case 6:
					path = documentoService.generateDeclaracao(id, cargoId);
					break;
				case 7:
					path = documentoService.generateAta(id, cargoId);
					break;
			}
			
			if(path==null) {
				return null;
			}
			
			File file = new File(path);
			
			String docPath = documentoService.pdfToDoc(file);
			
			if(docPath==null) {
				return null;
			}
			
			File docFile = new File(docPath);
			
			byte[] contents = Files.readAllBytes(docFile.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/msword"));
			
			headers.setContentDispositionFormData("document.doc", "document.doc");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@PostMapping("/render")
	public ResponseEntity<byte[]> render(@RequestBody DocumentoDTO documentoDto, @RequestParam Long cargoId) {
		
		if(documentoDto == null) {
			return null;
		}
		
		if(documentoDto.getTipoDocumentoId() == null) {
			return null;
		}
		
		try {
			String path = "";
			if(documentoDto.getTipoDocumentoId() == 1) {
				path = documentoService.renderOficio(documentoDto, cargoId);
			} else if (documentoDto.getTipoDocumentoId() == 3) {
				path = documentoService.renderPortaria(documentoDto, cargoId);
			} else if (documentoDto.getTipoDocumentoId() == 4) {
				path = documentoService.renderRequerimento(documentoDto, cargoId);
			} else if (documentoDto.getTipoDocumentoId() == 5) {
				path = documentoService.renderDespacho(documentoDto, cargoId);
			} else if (documentoDto.getTipoDocumentoId() == 6) {
				path = documentoService.renderDeclaracao(documentoDto, cargoId);
			} else if (documentoDto.getTipoDocumentoId() == 7) {
				path = documentoService.renderAta(documentoDto, cargoId);
			} else if (documentoDto.getTipoDocumentoId() == 8) {
				path = documentoService.renderParecer(documentoDto, cargoId);
			} else {
				return null;
			}
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@GetMapping("/oficio/{id}")
	public ResponseEntity<byte[]> createDocumentFile(@PathVariable Long id, @RequestParam Long cargoId) {
		
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generateOficio(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@GetMapping("/portaria/{id}")
	public ResponseEntity<byte[]> createPortariaFile(@PathVariable Long id, @RequestParam Long cargoId) {
		
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generatePortaria(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@GetMapping("/declaracao/{id}")
	public ResponseEntity<byte[]> createDeclaracaoFile(@PathVariable Long id, @RequestParam Long cargoId) {
		
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generateDeclaracao(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@GetMapping("/despacho/{id}")
	public ResponseEntity<byte[]> createDespachoFile(@PathVariable Long id, @RequestParam Long cargoId) {
		
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generateDespacho(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@GetMapping("/parecer/{id}")
	public ResponseEntity<byte[]> createParecerFile(@PathVariable Long id, @RequestParam Long cargoId) {
		
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generateParecer(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@GetMapping("/requerimento/{id}")
	public ResponseEntity<byte[]> createRequerimentoFile(@PathVariable Long id, @RequestParam Long cargoId) {
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generateRequerimento(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@GetMapping("/ata/{id}")
	public ResponseEntity<byte[]> createAtaFile(@PathVariable Long id, @RequestParam Long cargoId) {
		if(cargoId == null) {
			return null;
		}
		
		try {
			String path = documentoService.generateAta(id, cargoId);
			
			File file = new File(path);
			
			byte[] contents = Files.readAllBytes(file.toPath());
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			
			headers.setContentDispositionFormData("document.pdf", "document.pdf");
			
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			
			ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			return response;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@PostMapping("/oficio")
	public Documento createOficio(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PostMapping("/portaria")
	public Documento createPortaria(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PostMapping("/declaracao")
	public Documento createDeclaracao(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PostMapping("/despacho")
	public Documento createDespacho(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PostMapping("/parecer")
	public Documento createParecer(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PostMapping("/requerimento")
	public Documento createRequerimento(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PostMapping("/ata")
	public Documento createAta(@RequestBody DocumentoDTO documento) {
		
		return documentoService.create(documento);
	}
	
	@PutMapping("send/{id}")
	public boolean send(@PathVariable Long id) {
		return documentoService.send(id);
	}
	
	@PutMapping("cancelsend/{id}")
	public boolean cancelSend(@PathVariable Long id) {
		return documentoService.cancelSend(id);
	}
	
	@PostMapping("/send-to-user")
	public List<UsuarioDocumento> sendToUser(@RequestBody UsuarioDocumentoDTO usuarioDocumento, Authentication authentication) {
		
		return usuarioDocumentoService.create(usuarioDocumento, authentication);
	}
	
	
	@PutMapping
	public Documento update(@RequestBody DocumentoDTO documento) {
		
		return documentoService.update(documento);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		if(!documentoService.delete(id)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
}
