package sk.uniba.fmph.dcs.stone_age;

public interface InterfaceTakeReward {

    public boolean takeReward(PlayerOrder player, Effect reward);
    public HasAction tryMakeAction(PlayerOrder player);

}
