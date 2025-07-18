package tech.goodquestion.lembot.entity;

public enum SanctionType {
    BAN("gebannt"), TIMEOUT("timeouted"), WARN("verwarnt");

    private final String verbalizedSanctionTyp;

    SanctionType(final String verbalizedSanctionTyp) {
        this.verbalizedSanctionTyp = verbalizedSanctionTyp;
    }

    public String getVerbalizedSanctionTyp() {
        return verbalizedSanctionTyp;
    }
}