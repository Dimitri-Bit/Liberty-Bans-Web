package me.dimitri.liberty.model;

public class Punishment {

    private String victimUuid, victimUsername, operatorUuid, operatorUsername, reason;
    private boolean active;

    public Punishment() {
        super();
    }

    public String getVictimUuid() {
        return victimUuid;
    }

    public void setVictimUuid(String victimUuid) {
        this.victimUuid = victimUuid;
    }

    public String getVictimUsername() {
        return victimUsername;
    }

    public void setVictimUsername(String victimUsername) {
        this.victimUsername = victimUsername;
    }

    public String getOperatorUuid() {
        return operatorUuid;
    }

    public void setOperatorUuid(String operatorUuid) {
        this.operatorUuid = operatorUuid;
    }

    public String getOperatorUsername() {
        return operatorUsername;
    }

    public void setOperatorUsername(String operatorUsername) {
        this.operatorUsername = operatorUsername;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
