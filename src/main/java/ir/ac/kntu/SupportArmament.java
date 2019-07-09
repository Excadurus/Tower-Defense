package ir.ac.kntu;

import javafx.scene.image.ImageView;

public abstract class SupportArmament extends Armament {
    public SupportArmament(String name, int manaCost, int healthPoint, int x, int y, ImageView imageView, int domain) {
        super(name, manaCost, healthPoint, x, y, imageView, domain);
    }

    public abstract void function(Database database);
}
