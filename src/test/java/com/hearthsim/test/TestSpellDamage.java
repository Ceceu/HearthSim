package com.hearthsim.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.KoboldGeomancer;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestSpellDamage {

    private HearthTreeNode board;
    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 2;
    private static final byte health1 = 3;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
        Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        board.data_.getCurrentPlayer().setMana((byte)2);
    }

    @Test
    public void testCanTargetOwnHero() throws HSException {
        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        assertEquals(ret, board);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);

        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 28);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
    }

    @Test
    public void testCanTargetEnemyHero() throws HSException {
        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, null, null);
        assertEquals(ret, board);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);

        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 28);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
    }

    @Test
    public void testKillOwnMinion() throws HSException {
        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board, null, null);
        assertEquals(ret, board);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);

        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
    }

    @Test
    public void testCanTargetOwnMinion() throws HSException {
        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board, null, null);
        assertEquals(ret, board);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);

        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
    }

    @Test
    public void testKillEnemyMinion() throws HSException {
        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 1, board, null, null);
        assertEquals(ret, board);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);

        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health1);
    }

    @Test
    public void testTargetEnemyMinion() throws HSException {
        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 2, board, null, null);
        assertEquals(ret, board);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);

        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 2);
    }

    @Test
    public void testSpellpower() throws HSException {
        KoboldGeomancer kobold = new KoboldGeomancer();
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, kobold);

        HolySmite hs = new HolySmite();
        board.data_.placeCardHandCurrentPlayer(hs);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 2, board, null, null);
        assertEquals(ret, board);

        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
    }

    @Test
    public void testDeepCopyDamage() throws HSException {
        HolySmite smite = new HolySmite();
        HolySmite copy = (HolySmite)smite.deepCopy();

        assertEquals(smite.getAttack(), copy.getAttack());
    }
}
