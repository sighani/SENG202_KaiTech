package kaitech.api.model;

import kaitech.util.ThreeValueLogic;
import kaitech.util.UnitType;
import org.joda.money.Money;

public interface Ingredient {
    String getCode();

    String getName();

    UnitType getUnit();

    ThreeValueLogic isVeg();

    ThreeValueLogic isVegan();

    ThreeValueLogic isGF();

    Money getPrice();

    void setCode(String code);

    void setName(String name);

    void setUnit(UnitType unit);

    void setPrice(Money price);

    void setIsVeg(ThreeValueLogic isVeg);

    void setIsVegan(ThreeValueLogic isVegan);

    void setIsGF(ThreeValueLogic isGF);
}
