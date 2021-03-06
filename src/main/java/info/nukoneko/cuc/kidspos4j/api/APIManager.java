package info.nukoneko.cuc.kidspos4j.api;

import info.nukoneko.cuc.kidspos4j.KidsPos4jConfig;

public class APIManager {
    private APIManager() {

    }

    public static Item Item() {
        return KidsPos4jConfig.getRetrofit().create(Item.class);
    }

    public static Setting Setting() {
        return KidsPos4jConfig.getRetrofit().create(Setting.class);
    }

    public static ItemGenre ItemGenre() {
        return KidsPos4jConfig.getRetrofit().create(ItemGenre.class);
    }

    public static Staff Staff() {
        return KidsPos4jConfig.getRetrofit().create(Staff.class);
    }

    public static Store Store() {
        return KidsPos4jConfig.getRetrofit().create(Store.class);
    }

    public static Sale Sale() {
        return KidsPos4jConfig.getRetrofit().create(Sale.class);
    }
}
