package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Warrior extends Hero {

    @Override
    public boolean canBeUsedOn(PlayerSide playerSide, Minion minion, BoardModel boardModel) {
        return playerSide == PlayerSide.CURRENT_PLAYER && minion instanceof Hero;
    }

    /**
     * Use the hero ability on a given target
     *
     * Warrior: +2 armor
     *
     *
     *
     * @param targetPlayerSide
     * @param boardState
     * @return
     */
    @Override
    public HearthTreeNode useHeroAbility_core(
            PlayerSide targetPlayerSide,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = boardState;
        if (isHero(targetMinion) && targetPlayerSide == PlayerSide.CURRENT_PLAYER) {
            this.hasBeenUsed = true;
            toRet.data_.getCurrentPlayer().subtractMana(HERO_ABILITY_COST);
            ((Hero)targetMinion).setArmor((byte)(((Hero)targetMinion).getArmor() + 2));
            return toRet;
        } else {
            return null;
        }
    }

}
