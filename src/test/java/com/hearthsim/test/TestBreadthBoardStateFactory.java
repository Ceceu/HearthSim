package com.hearthsim.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.ArgentSquire;
import com.hearthsim.card.minion.concrete.BloodfenRaptor;
import com.hearthsim.card.minion.concrete.DarkIronDwarf;
import com.hearthsim.card.minion.concrete.FenCreeper;
import com.hearthsim.card.minion.concrete.RiverCrocolisk;
import com.hearthsim.card.minion.concrete.ShatteredSunCleric;
import com.hearthsim.card.minion.concrete.StonetuskBoar;
import com.hearthsim.card.minion.concrete.Sunwalker;
import com.hearthsim.card.spellcard.concrete.AnimalCompanion;
import com.hearthsim.card.spellcard.concrete.ArcaneIntellect;
import com.hearthsim.card.spellcard.concrete.EarthShock;
import com.hearthsim.card.spellcard.concrete.Frostbolt;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.Silence;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.player.playercontroller.BoardScorer;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.factory.BreadthBoardStateFactory;
import com.hearthsim.util.factory.DepthBoardStateFactory;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.StopNode;

public class TestBreadthBoardStateFactory {
    @Rule
    public TestName name = new TestName();

    protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public Deck deck0;
    public Deck deck1;

    @Before
    public void setup() {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck0 = new Deck(cards);
        deck1 = deck0.deepCopy();
    }

    @Test
    public void testRepeatableStatesMinionAttacks() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new RiverCrocolisk());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 2);
        assertActionTreeIsRepeatable(root);
    }

    @Test
    public void testRepeatableStatesMinionPlacement() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)3);
        startingBoard.getCurrentPlayer().addMaxMana((byte)3);
        startingBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
        startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 2);
        assertActionTreeIsRepeatable(root);
    }

    @Test
    public void testRepeatableStatesCardTargets() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)4);
        firstPlayer.addMaxMana((byte)4);
        firstPlayer.placeCardHand(new HolySmite());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 2);
        assertActionTreeIsRepeatable(root);
    }

    @Test
    public void testRepeatableStatesCardRngTargets() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)5);
        firstPlayer.addMaxMana((byte)5);
        firstPlayer.placeCardHand(new AnimalCompanion());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 3);
        assertActionTreeIsRepeatable(root);
    }

    @Test
    public void testRepeatableStatesCardDraw() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)8);
        firstPlayer.placeCardHand(new ArcaneIntellect());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 3);
        assertActionTreeIsRepeatable(root);
    }

    @Test
    public void testDuplicateStatesMinionAttacks() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new RiverCrocolisk());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 2);
        assertDescendentsDoNotContainDuplicates(root);
    }

    @Test
    public void testDuplicateStatesMinionPlacement() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)3);
        startingBoard.getCurrentPlayer().addMaxMana((byte)3);
        startingBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
        startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 2);
        assertDescendentsDoNotContainDuplicates(root);
    }

    @Test
    public void testDuplicateStatesCardTargets() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)4);
        firstPlayer.addMaxMana((byte)4);
        firstPlayer.placeCardHand(new HolySmite());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 2);
        assertDescendentsDoNotContainDuplicates(root);
    }

    @Test
    public void testDuplicateStatesCardRngTargets() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)5);
        firstPlayer.addMaxMana((byte)5);
        firstPlayer.placeCardHand(new AnimalCompanion());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        BreadthBoardStateFactory factory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        HearthTreeNode root = new HearthTreeNode(startingBoard);
        factory.addChildLayers(root, 4);
        assertDescendentsDoNotContainDuplicates(root);
    }

    @Test
    public void testBreadthDepthAttackingMinionsMultiple() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());
        startingBoard.getCharacter(PlayerSide.CURRENT_PLAYER, 0).addAttack((byte)1);

        this.testBreadthDepth(startingBoard);
    }

    @Test
    public void testBreadthDepthCardTargeting() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)2);
        firstPlayer.addMaxMana((byte)2);
        firstPlayer.placeCardHand(new HolySmite());
        firstPlayer.placeCardHand(new HolySmite());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        this.testBreadthDepth(startingBoard);
    }

    @Test
    public void testBreadthDepthMinionPlacementPositioning() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)3);
        startingBoard.getCurrentPlayer().addMaxMana((byte)3);
        startingBoard.getCurrentPlayer().placeCardHand(new BloodfenRaptor());
        startingBoard.getCurrentPlayer().placeCardHand(new ArgentSquire());

        Minion bloodfenRaptor = new BloodfenRaptor();
        bloodfenRaptor.hasAttacked(true);
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, bloodfenRaptor);

        this.testBreadthDepth(startingBoard);
    }

    @Test
    public void testBreadthDepthMinionBattlecry() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)4);
        startingBoard.getCurrentPlayer().addMaxMana((byte)4);
        startingBoard.getCurrentPlayer().placeCardHand(new DarkIronDwarf());
        startingBoard.getCurrentPlayer().placeCardHand(new ShatteredSunCleric());

        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

        this.testBreadthDepth(startingBoard);
    }

    @Test
    public void testBreadthDepthCardRng() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)5);
        firstPlayer.addMaxMana((byte)5);
        firstPlayer.placeCardHand(new AnimalCompanion());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        this.testBreadthDepth(startingBoard);
    }

    @Test
    public void testBreadthDepthCardDraw() throws HSException {
        BoardModel startingBoard = new BoardModel();
        PlayerModel firstPlayer = startingBoard.getCurrentPlayer();
        firstPlayer.addMana((byte)5);
        firstPlayer.addMaxMana((byte)5);
        firstPlayer.placeCardHand(new ArcaneIntellect());
        firstPlayer.placeCardHand(new Frostbolt());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new RiverCrocolisk());

        this.testBreadthDepth(startingBoard);
    }

    @Test
    @Ignore("Long test")
    public void testBreadthDepthComplicatedDupes() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)10);
        startingBoard.getCurrentPlayer().addMaxMana((byte)10);
        startingBoard.getCurrentPlayer().placeCardHand(new StonetuskBoar());
        startingBoard.getCurrentPlayer().placeCardHand(new Silence());
        startingBoard.getCurrentPlayer().placeCardHand(new Silence());

        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new ArgentSquire());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new ArgentSquire());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new Sunwalker());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new Sunwalker());

        this.testBreadthDepth(startingBoard);
    }

    @Test
    @Ignore("Long test")
    public void testBreadthDepthComplicatedWithoutDupes() throws HSException {
        BoardModel startingBoard = new BoardModel();
        startingBoard.getCurrentPlayer().addMana((byte)10);
        startingBoard.getCurrentPlayer().addMaxMana((byte)10);
        startingBoard.getCurrentPlayer().placeCardHand(new StonetuskBoar());
        startingBoard.getCurrentPlayer().placeCardHand(new EarthShock());
        startingBoard.getCurrentPlayer().placeCardHand(new Silence());

        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new RiverCrocolisk());
        startingBoard.placeMinion(PlayerSide.CURRENT_PLAYER, new BloodfenRaptor());

        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new StonetuskBoar());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new ArgentSquire());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new FenCreeper());
        startingBoard.placeMinion(PlayerSide.WAITING_PLAYER, new Sunwalker());

        this.testBreadthDepth(startingBoard);
    }

    private void testBreadthDepth(BoardModel startingBoard) throws HSException {
        this.testBreadthDepth(startingBoard, BruteForceSearchAI.buildStandardAI2().scorer);
    }

    private void testBreadthDepth(BoardModel startingBoard, BoardScorer ai) throws HSException {
        BreadthBoardStateFactory breadthFactory = new BreadthBoardStateFactory(this.deck0, this.deck1);
        DepthBoardStateFactory depthFactory = new DepthBoardStateFactory(this.deck0, this.deck1, false);

        this.testFactories(startingBoard, ai, breadthFactory, depthFactory);
    }

    private void testFactories(BoardModel startingBoard, BoardScorer ai, BoardStateFactoryBase us,
            BoardStateFactoryBase them) throws HSException {
        HearthTreeNode usRoot = new HearthTreeNode(startingBoard);
        double usTimer = System.currentTimeMillis();
        us.doMoves(usRoot, ai);
        usTimer = System.currentTimeMillis() - usTimer;

        HearthTreeNode themRoot = new HearthTreeNode(startingBoard);
        double themTimer = System.currentTimeMillis();
        them.doMoves(themRoot, ai);
        themTimer = System.currentTimeMillis() - themTimer;

        log.debug("testFactories " + name.getMethodName() + " usTimer=" + usTimer + " themTimer=" + themTimer);

        assertEquals(themRoot.data_, usRoot.data_);
        assertEquals(themRoot.getBestChildScore(), usRoot.getBestChildScore(), 0);
        assertEquals(themRoot.isLeaf(), usRoot.isLeaf());

        assertDescendentsCoversNode(usRoot, themRoot);
    }

    private void assertActionTreeIsRepeatable(HearthTreeNode root) throws HSException {
        ArrayList<HearthTreeNode> unprocessed = new ArrayList<HearthTreeNode>();
        unprocessed.add(root);

        HearthTreeNode current = null;
        while(!unprocessed.isEmpty()) {
            current = unprocessed.remove(0);
            if (!current.isLeaf()) {
                for (HearthTreeNode child : current.getChildren()) {
                    HearthTreeNode origin = new HearthTreeNode(current.data_.deepCopy(), current.getAction());
                    assertNotNull(child.getAction());
                    HearthTreeNode reproduced = child.getAction().perform(origin, null, null, false);
                    assertNotNull(reproduced);
                    assertEquals(child.data_, reproduced.data_);
                }
                unprocessed.addAll(current.getChildren());
            }
        }
    }

    private void assertDescendentsCoversNode(HearthTreeNode us, HearthTreeNode them) {
        ArrayList<HearthTreeNode> unprocessed = new ArrayList<HearthTreeNode>();
        unprocessed.add(them);

        HearthTreeNode current = null;
        while(!unprocessed.isEmpty()) {
            current = unprocessed.remove(0);
            this.assertDescendentsContainBoardModel(us, current.data_);
            if (!current.isLeaf()) {
                unprocessed.addAll(current.getChildren());
            }
        }
    }

    private void assertDescendentsContainBoardModel(HearthTreeNode root, BoardModel expectedBoard) {
        boolean success = false;
        ArrayList<HearthTreeNode> unprocessed = new ArrayList<HearthTreeNode>();
        unprocessed.add(root);

        HearthTreeNode current = null;
        while(!unprocessed.isEmpty()) {
            current = unprocessed.remove(0);
            if (current.data_.equals(expectedBoard)) {
                success = true;
                break;
            }
            if (!current.isLeaf()) {
                unprocessed.addAll(current.getChildren());
            }
        }

        assertTrue(success);
    }

    private void assertDescendentsDoNotContainDuplicates(HearthTreeNode root) {
        ArrayList<BoardModel> states = new ArrayList<BoardModel>();
        ArrayList<HearthTreeNode> unprocessed = new ArrayList<HearthTreeNode>();
        unprocessed.add(root);

        HearthTreeNode current = null;
        while(!unprocessed.isEmpty()) {
            current = unprocessed.remove(0);
            if (!(current instanceof StopNode)) {
                states.add(current.data_);
                if (!current.isLeaf()) {
                    unprocessed.addAll(current.getChildren());
                }
            } else {
                for (HearthTreeNode child : current.getChildren()) {
                    assertDescendentsDoNotContainDuplicates(child);
                }
            }
        }

        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < states.size(); j++) {
                if (i != j) {
                    assertNotEquals(states.get(i), states.get(j));
                }
            }
        }
    }
}
