package ru.melnikov.hdAgent.models;

public enum PhoneType {

    t9641("AVAYA 9641","9641"),
    t9608("AVAYA 9608","9608"),
    t9620("AVAYA 9620","9620"),
    t4620("ONE-X Communicator","4620"),
    t1608("AVAYA 1608","1608"),
    uncknown("null","");

    private final String name;
    private final String type;

    PhoneType(String name, String type) {
        this.type = type;
        this.name = name;
    }

    public static PhoneType getByType(String type) {
        for (PhoneType item : PhoneType.values()) {
            if (item.type.equals(type)) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.name;
    }


}
