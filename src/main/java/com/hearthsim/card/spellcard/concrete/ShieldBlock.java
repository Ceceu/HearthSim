package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.CardDrawNode;
import com.hearthsim.util.tree.HearthTreeNode;

public class ShieldBlock extends SpellCard {


    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    public ShieldBlock(boolean hasBeenUsed) {
        super((byte)3, hasBeenUsed);

        this.canTargetEnemyHero = false;
        this.canTargetEnemyMinions = false;
        this.canTargetOwnMinions = false;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public ShieldBlock() {
        this(false);
    }

    /**
     *
     * Use the card on the given target
     *
     * Gives all friendly characters +2 attack for this turn
     *
     *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
     */
    @Override
    protected HearthTreeNode use_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1,
            boolean singleRealizationOnly)
        throws HSException {
        HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
        if (toRet != null) {
            toRet.data_.getCurrentPlayerHero().setArmor((byte)(toRet.data_.getCurrentPlayerHero().getArmor() + 5));
            if (toRet instanceof CardDrawNode) {
                ((CardDrawNode) toRet).addNumCardsToDraw(1);
            } else {
                toRet = new CardDrawNode(toRet, 1); //draw two cards
            }
        }
        return toRet;

    }
}
