package br.uece.sisdoc.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

import br.uece.sisdoc.dto.DocumentoDTO;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.repository.DocumentoRepository;
import br.uece.sisdoc.repository.TipoDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;

@Service
public class DocumentoService {
	
	@Autowired
	DocumentoRepository documentoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	
	public Documento create(DocumentoDTO documentoDto) {
		
		
		try {
			Documento documento = dtoToDocumento(documentoDto);
			
			File file = new File("/home/isaac/dev/iTextHelloWorld.pdf");
			file.createNewFile();
			
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
		
			document.open();
			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Chunk chunk = new Chunk(documento.getConteudo(), font);
			
			document.add(chunk);
			document.close();
			
			return documentoRepository.save(documento);
		
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (DocumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public Documento update(DocumentoDTO documentoDto) {
		
		Documento documento = dtoToDocumento(documentoDto);
		
		documento.setId(documentoDto.getId());
		
		return documentoRepository.save(documento);
	}
	
	
	public void delete(Long id) {
		
		documentoRepository.deleteById(id);
		
	}
	
	
	public Page<Documento> findAll(Pageable pageable) {
		
		return documentoRepository.findAll(pageable);
	}
	
	
	public Documento findById(Long id) {
		
		Optional<Documento> optionalDocumento = documentoRepository.findById(id);
		
		if(optionalDocumento.isPresent()) {
			return optionalDocumento.get();
		} else {
			return null;
		}
	}
	
	private Documento dtoToDocumento(DocumentoDTO documentoDTO) {
		Documento documento = new Documento();
		
		Calendar calendar = Calendar.getInstance();
		
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(documentoDTO.getUsuarioId());
		if(optionalUsuario.isPresent()) {
			documento.setUsuario(optionalUsuario.get());
		}
		
		Optional<TipoDocumento> optionalTipoDocumento = tipoDocumentoRepository.findById(documentoDTO.getTipoDocumentoId());
		if(optionalTipoDocumento.isPresent()) {
			documento.setTipoDocumento(optionalTipoDocumento.get());
		}
		
		documento.setCodigo(41L);
		documento.setIdentificador(documento.getCodigo().toString()+'/'+calendar.get(Calendar.YEAR));
		documento.setConteudo(documentoDTO.getConteudo());
		documento.setDataCriacao(calendar.getTime());
		
		return documento;
	}
	
}
