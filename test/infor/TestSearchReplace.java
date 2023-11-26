package infor;

import static org.junit.jupiter.api.Assertions.*;

import com.infor.exceptions.LibraryException;
import com.infor.processor.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class TestSearchReplace {

  public static final String CLIENT = "client";
  public static final String CUSTOMER = "customer";
  public static final String TRACE = "trace";
  public static final String ERROR = "error";

  @Test
  void whenNotRecognizedFileType_ThrowAnException() {
    var unknownParams = new JobParams("unknown", "input.unknown", "output.unknown");
    var exception =
        assertThrows(IllegalArgumentException.class, () -> FileProcessorFactory.of(unknownParams));
    String expectedMessage = "No enum constant com.infor.enums.FileType.UNKNOWN";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void whenNotSupportedFileType_ThrowAnException() {
    var jsonParams = new JobParams("json", "input.json", "output.json");
    var exception = assertThrows(LibraryException.class, () -> FileProcessorFactory.of(jsonParams));
    String expectedMessage =
        "Unable to instantiate parser: No parser associated to price file type 'json'";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void whenGivenXMLFileType_ReturnXMLProcessor() throws IOException {
    var xmlParams = new JobParams("xml", "test/resources/input.xml", "test/resources/output.xml");
    FileProcessor xmlProcessor = FileProcessorFactory.of(xmlParams);
    assert xmlProcessor instanceof XMLFileProcessor;
  }

  @Test
  void whenGivenXMLTagWithAttributes_ProcessorShouldReplace() throws IOException {

    String inputFileName = "test/resources/tagWithAttributes.xml";
    String outputFileName = "test/resources/output.xml";
    var xmlProcessor = new XMLFileProcessor(inputFileName, outputFileName);

    var inputContent = Files.readString(Paths.get(inputFileName));
    assertTrue(inputContent.contains(TRACE));
    assertFalse(inputContent.contains(ERROR));

    xmlProcessor.searchAndReplace(TRACE, ERROR);

    var outputContent = Files.readString(Paths.get(outputFileName));
    assertFalse(outputContent.contains(TRACE));
    assertTrue(outputContent.contains(ERROR));
  }

  @Test
  void whenGivenXMLTag_ProcessorShouldNotReplace() throws IOException {

    String inputFileName = "test/resources/tagWithoutAttributes.xml";
    String outputFileName = "test/resources/output.xml";
    var xmlProcessor = new XMLFileProcessor(inputFileName, outputFileName);

    var inputContent = Files.readString(Paths.get(inputFileName));
    assertTrue(inputContent.contains(TRACE) && !inputContent.contains(ERROR));

    xmlProcessor.searchAndReplace(TRACE, ERROR);

    var outputContent = Files.readString(Paths.get(outputFileName));
    assertTrue(outputContent.contains(TRACE) && !outputContent.contains(ERROR));
  }

  @Test
  void whenGivenXMLComment_ProcessorShouldNotReplace() throws IOException {

    String inputFileName = "test/resources/onlyComment.xml";
    String outputFileName = "test/resources/output.xml";
    var xmlProcessor = new XMLFileProcessor(inputFileName, outputFileName);

    var inputContent = Files.readString(Paths.get(inputFileName));
    assertTrue(inputContent.contains(TRACE) && !inputContent.contains(ERROR));

    xmlProcessor.searchAndReplace(TRACE, ERROR);

    var outputContent = Files.readString(Paths.get(outputFileName), StandardCharsets.UTF_8);
    assertTrue(outputContent.contains(TRACE) && !outputContent.contains(ERROR));
  }

  @Test
  void whenGivenTXTFileType_ReturnTXTProcessor() {
    var txtParams = new JobParams("txt", "test/resources/input.txt", "test/resources/output.txt");
    FileProcessor txtProcessor = FileProcessorFactory.of(txtParams);
    assert txtProcessor instanceof TXTFileProcessor;
  }

  @Test
  void whenGivenTXTFileType_ProcessorShouldReplace() throws IOException {
    String inputFileName = "test/resources/input.txt";
    String outputFileName = "test/resources/output.txt";

    var txtProcessor = new TXTFileProcessor(inputFileName, outputFileName);

    var inputContent = Files.readString(Paths.get(inputFileName));
    assertTrue(inputContent.contains(CLIENT) && !inputContent.contains(CUSTOMER));

    txtProcessor.searchAndReplace(CLIENT, CUSTOMER);

    var outputContent = Files.readString(Paths.get(outputFileName));
    assertTrue(!outputContent.contains(CLIENT) && outputContent.contains(CUSTOMER));
  }
}
