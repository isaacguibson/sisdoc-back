package br.uece.sisdoc.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.text.pdf.PdfPTable;

import br.uece.sisdoc.configuration.CustomUserDetailService;
import br.uece.sisdoc.configuration.CustomUserPrincipal;
import br.uece.sisdoc.dto.DocumentoDTO;
import br.uece.sisdoc.dto.GenericListObject;
import br.uece.sisdoc.model.Cargo;
import br.uece.sisdoc.model.Documento;
import br.uece.sisdoc.model.DocumentoRotina;
import br.uece.sisdoc.model.Rotina;
import br.uece.sisdoc.model.Setor;
import br.uece.sisdoc.model.TipoDocumento;
import br.uece.sisdoc.model.Usuario;
import br.uece.sisdoc.model.UsuarioDocumento;
import br.uece.sisdoc.repository.CargoRepository;
import br.uece.sisdoc.repository.DocumentoRepository;
import br.uece.sisdoc.repository.DocumentoRotinaRepository;
import br.uece.sisdoc.repository.GenericListObjectRepository;
import br.uece.sisdoc.repository.RotinaRepository;
import br.uece.sisdoc.repository.SetorRepository;
import br.uece.sisdoc.repository.TipoDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioCargoRepository;
import br.uece.sisdoc.repository.UsuarioDocumentoRepository;
import br.uece.sisdoc.repository.UsuarioRepository;
import br.uece.sisdoc.specification.DocumentoRotinaSpecification;
import br.uece.sisdoc.specification.DocumentoSpecification;
import br.uece.sisdoc.specification.GenericListObjectSpecification;
import br.uece.sisdoc.utils.HeaderFooterPageEvent;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Service
public class DocumentoService {
	
	private final static int INIT_TEXT = 30;
	private final static String REPORTS_LOCATION = "C:\\Users\\Isaac\\documentos\\reports\\";
	private final static String EXPORTED_REPORT_LOCATION = "C:\\Users\\Isaac\\documentos\\exported_reports\\";
	private final static String HTML_LOCATION = "C:\\Users\\Isaac\\documentos\\htmls\\";
	
	@Autowired
	private Environment env;
	
	@Autowired
	DocumentoRepository documentoRepository;
	
	@Autowired
	DocumentoRotinaRepository documentoRotinaRepository;
	
	@Autowired
	RotinaRepository rotinaRepository;
	
	@Autowired
	CargoRepository cargoRepository;
	
	@Autowired
	SetorRepository setorRepository;
	
	@Autowired
	UsuarioDocumentoRepository usuarioDocumentoRepository;
	
	@Autowired
	GenericListObjectRepository genericListObjectRepository;
	
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
			
			if(documento == null) {
				return null;
			}
			
			documento.setEnviada(false);
			
			Documento documentoSaved = documentoRepository.save(documento);
			
			if(documentoSaved == null) {
				//Nao foi possivel salvar o documento
				return null;
			}
			
			//Caso a lista de usuarios venha nula ou vazia enviar o documentos para todos
			//Deve ser tratado no front
			int documentosEnviados = 0;
			if(documentoDto.getDestinatariosIds() == null || documentoDto.getDestinatariosIds().isEmpty() || documento.getMensagemGeral()) {
				
				if(documentoDto.getMensagemSetor()) {
					documentosEnviados = enviarMensagemParaTodosSetores(documentoSaved);
				} else {
					documentosEnviados = enviarMensagemParaTodos(documentoSaved);
				}
				
			} else {
				
				if(documentoDto.getMensagemSetor()) {
					documentosEnviados = enviarMensagemParaListaSetores(documentoSaved, documentoDto.getDestinatariosIds());
				} else {
					documentosEnviados = enviarMensagemParaListaUsuarios(documentoSaved, documentoDto.getDestinatariosIds());
				}
				
			}
			
			if(documentosEnviados == 0) {
				documentoRepository.delete(documentoSaved);
				
				//Nao foi possivel enviar este documento para nenhum usuarios
				return null;
			}
			
			//Se for um requerimento
			if(documentoDto.getTipoDocumentoId() == 4) {
				salvarRotinasRequerimento(documentoSaved, documentoDto);
				salvarOutrasRotinasRequerimento(documentoSaved, documentoDto);
				salvarOutrasInformacoesRequerimento(documentoSaved, documentoDto);
			}
			
			return documentoSaved;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void salvarRotinasRequerimento(Documento documento, DocumentoDTO documentoDTO) {
		if(documentoDTO.getRotinas() != null && documentoDTO.getRotinas().size() > 0) {
			
			List<DocumentoRotina> documentosRotina = new ArrayList<DocumentoRotina>();
			DocumentoRotinaSpecification docRotinaSpec = new DocumentoRotinaSpecification();
			documentosRotina
			  = documentoRotinaRepository.findAll(Specification.where(
					  docRotinaSpec.getByDocumento(documento)
				));
			
			if(documentosRotina.size()>0) {
				//apagando todas as rotinas deste documento
				for(DocumentoRotina docRotina : documentosRotina) {
					documentoRotinaRepository.delete(docRotina);
				}
			}
			
			for(Long rotinaID : documentoDTO.getRotinas()) {
				DocumentoRotina docRotina = new DocumentoRotina();
				docRotina.setDocumento(documento);
				Optional<Rotina> optRotina = rotinaRepository.findById(rotinaID);
				if(optRotina.isPresent()) {
					docRotina.setRotina(optRotina.get());
					documentoRotinaRepository.save(docRotina);
				} else {
					continue;
				}
			}
		}
	}
	
	private void salvarOutrasRotinasRequerimento(Documento documento, DocumentoDTO documentoDTO) {
		List<GenericListObject> genericObjectList = new ArrayList<GenericListObject>();
		if(documentoDTO.getOutrasRotinas() != null && documentoDTO.getOutrasRotinas().size() > 0) {
			GenericListObjectSpecification genericListObjectSpec = new GenericListObjectSpecification();
			genericObjectList =
					genericListObjectRepository.findAll(Specification.where(
							genericListObjectSpec.findByDocumento(documento))
							.and(genericListObjectSpec.findByType("ROTINA")));
			if(genericObjectList.size() > 0) {
				//Apagando todos os dados existentes para aquele objeto
				for(GenericListObject generic : genericObjectList) {
					genericListObjectRepository.delete(generic);
				}
			}
			
			//Salvando os novos objetos
			if(documentoDTO.getOutrasRotinas() != null
				&& documentoDTO.getOutrasRotinas().size() > 0) {
				for(GenericListObject generic : documentoDTO.getOutrasRotinas()) {
					generic.setDocumento(documento);
					generic.setType("ROTINA");
					genericListObjectRepository.save(generic);
				}
			}
		}
	}
	
	private void salvarOutrasInformacoesRequerimento(Documento documento, DocumentoDTO documentoDTO) {
		List<GenericListObject> genericObjectList = new ArrayList<GenericListObject>();
		if(documentoDTO.getInformacoes() != null && documentoDTO.getInformacoes().size() > 0) {
			GenericListObjectSpecification genericListObjectSpec = new GenericListObjectSpecification();
			genericObjectList =
					genericListObjectRepository.findAll(Specification.where(
							genericListObjectSpec.findByDocumento(documento))
							.and(genericListObjectSpec.findByType("INFORMACAO")));
			if(genericObjectList.size() > 0) {
				//Apagando todos os dados existentes para aquele objeto
				for(GenericListObject generic : genericObjectList) {
					genericListObjectRepository.delete(generic);
				}
			}
			
			//Salvando os novos objetos
			if(documentoDTO.getInformacoes() != null
				&& documentoDTO.getInformacoes().size() > 0) {
				for(GenericListObject generic : documentoDTO.getInformacoes()) {
					generic.setDocumento(documento);
					generic.setType("INFORMACAO");
					genericListObjectRepository.save(generic);
				}
			}
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
			
			String path = env.getProperty("default-path-pdf");
			
			File file = new File(path);
			file.createNewFile();
			
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			
			generateHeader(writer, document, cargo.getSetor().getNome());
			List<Long> destinatariosIds = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
			int pageNumber = generateOficioBody(writer, document, documento, cargo, destinatariosIds, documento.getMensagemGeral(), documento.getMensagemSetor());
			documento.setTotalPages(pageNumber);
			
			HeaderFooterPageEvent event = new HeaderFooterPageEvent(documento, cargo);
			writer.setPageEvent(event);
			
			document.close();
			
			return path;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String generatePortaria(Long id, Long cargoId) {
		
		Optional<Cargo> optCargo = cargoRepository.findById(cargoId);
		Cargo cargo = null;
		if(optCargo.isPresent()) {
			cargo = optCargo.get();
		} else {
			return null;
		}
		
		try {
			Documento documento = documentoRepository.getOne(id);
			
			String path = env.getProperty("default-path-pdf");
			
			File file = new File(path);
			file.createNewFile();
			
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			
			List<Long> destinatariosIds = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
			int pageNumber = generatePortariaBody(writer, document, documento, cargo, destinatariosIds, documento.getMensagemGeral(), documento.getMensagemSetor());
			documento.setTotalPages(pageNumber);
			
			document.close();
			
			return path;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String generateAta(Long id, Long cargoId) {
		try {
			Documento documento = findById(id);
			Cargo cargo = null;
			
			if(documento == null) {
				return null;
			} 
			
			Optional<Cargo> optCargo = cargoRepository.findById(cargoId);
			
			if(optCargo.isPresent()) {
				cargo = optCargo.get();
			} else {
				return null;
			}
			
			JasperReport jasper = JasperCompileManager.compileReport(REPORTS_LOCATION+"ata.jrxml");
			Map<String, Object> map = new HashMap<>();
			
			map.put("TITULO", "ATA DA REUNIÃO DO COLEGIADO DO CURSO DE CIÊNCIA DA COMPUTAÇÂO DA UNIVERSIDADE ESTADUAL DO CEARÁ. REALIZADA EM 12/09/2012.");
			map.put("CONTEUDO", "Aos doze dias do mês de Setembro do ano de dois mil e doze, na sala A da Pós-Graduação em Computação, foi realizada às quatorze horas e quinze minutos, a reunião do Colegiado do Curso de Computação com a presença de 15 docentes e 07 discentes que formam o Aos doze dias do mês de Setembro do ano de dois mil e doze, na sala A da Pós-Graduação em Computação, foi realizada às quatorze horas e quinze minutos, a reunião do Colegiado do Curso de Computação com a presença de 15 docentes e 07 discentes que formam o");
			map.put("LOCAL_E_DATA", "Fortaleza, 28 de Janeiro de 2020");
			
//			List<MembroColegiado> membros = new ArrayList<MembroColegiado>();
//			
//			MembroColegiado membro1 = new MembroColegiado("Isaac Guibson", "Diretor de Marketing da Reitoria");
//			membros.add(membro1);
//			
//			MembroColegiado membro2 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro2);
//			
//			MembroColegiado membro3 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro3);
//			
//			MembroColegiado membro4 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro4);
//			
//			MembroColegiado membro5 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro5);
//			
//			MembroColegiado membro6 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro6);
//			
//			MembroColegiado membro7 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro7);
//			
//			MembroColegiado membro8 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro8);
//			
//			MembroColegiado membro9 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro9);
//			
//			MembroColegiado membro10 = new MembroColegiado("Luan Marques", "Vice-Presidente de Marketing da Reitoria");
//			membros.add(membro10);
			
//			JRDataSource jrds = new JRBeanCollectionDataSource(membros);
//			map.put("LIST", jrds);
			
			JRDataSource jrDataSource = new JREmptyDataSource();
			
			JasperPrint print = JasperFillManager.fillReport(jasper, map, jrDataSource);
			
			JasperExportManager.exportReportToPdfFile(print, EXPORTED_REPORT_LOCATION+"ata.pdf");
			
			File file = new File(EXPORTED_REPORT_LOCATION+"ata.pdf");
			
            return file.getAbsolutePath();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String generateRequerimento(Long id, Long cargoId) {
		
		try {
			
			Documento documento = findById(id);
			Cargo cargo = null;
			
			if(documento == null) {
				return null;
			} 
			
			Optional<Cargo> optCargo = cargoRepository.findById(cargoId);
			
			if(optCargo.isPresent()) {
				cargo = optCargo.get();
			} else {
				return null;
			}
			
			JasperReport jasper = JasperCompileManager.compileReport(REPORTS_LOCATION+"requerimento.jrxml");
			Map<String, Object> map = new HashMap<>();
			
			map.put("NOME", documento.getUsuario().getNome().toUpperCase());
			map.put("VINCULO", documento.getVinculo());
			map.put("SETOR", cargo.getSetor().getSigla());
			map.put("CURSO", documento.getUsuario().getCurso());
			map.put("MATRICULA", documento.getUsuario().getMatricula());
			map.put("REQUERIDO", documento.getRequerido());
			
			DocumentoDTO documentoDTO = documentoToDTO(documento);
			
			List<Long> rotinasJasper = new ArrayList<Long>();
			if(documentoDTO.getRotinas() != null && documentoDTO.getRotinas().size() > 0) {
				for(Long rotinaId : documentoDTO.getRotinas()) {
					rotinasJasper.add(rotinaId);
				}
			}
			map.put("ROTINAS", rotinasJasper);
			
			if(documentoDTO != null) {
				if(documentoDTO.getOutrasRotinas() != null) {
					for(int index = 0; index < documentoDTO.getOutrasRotinas().size(); index++) {
						if(index == 0) {
							map.put("O_ROT_1", documentoDTO.getOutrasRotinas().get(index).getLabel());
						} else if (index == 1) {
							map.put("O_ROT_2", documentoDTO.getOutrasRotinas().get(index).getLabel());
						} else if (index == 2) {
							map.put("O_ROT_3", documentoDTO.getOutrasRotinas().get(index).getLabel());
						} else if (index == 3) {
							map.put("O_ROT_4", documentoDTO.getOutrasRotinas().get(index).getLabel());
						} else if(index == 4) {
							map.put("O_ROT_5", documentoDTO.getOutrasRotinas().get(index).getLabel());
						} else {
							break;
						}
					}
				}
				
				if(documentoDTO.getInformacoes() != null) {
					for(int index = 0; index < documentoDTO.getInformacoes().size(); index++) {
						if(index == 0) {
							map.put("INFO_1", documentoDTO.getInformacoes().get(index).getLabel());
						} else if (index == 1) {
							map.put("INFO_2", documentoDTO.getInformacoes().get(index).getLabel());
						} else if (index == 2) {
							map.put("INFO_3", documentoDTO.getInformacoes().get(index).getLabel());
						} else if (index == 3) {
							map.put("INFO_4", documentoDTO.getInformacoes().get(index).getLabel());
						} else {
							break;
						}
					}
				}
			}
			
			JRDataSource jrDataSource = new JREmptyDataSource();
			
			JasperPrint print = JasperFillManager.fillReport(jasper, map, jrDataSource);
			
//			JRExporter exporter = new JRPdfExporter();
//			
//			File file = new File("C:\\Users\\Isaac\\requerimento.pdf");
//			file.createNewFile();
//			
//			OutputStream saida = new FileOutputStream(file);
//			
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, saida);
//
//            exporter.exportReport();
			
			JasperExportManager.exportReportToPdfFile(print, EXPORTED_REPORT_LOCATION+"requerimento.pdf");
			
			File file = new File(EXPORTED_REPORT_LOCATION+"requerimento.pdf");
			
            return file.getAbsolutePath();
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String generateDeclaracao(Long id, Long cargoId) {
		
		Optional<Cargo> optCargo = cargoRepository.findById(cargoId);
		Cargo cargo = null;
		if(optCargo.isPresent()) {
			cargo = optCargo.get();
		} else {
			return null;
		}
		
		try {
			Documento documento = documentoRepository.getOne(id);
			
			String path = env.getProperty("default-path-pdf");
			
			File file = new File(path);
			file.delete();
			file.createNewFile();
			
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			
			generateDeclaracaoHeader(writer, document, cargo.getSetor().getNome());
			List<Long> destinatariosIds = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
			int pageNumber = generateDeclaracaoBody(writer, document, documento, cargo, destinatariosIds, documento.getMensagemGeral(), documento.getMensagemSetor());
			documento.setTotalPages(pageNumber);
			
			document.close();
			
			return path;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String generateDespacho(Long id, Long cargoId) {
		
		Optional<Cargo> optCargo = cargoRepository.findById(cargoId);
		Cargo cargo = null;
		if(optCargo.isPresent()) {
			cargo = optCargo.get();
		} else {
			return null;
		}
		
		try {
			Documento documento = documentoRepository.getOne(id);
			
			String path = env.getProperty("default-path-pdf");
			
			File file = new File(path);
			file.delete();
			file.createNewFile();
			
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			
			generateDespachoHeader(writer, document, cargo.getSetor().getNome());
			List<Long> destinatariosIds = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
			int pageNumber = generateDespachoBody(writer, document, documento, cargo, destinatariosIds, documento.getMensagemGeral(), documento.getMensagemSetor());
			documento.setTotalPages(pageNumber);
			
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
			
			Documento documentoToUpdate = null;
			if(!optDocumentoToUpdate.isPresent()) {
				return null;
			} else {
				documentoToUpdate = optDocumentoToUpdate.get();
			}
			
			documentoToUpdate.setConteudo(documento.getConteudo());
			documentoToUpdate.setMensagemGeral(documento.getMensagemGeral());
			documentoToUpdate.setMensagemSetor(documento.getMensagemSetor());
			documentoToUpdate.setAssunto(documento.getAssunto());
			
			documento = documentoRepository.save(documentoToUpdate);
			
			if(documento == null) {
				return null;
			}
			
			//Se for um requerimento
			if(documentoDto.getTipoDocumentoId() == 4) {
				salvarRotinasRequerimento(documento, documentoDto);
				salvarOutrasRotinasRequerimento(documento, documentoDto);
				salvarOutrasInformacoesRequerimento(documento, documentoDto);
			}
			
			//Ids dos usuarios da lista de envio
			List<Long> idsDestinatariosParaEnviar = documentoDto.getDestinatariosIds();
			
			if(idsDestinatariosParaEnviar == null || idsDestinatariosParaEnviar.isEmpty() || documento.getMensagemGeral()) {
				
				excluirTodosOsEnvios(documento);
				
				if(documentoDto.getMensagemSetor()) {
					enviarMensagemParaTodosSetores(documento);
				} else {
					enviarMensagemParaTodos(documento);
				}
				
				
				
			} else if (documentoDto.getMensagemSetor()) {
				
				excluirTodosOsEnvios(documento);
				enviarMensagemParaListaSetores(documento, idsDestinatariosParaEnviar);
				
			} else {
				
				excluirTodosOsEnvios(documento);
				enviarMensagemParaListaUsuarios(documento, idsDestinatariosParaEnviar);
				
			}
			
			return documento;
		}
		
		return null;
	}
	
	public boolean send(Long id) {
		
		if(id != null) {
			Optional<Documento> optDocumento = documentoRepository.findById(id);
			
			if(optDocumento.isPresent()) {
				
				Documento documento = optDocumento.get();
				
				documento.setEnviada(true);
				
				documentoRepository.save(documento);
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean cancelSend(Long id) {
		
		if(id != null) {
			Optional<Documento> optDocumento = documentoRepository.findById(id);
			
			if(optDocumento.isPresent()) {
				
				Documento documento = optDocumento.get();
				
				documento.setEnviada(false);
				
				documentoRepository.save(documento);
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean delete(Long id) {
		
		Documento documento = null;
		Optional<Documento> optDocumento = documentoRepository.findById(id);
		
		if(optDocumento.isPresent()) {
			documento = optDocumento.get();
		} else {
			return false;
		}
		
		excluirTodosOsEnvios(documento);
		documentoRepository.deleteById(id);
		
		return true;
		
	}
	
	private void excluirTodosOsEnvios(Documento documento) {
		
		List<UsuarioDocumento> usuarioDocumentos = usuarioDocumentoRepository.getUserDocByDocId(documento.getId());
		
		if(usuarioDocumentos != null && !usuarioDocumentos.isEmpty()) {
			for(UsuarioDocumento usuarioDocumento : usuarioDocumentos) {
				usuarioDocumentoRepository.delete(usuarioDocumento);
			}
		}
		
	}
	
	private void excluirEnviosByUserIds(Documento documento, List<Long> idsParaExcluir) {
		
		for(Long idParaExcluir : idsParaExcluir) {
			List<UsuarioDocumento> usuarioDocumentos = usuarioDocumentoRepository.getUserDocByUserDestIdAndDocId(idParaExcluir, documento.getId());
		
			for(UsuarioDocumento usuDocParaExcluir : usuarioDocumentos) {
				usuarioDocumentoRepository.deleteById(usuDocParaExcluir.getId());
			}
		}
		
	}
	
	private int enviarMensagemParaListaSetores(Documento documento, List<Long> setoresIds) {
		//Caso a lista de usuarios venha nula ou vazia enviar o documentos para todos
		//Deve ser tratado no front
		int documentosEnviados = 0;
		
		if(setoresIds != null && !setoresIds.isEmpty()) {
			
			Optional<Setor> optSetor = null;
			UsuarioDocumento usuarioDocumento = null;
			List<Usuario> usuariosDestino = new ArrayList<Usuario>();
			UsuarioDocumento usuarioDocumentoSalvo = null;
			
			for(Long setorId : setoresIds) {
				optSetor = setorRepository.findById(setorId);
				
				if(optSetor.isPresent()) {
					
					usuariosDestino = usuarioRepository.getPrincipalUsersFromSetor(setorId);
					
					if(usuariosDestino != null) {
						for(Usuario usuarioDestino : usuariosDestino) {
							
							usuarioDocumento = new UsuarioDocumento();
							
							usuarioDocumento.setDocumento(documento);
							usuarioDocumento.setUsuarioDestino(usuarioDestino);
							usuarioDocumento.setAbertaPeloUsuario(false);
							usuarioDocumento.setSetor(optSetor.get());
							
							usuarioDocumentoSalvo = usuarioDocumentoRepository.save(usuarioDocumento);
							
							if(usuarioDocumentoSalvo != null) {
								documentosEnviados++;
							
							}
						}
					}
					
				}
			}
		}
		
		return documentosEnviados;
	}
	
	private int enviarMensagemParaTodosSetores(Documento documento) {
		
		List<Usuario> usuariosDestino = null;
		UsuarioDocumento usuarioDocumento = null;
		int documentosEnviados = 0;
		UsuarioDocumento usuarioDocumentoSalvo = null;
		
		List<Setor> setores = setorRepository.findAll();
		
		if(setores != null) {
			for(Setor setor : setores) {
				
				usuariosDestino = usuarioRepository.getPrincipalUsersFromSetor(setor.getId());
				
				if(usuariosDestino != null) {
					for(Usuario usuarioDestino : usuariosDestino) {
						
						usuarioDocumento = new UsuarioDocumento();
						
						usuarioDocumento.setDocumento(documento);
						usuarioDocumento.setUsuarioDestino(usuarioDestino);
						usuarioDocumento.setAbertaPeloUsuario(false);
						usuarioDocumento.setSetor(setor);
						
						usuarioDocumentoSalvo = usuarioDocumentoRepository.save(usuarioDocumento);
						
						if(usuarioDocumentoSalvo != null) {
							documentosEnviados++;
						
						}
					}
				}
				
			}
		}
		
		return documentosEnviados;
		
	}
	
	private int enviarMensagemParaTodos(Documento documento) {
		
		UsuarioDocumento usuarioDocumento = null;
		
		//Caso a lista de usuarios venha nula ou vazia enviar o documentos para todos
		//Deve ser tratado no front
		int documentosEnviados = 0;
		
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		for(Usuario usuarioDestino : usuarios) {
			
			usuarioDocumento = new UsuarioDocumento();
			
			usuarioDocumento.setUsuarioDestino(usuarioDestino);
			usuarioDocumento.setDocumento(documento);
			usuarioDocumento.setAbertaPeloUsuario(false);
			
			UsuarioDocumento usuarioDocumentoSalvo =  usuarioDocumentoRepository.save(usuarioDocumento);
			
			if(usuarioDocumentoSalvo != null) {
				documentosEnviados++;
			}
			
		}
		
		return documentosEnviados;
	}
	
	private int enviarMensagemParaListaUsuarios(Documento documento, List<Long> ids) {
		//Caso a lista de usuarios venha nula ou vazia enviar o documentos para todos
		//Deve ser tratado no front
		int documentosEnviados = 0;
		
		UsuarioDocumento usuarioDocumento = null;
		
		for(Long destinatarioID : ids) {
			usuarioDocumento = new UsuarioDocumento();
			usuarioDocumento.setDocumento(documento);
			
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
		
		return documentosEnviados;
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
					,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id")));
		
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
				.and(docSpecification.filterSendeds())
				.and(docSpecification.filterByIdentificador(documento.getIdentificador()))
				,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id")));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Documento findById(Long id) {
		
		Optional<Documento> optionalDocumento = documentoRepository.findById(id);
		
		if(optionalDocumento.isPresent()) {
			
			Documento documento = optionalDocumento.get();
			
			//REQUERIMENTO
			//Recuperando as rotinas
			if(documento.getTipoDocumento().getId().equals(4L)) {
				List<Rotina> rotinas = new ArrayList<Rotina>();
				List<DocumentoRotina> documentosRotina = new ArrayList<DocumentoRotina>();
				
				DocumentoRotinaSpecification docRotSpec = new DocumentoRotinaSpecification();
				documentosRotina = documentoRotinaRepository.findAll(Specification.where(
							docRotSpec.getByDocumento(documento)
						));
				if(documentosRotina != null) {
					if(documentosRotina.size() > 0) {
						for (DocumentoRotina docRot : documentosRotina) {
							rotinas.add(docRot.getRotina());
						}
					}
				}
				
				documento.setRotinas(rotinas);
			}
			
			return documento;
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
		
		documentoDTO.setMensagemGeral(documento.getMensagemGeral());
		
		if(!documento.getMensagemGeral()) {
			if(documento.getMensagemSetor()) {
				List<Setor> setoresDestinatarios = usuarioDocumentoRepository.getSetoresDoDoc(documento.getId());
				
				if(setoresDestinatarios != null && !setoresDestinatarios.isEmpty()) {
					for(Setor setorDestino : setoresDestinatarios) {
						
						if(!destinatariosIds.contains(setorDestino.getId())) {
							destinatariosIds.add(setorDestino.getId());
						}
						
					}
				}
				
			} else {
				destinatariosIds = usuarioDocumentoRepository.getDestinatariosDoDoc(documento.getId());
			}
			
		}
		
		documentoDTO.setDestinatariosIds(destinatariosIds);
		documentoDTO.setMensagemSetor(documento.getMensagemSetor());
		documentoDTO.setAssunto(documento.getAssunto());
		documentoDTO.setRequerido(documento.getRequerido());
		documentoDTO.setVinculo(documento.getVinculo());
		
		//REQUERIMENTO
		if(documento.getTipoDocumento().getId() == 4) {
			//BUSCANDO ROTINAS
			documentoDTO.setRotinas(new ArrayList<Long>());
			List<DocumentoRotina> documentosRotina = new ArrayList<DocumentoRotina>();
			DocumentoRotinaSpecification docRotinaSpec = new DocumentoRotinaSpecification();
			documentosRotina
			  = documentoRotinaRepository.findAll(Specification.where(
					  docRotinaSpec.getByDocumento(documento)
				));
			
			if(documentosRotina.size()>0) {
				for(DocumentoRotina docRotina : documentosRotina) {
					documentoDTO.getRotinas().add(docRotina.getRotina().getId());
				}
			}
			
			//BUSCANDO OUTRAS ROTINAS
			documentoDTO.setOutrasRotinas(new ArrayList<GenericListObject>());
			GenericListObjectSpecification genericObjSpec = new GenericListObjectSpecification();
			List<GenericListObject> genericList = new ArrayList<GenericListObject>();
			genericList = genericListObjectRepository.findAll(Specification.where(
							genericObjSpec.findByDocumento(documento)
						 ).and(genericObjSpec.findByType("ROTINA")));
			if(genericList.size()>0) {
				documentoDTO.setOutrasRotinas(genericList);
			}
			
			//BUSCANDO INFORMACOES
			documentoDTO.setInformacoes(new ArrayList<GenericListObject>());
			genericObjSpec = new GenericListObjectSpecification();
			List<GenericListObject> genericInfoList = new ArrayList<GenericListObject>();
			genericInfoList = genericListObjectRepository.findAll(Specification.where(
							genericObjSpec.findByDocumento(documento)
						 ).and(genericObjSpec.findByType("INFORMACAO")));
			if(genericInfoList.size()>0) {
				documentoDTO.setInformacoes(genericInfoList);
			}
		}
		
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
		} else {
			return null;
		}
		
		documento.setCodigo(generateCodDocumento(documento.getTipoDocumento().getId())); //Nao esta no banco ainda
		documento.setIdentificador(documento.getCodigo()+"/"+calendar.get(Calendar.YEAR));
		
		// Apenas para documentos com conteudo
		if(documentoDTO.getConteudo() != null && !documentoDTO.equals("")) {
			documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll(" class=\"ql-align-justify\"", ""));
			documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll(" align='justify'", ""));
			documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", "<p>"));
			documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll("<p>", "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
			documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll("<p", "<p align='justify'")); //Justificando paragrafos
			documentoDTO.setConteudo(documentoDTO.getConteudo().replaceAll("<br>", "<br />")); //Justificando paragrafos
		}
		
		documento.setConteudo(documentoDTO.getConteudo());
		documento.setDataCriacao(calendar.getTime());
		
		documento.setMensagemGeral(documentoDTO.getMensagemGeral());
		documento.setMensagemSetor(documentoDTO.getMensagemSetor());
		documento.setAssunto(documentoDTO.getAssunto());
		documento.setRequerido(documentoDTO.getRequerido());
		documento.setVinculo(documentoDTO.getVinculo());
		
		return documento;
	}
	
	protected int generateHeader(PdfWriter writer, Document document, String setor) {
		
    	Image imageUece;
    	Image imageEstado;
    	
    	try {
    		
    		//ADICIONANDO IMAGEM UECE
    		imageUece = Image.getInstance(env.getProperty("default-image-uece"));
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
    		imageEstado = Image.getInstance(env.getProperty("default-image-estado"));
    		imageEstado.setAlignment(Element.ALIGN_LEFT);
    		imageEstado.setAbsolutePosition(500, 750);
    		imageEstado.scalePercent(18.0f, 18.0f);
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
	protected int generateOficioBody(PdfWriter writer, Document document, Documento documento, Cargo cargo, List<Long> destinatariosIds, Boolean isMensagemGeral, Boolean isMensagemSetor) {
		
		try {
			
			Paragraph identificador = new Paragraph("Oficio N°: "+documento.getIdentificador());
			
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, identificador, INIT_TEXT, 700, 0);
			
			Calendar calendar = Calendar.getInstance();
			
			Paragraph localData = new Paragraph("Fortaleza "+ calendar.get(Calendar.DAY_OF_MONTH) + " de " + getDia(calendar) + " de " + calendar.get(Calendar.YEAR));
			
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, localData, 560, 680, 0);
			
			Paragraph deQuem = new Paragraph("De: "+documento.getUsuario().getTratamento()+" "+documento.getUsuario().getNome()+" - "+cargo.getNome() + " do " + cargo.getSetor().getNome());
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, deQuem, INIT_TEXT, 660, 0);
			
			if(isMensagemGeral) {
				Paragraph saudacoes = new Paragraph("Prezados,");
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, saudacoes, INIT_TEXT, 0, 0);
			} else if (isMensagemSetor) {
				if(!destinatariosIds.isEmpty()) {
					if(destinatariosIds.size() > 1) {
						Paragraph saudacoes = new Paragraph("Prezados,");
						ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, saudacoes, INIT_TEXT, 0, 0);
					} else {
						
						Paragraph saudacoes = new Paragraph("Prezado(a),");
						ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, saudacoes, INIT_TEXT, 0, 0);
					}
				}
			} else {
				if(!destinatariosIds.isEmpty()) {
					if(destinatariosIds.size() > 1) {
						
					} else {
						
						Paragraph saudacoes = new Paragraph("Prezado(a),");
						ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, saudacoes, INIT_TEXT, 600, 0);
						
						Optional<Usuario> optUsuarioDestino = usuarioRepository.findById(destinatariosIds.get(0));
						
						if(optUsuarioDestino.isPresent()) {
						
							List<Cargo> cargosDestinatario = usuarioCargoRepository.getUserCargos(optUsuarioDestino.get().getId());
							Cargo cargoDestinatario = null;
							
							if(cargosDestinatario.size() > 1) {
								for(Cargo cargoDest : cargosDestinatario) {
									if(cargoDest.getIsCargoPrincipal()) {
										cargoDestinatario = cargoDest;
									}
								}
							} else if (cargosDestinatario.size() == 1) {
								cargoDestinatario = cargosDestinatario.get(0);
							}
							
							if(cargoDestinatario != null) {
								Paragraph paraQuem = new Paragraph("Para: "+optUsuarioDestino.get().getTratamento()+" "+optUsuarioDestino.get().getNome()+" - "+cargoDestinatario.getNome() + " do " + cargoDestinatario.getSetor().getNome());
								ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, paraQuem, INIT_TEXT, 640, 0);
							}
						
						}
					}
				}
			}
			
			if(documento.getAssunto() != null && !documento.getAssunto().equals("")) {
				Paragraph assunto = new Paragraph("Assunto: "+documento.getAssunto());
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, assunto, INIT_TEXT, 620, 0);
			}
			
			
			
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
			document.add(new Phrase("\n"));
			
			
			//HTMLWorker htmlWorker = new HTMLWorker(document);
			//htmlWorker.parse(new StringReader(documento.getConteudo()));
			
			CSSResolver cssResolver =
	                XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
			
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
	        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
	        htmlContext.autoBookmark(false);
	        
	        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
	        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
	        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
	        
	        XMLWorker worker = new XMLWorker(css, true);
	        XMLParser p = new XMLParser(worker);
	        
	        File file = generateHtmlFile(documento);
	        
	        p.parse(new FileInputStream(file));
		    
		    document.add(new Phrase("\n"));
			document.add(new Phrase("Atenciosamente,"));
			document.add(new Phrase("\n"));
			document.add(new Phrase(documento.getUsuario().getNome()));
			
			file.delete();
			return writer.getPageNumber();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	private int generateDeclaracaoHeader(PdfWriter writer, Document document, String setor) {
		
    	Image imageUece;
    	Image imageEstado;
    	
    	try {
    		
    		PdfContentByte canvas = writer.getDirectContent();
    		CMYKColor blackColor = new CMYKColor(1.f, 1.f, 1.f, 0.f);
    		
    		canvas.setColorStroke(blackColor);
    		
    		canvas.moveTo(36, 730);
    		canvas.lineTo(560, 730);
    		canvas.closePathStroke();
    		
    		//ADICIONANDO IMAGEM UECE
    		imageUece = Image.getInstance(env.getProperty("default-image-uece"));
    		imageUece.setAlignment(Element.ALIGN_LEFT);
    		imageUece.setAbsolutePosition(50, 750);
    		imageUece.scalePercent(18.0f, 18.0f);
    		writer.getDirectContent().addImage(imageUece, true);
    		
    		//ADIOCIONANDO TEXTO CENTRAL
    		Phrase gov = new Phrase("GOVERNO DO ESTADO DO CEARÁ");
    		Phrase dep = new Phrase(setor.toUpperCase());
    		Phrase uni = new Phrase("UNIVERSIDADE ESTADUAL DO CEARÁ - UECE");
    		
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, gov, 295, 790, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, dep, 295, 770, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, uni, 295, 750, 0);

    		//ADICIONANDO IMAGEM ESTADO
    		imageEstado = Image.getInstance(env.getProperty("default-image-estado"));
    		imageEstado.setAlignment(Element.ALIGN_LEFT);
    		imageEstado.setAbsolutePosition(500, 750);
    		imageEstado.scalePercent(18.0f, 18.0f);
    		writer.getDirectContent().addImage(imageEstado, true);
    		
    		return document.getPageNumber();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return 0;
	}
	
	private int generateDespachoHeader(PdfWriter writer, Document document, String setor) {
		
    	Image imageUece;
    	Image imageEstado;
    	
    	try {
    		
    		PdfContentByte canvas = writer.getDirectContent();
    		CMYKColor blackColor = new CMYKColor(1.f, 1.f, 1.f, 0.f);
    		
    		canvas.setColorStroke(blackColor);
    		
    		canvas.moveTo(36, 730);
    		canvas.lineTo(560, 730);
    		canvas.closePathStroke();
    		
    		//ADICIONANDO IMAGEM UECE
    		imageUece = Image.getInstance(env.getProperty("default-image-uece"));
    		imageUece.setAlignment(Element.ALIGN_LEFT);
    		imageUece.setAbsolutePosition(50, 750);
    		imageUece.scalePercent(18.0f, 18.0f);
    		writer.getDirectContent().addImage(imageUece, true);
    		
    		//ADIOCIONANDO TEXTO CENTRAL
    		Phrase gov = new Phrase("GOVERNO DO ESTADO DO CEARÁ");
    		Phrase dep = new Phrase(setor.toUpperCase());
    		Phrase uni = new Phrase("UNIVERSIDADE ESTADUAL DO CEARÁ - UECE");
    		
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, gov, 295, 790, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, dep, 295, 770, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, uni, 295, 750, 0);

    		//ADICIONANDO IMAGEM ESTADO
    		imageEstado = Image.getInstance(env.getProperty("default-image-estado"));
    		imageEstado.setAlignment(Element.ALIGN_LEFT);
    		imageEstado.setAbsolutePosition(500, 750);
    		imageEstado.scalePercent(18.0f, 18.0f);
    		writer.getDirectContent().addImage(imageEstado, true);
    		
    		return document.getPageNumber();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return 0;
	}
	
	private int generateDeclaracaoBody(PdfWriter writer, Document document, Documento documento, Cargo cargo, List<Long> destinatariosIds, Boolean isMensagemGeral, Boolean isMensagemSetor) {
		
		try {
    		
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		
    		Paragraph declaracao = new Paragraph("DECLARAÇÃO");
    		declaracao.setAlignment(Element.ALIGN_CENTER);
    		declaracao.getFont().setStyle(Font.BOLD);
    		document.add(declaracao);
    		
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		
    		document = generateMainText(documento, document, writer);
    		
    		document.add(new Phrase("\n"));
    		
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(documento.getDataCriacao());
    		Paragraph paragrafoFinal = new Paragraph("Fortaleza, "+ calendar.get(Calendar.DAY_OF_MONTH) + " de " + getDia(calendar) + " de " + calendar.get(Calendar.YEAR) + ".");
    		document.add(paragrafoFinal);
    		
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		
    		Paragraph nomeParagrafo = new Paragraph();
    		Phrase nome = new Phrase();
    		nome.getFont().setStyle(Font.BOLD);
    		nome.add(documento.getUsuario().getNome());
    		nomeParagrafo.add(nome);
			document.add(nomeParagrafo);
			
			return writer.getPageNumber();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	private int generateDespachoBody(PdfWriter writer, Document document, Documento documento, Cargo cargo, List<Long> destinatariosIds, Boolean isMensagemGeral, Boolean isMensagemSetor) {
		
		try {
    		
			Usuario usuario = null;
			Optional<Usuario> optUsuario = usuarioRepository.findById(destinatariosIds.get(0));
			if(optUsuario.isPresent()) {
				usuario = optUsuario.get();
			}
			
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		
    		Paragraph declaracao = new Paragraph("FOLHA DE INFORMAÇÃO E DESPACHO");
    		declaracao.setAlignment(Element.ALIGN_CENTER);
    		declaracao.getFont().setStyle(Font.BOLD);
    		document.add(declaracao);
    		
    		document.add(new Phrase("\n"));
    		
    		Paragraph paragraphTable = new Paragraph();
    		PdfPTable mainTable = new PdfPTable(2);
    		mainTable.setWidthPercentage(100.0f);
    		
    		// First table
    		PdfPCell firstTableCell = new PdfPCell();
            firstTableCell.setBorder(PdfPCell.NO_BORDER);
            PdfPTable firstTable = new PdfPTable(1);
            firstTable.setWidthPercentage(125);
            
            Paragraph paragraphProcesso = new Paragraph();
    		Phrase labelNumeroProcesso = new Phrase("PROCESSO Nº");
    		Phrase numeroProcesso = new Phrase(documento.getIdentificador());
    		numeroProcesso.getFont().setStyle(Font.BOLD);
    		paragraphProcesso.add(labelNumeroProcesso);
    		paragraphProcesso.add(new Phrase("\n"));
    		paragraphProcesso.add(numeroProcesso);
    		PdfPCell celulaProcesso = new PdfPCell(paragraphProcesso);
    		firstTable.addCell(celulaProcesso);
            
    		Paragraph paragraphInteressado = new Paragraph();
    		Phrase labelInteressado = new Phrase("INTERESSADO");
    		Phrase interessado = new Phrase(documento.getUsuario().getNome());
    		interessado.getFont().setStyle(Font.BOLD);
    		paragraphInteressado.add(labelInteressado);
    		paragraphInteressado.add(new Phrase("\n"));
    		paragraphInteressado.add(interessado);
    		PdfPCell celulaInteressado = new PdfPCell(paragraphInteressado);
    		firstTable.addCell(celulaInteressado);
            
    		Paragraph paragraphAssunto = new Paragraph();
    		Phrase labelAssunto = new Phrase("ASSUNTO");
    		Phrase assunto = new Phrase(documento.getAssunto());
    		assunto.getFont().setStyle(Font.BOLD);
    		paragraphAssunto.add(labelAssunto);
    		paragraphAssunto.add(new Phrase("\n"));
    		paragraphAssunto.add(assunto);
    		PdfPCell celulaAssunto = new PdfPCell(paragraphAssunto);
    		firstTable.addCell(celulaAssunto);
    		
            firstTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            firstTableCell.addElement(firstTable);
            mainTable.addCell(firstTableCell);
            
            // Second table
            PdfPCell secondTableCell = new PdfPCell();
            secondTableCell.setBorder(PdfPCell.NO_BORDER);
            PdfPTable secondTable = new PdfPTable(1);
            secondTable.setWidthPercentage(60);
            
            Paragraph paragraphDe = new Paragraph();
    		Phrase labelDe = new Phrase("DE");
    		Phrase phraseDe = new Phrase(documento.getUsuario().getNome());
    		phraseDe.getFont().setStyle(Font.BOLD);
    		paragraphDe.add(labelDe);
    		paragraphDe.add(new Phrase("\n"));
    		paragraphDe.add(phraseDe);
    		PdfPCell celulaDe = new PdfPCell(paragraphDe);
    		secondTable.addCell(celulaDe);
    		
    		Paragraph paragraphPara = new Paragraph();
    		Phrase labelPara = new Phrase("PARA");
    		Phrase para = new Phrase(usuario.getNome());
    		para.getFont().setStyle(Font.BOLD);
    		paragraphPara.add(labelPara);
    		paragraphPara.add(new Phrase("\n"));
    		paragraphPara.add(para);
    		PdfPCell celulaPara = new PdfPCell(paragraphPara);
    		secondTable.addCell(celulaPara);
            
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    		Paragraph paragraphData = new Paragraph();
    		Phrase labelData = new Phrase("DATA DO DESPACHO");
    		Phrase data = new Phrase(sdf.format(documento.getDataCriacao()));
    		data.getFont().setStyle(Font.BOLD);
    		paragraphData.add(labelData);
    		paragraphData.add(new Phrase("\n"));
    		paragraphData.add(data);
    		PdfPCell celulaData = new PdfPCell(paragraphData);
    		secondTable.addCell(celulaData);
            
            secondTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            secondTableCell.addElement(secondTable);
            mainTable.addCell(secondTableCell);
            
            paragraphTable.add(mainTable);
            document.add(paragraphTable);
    		
            document.add(new Phrase("\n"));
            
    		document = generateMainText(documento, document, writer);
    		
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		
    		Paragraph nomeParagrafo = new Paragraph();
    		Phrase nome = new Phrase();
    		nome.add(documento.getUsuario().getNome());
    		nomeParagrafo.add(nome);
    		nomeParagrafo.setAlignment(Element.ALIGN_CENTER);
			document.add(nomeParagrafo);
			
			Paragraph cargoParagraph = new Paragraph();
    		Phrase cargoNome = new Phrase();
    		cargoNome.add(cargo.getNome() + " do(a) " + cargo.getSetor().getNome());
    		cargoParagraph.add(cargoNome);
    		cargoParagraph.setAlignment(Element.ALIGN_CENTER);
			document.add(cargoParagraph);
			
			return writer.getPageNumber();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	@SuppressWarnings("deprecation")
	protected int generatePortariaBody(PdfWriter writer, Document document, Documento documento, Cargo cargo, List<Long> destinatariosIds, Boolean isMensagemGeral, Boolean isMensagemSetor) {
		
		try {
			
			for(int index = 0; index < 2; index++) {
				document.add(new Phrase("\n"));
			}
			
			Image imageEstado = Image.getInstance(env.getProperty("portaria-image-estado"));
			imageEstado.setAlignment(Element.ALIGN_CENTER);
    		imageEstado.scalePercent(30.0f, 30.0f);
    		
    		document.add(imageEstado);
    		
    		document.add(new Phrase("\n"));
    		
    		Paragraph numPortaria = new Paragraph("PORTARIA Nº "+documento.getIdentificador());
    		numPortaria.setAlignment(Element.ALIGN_CENTER);
    		numPortaria.getFont().setStyle(Font.BOLD);
    		document.add(numPortaria);
    		
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		document.add(new Phrase("\n"));
    		
    		document = generateMainText(documento, document, writer);
    		
    		document.add(new Phrase("\n"));
    		
    		Paragraph paragrafoFinal = new Paragraph();
			Phrase funece = new Phrase();
			funece.getFont().setStyle(Font.BOLD);
			funece.add("FUNDAÇÃO UNIVERSIDADE ESTADUAL DO CEARÁ - FUNECE");
			paragrafoFinal.add(funece);
			Calendar calendar = Calendar.getInstance();
			Phrase localEData = new Phrase();
			localEData.add(", em Fortaleza, "+ calendar.get(Calendar.DAY_OF_MONTH) + " de " + getDia(calendar) + " de " + calendar.get(Calendar.YEAR));
			paragrafoFinal.add(localEData);
			document.add(paragrafoFinal);
			
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			
			Paragraph nomeUsuario = new Paragraph(documento.getUsuario().getNome());
			nomeUsuario.setAlignment(Element.ALIGN_CENTER);
			document.add(nomeUsuario);
			
			Paragraph funcaoUsuario = new Paragraph();
			funcaoUsuario.setAlignment(Element.ALIGN_CENTER);
			funcaoUsuario.getFont().setStyle(Font.BOLD);
			funcaoUsuario.add(cargo.getNome().toUpperCase() + ", NO EXERCÍCIO DA PRESIDÊNCIA, DA FUNECE");
			document.add(funcaoUsuario);
			
			return writer.getPageNumber();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	private Document generateMainText(Documento documento, Document document, PdfWriter writer) {
		
		try {
			CSSResolver cssResolver =
	                XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
			
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
	        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
	        htmlContext.autoBookmark(false);
	        
	        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
	        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
	        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
	        
	        XMLWorker worker = new XMLWorker(css, true);
	        XMLParser p = new XMLParser(worker);
	        
	        File file = generateHtmlFile(documento);
	        
	        p.parse(new FileInputStream(file));
			
	        file.delete();
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
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
	
	private Long generateCodDocumento(Long tipoDocumentoId){
		
		//Pegando data até o primeiro dia do ano posterior
		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		
//		Long lastCodDocumento = documentoRepository.ultimoCodDocumento(calendar.getTime());
		Long lastCodDocumento = documentoRepository.ultimoCodDocumentoByDoc(tipoDocumentoId, calendar.getTime());
		
		if(lastCodDocumento == null) {
			return 1L;
		}
		
		return lastCodDocumento + 1;
	}
	
	private File generateHtmlFile(Documento documento) {
		try {
			
			String rootPath = env.getProperty("SISDOC_FILES");
			
			File file = new File(HTML_LOCATION+documento.getUsuario().getId()+'_'+documento.getId()+".html");
			
			if(file.exists()) {
				file.delete();
				file.createNewFile();
			} else {
				file.createNewFile();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(documento.getConteudo());
			writer.close();
			
			return file;
		} catch (Exception e) {
			return null;
		}
	}
	
}
