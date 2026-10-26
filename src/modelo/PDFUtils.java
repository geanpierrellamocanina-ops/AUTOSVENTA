/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author JE
 */
public class PDFUtils {

    public Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL, new BaseColor(48, 55, 63));
    public Font fuenteSubTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(48, 55, 63));

    public final int ALINEACION_IZQUIERDA = Element.ALIGN_LEFT;
    public final int ALINEACION_DERECHA = Element.ALIGN_RIGHT;
    public final int ALINEACION_CENTRO = Element.ALIGN_CENTER;

    protected Document documento = null;
    protected PdfWriter writer = null;

    public void AgregarSubTitulo(String texto, Font fuenteTitulo, int espacioAntes, int espacioDespues, int alineacion) {
        try {
            PdfPTable tablaTitulo = new PdfPTable(1);
            PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteTitulo));
            celda.setBorder(0);
            celda.setHorizontalAlignment(alineacion);
            celda.setVerticalAlignment(alineacion);
            tablaTitulo.addCell(celda);
            tablaTitulo.setSpacingBefore(espacioAntes);
            tablaTitulo.setSpacingAfter(espacioDespues);
            documento.add(tablaTitulo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PdfPCell Celda(String texto) {
        Font fuenteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteTitulo));
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        return celda;
    }

    public PdfPCell CeldaColspan(int col, String texto) {
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new BaseColor(5, 2, 22));
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteTitulo));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setVerticalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(col);
        celda.setPadding(5);
        celda.setBorder(0);
        return celda;
    }

    public PdfPCell Cabecera(String texto) {
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.NORMAL, BaseColor.WHITE);
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuenteTitulo));
        celda.setBackgroundColor(new BaseColor(245, 51, 51));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        return celda;
    }
}