package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestCharge {

	private BoardModel board;
	private DummyStateFunc scoreFunc;
	private static final byte mana = 1;
	private static final byte attack0 = 2;
	private static final byte health0 = 3;
	private static final byte health1 = 7;

	private class DummyStateFunc implements BoardScorer {

		@Override
		public double boardScore(BoardModel xval) {
			return 0;
		}

		@Override
		public double cardInHandScore(Card card) {
			return 0;
		}

		@Override
		public double heroHealthScore_p0(double heroHealth, double heroArmor) {
			return 0;
		}

		@Override
		public double heroHealthScore_p1(double heroHealth, double heroArmor) {
			return 0;
		}
	}

	@Before
	public void setup() throws HSException {
		board = new BoardModel();
		scoreFunc = new DummyStateFunc();

		Minion minion1_0 = new Minion("" + 0, mana, attack0, health0, attack0, health0, health0);
		board.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);

	}

	@Test
	public void testAiMinionAttack() throws HSException {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, (byte)0, (byte)0);
		minion.setCharge(true);
		board.placeMinion(PlayerSide.CURRENT_PLAYER, minion);

		BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null, 2000000000);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch(HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 3 possibilites: attack enemy minion, attack enemy hero, do nothing
		assertEquals(tree.getNumNodesTried(), 3);
	}

	@Test
	public void testAiPlayChargeAndAttack() {
		Minion minion = new Minion("" + 0, mana, attack0, health1, attack0, (byte)0, (byte)0);
		minion.setCharge(true);
		board.placeCardHandCurrentPlayer(minion);
		board.getCurrentPlayer().setMana(1);

		BoardStateFactoryBase factory = new DepthBoardStateFactory(null, null);
		HearthTreeNode tree = new HearthTreeNode(board);
		try {
			tree = factory.doMoves(tree, scoreFunc);
		} catch(HSException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// 4 possibilities:
		// 1. Do nothing
		// 2. Play charge minion card, then don't attack
		// 3. Play card, charge attack enemy hero
		// 4. Play card, charge attack enemy minion
		assertEquals(tree.getNumNodesTried(), 4);
	}
}
