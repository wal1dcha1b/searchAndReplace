package com.infor.processor;

import com.infor.exceptions.LibraryException;
import com.infor.utils.FileWriterEAM;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TXTFileProcessor implements FileProcessor {

  private final String inputFileName;
  private final String outputFileName;

  public TXTFileProcessor(String inputFileName, String outputFileName) {
    this.inputFileName = inputFileName;
    this.outputFileName = outputFileName;
  }

  @Override
  public void searchAndReplace(String target, String replacement) {
    Path path = Paths.get(inputFileName);
    try (var fileBody = Files.lines(path)) {

      FileWriterEAM.use(
          outputFileName,
          fileWriter ->
              fileBody
                  .map(line -> line.replace(target, replacement))
                  .map(line -> line + String.format("%n"))
                  .forEach(fileWriter::write));

    } catch (IOException io) {
      throw new LibraryException("An error occurred while processing the TXT file.", io);
    }
  }
}
