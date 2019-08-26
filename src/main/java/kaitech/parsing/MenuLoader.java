package kaitech.parsing;

import kaitech.model.Menu;
import kaitech.model.MenuItem;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MenuLoader {

    //document builder and document for parsed doc
    private DocumentBuilder db = null;
    private Document parsedDoc = null;

    private String fileName;

    private Menu menu;
    private MenuItem menuItem;


    public MenuLoader(String fileName, boolean validating){

        //docuement builder factory setup
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validating);

        this.fileName = fileName;

        try {
            this.db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }

        //need to write new error handler
        db.setErrorHandler(new MyErrorHandler(System.err));
    }


    public void parseInput(){
        try {
            this.parsedDoc = db.parse(this.fileName);
        } catch (SAXException sxe) {
            System.err.println(sxe);
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
    }


    public Menu getMenu(){
        //readng the parsed document in
        parseInput();

        //Items to be iterated over
        NodeList items = parsedDoc.getElementsByTagName("item");
        //this is a placeholder
        return null;
    }


}
