package kaitech.parsing;

import kaitech.api.model.Ingredient;
import kaitech.model.IngredientImpl;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IngredientLoader {

    //document builder and document, for parsing and storing the parsed document
    private DocumentBuilder db = null;
    private Document parsedDoc = null;

    //filename
    private String fileSource;


    //variables for writing out to the ingredient object
    private String code;
    private String name;
    private int stock;
    private Money cost;
    private UnitType unit;
    private ThreeValueLogic isVeg, isVegan, isGF;

    //map
    private Map<Ingredient, Integer> ingredients;


    public IngredientLoader(String path, boolean isValidating) {

        this.fileSource = path;

        //doc builder factory for creating document builders
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

    //TODO fix dtd so that price has to be proper



    /**
     * Takes the file input and parses it into a DOM tree
     */

    public void parseInput() throws SAXException {
        try {
            //passing the passed file to document parsedDoc
            this.parsedDoc = db.parse(this.fileSource);
        } catch (IOException e) {
            //has already been caught by the checkFileOk method
        }
    }

    //turning our parsed doc tree into a ingredient objects
    public Map<Ingredient, Integer> getIngredients() {
        //setting up hashmap
        this.ingredients = new HashMap<>();

        NodeList nodes = this.parsedDoc.getElementsByTagName("ingredient");

        Node currentNode;
        NodeList children;
        NamedNodeMap attr;

        for (int i = 0; i < nodes.getLength(); i++) {

            reset();
            //setting up for this ingredient
            currentNode = nodes.item(i);
            children = currentNode.getChildNodes();
            attr = currentNode.getAttributes();


            this.code = children.item(1).getTextContent();
            this.name = children.item(3).getTextContent();
            this.stock = Integer.parseInt(children.item(5).getTextContent());
            this.cost = Money.parse(children.item(7).getTextContent());

            switch (attr.getNamedItem("unit").getNodeValue()) {
                case "g":
                    this.unit = UnitType.GRAM;
                    break;
                case "ml":
                    this.unit = UnitType.ML;
                    break;
                case "count":
                    this.unit = UnitType.COUNT;
                    break;
                default:
                    //do we want to exit here or..
            }

            this.isVeg = yesNoMaybe(attr.getNamedItem("isveg").getNodeValue());
            this.isVegan = yesNoMaybe(attr.getNamedItem("isvegan").getNodeValue());
            this.isGF = yesNoMaybe(attr.getNamedItem("isgf").getNodeValue());

            ingredients.put(new IngredientImpl(this.code, this.name, this.unit, this.cost,
                    this.isVeg, this.isVegan, this.isGF), this.stock);
        }

        return this.ingredients;
    }

    //three value logic method
    private ThreeValueLogic yesNoMaybe(String s) {
        ThreeValueLogic tvl;
        switch (s) {
            case "yes":
                tvl = ThreeValueLogic.YES;
                break;
            case "no":
                tvl = ThreeValueLogic.NO;
                break;
            case "unknown":
            default:
                tvl = ThreeValueLogic.UNKNOWN;
                break;
        }
        return tvl;
    }


    //resets all the values of the class
    public void reset() {
        this.name = "";
        this.code = "";
        this.unit = UnitType.UNKNOWN;
        this.isVegan = ThreeValueLogic.UNKNOWN;
        this.isVeg = ThreeValueLogic.UNKNOWN;
        this.isGF = ThreeValueLogic.UNKNOWN;
    }

}
