package tech.goodquestion.lembot.entity;

public final class UserData {

    public Long userId;
    public Long amountOf;
    public Long nextHigherUserId;
    public Integer nextHigherUserAmountOf;

    public boolean hasBump() {
        return amountOf != null;
    }

    public boolean isTop() {
        return nextHigherUserId == null || nextHigherUserAmountOf == null;
    }
}