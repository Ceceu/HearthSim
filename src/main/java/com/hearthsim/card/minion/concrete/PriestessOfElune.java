package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionUntargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class PriestessOfElune extends Minion implements MinionUntargetableBattlecry {

    private static final boolean HERO_TARGETABLE = true;

    private static final byte SPELL_DAMAGE = 1;

    public PriestessOfElune() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

    }

    /**
     * Battlecry: Restore 4 Health to your Hero
     */
    @Override
    public HearthTreeNode useUntargetableBattlecry_core(
            Minion minionPlacementTarget,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        toRet = toRet.data_.getCurrentPlayerHero().takeHeal((byte)4, PlayerSide.CURRENT_PLAYER, toRet, deckPlayer0, deckPlayer1);
        return toRet;
    }

}
