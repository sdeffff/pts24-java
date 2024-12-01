package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;

public interface PlayerBoard {
    boolean hasFigures(int count);
    void giveEffect(Collection<Effect> stuff);
}
