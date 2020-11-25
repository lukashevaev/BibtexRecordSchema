package com.ols.record;

import com.sun.prism.impl.Disposer.Record;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Map;
import java.util.logging.Logger;


@Singleton(name = "bibtex")
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
@Remote(RecordSchema.class)
@EJB(name = "java:global/ruslan/recordSchema/bibtex", beanInterface = RecordSchema.class)
public class BibtexRecordSchema  implements RecordSchema {
    private static final String URI = "";
    private static final Logger log = Logger.getLogger(BibtexRecordSchema.class
            .getName());
    private static TransformerFactory transformerFactory = TransformerFactory
            .newInstance();
    private static Templates templates;
    private String biBteXml;
    private static PatternFactory patternFactory;
    private static BibTexBuilder builder;


    @EJB(lookup = "java:global/ruslan/recordSchema/ruslan", beanInterface = RecordSchema.class)
    private RecordSchema ruslanRecordSchema;

    @PostConstruct
    public void init() {
        log.fine("Preparing XSL templates");
        //log.fine(getClass().getClassLoader().getResource("RUSMARC2BibTex.xsl").toString());
        try {

            InputStream inputStream = new FileInputStream(new File("C:\\Users\\Daniel\\Desktop\\bibtex-rs\\bibtex-rs-ejb\\src\\main\\resources\\RUSMARC2BibTex.xsl"));
            templates = transformerFactory.newTemplates(new StreamSource(
                   // getClass().getClassLoader().getResourceAsStream(
                      //      "RUSMARC2BibTex.xsl")));
                    inputStream));
        } catch (TransformerConfigurationException e) {
            log.severe("Unable to initialise templates: " + e.getMessage());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
    }

    @Override
    public String getURI() {
        return URI;
    }

    @Override
    public String toString(Record record, String encoding) throws Exception {
        return ruslanRecordSchema.toString(record, encoding);
    }

    @Override
    public Document toDocument(Record record, String encoding) throws Exception {
        return transformSchemaToBibTeXml(ruslanRecordSchema.toDocument(record, encoding));
    }

    public Document transformSchemaToBibTeXml(Document src) throws TransformerException {
        Transformer transformer = templates.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        //getting XML (String)
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(new DOMSource(src), result);
        this.setBiBteXml(result.getWriter().toString());

        DOMResult dst = new DOMResult();
        transformer.transform(new DOMSource(src), dst);
        return (Document) dst.getNode();
    }

    public String getBiBteXml() {
        return biBteXml;
    }

    @Deprecated
    public String transformXmlToBibTex() {
        if (getBiBteXml() == null) throw new AssertionError("Method transformSchemaToBibTeXml was not invoked");
        patternFactory = new PatternFactory(biBteXml);
        Map<String, String> patterns = patternFactory.getPatterns();
        builder = new BibTexBuilder(patterns);
        String bibTexResult = "";
        try {
            bibTexResult = builder.build();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bibTexResult;
    }

    @Override
    public Record normalize(Record record, String encoding) {
        return ruslanRecordSchema.normalize(record, encoding);
    }

    @Override
    public Record denormalize(Record record, String encoding) {
        return ruslanRecordSchema.denormalize(record, encoding);

    }

    private void setBiBteXml(String biBteXml) {
        this.biBteXml = biBteXml;
    }

    //src - result of transformSchemaToBibTeXml()
    public String getBibTex(Document src) {
        Node root = src.getDocumentElement();
        //every record contains just 1 book
        Node book = root.getChildNodes().item(0);
        StringBuilder bibTexBuilder = new StringBuilder();
        NodeList bookProps = book.getChildNodes();
        StringBuilder authorYear = new StringBuilder();
        for(int j = 0; j < bookProps.getLength(); j++) {
            Node bookProp = bookProps.item(j);
            String nodeName = bookProp.getNodeName();
            String parameter = bookProp.getChildNodes().item(0).getTextContent();
            String ending = ",";
            if (nodeName.equals("author")) {
                parameter = parameter.substring(0, parameter.length() - 5);
                authorYear.append(parameter.split(" ")[0]);
            }
            if (nodeName.equals("year")) authorYear.append(parameter);
            if (j == bookProps.getLength() - 1) ending = "";
            bibTexBuilder.append(nodeName)
                    .append("={")
                    .append(parameter)
                    .append("}")
                    .append(ending)
                    .append("\n");
        }
        bibTexBuilder.insert(0, "\n")
                .insert(0, authorYear.toString() + ",")
                .insert(0, "@book{")
                .append("}");
        return bibTexBuilder.toString();
    }

}
