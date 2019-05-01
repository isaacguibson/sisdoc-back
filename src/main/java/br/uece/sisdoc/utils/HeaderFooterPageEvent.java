package br.uece.sisdoc.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import br.uece.sisdoc.model.Documento;

public class HeaderFooterPageEvent extends PdfPageEventHelper{

	private Documento documento;
	
	public HeaderFooterPageEvent(Documento documento) {
		super();
		this.documento = documento;
	}

	//	Exemplo Cabeçalho
    public void onStartPage(PdfWriter writer, Document document) {
    		
//    	ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("TEXTO"), 110, 800, 0);
//      ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 800, 0);

    }

    // Exemplo Footer    
    public void onEndPage(PdfWriter writer, Document document) {
    	
    	if(document.getPageNumber() == getDocumento().getTotalPages()) {
    		
    		Paragraph name = new Paragraph(documento.getUsuario().getTratamento()+" "+documento.getUsuario().getNome());
    		Paragraph department = new Paragraph(documento.getUsuario().getCargo().getNome() + " do " + documento.getUsuario().getSetor().getNome() + " da UECE.");
    		
    		Paragraph endereco = new Paragraph("Av. Silas Munguba, 1700 – Campus do Itaperi – Fortaleza/CE – CEP: 60740-903");
    		Paragraph phones = new Paragraph("Fone (85) 3101.96.01 – Fax (85) 3101.96.03");
    		Paragraph webContact = new Paragraph("Site: www.uece.br – e-mail: "+getDocumento().getUsuario().getEmail());
    		
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, name, 300, 115, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, department, 300, 100, 0);
    		
    		PdfContentByte canvas = writer.getDirectContent();
    		CMYKColor blackColor = new CMYKColor(1.f, 1.f, 1.f, 0.f);
    		
    		canvas.setColorStroke(blackColor);
    		
    		canvas.moveTo(36, 77);
    		canvas.lineTo(560, 77);
    		canvas.setLineWidth(5.0f);
    		canvas.closePathStroke();
    		
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, endereco, 300, 55, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, phones, 300, 40, 0);
    		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, webContact, 300, 25, 0);
    	}
    }

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
}
