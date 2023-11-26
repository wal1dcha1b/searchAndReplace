package com.infor.utils;

import com.infor.exceptions.LibraryException;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterEAM {
  private FileWriter writer;

  private FileWriterEAM(String fileName) throws IOException {
    writer = new FileWriter(fileName);
  }

  public static void use(String fileName, UseInstance<FileWriterEAM, IOException> block)
      throws IOException {
    FileWriterEAM writerEAM = new FileWriterEAM(fileName);

    try {
      block.accept(writerEAM);
    } catch (IOException e) {
      throw new LibraryException(e);
    } finally {
      writerEAM.close();
    }
  }

  private void close() throws IOException {
    writer.close();
  }

  public void write(String message) {
    try {
      writer.write(message);
    } catch (IOException e) {
      throw new LibraryException(e);
    }
  }
}
