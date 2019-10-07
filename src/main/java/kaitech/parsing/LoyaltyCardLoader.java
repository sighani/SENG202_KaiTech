package kaitech.parsing;

import kaitech.api.model.LoyaltyCard;
import kaitech.model.LoyaltyCardImpl;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class LoyaltyCardLoader {

    /**
     * Document Builder for parsing the Loyalty card XML.
     */
    private DocumentBuilder db;

    private Document parsedDoc;

    private String fileSource;

    public LoyaltyCardLoader(String path, boolean isValidating) {

        this.fileSource = path;

        //document builder factory for creating document builders
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(isValidating);


        //creating new document builder
        try {
            this.db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            System.exit(1);
        }

        db.setErrorHandler(new KaiTechErrorHandler(System.err));

    }

    /**
     * Takes the file input and parses it into a DOM tree,
     * so that the attributes can be extracted
     *
     * @throws SAXException in case something goes wrongs during parsing
     * @throws IOException  when there is an error during parsing
     */

    public void parseInput() throws SAXException, IOException {
        this.parsedDoc = db.parse(this.fileSource);
    }

    /**
     * Takes the parsed document and creates loyalty card
     * objects with it
     *
     * @return Map of cardID to card object
     */
    public Map<Integer, LoyaltyCard> getLoyaltyCards() {
        //setting up the map to return and getting the node element for each card
        Map<Integer, LoyaltyCard> loyaltyCardMap = new HashMap<>();
        NodeList nodes = parsedDoc.getElementsByTagName("card");

        //temp storage for information
        Node currentNode;
        NodeList children;

        //iterating through the nodes and parsing the date
        for (int i = 0; i < nodes.getLength(); i++) {
            //setting up for this card
            currentNode = nodes.item(i);
            children = currentNode.getChildNodes();

            //loading all the attributes in
            int id = Integer.parseInt(children.item(1).getTextContent());
            LocalDate lastUsedDate;
            try {
                lastUsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(children.item(3).getTextContent()) //
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ParseException p) {
                //the date is in the wrong format, set it to today's date
                lastUsedDate = LocalDate.now();
            }
            Money balance = Money.parse(children.item(5).getTextContent());
            String name = children.item(7).getTextContent();
            loyaltyCardMap.put(id, new LoyaltyCardImpl(id, lastUsedDate, name, balance));
        }
        return loyaltyCardMap;
    }

}
