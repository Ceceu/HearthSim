package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleSummonMinionAction extends DeathrattleAction {

    private final int numMinions_;
    private final Class<?> minionClass_;

    public DeathrattleSummonMinionAction(Class<?> minionClass, int numMnions) {
        numMinions_ = numMnions;
        minionClass_ = minionClass;
    }

    @Override
    public HearthTreeNode performAction(Card origin,
                                        PlayerSide playerSide,
                                        HearthTreeNode boardState,
                                        Deck deckPlayer0,
                                        Deck deckPlayer1,
                                        boolean singleRealizationOnly) throws HSException {

        HearthTreeNode toRet = super.performAction(origin, playerSide, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

        int targetIndex = playerSide.getPlayer(boardState).getNumMinions();
        if (origin instanceof Minion) {
            targetIndex = toRet.data_.getMinions(playerSide).indexOf(origin);
            toRet.data_.removeMinion((Minion) origin);
        }
        Minion placementTarget = toRet.data_.getCharacter(playerSide, targetIndex);

        int numMinionsToActuallySummon = numMinions_;
        if (playerSide.getPlayer(toRet).getMinions().size() + numMinions_ > 7)
            numMinionsToActuallySummon = 7 - playerSide.getPlayer(toRet).getMinions().size();

        for (int index = 0; index < numMinionsToActuallySummon; ++index) {
            try {
                Minion newMinion = (Minion) minionClass_.newInstance();
                toRet = newMinion.summonMinion(playerSide, placementTarget, toRet, deckPlayer0, deckPlayer1, false, true);
            } catch (InstantiationException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                throw new HSException();
            }
        }
        return toRet;
    }
}
