package kaitech.parsing;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.PrintStream;

public class KaiTechErrorHandler implements ErrorHandler {

    private PrintStream errDest;

    public KaiTechErrorHandler(PrintStream dest) {
        this.errDest = dest;
    }

    //all sax required methods
    @Override
    public void warning(SAXParseException e) {
        errDest.println("SAX warning: " + e);
    }

    @Override
    public void error(SAXParseException e) {
        errDest.println("Sax Error: " + e);
    }

    @Override
    public void fatalError(SAXParseException e) {
        errDest.println("SAX fatal error: " + e);
    }
}
