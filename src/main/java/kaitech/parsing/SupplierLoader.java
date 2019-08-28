package kaitech.parsing;

import kaitech.api.model.Supplier;
import kaitech.model.SupplierImpl;
import kaitech.util.PhoneType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SupplierLoader {

    //Document builder to parse XML files, document to store the parsed info

    private DocumentBuilder db = null;
    private Document parsedDoc = null;

    //file source

    private String fileSource;

    private String sid;
    private String name;
    private String address;
    private String phone;
    private PhoneType phType;
    private String email;
    private String url;


    private Map<String, Supplier> suppliers;

    //constructor

    public SupplierLoader(String path, boolean validating) {

        this.fileSource = path;

        //doc builder factory creating and set options(validating)
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(validating);

        //now we can make as many document builders as needed

        try {
            this.db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            //if theres an error, print it out and exit with code 1
            e.printStackTrace();
            System.exit(1);
        }

        //sticking with the default error handler for now, can create our own one to work with the GUI in the future
        //needs to be fixed
        db.setErrorHandler(new MyErrorHandler(System.err));
    }


    //this takes the file and turns it into a tree for use in turning into objects

    public void parseInput() {
        try {
            this.parsedDoc = db.parse(this.fileSource);
        } catch (SAXException | IOException e) {
            //returning error and exiting
            e.printStackTrace();
            System.exit(1);
        }
    }


    //doc has been built, now we need to create objects with it
    public Map<String, Supplier> getSuppliers() {
        //creating the hashmap
        this.suppliers = new HashMap<>();

        //nodelist of all suppliers in the document
        NodeList nodes = this.parsedDoc.getElementsByTagName("supplier");

        Node currentNode;
        NodeList children;

        for (int i = 0; i < nodes.getLength(); i++) {
            //rewriting all the values
            reset();

            //getting node and kids
            currentNode = nodes.item(i);
            children = currentNode.getChildNodes();


            //now we start writing out the info to our variables
            this.sid = children.item(1).getTextContent();
            this.name = children.item(3).getTextContent();
            this.address = children.item(5).getTextContent();

            //switch for phonetype and phone >:(
            switch (children.item(7).getAttributes().getNamedItem("type").getTextContent()) {
                case "mobile":
                    this.phType = PhoneType.MOBILE;
                    break;
                case "work":
                    this.phType = PhoneType.WORK;
                    break;
                case "home":
                    this.phType = PhoneType.WORK;
                default:
                    this.phType = PhoneType.UNKNOWN;
            }
            //phone number
            this.phone = children.item(7).getTextContent();

            //not the best way to do this needs fixing
            try {
                this.email = children.item(9).getTextContent();
            } catch (NullPointerException ne) {
                this.email = Supplier.UNKNOWN_EMAIL;
            }

            try {
                this.url = children.item(11).getTextContent();
                if (this.url.isEmpty()) {
                    this.url = Supplier.UNKNOWN_URL;
                }
            } catch (NullPointerException ne) {
                this.url = Supplier.UNKNOWN_URL;
            }

            //write to map and supplier object
            this.suppliers.put(this.sid, new SupplierImpl(this.sid, this.name, this.address, this.phone, this.phType,
                    this.email, this.url));

        }
        return this.suppliers;
    }


    //resets all the values to empty
    public void reset() {
        this.sid = "";
        this.name = "";
        this.address = "";
        this.phone = "";
        this.email = "";
        this.url = "";
    }

}
