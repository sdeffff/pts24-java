package sk.uniba.fmph.dcs.game_board;

import java.util.Collection;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Player;

public class PlaceOnFieldsAdaptor implements InterfaceFigureLocationInternal {
    private ToolMakerHutFields toolMakerHutFields;
    public PlaceOnFieldsAdaptor(ToolMakerHutFields toolMakerHutFields){
        this.toolMakerHutFields = toolMakerHutFields;
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(figureCount >= 1 && toolMakerHutFields.canPlaceOnFields(player)){
            toolMakerHutFields.placeOnFields(player);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(!player.playerBoard().hasFigures(count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(toolMakerHutFields.canPlaceOnFields(player)){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if(!toolMakerHutFields.canPlaceOnFields(player)){
            return ActionResult.FAILURE;
        }

        if(toolMakerHutFields.actionFields(player)) {
            return ActionResult.ACTION_DONE;
        }

        return ActionResult.FAILURE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(toolMakerHutFields.canPlaceOnFields(player)){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }else{
            return HasAction.NO_ACTION_POSSIBLE;
        }
    }

    @Override
    public boolean newTurn() {
        return false;
    }
}
