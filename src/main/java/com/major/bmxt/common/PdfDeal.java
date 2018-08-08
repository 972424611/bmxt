package com.major.bmxt.common;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;

/**
 * pdf文件操作类
 */
public class PdfDeal {

    private static final float[] columnWidths = {5f, 3f, 3f, 3f};

    public static Font getFont() {
        //加上这个才能输出中文
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new Font(baseFont, 12, Font.NORMAL);
    }

    public static void setCell(PdfPCell[] cells, String data, int i) {
        cells[i] = new PdfPCell(new Paragraph(data, getFont()));
        cells[i].setHorizontalAlignment(Element.ALIGN_CENTER);
        cells[i].setVerticalAlignment(Element.ALIGN_CENTER);
    }

    public static PdfPTable getTable(Integer columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
        try {
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return table;
    }
}
