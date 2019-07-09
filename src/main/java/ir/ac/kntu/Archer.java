package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Archer extends RangedSoldier {

    public Archer(int x, int y,int domain) {
        super("AR", 15, 300, x, y, null, domain, 200, 2, 1);
        this.setImageView(this.getImage());
    }

    @Override
    public void attackHandler(Database database) {
        attack(database,database.getMap(),database.getArmaments(),1-this.getDomain());
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//archer.jpg")){
            image = new Image(fis);
        }catch (Exception e){

        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        return imageView;

    }
}
