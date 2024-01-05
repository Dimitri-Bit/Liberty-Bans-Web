package me.dimitri.libertyweb.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable.Serializable
public class WebPunishment {

    private String victimUuid, victimUsername,
            operatorUuid, operatorUsername, reason, label, punishmentLength, startDate, endDate;
    private boolean active;

    public WebPunishment() {
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPunishmentLength() {
        return punishmentLength;
    }

    public void setPunishmentLength(String punishmentLength) {
        this.punishmentLength = punishmentLength;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
