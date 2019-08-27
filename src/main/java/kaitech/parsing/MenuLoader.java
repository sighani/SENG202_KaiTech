package kaitech.parsing;

import kaitech.model.Menu;
import kaitech.model.MenuItem;
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

    private String fileName, menuFrom, menuTo, menuDescription, menuTitle;
    private String code, name;
    private MenuItemType type;
    private Money cost;

    private List<String> ingredientNames;

;


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
        NodeList menuNodes = parsedDoc.getElementsByTagName("menu");
        NodeList children = menuNodes.item(0).getChildNodes();
        NamedNodeMap attr = menuNodes.item(0).getAttributes();

        menuTitle = children.item(1).getTextContent();
        menuDescription = children.item(3).getTextContent();

        menuFrom = attr.getNamedItem("from").getTextContent();
        menuTo = attr.getNamedItem("to").getTextContent();
        Map<String, MenuItem> menuItems = getMenuItems();

        Menu menu = new Menu(menuTitle, menuDescription, menuItems);

        return menu;
    }


    public Map<String, MenuItem> getMenuItems() {
        Map<String, MenuItem> menuItems = new HashMap<String, MenuItem>();
        NodeList itemNodes = parsedDoc.getElementsByTagName("item");
        int numItemIngredients = 0;

        Node itemNode;
        Node ingredientNode;
        NodeList children;
        NamedNodeMap attrs;

        for (int i = 0; i < itemNodes.getLength(); i++) {
            ingredientNames = new ArrayList<String>();
            itemNode = itemNodes.item(i);

            children = itemNode.getChildNodes();
            attrs = itemNode.getAttributes();
            code = children.item(1).getTextContent();
            name = children.item(3).getTextContent();

            try {
                cost = Money.parse(attrs.getNamedItem("cost").getTextContent());
            }catch (NullPointerException nl){
                cost = Money.parse("NZD 0.00");
            }
            switch(attrs.getNamedItem("type").getTextContent()){
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
            menuItems.put(code, new MenuItem(code, name, ingredientNames, null, cost, type));
        }
        return menuItems;
    }

    //reset?

}
