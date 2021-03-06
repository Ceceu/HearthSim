package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.MirrorImageMinion;
import com.hearthsim.card.spellcard.concrete.MirrorImage;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMirrorImage {


    private HearthTreeNode board;
    private Deck deck;
    private static final byte mana = 2;
    private static final byte attack0 = 5;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        Minion minion0_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new Minion("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new MirrorImage();
        board.data_.placeCardHandCurrentPlayer(fb);

        board.data_.getCurrentPlayer().setMana((byte)10);
        board.data_.getWaitingPlayer().setMana((byte)4);

        board.data_.getCurrentPlayer().setMaxMana((byte)7);
        board.data_.getWaitingPlayer().setMaxMana((byte)4);

    }

    @Test
    public void test2() throws HSException {
        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);

        assertFalse(ret == null);
        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getNumMinions(), 4);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 9);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 4);
        assertEquals(board.data_.getCurrentPlayerHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayerHero().getHealth(), 30);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getHealth(), 2);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getHealth(), 2);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(0).getHealth(), health0);
        assertEquals(PlayerSide.WAITING_PLAYER.getPlayer(board).getMinions().get(1).getHealth(), health1 - 1);

        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(0).getTotalAttack(), attack0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(1).getTotalAttack(), attack0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2).getTotalAttack(), 0);
        assertEquals(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3).getTotalAttack(), 0);

        assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(2) instanceof MirrorImageMinion);
        assertTrue(PlayerSide.CURRENT_PLAYER.getPlayer(board).getMinions().get(3) instanceof MirrorImageMinion);
    }

    @Test
    public void testCannotPlayWithFullBoard() throws HSException {
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

        Card theCard = board.data_.getCurrentPlayerCardHand(0);
        Minion target = board.data_.getCharacter(PlayerSide.CURRENT_PLAYER, 0);
        assertTrue(theCard.canBeUsedOn(PlayerSide.CURRENT_PLAYER, target, board.data_)); // TODO should implement canBeUsedOn

        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, target, board, null, null);
        assertNull(ret);
    }
}
