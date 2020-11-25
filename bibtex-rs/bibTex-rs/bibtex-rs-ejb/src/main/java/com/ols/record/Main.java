package com.ols.record;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class Main {
    public static void main(String[] args) throws TransformerException, IOException, SAXException, ParserConfigurationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("C:\\Users\\Daniel\\Desktop\\bibtex-rs\\bibtex-rs-ejb\\src\\main\\resources\\RUSMARC.xml");
        FileReader fileReader = new FileReader(file);
        InputSource inputSourceFile = new InputSource(fileReader);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(inputSourceFile);

        BibtexRecordSchema bibtexRecordSchema = new BibtexRecordSchema();
        BibtexRecordSchema.class.getDeclaredMethod("init", new Class[]{}).invoke(bibtexRecordSchema);

        System.out.println(bibtexRecordSchema.getBibTex(bibtexRecordSchema.transformSchemaToBibTeXml(document)));




    }


}
