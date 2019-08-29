package kaitech.api.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

public interface Ingredient {
    String getCode();

    String getName();

    UnitType getUnit();

    ThreeValueLogic getIsVeg();

    ThreeValueLogic getIsVegan();

    ThreeValueLogic getIsGF();

    Money getPrice();
}
