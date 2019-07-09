package ir.ac.kntu;

import javafx.scene.image.ImageView;

public abstract class Armament {
    private String name;
    private static int manaCost;
    private int healthPoint;
    private ImageView imageView;
    private int x;
    private int y;
    private int domain;

    public Armament(String name, int manaCost, int healthPoint, int x, int y, ImageView imageView, int domain) {
        this.name = name;
        this.manaCost = manaCost;
        this.healthPoint = healthPoint;
        this.x = x;
        this.y = y;
        this.imageView=imageView;
        this.domain=domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getManaCost() {
        return manaCost;
    }

    public static void setManaCost(int manaCost) {
        manaCost = manaCost;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public ImageView getShape() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDomain() {
        return domain;
    }

    public void setDomain(int domain) {
        this.domain = domain;
    }


}
