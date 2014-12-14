package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class AcidicSwampOoze extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final byte SPELL_DAMAGE = 0;
	
	public AcidicSwampOoze() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

	}
	@Override
	public EnumSet<BattlecryTargetType> getBattlecryTargets() {
		return EnumSet.of(BattlecryTargetType.NO_TARGET);
	}
	
	/**
	 * Battlecry: Destroy your opponent's weapon
	 */
	@Override
	public HearthTreeNode useUntargetableBattlecry_core(
			Minion minionPlacementTarget,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly
		) throws HSException
	{
        boardState.data_.getWaitingPlayerHero().destroyWeapon();
		return boardState;
	}

}
