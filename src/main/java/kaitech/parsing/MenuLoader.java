package kaitech.parsing;

import kaitech.api.model.Business;
import kaitech.api.model.Menu;
import kaitech.api.model.MenuItem;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuImpl;
import kaitech.model.MenuItemImpl;
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

    //still need the recipie stuff, and need to change menu with to/from


    //document builder and document for parsed doc
    private DocumentBuilder db = null;
    private Document parsedDoc = null;

    private String fileName;
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
        this.business = BusinessImpl.getInstance();
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

        //need to write new error handler
        db.setErrorHandler(new KaiTechErrorHandler(System.err));
    }


    /**
     * Takes the given filename and parses the XMl into a DOM tree
     *
     * @throws SAXException
     */

    public void parseInput() throws SAXException {
        try {
            this.parsedDoc = db.parse(this.fileName);
        } catch (IOException e) {
            //already handled by load data
        }
    }


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

    //reset?

}
