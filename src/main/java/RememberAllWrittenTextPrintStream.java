import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class RememberAllWrittenTextPrintStream extends PrintStream {

    private static final String newLine = System.getProperty("line.separator");

    private final StringBuffer sb = new StringBuffer();
    private final PrintStream original;

//    public RememberAllWrittenTextPrintStream(PrintStream original/*, boolean autoFlush, OutputStream out*/) {
////        super(out, autoFlush);
//        this.original = original;
//    }

    public RememberAllWrittenTextPrintStream(PrintStream original, String fileName) throws FileNotFoundException {
        super(fileName);
        this.original = original;
    }

    public void print(double d) {
        sb.append(d);
        original.print(d);
    }

    public void print(String s) {
        sb.append(s);
        original.print(s);
    }

    public void println(String s) {
        sb.append(s).append(newLine);
        original.println(s);
    }

    public void println() {
        sb.append(newLine);
        original.println();
    }

    public PrintStream printf(String s, Object... args) {
        sb.append( String.format(s, args) );
        original.printf(s, args);
        return null;
    }


    // .....
    // the same for ALL the public methods in PrintStream....
    // (your IDE should help you easily create delegates for the `original` methods.)

    public String getAllWrittenText() {
        return sb.toString();
    }

}