package com.hearthsim.util.tree;

import java.util.ArrayList;

import com.hearthsim.card.Deck;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.HearthAction;

/**
 * Random effect node
 *
 * This node signifies that the immediate children of the node are all outcomes of a random event.
 * Each random outcome can have a weighting associated to it.  The total expected score of this node
 * is computed as a weighted average of the scores of all outcomes.
 *
 */
public class RandomEffectNode extends StopNode {

    private ArrayList<Double> childWeighting_;

    public RandomEffectNode(HearthTreeNode origNode,
                            HearthAction action) {
        super(origNode);
        this.setAction(action);
        childWeighting_ = new ArrayList<Double>();
    }

    @Override
    public HearthTreeNode finishAllEffects(Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        if (this.numChildren() > 0) {
            return this.getChildren().get((int)(Math.random() * this.numChildren()));
        }

        return null;
    }

    @Override
    public HearthTreeNode addChild(HearthTreeNode node) {
        node.setAction(new HearthAction(HearthAction.Verb.RNG, PlayerSide.CURRENT_PLAYER, this.numChildren()));
        HearthTreeNode toRet = super.addChild(node);
        childWeighting_.add(new Double(1.0));
        return toRet;
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        childWeighting_.clear();
    }

    //If you are going to play a random effect card, it's usually better to play it when you have more mana
    private double getManaBenefit() {
        return 1.e-2 * data_.getCurrentPlayer().getMana();
    }

    public double weightedAverageScore() {
        double toRet = 0.0;
        for (int index = 0; index < children_.size(); ++index) {
            toRet += childWeighting_.get(index).doubleValue() * children_.get(index).getScore();
        }
        toRet = toRet / children_.size();
        toRet += this.getManaBenefit();
        return toRet;
    }

    public double weightedAverageBestChildScore() {
        double toRet = 0.0;
        for (int index = 0; index < children_.size(); ++index) {
            toRet += childWeighting_.get(index).doubleValue() * children_.get(index).getBestChildScore();
        }
        toRet = toRet / children_.size();
        toRet += this.getManaBenefit();
        return toRet;
    }
}
