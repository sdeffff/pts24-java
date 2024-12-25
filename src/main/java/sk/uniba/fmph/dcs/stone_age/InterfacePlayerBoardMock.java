package sk.uniba.fmph.dcs.stone_age;

public interface InterfacePlayerBoardMock {
    boolean hasFigures(int count);
    boolean takeFigures(int count);
    boolean takeResources(Collection<Effect> resources);
    void giveCard(CivilizationCard card);
    void giveEffect(Collection<Effect> effects);
}
