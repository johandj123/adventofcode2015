import lib.GraphUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day22 {
    public static void main(String[] args) {
        simulate(false);
        simulate(true); // TODO Does not give the correct result yet
    }

    private static void simulate(boolean hardMode) {
        State startState = new State(50, 500, 51, hardMode);
        int manaUsed = GraphUtil.dijkstra(startState, State::getNeighbours, state -> state.bossHitpoints <= 0 && state.playerHitpoints > 0);
        System.out.println(manaUsed);
    }

    static class State implements Comparable<State> {
        int playerHitpoints;
        int mana;
        int bossHitpoints;
        int shieldTimer;
        int poisonTimer;
        int rechargeTimer;
        boolean hardMode;

        static Comparator<State> COMPARATOR = Comparator.comparing((State state) -> state.playerHitpoints)
                .thenComparing(state -> state.mana)
                .thenComparing(state -> state.bossHitpoints)
                .thenComparing(state -> state.shieldTimer)
                .thenComparing(state -> state.poisonTimer)
                .thenComparing(state -> state.rechargeTimer);

        public State(int playerHitpoints, int mana, int bossHitpoints, boolean hardMode) {
            this.playerHitpoints = playerHitpoints;
            this.mana = mana;
            this.bossHitpoints = bossHitpoints;
            this.hardMode = hardMode;
        }

        public State(State state) {
            this.playerHitpoints = state.playerHitpoints;
            this.mana = state.mana;
            this.bossHitpoints = state.bossHitpoints;
            this.shieldTimer = state.shieldTimer;
            this.poisonTimer = state.poisonTimer;
            this.rechargeTimer = state.rechargeTimer;
            this.hardMode = state.hardMode;
        }

        public Map<State, Integer> getNeighbours() {
            Map<State, Integer> result = new HashMap<>();
            if (playerHitpoints > 0 && bossHitpoints > 0) {
                State state = new State(this);
                state.applyTimers();
                if (state.playerHitpoints > 0) {
                    if (state.bossHitpoints <= 0) {
                        State next = new State(state);
                        result.put(next, 0);
                    } else {
                        if (state.mana >= 53) {
                            State next = new State(state);
                            next.mana -= 53;
                            next.bossHitpoints -= 4;
                            next.applyBossTurn();
                            result.put(next, 53);
                        }
                        if (state.mana >= 73) {
                            State next = new State(state);
                            next.mana -= 73;
                            next.playerHitpoints += 2;
                            next.bossHitpoints -= 2;
                            next.applyBossTurn();
                            result.put(next, 73);
                        }
                        if (state.mana >= 113 && state.shieldTimer == 0) {
                            State next = new State(state);
                            next.mana -= 113;
                            next.shieldTimer = 6;
                            next.applyBossTurn();
                            result.put(next, 113);
                        }
                        if (state.mana >= 173 && state.poisonTimer == 0) {
                            State next = new State(state);
                            next.mana -= 173;
                            next.poisonTimer = 6;
                            next.applyBossTurn();
                            result.put(next, 173);
                        }
                        if (state.mana >= 229 && state.rechargeTimer == 0) {
                            State next = new State(state);
                            next.mana -= 229;
                            next.rechargeTimer = 5;
                            next.applyBossTurn();
                            result.put(next, 229);
                        }
                    }
                }
            }
            return result;
        }

        private void applyTimers() {
            if (hardMode) {
                playerHitpoints--;
            }
            if (playerHitpoints > 0) {
                if (shieldTimer > 0) {
                    shieldTimer--;
                }
                if (poisonTimer > 0) {
                    poisonTimer--;
                    bossHitpoints -= 3;
                }
                if (rechargeTimer > 0) {
                    rechargeTimer--;
                    mana += 101;
                }
            }
        }

        private void applyBossTurn() {
            if (bossHitpoints > 0) {
                applyTimers();
                if (bossHitpoints > 0) {
                    if (shieldTimer > 0) {
                        playerHitpoints -= 2;
                    } else {
                        playerHitpoints -= 9;
                    }
                }
            }
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
            return playerHitpoints == state.playerHitpoints && mana == state.mana && bossHitpoints == state.bossHitpoints && shieldTimer == state.shieldTimer && poisonTimer == state.poisonTimer && rechargeTimer == state.rechargeTimer;
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerHitpoints, mana, bossHitpoints, shieldTimer, poisonTimer, rechargeTimer);
        }
    }
}
