package kaitech.parsing;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.PrintStream;

public class KaiTechErrorHandler implements ErrorHandler {


    private PrintStream dest;

    public KaiTechErrorHandler(PrintStream dest){ this.dest = dest;}

    //all sax requeired methods
    @Override
    public void warning(SAXParseException e) throws SAXException {
        dest.println("SAX warning: " + e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        dest.println("Sax Error: " + e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        dest.println("SAX fatal error: " + e);
    }
}
