package ir.ac.kntu;

import javafx.scene.image.ImageView;

public abstract class Tower extends AttackArmament {
    public Tower(String name, int manaCost, int healthPoint, int x, int y, ImageView shape, int domain, int damage, int range) {
        super(name, manaCost, healthPoint, x, y, shape, domain,damage,range);
    }


}
