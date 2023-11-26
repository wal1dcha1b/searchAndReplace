package com.infor.processor;

import static javax.xml.stream.XMLStreamConstants.*;

import com.infor.exceptions.LibraryException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.stream.*;

public class XMLFileProcessor implements FileProcessor {

  private final String inputFileName;
  private final String outputFileName;
  private XMLStreamReader xmlStreamReader;
  private XMLStreamWriter xmlStreamWriter;

  public XMLFileProcessor(String inputFileName, String outputFileName) {
    this.inputFileName = inputFileName;
    this.outputFileName = outputFileName;
  }

  private void initXmlStreams() throws FileNotFoundException, XMLStreamException {
    var fileInputStream = new FileInputStream(inputFileName);
    xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fileInputStream);
    var fileOutputStream = new FileOutputStream(outputFileName);
    xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(fileOutputStream);
  }

  @Override
  public void searchAndReplace(String target, String replacement) {

    try {
      initXmlStreams();
      xmlStreamWriter.writeStartDocument();
      while (xmlStreamReader.hasNext()) {
        int eventCode = xmlStreamReader.next();
        switch (eventCode) {
          case START_ELEMENT -> handleStartElement(target, replacement);
          case END_ELEMENT -> xmlStreamWriter.writeEndElement();
          case PROCESSING_INSTRUCTION -> xmlStreamWriter.writeProcessingInstruction(
              xmlStreamReader.getLocalName());
          case CHARACTERS -> xmlStreamWriter.writeCharacters(xmlStreamReader.getText());
          case COMMENT -> xmlStreamWriter.writeComment(xmlStreamReader.getText());
          case END_DOCUMENT -> xmlStreamWriter.writeEndDocument();
            // default -> xmlStreamWriter.writeCharacters(xmlStreamReader.getText());
        }
      }
    } catch (FileNotFoundException | XMLStreamException e) {
      throw new LibraryException("An error occurred while processing the XML file.", e);
    } finally {
      close();
    }
  }

  private void handleStartElement(String target, String replacement) throws XMLStreamException {
    xmlStreamWriter.writeStartElement(xmlStreamReader.getLocalName());
    int attributesCount = xmlStreamReader.getAttributeCount();
    for (int i = 0; i < attributesCount; i++) {
      var attName = xmlStreamReader.getAttributeLocalName(i);
      var attValue = xmlStreamReader.getAttributeValue(i);
      xmlStreamWriter.writeAttribute(attName, attValue.replace(target, replacement));
    }
  }

  private void close() {
    try {
      xmlStreamWriter.close();
      xmlStreamReader.close();
    } catch (XMLStreamException e) {
      throw new LibraryException("An error occurred while closing the file Streams", e);
    }
  }
}
