package com.hearthsim.test.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.concrete.Assassinate;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestAssassinate {

    private HearthTreeNode board;
    private static final byte mana = 2;
    private static final byte attack0 = 2;
    private static final byte health0 = 5;
    private static final byte health1 = 1;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        Minion minion0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion2 = new Minion("" + 0, mana, attack0, health1, attack0, health1, health1);
        Minion minion3 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);

        Assassinate fb = new Assassinate();
        board.data_.placeCardHandCurrentPlayer(fb);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion2);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion3);

        board.data_.getCurrentPlayer().setMana((byte)10);
    }

    @Test
    public void test0() throws HSException {


        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        Deck deck = new Deck(cards);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode res;

        res = theCard.useOn(PlayerSide.WAITING_PLAYER, 1, board, deck, null);
        assertNotNull(res);
        assertEquals(res.data_.getNumCards_hand(), 0);
        assertEquals(res.data_.getCurrentPlayer().getNumMinions(), 1);
        assertEquals(res.data_.getWaitingPlayer().getNumMinions(), 2);
        assertEquals(res.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(res.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
        assertEquals(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health1);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(res.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(res.data_.getWaitingPlayerHero().getHealth(), 30);

    }

    @Test
    public void test1() throws HSException {

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        Deck deck = new Deck(cards);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode res;

        res = theCard.useOn(PlayerSide.WAITING_PLAYER, 2, board, deck, null);
        assertNotNull(res);
        assertEquals(res.data_.getNumCards_hand(), 0);
        assertEquals(res.data_.getCurrentPlayer().getNumMinions(), 1);
        assertEquals(res.data_.getWaitingPlayer().getNumMinions(), 2);
        assertEquals(res.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(res.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
        assertEquals(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(res.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(res.data_.getWaitingPlayerHero().getHealth(), 30);

    }

    @Test
    public void test2() throws HSException {


        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        Deck deck = new Deck(cards);

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode res;

        res = theCard.useOn(PlayerSide.WAITING_PLAYER, 3, board, deck, null);
        assertNotNull(res);
        assertEquals(res.data_.getNumCards_hand(), 0);
        assertEquals(res.data_.getCurrentPlayer().getNumMinions(), 1);
        assertEquals(res.data_.getWaitingPlayer().getNumMinions(), 2);
        assertEquals(res.data_.getCurrentPlayer().getMana(), 5);
        assertEquals(res.data_.getCurrentPlayer().getMinions().get(0).getHealth(), health0);
        assertEquals(res.data_.getCurrentPlayer().getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(0).getHealth(), health0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(1).getHealth(), health1);
        assertEquals(res.data_.getWaitingPlayer().getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(res.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(res.data_.getWaitingPlayerHero().getHealth(), 30);

    }
}
