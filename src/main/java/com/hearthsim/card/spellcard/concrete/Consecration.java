package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.spellcard.SpellDamageAoe;

public class Consecration extends SpellDamageAoe {

    private static final byte DAMAGE_AMOUNT = 2;

    /**
     * Constructor
     *
     * @param hasBeenUsed Whether the card has already been used or not
     */
    public Consecration(boolean hasBeenUsed) {
        super((byte)4, DAMAGE_AMOUNT, hasBeenUsed);
        this.hitsEnemyHero = true;
    }

    /**
     * Constructor
     *
     * Defaults to hasBeenUsed = false
     */
    public Consecration() {
        this(false);
    }
}
