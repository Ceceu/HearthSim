package com.hearthsim.card.minion.concrete;

import java.util.EnumSet;

import com.hearthsim.card.minion.AuraTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionWithAura;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;

public class TimberWolf extends Minion implements MinionWithAura {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public TimberWolf() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;

        this.tribe = MinionTribe.BEAST;
    }

    @Override
    public EnumSet<AuraTargetType> getAuraTargets() {
        return EnumSet.of(AuraTargetType.AURA_FRIENDLY_MINIONS);
    }

    @Override
    public void applyAura(PlayerSide targetSide, Minion targetMinion,
            BoardModel boardModel) {
        if (targetMinion.getTribe() == Minion.MinionTribe.BEAST)
            targetMinion.setAuraAttack((byte)(targetMinion.getAuraAttack() + 1));
    }


    @Override
    public void removeAura(PlayerSide targetSide, Minion targetMinion,
            BoardModel boardModel) {
        if (targetMinion.getTribe() == Minion.MinionTribe.BEAST)
            targetMinion.setAuraAttack((byte)(targetMinion.getAuraAttack() - 1));
    }

}
