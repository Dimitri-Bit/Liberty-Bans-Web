package me.dimitri.liberty.utils;

public enum PType {

    BAN('0'),
    MUTE('1'),
    WARN('2'),
    KICK('3'),
    UNKNOWN('9');

    private final char type;

    PType(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }
}
