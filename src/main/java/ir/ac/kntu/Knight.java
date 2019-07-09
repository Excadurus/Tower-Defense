package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Knight extends MeleeSoldier {

    public Knight(int x, int y,int domain) {
        super("KN", 30, 600, x, y, null, domain, 400, 1, 2);
        this.setImageView(this.getImage());
    }

    @Override
    public void attackHandler(Database database) {
        attack(database,database.getMap(),database.getArmaments(),1-this.getDomain());
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//knight.jpg")){
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
