package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ColdlightSeer;
import com.hearthsim.card.minion.concrete.MurlocRaider;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestColdlightSeer {

    private HearthTreeNode board;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        Minion minion0_0 = new MurlocRaider();
        Minion minion0_1 = new RaidLeader();
        Minion minion1_1 = new RaidLeader();

        board.data_.placeCardHandCurrentPlayer(minion0_0);
        board.data_.placeCardHandCurrentPlayer(minion0_1);

        board.data_.placeCardHandWaitingPlayer(minion1_1);

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new ColdlightSeer();
        board.data_.placeCardHandCurrentPlayer(fb);

        board.data_.getCurrentPlayer().setMana((byte)8);
        board.data_.getWaitingPlayer().setMana((byte)8);

        board.data_.getCurrentPlayer().setMaxMana((byte)8);
        board.data_.getWaitingPlayer().setMaxMana((byte)8);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayerHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);
        board.data_.getCurrentPlayerCardHand(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayerHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();

    }

    @Test
    public void test0() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, null);

        assertNull(ret);
        assertEquals(board.data_.getNumCards_hand(), 1);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 8);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 1);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board, deck, null);

        assertFalse(ret == null);
        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 3);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 1);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 8);
        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalHealth(), 3);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalHealth(), 3);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalHealth(), 2);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), 3);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 3);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), 2);
    }
}
