package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;

public class HospitalTower extends Tower {
    public HospitalTower(int x, int y, int domain) {
        super("HT", 50, 1000, x, y, null, domain, -100, 2);
        this.setImageView(this.getImage());
    }

    @Override
    public void attackHandler(Database database) {
        attack(database,database.getMap(),database.getSoldiers(),this.getDomain());
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//hospitaltower.jpg")){
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
