package br.uece.sisdoc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import br.uece.sisdoc.configuration.CustomUserDetailService;
import br.uece.sisdoc.configuration.CustomUserPrincipal;
import br.uece.sisdoc.dto.DocumentoDTO;
import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioDocumento;
import br.uece.sisdoc.repository.CargoRepository;
import br.uece.sisdoc.repository.DocumentoRepository;
import br.uece.sisdoc.repository.TipoDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioCargoRepository;
import br.uece.sisdoc.repository.UsuarioDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;
import br.uece.sisdoc.specification.DocumentoSpecification;
import br.uece.sisdoc.utils.HeaderFooterPageEvent;

@Service
public class DocumentoService {
	
	private final static int INIT_TEXT = 80;
	
	@Autowired
	DocumentoRepository documentoRepository;
	
	@Autowired
	CargoRepository cargoRepository;
	
	@Autowired
	UsuarioDocumentoRepository usuarioDocumentoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	UsuarioCargoRepository usuarioCargoRepository;

	@Autowired
	CustomUserDetailService customUserDetailService;
	
	@Transactional
	public Documento create(DocumentoDTO documentoDto) {
		
		try {
		
			Documento documento = dtoToDocumento(documentoDto);
			
			Documento documentoSaved = documentoRepository.save(documento);
			
			if(documentoSaved == null) {
				//Nao foi possivel salvar o documento
				return null;
			}
			
			
			//TODO Criar metodo de envio separado
			UsuarioDocumento usuarioDocumento = null;
			
			//Caso a lista de usuarios venha nula ou vazia enviar o documentos para todos
			//Deve ser tratado no front
			int documentosEnviados = 0;
			if(documentoDto.getDestinatariosIds() == null || documentoDto.getDestinatariosIds().isEmpty()) {
				
				List<Usuario> usuarios = usuarioRepository.findAll();
				
				for(Usuario usuarioDestino : usuarios) {
					
					usuarioDocumento = new UsuarioDocumento();
					
					usuarioDocumento.setUsuarioDestino(usuarioDestino);
					usuarioDocumento.setDocumento(documentoSaved);
					usuarioDocumento.setAbertaPeloUsuario(false);
					
					UsuarioDocumento usuarioDocumentoSalvo =  usuarioDocumentoRepository.save(usuarioDocumento);
					
					if(usuarioDocumentoSalvo != null) {
						documentosEnviados++;
					}
					
				}
				
				
			} else {
				
				for(Long destinatarioID : documentoDto.getDestinatariosIds()) {
					usuarioDocumento = new UsuarioDocumento();
					usuarioDocumento.setDocumento(documentoSaved);
					
					Optional<Usuario> usuarioDestinoOptional = usuarioRepository.findById(destinatarioID);
					
					if(usuarioDestinoOptional.isPresent()) {
						usuarioDocumento.setUsuarioDestino(usuarioDestinoOptional.get());
					} else {
						continue;
					}
					
					usuarioDocumento.setAbertaPeloUsuario(false);
					
					UsuarioDocumento usuarioDocumentoSalvo = usuarioDocumentoRepository.save(usuarioDocumento);
					
					if(usuarioDocumentoSalvo != null) {
						documentosEnviados++;
					}
				}
			}
			
			if(documentosEnviados == 0) {
				documentoRepository.delete(documentoSaved);
				
				//Nao foi possivel enviar este documento para nenhum usuarios
				return null;
			}
			
			return documentoSaved;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String generateOficio(Long id, Long cargoId) {
		
		Optional<Cargo> optCargo = cargoRepository.findById(cargoId);
		Cargo cargo = null;
		if(optCargo.isPresent()) {
			cargo = optCargo.get();
		} else {
			return null;
		}
		
		try {
			Documento documento = documentoRepository.getOne(id);
			
			String path = "/home/isaac/dev/iTextHelloWorld.pdf";
			
			File file = new File(path);
			file.createNewFile();
			
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
//			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
//			writer.setPageEvent(event);
			
			document.open();
			
//			generateHeader(writer, document, documento.getUsuario().getSetor().getNome());
			int pageNumber = generateOficioBody(writer, document, documento);
			documento.setTotalPages(pageNumber);
//			generateFooter(writer, document, documento);
			
			HeaderFooterPageEvent event = new HeaderFooterPageEvent(documento, cargo);
			writer.setPageEvent(event);
			
//			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//			Chunk chunk = new Chunk(documento.getConteudo(), font);
//			document.add(chunk);
			
			document.close();
			
			return path;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public Documento update(DocumentoDTO documentoDto) {
		
		Documento documento = dtoToDocumento(documentoDto);
		documento.setId(documentoDto.getId());
		
		if(documento.getId() != null) {
			Optional<Documento> optDocumentoToUpdate = documentoRepository.findById(documento.getId());
			
			if(!optDocumentoToUpdate.isPresent()) {
				return null;
			}
			
			documento = documentoRepository.save(documento);
			
			if(documento == null) {
				return null;
			}
			
			//Ids dos usuarios da lista de envio
			List<Long> idsDestinatariosParaEnviar = documentoDto.getDestinatariosIds();
			//Ids dos usuarios que ja estao no banco
			List<Long> idsDestinatariosExistentes = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
			
			//Usuarios que foram removidos da lista de envio
			List<Long> idsParaExcluir = new ArrayList<Long>();
			//Usuarios que fora adicionados na lista de envio
			List<Long> idsParaIncluir = new ArrayList<Long>();
			
			int usuariosChecados = idsDestinatariosParaEnviar.size() - 1;
			while(usuariosChecados >= 0) {
				if(!idsDestinatariosExistentes.contains(idsDestinatariosParaEnviar.get(usuariosChecados))) {
					idsParaIncluir.add(idsDestinatariosParaEnviar.get(usuariosChecados));
				}
				usuariosChecados--;
			}
			
			usuariosChecados = idsDestinatariosExistentes.size() - 1;
			while(usuariosChecados >= 0) {
				if(!idsDestinatariosParaEnviar.contains(idsDestinatariosExistentes.get(usuariosChecados))) {
					idsParaExcluir.add(idsDestinatariosExistentes.get(usuariosChecados));
				}
				usuariosChecados--;
			}
			
			UsuarioDocumento usuarioDocumentoParaIncluir = null;
			Usuario usuario = new Usuario();
			Optional<Usuario> optUsuario = null;
			for(Long idParaIncluir : idsParaIncluir) {
				usuarioDocumentoParaIncluir = new UsuarioDocumento();
				usuarioDocumentoParaIncluir.setDocumento(documento);
				
				optUsuario = usuarioRepository.findById(idParaIncluir);
				
				if(optUsuario.isPresent()) {
					usuario = optUsuario.get();
				} else {
					continue;
				}
				
				usuarioDocumentoParaIncluir.setUsuarioDestino(usuario);
				usuarioDocumentoParaIncluir.setAbertaPeloUsuario(false);
				usuarioDocumentoRepository.save(usuarioDocumentoParaIncluir);
			}
			
			for(Long idParaExcluir : idsParaExcluir) {
				List<UsuarioDocumento> usuarioDocumentos = usuarioDocumentoRepository.getUserDocByUserDestIdAndDocId(idParaExcluir, documento.getId());
			
				for(UsuarioDocumento usuDocParaExcluir : usuarioDocumentos) {
					usuarioDocumentoRepository.deleteById(usuDocParaExcluir.getId());
				}
			}
			
			return documento;
		}
		
		return null;
	}
	
	
	public void delete(Long id) {
		
		documentoRepository.deleteById(id);
		
	}
	
	
	public Page<Documento> findAll(Pageable pageable, DocumentoDTO documento) {
		
		DocumentoSpecification docSpecification = new DocumentoSpecification();
		
		try {
			Date dataInicial = new SimpleDateFormat("yyyy-MM-dd").parse(documento.getDataInicial());  
			Date dataFinal= new SimpleDateFormat("yyyy-MM-dd").parse(documento.getDataFinal());  
		
			return documentoRepository.findAll(Specification.where(
						docSpecification.filterByIdentificador(documento.getIdentificador())
					).and(docSpecification.filterByTipo(documento.getTipoDocumentoId()))
					.and(docSpecification.filterByDate(dataInicial, dataFinal))
					,pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public Page<Documento> findAllFromUser(Pageable pageable, Long id, Authentication authentication, DocumentoDTO documento) {
		
		CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) customUserDetailService.loadUserByUsername(authentication.getName());
		
		if(!customUserPrincipal.getUsuario().getId().equals(id)) {
			return null;
		}
		
		DocumentoSpecification docSpecification = new DocumentoSpecification();
		
		try {
			
			Date dataInicial = null;
			Date dataFinal = null;
			
			if(documento.getDataInicial() != null) {
				dataInicial = new SimpleDateFormat("yyyy-MM-dd").parse(documento.getDataInicial());
			}
			
			if(documento.getDataFinal() != null) {
				dataFinal = new SimpleDateFormat("yyyy-MM-dd").parse(documento.getDataFinal());
			}
			
			return documentoRepository.findAll(Specification.where(
						docSpecification.filterByUserId(id)
					).and(docSpecification.filterByTipo(documento.getTipoDocumentoId()))
					.and(docSpecification.filterByDate(dataInicial, dataFinal))
					.and(docSpecification.filterByIdentificador(documento.getIdentificador()))
					,pageable);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public Page<Documento> findAllToUser(Pageable pageable, Long id, Authentication authentication, DocumentoDTO documento) {
		
		CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) customUserDetailService.loadUserByUsername(authentication.getName());
		
		if(!customUserPrincipal.getUsuario().getId().equals(id)) {
			return null;
		}
		
		DocumentoSpecification docSpecification = new DocumentoSpecification();
		
		try {
			
			Date dataInicial = null;
			Date dataFinal = null;
			
			if(documento.getDataInicial() != null) {
				dataInicial = new SimpleDateFormat("yyyy-MM-dd").parse(documento.getDataInicial());
			}
			
			if(documento.getDataFinal() != null) {
				dataFinal = new SimpleDateFormat("yyyy-MM-dd").parse(documento.getDataFinal());
			}
			
			return documentoRepository.findAll(Specification.where(
					docSpecification.filterToUserId(id)
				).and(docSpecification.filterByTipo(documento.getTipoDocumentoId()))
				.and(docSpecification.filterByDate(dataInicial, dataFinal))
				.and(docSpecification.filterByIdentificador(documento.getIdentificador()))
				,pageable);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public Documento findById(Long id) {
		
		Optional<Documento> optionalDocumento = documentoRepository.findById(id);
		
		if(optionalDocumento.isPresent()) {
			return optionalDocumento.get();
		} else {
			return null;
		}
	}
	
	public DocumentoDTO findFullDocById(Long id) {
		
		Optional<Documento> optionalDocumento = documentoRepository.findById(id);
		
		if(optionalDocumento.isPresent()) {
			
			return documentoToDTO(optionalDocumento.get());
			
		} else {
			return null;
		}
	}
	
	private DocumentoDTO documentoToDTO(Documento documento) {
		DocumentoDTO documentoDTO = new DocumentoDTO();
		
		documentoDTO.setUsuarioId(documento.getUsuario().getId());
		documentoDTO.setConteudo(documento.getConteudo());
		documentoDTO.setId(documento.getId());
		documentoDTO.setTipoDocumentoId(documento.getTipoDocumento().getId());
		
		List<Long> destinatariosIds = new ArrayList<Long>();
		destinatariosIds = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
		documentoDTO.setDestinatariosIds(destinatariosIds);
		
		return documentoDTO;
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
		
		documento.setCodigo(generateCodDocumento()); //Nao esta no banco ainda
		documento.setIdentificador(documento.getCodigo()+"/"+calendar.get(Calendar.YEAR));
		
		documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll("<p", "<p align='justify'")); //Justificando paragrafos
		
		documento.setConteudo(documentoDTO.getConteudo());
		documento.setDataCriacao(calendar.getTime());
		
		return documento;
	}
	
	
	protected int generateHeader(PdfWriter writer, Document document, String setor) {
		String img_uece = "/home/isaac/dev/brasao_uece.jpg";
    	Image imageUece;
    	
    	String img_estado = "/home/isaac/dev/brasao_estado.jpg";
    	Image imageEstado;
    	
    	try {
    		
    		//ADICIONANDO IMAGEM UECE
    		imageUece = Image.getInstance(img_uece);
    		imageUece.setAlignment(Element.ALIGN_LEFT);
    		imageUece.setAbsolutePosition(50, 750);
    		imageUece.scalePercent(18.0f, 18.0f);
    		writer.getDirectContent().addImage(imageUece, true);
    		
    		//ADIOCIONANDO TEXTO CENTRAL
    		Phrase gov = new Phrase("GOVERNO DO ESTADO DO CEARÁ");
    		Phrase dep = new Phrase(setor.toUpperCase());
    		
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, gov, 295, 790, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, dep, 295, 770, 0);

    		//ADICIONANDO IMAGEM ESTADO
    		imageEstado = Image.getInstance(img_estado);
    		imageEstado.setAlignment(Element.ALIGN_LEFT);
    		imageEstado.setAbsolutePosition(500, 750);
    		imageEstado.scalePercent(2.5f, 2.5f);
    		writer.getDirectContent().addImage(imageEstado, true);
    		
    		PdfContentByte canvas = writer.getDirectContent();
    		CMYKColor blackColor = new CMYKColor(1.f, 1.f, 1.f, 0.f);
    		
    		canvas.setColorStroke(blackColor);
    		
    		canvas.moveTo(36, 740);
    		canvas.lineTo(560, 740);
    		canvas.closePathStroke();
    		
    		return document.getPageNumber();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return 0;
	}
	
	@SuppressWarnings("deprecation")
	protected int generateOficioBody(PdfWriter writer, Document document, Documento documento) {
		
		try {
			Paragraph identificador = new Paragraph("Oficio N°: "+documento.getIdentificador());
			
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, identificador, INIT_TEXT, 700, 0);
			
			Calendar calendar = Calendar.getInstance();
			
			Paragraph localData = new Paragraph("Fortaleza "+ calendar.get(Calendar.DAY_OF_MONTH) + " de " + getDia(calendar) + " de " + calendar.get(Calendar.YEAR));
			
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, localData, 460, 680, 0);
			
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			
			
			HTMLWorker htmlWorker = new HTMLWorker(document);
		    htmlWorker.parse(new StringReader(documento.getConteudo()));
			
//			Paragraph conteudo = new Paragraph(documento.getConteudo());
//			
//			conteudo.setAlignment(Element.ALIGN_JUSTIFIED);
////			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_JUSTIFIED, conteudo, 50, 640, 0);
//			
//			document.add(conteudo);
			
			return writer.getPageNumber();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	protected void generateFooter(PdfWriter writer, Document document, Documento documento) {
		try {
//			Paragraph footer =
//					new Paragraph(documento.getUsuario().getTratamento()+" "+documento.getUsuario().getNome() + "\n"+
//									documento.getUsuario().getCargo().getNome() + " do " + documento.getUsuario().getSetor().getNome() + " da UECE."
//								);
//			
//			footer.setAlignment(Element.ALIGN_CENTER);
//			document.add(footer);
			
//			HeaderFooterPageEvent event = new HeaderFooterPageEvent(documento);
//			writer.setPageEvent(event);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getDia(Calendar calendar){
		switch (calendar.get(Calendar.MONTH)) {
		case 0:
			return "Janeiro";
		case 1:
			return "Fevereiro";
		case 2:
			return "Março";
		case 3:
			return "Abril";
		case 4:
			return "Maio";
		case 5:
			return "Junho";
		case 6:
			return "Julho";
		case 7:
			return "Agosto";
		case 8:
			return "Setembro";
		case 9:
			return "Outubro";
		case 10:
			return "Novembro";
		case 11:
			return "Dezembro";
		default:
			return "";
		}
	}
	
	private Long generateCodDocumento(){
		
		//Pegando data até o primeiro dia do ano posterior
		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		
		Long lastCodDocumento = documentoRepository.ultimoCodDocumento(calendar.getTime());
		
		if(lastCodDocumento == null) {
			return 1L;
		}
		
		return lastCodDocumento + 1;
	}
	
}
