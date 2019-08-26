package kaitech.parsing;

import kaitech.model.Ingredient;
import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
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
    private UnitType unit;
    private ThreeValueLogic isVeg, isVegan, isGF;

    //map
    private Map<String, Ingredient> ingredients;


    public IngredientLoader(String path, boolean isValidating){

        this.fileSource = path;

        //doc builder factory for creating document builders
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(isValidating);


        //creating new document builder
        try {
            this.db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }

        db.setErrorHandler(new MyErrorHandler(System.err));

    }


    public void parseInput(){
        try {
            //passing the passed file to document parsedDoc
            this.parsedDoc = db.parse(this.fileSource);
        } catch (SAXException sxe) {
            //error catchin
            System.err.println(sxe);
            System.exit(1);
        } catch (IOException ioe) {
            //error catchin
            System.err.println(ioe);
            System.exit(1);
        }
    }

    //turning our parsed doc tree into a ingredient objects
    public Map<String, Ingredient> getIngredients(){
        //setting up hashmap
        this.ingredients = new HashMap<String, Ingredient>();

        //input
        parseInput();

        NodeList nodes = this.parsedDoc.getElementsByTagName("ingredient");

        Node currentNode;
        NodeList children;
        NamedNodeMap attr;

        for(int i = 0; i < nodes.getLength(); i++){

            reset();
            //setting up for this ingredient
            currentNode = nodes.item(i);
            children = currentNode.getChildNodes();
            attr = currentNode.getAttributes();


            this.code = children.item(1).getTextContent();
            this.name = children.item(3).getTextContent();

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


            ingredients.put(this.code, new Ingredient(this.code, this.name, this.unit, Money.parse("-1"), this.isVeg, this.isVegan, this.isGF));
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
                tvl = ThreeValueLogic.UNKNOWN;
                break;
            default:
                tvl = ThreeValueLogic.UNKNOWN;
        }
        return tvl;
    }



    //resets all the values of the class
    public void reset(){
        this.name = "";
        this.code = "";
        this.unit = UnitType.UNKNOWN;
        this.isVegan = ThreeValueLogic.UNKNOWN;
        this.isVeg = ThreeValueLogic.UNKNOWN;
        this.isGF = ThreeValueLogic.UNKNOWN;
    }

}
