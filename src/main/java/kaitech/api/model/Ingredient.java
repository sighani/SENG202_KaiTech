package kaitech.api.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

public interface Ingredient {
    String code();

    String name();

    UnitType unit();

    ThreeValueLogic isVeg();

    ThreeValueLogic isVegan();

    ThreeValueLogic isGF();

    Money getPrice();
}
