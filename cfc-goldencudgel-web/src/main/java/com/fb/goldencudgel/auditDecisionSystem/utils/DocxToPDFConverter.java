package com.fb.goldencudgel.auditDecisionSystem.utils;

import java.io.*;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocxToPDFConverter extends Converter {

	public DocxToPDFConverter(InputStream inStream, OutputStream outStream, boolean showMessages,
			boolean closeStreamsWhenComplete) {
		super(inStream, outStream, showMessages, closeStreamsWhenComplete);
	}

	@Override
	public void convert() throws Exception {
		loading();

		PdfOptions options = PdfOptions.create();
		XWPFDocument document = new XWPFDocument(inStream);

		processing();
		PdfConverter.getInstance().convert(document, outStream, options);

		finished();
	}

}