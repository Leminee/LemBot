package tech.goodquestion.lembot.entity;

public enum SanctionType {
    BAN("gebannt"),
    MUTE("gemutet"),
    WARN("verwarnt");

    private String verbalizedSanctionTyp;

    SanctionType(String verbalizedSanctionTyp) {
        this.verbalizedSanctionTyp = verbalizedSanctionTyp;
    }

    public String getVerbalizedSanctionTyp() {
        return verbalizedSanctionTyp;
    }
}
