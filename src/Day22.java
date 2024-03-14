import lib.GraphUtil;

import java.util.*;

public class Day22 {
    public static void main(String[] args) {
        simulate(false);
        simulate(true);
    }

    private static void simulate(boolean hardMode) {
        State startState = new State(50, 500, 51, hardMode);
        int manaUsed = GraphUtil.dijkstra(startState, State::getNeighbours, state -> state.bossHitpoints <= 0 && state.playerHitpoints > 0);
        System.out.println(manaUsed);
    }

    static class State implements Comparable<State> {
        boolean playerTurn;
        int playerHitpoints;
        int mana;
        int bossHitpoints;
        int shieldTimer;
        int poisonTimer;
        int rechargeTimer;
        boolean hardMode;

        static Comparator<State> COMPARATOR = Comparator
                .comparing((State state) -> state.playerTurn)
                .thenComparing(state -> state.playerHitpoints)
                .thenComparing(state -> state.mana)
                .thenComparing(state -> state.bossHitpoints)
                .thenComparing(state -> state.shieldTimer)
                .thenComparing(state -> state.poisonTimer)
                .thenComparing(state -> state.rechargeTimer);

        public State(int playerHitpoints, int mana, int bossHitpoints, boolean hardMode) {
            this.playerTurn = true;
            this.playerHitpoints = playerHitpoints;
            this.mana = mana;
            this.bossHitpoints = bossHitpoints;
            this.hardMode = hardMode;
        }

        public State(State state) {
            this.playerTurn = state.playerTurn;
            this.playerHitpoints = state.playerHitpoints;
            this.mana = state.mana;
            this.bossHitpoints = state.bossHitpoints;
            this.shieldTimer = state.shieldTimer;
            this.poisonTimer = state.poisonTimer;
            this.rechargeTimer = state.rechargeTimer;
            this.hardMode = state.hardMode;
        }

        public Map<State, Integer> getNeighbours() {
            if (playerHitpoints > 0 && bossHitpoints > 0) {
                State state = new State(this);
                if (hardMode && playerTurn) {
                    state.playerHitpoints--;
                }
                if (state.playerHitpoints <= 0) {
                    return Collections.emptyMap();
                }
                if (state.shieldTimer > 0) {
                    state.shieldTimer--;
                }
                if (state.poisonTimer > 0) {
                    state.poisonTimer--;
                    state.bossHitpoints -= 3;
                }
                if (state.rechargeTimer > 0) {
                    state.rechargeTimer--;
                    state.mana += 101;
                }
                if (state.bossHitpoints <= 0) {
                    return Map.of(state, 0);
                }
                if (state.playerTurn) {
                    Map<State, Integer> result = new HashMap<>();
                    if (state.mana >= 53) {
                        State next = new State(state);
                        next.mana -= 53;
                        next.bossHitpoints -= 4;
                        next.playerTurn = false;
                        result.put(next, 53);
                    }
                    if (state.mana >= 73) {
                        State next = new State(state);
                        next.mana -= 73;
                        next.playerHitpoints += 2;
                        next.bossHitpoints -= 2;
                        next.playerTurn = false;
                        result.put(next, 73);
                    }
                    if (state.mana >= 113 && state.shieldTimer == 0) {
                        State next = new State(state);
                        next.mana -= 113;
                        next.shieldTimer = 6;
                        next.playerTurn = false;
                        result.put(next, 113);
                    }
                    if (state.mana >= 173 && state.poisonTimer == 0) {
                        State next = new State(state);
                        next.mana -= 173;
                        next.poisonTimer = 6;
                        next.playerTurn = false;
                        result.put(next, 173);
                    }
                    if (state.mana >= 229 && state.rechargeTimer == 0) {
                        State next = new State(state);
                        next.mana -= 229;
                        next.rechargeTimer = 5;
                        next.playerTurn = false;
                        result.put(next, 229);
                    }
                    return result;
                } else {
                    if (state.shieldTimer > 0) {
                        state.playerHitpoints -= 2;
                    } else {
                        state.playerHitpoints -= 9;
                    }
                    state.playerTurn = true;
                    if (state.playerHitpoints <= 0) {
                        return Collections.emptyMap();
                    }
                    return Map.of(state, 0);
                }
            }
            return Collections.emptyMap();
        }

        @Override
        public int compareTo(State o) {
            return COMPARATOR.compare(this, o);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return playerTurn == state.playerTurn && playerHitpoints == state.playerHitpoints && mana == state.mana && bossHitpoints == state.bossHitpoints && shieldTimer == state.shieldTimer && poisonTimer == state.poisonTimer && rechargeTimer == state.rechargeTimer && hardMode == state.hardMode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerTurn, playerHitpoints, mana, bossHitpoints, shieldTimer, poisonTimer, rechargeTimer, hardMode);
        }
    }
}
