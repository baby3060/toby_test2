package com.tobsec.model;

public enum Level {
    GOLD(3, null), SILVER(2, GOLD), BRONZE(1, SILVER);

    private int value;
    private Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int getValue() {
        return this.value;
    }

    public Level getNextLevel() {
        return this.next;
    }

    public static Level valueOf(int value) {
        switch(value) {
            case 1 : return BRONZE; 
            case 2 : return SILVER;
            case 3 : return GOLD;
            default : throw new AssertionError("Unknown value : " + value);
        }
    }
}