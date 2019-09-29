package kaitech.parsing;

import kaitech.api.model.*;
import kaitech.model.*;
import kaitech.util.MenuItemType;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuLoader {


    /**
     * Document builder and Document for parsing and storing XML file
     */
    private DocumentBuilder db = null;
    private Document parsedDoc = null;

    private String fileName;


    /**
     * Attributes for tempoaray storage of menu values
     */
    private String menuFrom;
    private String menuTo;
    private String menuDescription;
    private String menuTitle;
    private String code;
    private String name;
    private MenuItemType type;
    private Money cost;

    private List<String> ingredientNames;

    private Business business;

    public MenuLoader(String fileName, boolean validating) {
        //document builder factory setup
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validating);

        this.fileName = fileName;

        try {
            this.db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            System.exit(1);
        }

        //Setting Error handler
        db.setErrorHandler(new KaiTechErrorHandler(System.err));
    }


    /**
     * Takes the given filename and parses the XMl into a DOM tree
     * Throws a SAX exception so that the controller can notifiy the
     * user if the file is unable to be parsed due to wrong DTD
     * @throws SAXException when there is an error during parsing
     */

    public void parseInput() throws SAXException {
        try {
            this.parsedDoc = db.parse(this.fileName);
        } catch (IOException e) {
            //already handled by load data
        }
    }

    /**
     * Parses the Document created with ParseInput and
     * Returns a menu object from the file
     * @return MenuImpl menu
     */

    public Menu getMenu() {
        NodeList menuNodes = parsedDoc.getElementsByTagName("menu");
        NodeList children = menuNodes.item(0).getChildNodes();
        NamedNodeMap attr = menuNodes.item(0).getAttributes();

        menuTitle = children.item(1).getTextContent();
        menuDescription = children.item(3).getTextContent();

        menuFrom = attr.getNamedItem("from").getTextContent();
        menuTo = attr.getNamedItem("to").getTextContent();
        Map<String, MenuItem> menuItems = getMenuItems();

        return new MenuImpl(menuTitle, menuDescription, menuItems);
    }

    /**
     * Creates a map of Names and MenuItems from the
     * XML file and returns it
     * @return Map of Strings to MenuItems
     */

    public Map<String, MenuItem> getMenuItems() {
        Map<String, MenuItem> menuItems = new HashMap<>();
        NodeList itemNodes = parsedDoc.getElementsByTagName("item");
        int numItemIngredients = 0;

        Node itemNode;
        Node ingredientNode;
        NodeList children;
        NamedNodeMap attrs;

        for (int i = 0; i < itemNodes.getLength(); i++) {
            ingredientNames = new ArrayList<>();
            itemNode = itemNodes.item(i);

            children = itemNode.getChildNodes();
            attrs = itemNode.getAttributes();
            code = children.item(1).getTextContent();
            name = children.item(3).getTextContent();

            try {
                cost = Money.parse(attrs.getNamedItem("cost").getTextContent());
            } catch (NullPointerException nl) {
                cost = Money.parse("NZD 0.00");
            }
            switch (attrs.getNamedItem("type").getTextContent()) {
                case "beverage":
                    type = MenuItemType.BEVERAGE;
                    break;
                case "cocktail":
                    type = MenuItemType.COCKTAIL;
                    break;
                case "snack":
                    type = MenuItemType.SNACK;
                    break;
                case "asian":
                    type = MenuItemType.ASIAN;
                    break;
                case "grill":
                    type = MenuItemType.GRILL;
                    break;
                case "main":
                    type = MenuItemType.MAIN;
                    break;
                default:
                    type = MenuItemType.MISC;
                    break;
            }
            for (int k = 0; k < children.getLength(); k++) {
                if (children.item(k).getNodeName().equals("ingredient")) {
                    ingredientNode = children.item(k);
                    ingredientNode.getFirstChild().getNextSibling();
                    ingredientNames.add(ingredientNode.getFirstChild().getNextSibling().getTextContent());
                }
            }
            menuItems.put(code, new MenuItemImpl(code, name, cost, null, type, ingredientNames));
        }
        return menuItems;
    }

}
