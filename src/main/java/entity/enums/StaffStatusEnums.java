package entity.enums;

public enum StaffStatusEnums {
    NO_STATE("no state"),
    INACTIVE("not active"),
    ACTIVE("active");

    private String name;
    StaffStatusEnums(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
