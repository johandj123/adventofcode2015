import java.util.List;

public class Day21 {
    private static List<Item> WEAPONS = List.of(
            new Item(8, 4, 0),
            new Item(10, 5, 0),
            new Item(25, 6, 0),
            new Item(40, 7, 0),
            new Item(74, 8, 0)
    );

    private static List<Item> ARMOR = List.of(
            new Item(0, 0, 0),
            new Item(13, 0, 1),
            new Item(31, 0, 2),
            new Item(53, 0, 3),
            new Item(75, 0, 4),
            new Item(102, 0, 5)
    );

    private static List<Item> RINGS = List.of(
            new Item(0, 0, 0),
            new Item(0, 0, 0),
            new Item(25, 1, 0),
            new Item(50, 2, 0),
            new Item(100, 3, 0),
            new Item(20, 0, 1),
            new Item(40, 0, 2),
            new Item(80, 0, 3)
    );

    public static void main(String[] args) {
        first();
        second();
    }

    private static void first() {
        int minCost = Integer.MAX_VALUE;
        for (Item weapon : WEAPONS) {
            for (Item armor : ARMOR) {
                for (Item ring1 : RINGS) {
                    for (Item ring2 : RINGS) {
                        if (ring1.equals(ring2)) {
                            continue;
                        }
                        List<Item> items = List.of(weapon, armor, ring1, ring2);
                        int cost = items.stream().mapToInt(item -> item.cost).sum();
                        if (cost < minCost) {
                            Fighter fighter = new Fighter(100, items);
                            Fighter boss = new Fighter(100, 8, 2);
                            if (fight(fighter, boss)) {
                                minCost = cost;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(minCost);
    }

    private static void second() {
        int maxCost = Integer.MIN_VALUE;
        for (Item weapon : WEAPONS) {
            for (Item armor : ARMOR) {
                for (Item ring1 : RINGS) {
                    for (Item ring2 : RINGS) {
                        if (ring1.equals(ring2)) {
                            continue;
                        }
                        List<Item> items = List.of(weapon, armor, ring1, ring2);
                        int cost = items.stream().mapToInt(item -> item.cost).sum();
                        if (cost > maxCost) {
                            Fighter fighter = new Fighter(100, items);
                            Fighter boss = new Fighter(100, 8, 2);
                            if (!fight(fighter, boss)) {
                                maxCost = cost;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(maxCost);
    }

    private static boolean fight(Fighter fighter, Fighter boss) {
        while (true) {
            fighter.attack(boss);
            if (boss.isDead()) {
                return true;
            }
            boss.attack(fighter);
            if (fighter.isDead()) {
                return false;
            }
        }
    }

    static class Item {
        final int cost;
        final int damage;
        final int armor;

        public Item(int cost, int damage, int armor) {
            this.cost = cost;
            this.damage = damage;
            this.armor = armor;
        }
    }

    static class Fighter {
        int hitpoints;
        final int damage;
        final int armor;

        public Fighter(int hitpoints, int damage, int armor) {
            this.hitpoints = hitpoints;
            this.damage = damage;
            this.armor = armor;
        }

        public Fighter(int hitpoints,List<Item> items) {
            this.hitpoints = hitpoints;
            damage = items.stream().mapToInt(item -> item.damage).sum();
            armor = items.stream().mapToInt(item -> item.armor).sum();
        }

        public void attack(Fighter defender) {
            int actualDamage = Math.max(damage - defender.armor, 1);
            defender.hitpoints -= actualDamage;
        }

        public boolean isDead() {
            return hitpoints <= 0;
        }

        @Override
        public String toString() {
            return "Fighter{" +
                    "hitpoints=" + hitpoints +
                    ", damage=" + damage +
                    ", armor=" + armor +
                    '}';
        }
    }
}
