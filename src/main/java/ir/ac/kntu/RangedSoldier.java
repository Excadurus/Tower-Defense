package ir.ac.kntu;

import javafx.scene.image.ImageView;

public abstract class RangedSoldier extends Soldier {

    public RangedSoldier(String name, int manaCost, int healthPoint, int x, int y, ImageView shape, int domain, int damage, int range, int speed) {
        super(name, manaCost, healthPoint, x, y, shape, domain, damage, range, speed);
    }
}
