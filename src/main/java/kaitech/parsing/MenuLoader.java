package kaitech.parsing;


import kaitech.api.model.*;
import kaitech.model.BusinessImpl;
import kaitech.model.MenuImpl;
import kaitech.model.MenuItemImpl;
import kaitech.model.RecipeImpl;
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

    private List<String> missingIngredientCodes;

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

        //Setting Error handler
        db.setErrorHandler(new KaiTechErrorHandler(System.err));
    }


    /**
     * Takes the given filename and parses the XMl into a DOM tree
     * Throws a SAX exception so that the controller can notify the
     * user if the file is unable to be parsed due to wrong DTD
     *
     * @throws SAXException when there is an error during parsing
     * @throws IOException  when there is an error during parsing
     */

    public void parseInput() throws SAXException, IOException {
        this.parsedDoc = db.parse(this.fileName);
    }

    /**
     * Parses the Document created with ParseInput and
     * Returns a menu object from the file
     *
     * @return MenuImpl menu
     */

    public Menu getMenu() {
        NodeList menuNodes = parsedDoc.getElementsByTagName("menu");
        NodeList children = menuNodes.item(0).getChildNodes();

        String menuTitle = children.item(1).getTextContent();
        String menuDescription = children.item(3).getTextContent();

        Map<String, MenuItem> menuItems = getMenuItems();

        if (missingIngredientCodes.size() == 0) {
            return new MenuImpl(menuTitle, menuDescription, menuItems);
        } else {
            return null;
        }

    }

    /**
     * Creates a map of Names and MenuItems from the
     * XML file and returns it
     *
     * @return Map of Strings to MenuItems
     */

    public Map<String, MenuItem> getMenuItems() {
        Map<String, MenuItem> menuItems = new HashMap<>();
        NodeList itemNodes = parsedDoc.getElementsByTagName("item");

        missingIngredientCodes = new ArrayList<String>();

        Node itemNode;
        Node ingredientNode;
        NodeList children;
        NamedNodeMap attrs;


        for (int i = 0; i < itemNodes.getLength(); i++) {
            List<String> ingredientNames = new ArrayList<>();
            itemNode = itemNodes.item(i);

            children = itemNode.getChildNodes();
            attrs = itemNode.getAttributes();
            String code = children.item(1).getTextContent();
            String name = children.item(3).getTextContent();

            Money cost;
            try {
                cost = Money.parse(attrs.getNamedItem("cost").getTextContent());
            } catch (NullPointerException nl) {
                cost = Money.parse("NZD 0.00");
            }
            MenuItemType type;
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

            Map<Ingredient, Integer> recipeTempMap = new HashMap<>();

            for (int k = 0; k < children.getLength(); k++) {
                if (children.item(k).getNodeName().equals("ingredient")) {
                    ingredientNode = children.item(k);
                    ingredientNode.getFirstChild().getNextSibling();
                    String ingredientCode = ingredientNode.getFirstChild().getNextSibling().getTextContent();
                    //we are given codes :) not names
                    String ingredientCount = ingredientNode.getAttributes().getNamedItem("quantity").getTextContent();
                    String ingredientUnit = ingredientNode.getAttributes().getNamedItem("unit").getTextContent();

                    //in ingredient exits then add it to recipe, else we need popups to create new ones
                    if (business.getIngredientTable().getAllIngredientCodes().contains(ingredientCode)) {
                        recipeTempMap.put(business.getIngredientTable().getIngredient(ingredientCode), Integer.parseInt(ingredientCount));
                    } else {
                        missingIngredientCodes.add(ingredientCode);
                    }

                }
            }
            //now we need to make a new recipe with all the ingreidents and a name
            Recipe tempRecipe = new RecipeImpl(code + "-Rec", recipeTempMap);
            menuItems.put(code, new MenuItemImpl(code, name, cost, business.getRecipeTable().getOrAddRecipe(tempRecipe), type, ingredientNames));
        }
        return menuItems;
    }

    public List<String> getMissingIngredientCodes() {
        return missingIngredientCodes;
    }

    public void setMissingIngredientCodes(List<String> missingIngredientCodes) {
        this.missingIngredientCodes = missingIngredientCodes;
    }
}
