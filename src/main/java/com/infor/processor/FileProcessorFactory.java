package com.infor.processor;

import com.infor.enums.FileType;
import com.infor.exceptions.LibraryException;

public class FileProcessorFactory {
  static final String ERROR_NO_PARSER =
      "Unable to instantiate parser: No parser associated to price file type '%s'";
  static final String ERROR_NO_FILETYPE =
      "Unable to instantiate parser: No price file type provided";

  public static FileProcessor of(JobParams params) {

    if (params.fileType() == null) {
      throw new LibraryException(ERROR_NO_FILETYPE);
    }
    switch (FileType.valueOf(params.fileType().toUpperCase())) {
      case TXT -> {
        return new TXTFileProcessor(params.inputFileName(), params.outputFileName());
      }
      case XML -> {
        return new XMLFileProcessor(params.inputFileName(), params.outputFileName());
      }
      default -> throw new LibraryException(String.format(ERROR_NO_PARSER, params.fileType()));
    }
  }
}
