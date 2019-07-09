package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class ElectricTower extends Tower {
    public ElectricTower(int x, int y,int domain) {
        super("ET", 45, 2000, x, y,
                null, domain,250,3);
        this.setImageView(this.getImage());

    }

    @Override
    public void attackHandler(Database database) {
        attack(database,database.getMap(),database.getSoldiers(),1-this.getDomain());
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//electrictower.jpg")){
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
