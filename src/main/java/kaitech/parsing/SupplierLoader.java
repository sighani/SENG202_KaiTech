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

    /**
     * Document builder to parse XML files, document to store the parsed info
     */

    private DocumentBuilder db = null;
    private Document parsedDoc = null;

    /**
     * File path string
     */
    private String fileSource;

    /**
     * Fields for each Supplier
     */
    private String sid;
    private String name;
    private String address;
    private String phone;
    private PhoneType phType;
    private String email;
    private String url;

    private Map<String, Supplier> suppliers;

    public SupplierLoader(String path, boolean validating) {

        this.fileSource = path;

        // Creating DocumentBuilderFactory and setting options (validating)
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(validating);

        try {
            this.db = dbf.newDocumentBuilder();
            db.setErrorHandler(new KaiTechErrorHandler(System.err));
        } catch (ParserConfigurationException e) {
            //TODO
            e.printStackTrace();
        }
    }

    /**
     * Takes the input filepath and
     * creates a tree for traversing with
     * the document builder made previously
     *
     * @throws SAXException when there is an error parsing the input
     * @throws IOException  when there is an error during parsing
     */
    public void parseInput() throws SAXException, IOException {
        this.parsedDoc = db.parse(this.fileSource);
    }


    /**
     * Finding and parsing each supplier into a new Supplier Object
     *
     * @return A map of supplier names and supplier objects
     */
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

            //getting node and children
            currentNode = nodes.item(i);
            children = currentNode.getChildNodes();


            //now we start writing out the info to our variables
            this.sid = children.item(1).getTextContent();
            this.name = children.item(3).getTextContent();
            this.address = children.item(5).getTextContent();

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

            //Getting all optional fields parsed
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


    /**
     * Resets all supplier fields to Empty
     */
    public void reset() {
        this.sid = "";
        this.name = "";
        this.address = "";
        this.phone = "";
        this.email = "";
        this.url = "";
    }

}
