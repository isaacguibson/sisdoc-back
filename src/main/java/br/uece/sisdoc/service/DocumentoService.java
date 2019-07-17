package br.uece.sisdoc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import br.uece.sisdoc.dto.DocumentoDTO;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.repository.DocumentoRepository;
import br.uece.sisdoc.repository.TipoDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;
import br.uece.sisdoc.utils.HeaderFooterPageEvent;

@Service
public class DocumentoService {
	
	private final static int INIT_TEXT = 80;
	
	@Autowired
	DocumentoRepository documentoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	
	public Documento create(DocumentoDTO documentoDto) {
		
		Documento documento = dtoToDocumento(documentoDto);
		
		return documentoRepository.save(documento);
		
	}
	
	public String generateOficio(Long id) {
		
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
			
			generateHeader(writer, document, documento.getUsuario().getSetor().getNome());
			int pageNumber = generateOficioBody(writer, document, documento);
			documento.setTotalPages(pageNumber);
//			generateFooter(writer, document, documento);
			
			HeaderFooterPageEvent event = new HeaderFooterPageEvent(documento);
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
			
			HeaderFooterPageEvent event = new HeaderFooterPageEvent(documento);
			writer.setPageEvent(event);
			
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
	
}
