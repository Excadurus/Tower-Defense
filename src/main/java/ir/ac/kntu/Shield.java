package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Shield extends MeleeSoldier {
    public Shield(int x, int y, int domain) {
        super("Sh", 10, 1000, x, y, null, domain, 150, 1, 1);
        this.setImageView(this.getImage());
    }

    @Override
    public void attackHandler(Database database) {
        attack(database,database.getMap(),database.getArmaments(),1-this.getDomain());
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//shield.jpg")){
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
