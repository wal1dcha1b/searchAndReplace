import com.infor.exceptions.LibraryException;
import com.infor.processor.FileProcessorFactory;
import com.infor.processor.JobParams;

public class Main {
  public static void main(String[] args) {

    System.out.println(
        "-------------------------------- Start Program --------------------------------");
    if (args.length != 5) {
      throw new LibraryException("Illegal number of arguments");
    }
    var params = new JobParams(args[0], args[3], args[4]);
    var target = args[1];
    var replacement = args[2];
    var fileProcessor = FileProcessorFactory.of(params);
    System.out.printf(
        "-------------------------------- Start Processing %s file --------------------------------%n",
        params.inputFileName());

    fileProcessor.searchAndReplace(target, replacement);

    System.out.printf(
        "-------------------------------- End Processing %s file --------------------------------%n",
        params.inputFileName());
    System.out.printf(
        "-------------------------------- The Result file will be found in %s --------------------------------%n",
        params.outputFileName());
    System.out.println(
        "-------------------------------- End Program --------------------------------");
  }
}
