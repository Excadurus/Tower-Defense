package ir.ac.kntu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.util.ArrayList;

public class SpecialTower extends SupportArmament {
    private int step;

    public SpecialTower(int x, int y, int domain) {
        super("ST", 0, 500, x, y, null, domain);
        this.step = 0;
        this.setImageView(this.getImage());

    }

    @Override
    public void function(Database database) {
        //Closing Condition 1: has to perform each 5 Cycles
        if (step%5 !=0){
            this.step = this.step + 1;
            return;
        }
        //Closing Condition 2: has to check if there are available Red Houses
        ArrayList<Coordinate> coordinates = findAvailableRedHouse(database.getMap(),this.getDomain());
        if (coordinates.size()==0){
            return;
        }
        //Randomly choose an empty Cell
        int cellIndex = Math.abs(RandomHelper.nextInt())%coordinates.size();
        Coordinate coordinate = coordinates.get(cellIndex);

        //Randomly choose a Card Index from enemy caard
        int cardIndex = Math.abs(RandomHelper.nextInt())%database.getPlayers(1-this.getDomain()).getCards().size();
        Player.Cards card = database.getPlayers(1-this.getDomain()).getCards().get(cardIndex);

        //Summon
        database.getPlayers(this.getDomain()).createArmament(database,card,coordinate,4);
        this.step = this.step + 1;
    }

    public static ArrayList<Coordinate> findAvailableRedHouse(Map map, int doamin){
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for(int i=0;i<map.getSize();i++){
            for(int j =0;j<map.getSize();j++){
                //Continue Condition one, if the cell is not a base
                if(map.getBoardValue(i,j)!=1){
                    continue;
                }
                //Continue condition two, if base is not friendly
                if (map.getDomain(i,j)!=doamin){
                    continue;
                }
                coordinates.add(new Coordinate(i,j));
            }
        }
        return coordinates;
    }

    private ImageView getImage(){
        Image image = null;
        try (FileInputStream fis = new FileInputStream(".//asset//specialtower.jpg")){
            image = new Image(fis);
        }catch (Exception e){

        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        return imageView;

    }

}
