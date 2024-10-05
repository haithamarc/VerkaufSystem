package entity.enums;

public enum OrderStatusEnum {
    NO_STATE("No state"),
    RECEIVED("Received"),
    IN_PROGRESS("In progress"),
    CANCELED("Canceled"),
    COMPLETED("Completed");

    private final String name;

    OrderStatusEnum(String name){ this.name = name; }

    public String getName() {return name; }

}
