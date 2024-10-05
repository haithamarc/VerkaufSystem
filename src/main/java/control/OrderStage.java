package control;

/**
 * is  Enum to determine in which stage of the order someone is,  to reduce sending Data from one View class to another.
 */
public enum OrderStage {
    SELECT_CUSTOMER("SELECT_CUSTOMER"), SELECT_PRODUCTS("SELECT_PRODUCTS"), CUSTOMIZE_ORDER_ITEM("CUSTOMIZE_ORDER_ITEM"), VIEW_CART("VIEW_CART"),;

    private final String name;

    OrderStage(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
