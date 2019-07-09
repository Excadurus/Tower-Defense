package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Dragon extends RangedSoldier {
    public Dragon(int x, int y,int domain) {
        super("DR", 35, 500, x, y, null, domain, 350, 3, 2);
        this.setImageView(this.getImage());
    }

    @Override
    public void attackHandler(Database database) {
        attack(database,database.getMap(),database.getArmaments(),1-this.getDomain());
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//dragon.jpg")){
            image = new Image(fis);
        }catch (Exception e){
            //Eat intentionally
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        return imageView;

    }
}
