package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Demon;

public class Voidwalker extends Demon {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public Voidwalker() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}

}
