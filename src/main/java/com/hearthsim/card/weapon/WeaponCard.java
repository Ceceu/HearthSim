package com.hearthsim.card.weapon;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.deathrattle.DeathrattleAction;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public abstract class WeaponCard extends Card {

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }

    private boolean immune = false; // Does not take damage from attacking

    byte weaponCharge;
    byte weaponDamage;

    public WeaponCard() {
        ImplementedCardList cardList = ImplementedCardList.getInstance();
        ImplementedCardList.ImplementedCard implementedCard = cardList.getCardForClass(this.getClass());
        weaponCharge = (byte) implementedCard.durability;
        weaponDamage = (byte) implementedCard.attack_;
        name_ = implementedCard.name_;
        baseManaCost = (byte) implementedCard.mana_;
        isInHand_ = true;
    }

    @Override
    public WeaponCard deepCopy() {
        WeaponCard weapon = (WeaponCard) super.deepCopy();
        weapon.weaponCharge = weaponCharge;
        weapon.weaponDamage = weaponDamage;
        return weapon;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        if (this.weaponDamage != ((WeaponCard) other).weaponDamage) {
            return false;
        }

        if (this.weaponCharge != ((WeaponCard) other).weaponCharge) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + weaponCharge;
        result = 31 * result + weaponDamage;
        return result;
    }

    /**
     * Use the card on the given target
     * <p>
     * This is the core implementation of card's ability
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
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
        if (this.hasBeenUsed()) {
            //Card is already used, nothing to do
            return null;
        }

        if (isWaitingPlayer(side) || isNotHero(targetMinion)) {
            return null;
        }

        HearthTreeNode toRet = boardState;
        if (toRet != null) {
            DeathrattleAction weaponDeathrattle = toRet.data_.getCurrentPlayerHero().setWeapon(this);
            if (weaponDeathrattle != null) {
                toRet = weaponDeathrattle.performAction(null, side, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
            }
            this.hasBeenUsed(true);
        }

        return super.use_core(side, targetMinion, toRet, deckPlayer0, deckPlayer1, singleRealizationOnly);
    }

    public byte getWeaponCharge() {
        return weaponCharge;
    }

    public void setWeaponCharge(byte weaponCharge) {
        this.weaponCharge = weaponCharge;
    }

    public byte getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(byte weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public void useWeaponCharge() {
        if (!this.immune) {
            this.setWeaponCharge((byte) (this.getWeaponCharge() - 1));
        }
    }

    public void beforeAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet, Deck deckPlayer0, Deck deckPlayer1) throws HSException {

    }

    public void afterAttack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode toRet, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        this.useWeaponCharge();
    }

    public void minionSummonedEvent(PlayerSide thisMinionPlayerSide, PlayerSide summonedMinionPlayerSide, Minion summonedMinion, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) {

    }
}
