package ir.ac.kntu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class SelectSoldiers {
    private ArrayList<Player.Cards> playerCardList = new ArrayList<>();
    private VBox vBox;
    private Button swordsman;
    private Button goblin;
    private Button shield;
    private Button knight;
    private Button archer;
    private Button dragon;
    private Button healer;

    public ArrayList<Player.Cards> getPlayerCardList() {
        return playerCardList;
    }

    public VBox getVBox() {
        return vBox;
    }

    public SelectSoldiers() {
        vBox=new VBox();
        this.swordsman = new Button("Swordsman");
        this.goblin = new Button("Goblin");
        this.shield = new Button("Shield");
        this.knight = new Button("Knight");
        this.archer = new Button("Archer");
        this.dragon = new Button("Dragon");
        this.healer = new Button("Healer");
        vBox.getChildren().add(swordsman);
        swordsman.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.SWORDSMAN);
                vBox.getChildren().remove(swordsman);
            }
        });
        vBox.getChildren().add(goblin);
        goblin.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.GOBLIN);
                vBox.getChildren().remove(goblin);
            }
        });
        vBox.getChildren().add(shield);
        shield.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.SHIELD);
                vBox.getChildren().remove(shield);
            }
        });
        vBox.getChildren().add(knight);
        knight.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.KNIGHT);
                vBox.getChildren().remove(knight);
            }
        });
        vBox.getChildren().add(archer);
        archer.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.ARCHER);
                vBox.getChildren().remove(archer);
            }
        });
        vBox.getChildren().add(dragon);
        dragon.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.DRAGON);
                vBox.getChildren().remove(dragon);
            }
        });
        vBox.getChildren().add(healer);
        healer.setOnMouseClicked(event -> {
            if(playerCardList.size()<4){
                playerCardList.add(Player.Cards.HEALER);
                vBox.getChildren().remove(healer);
            }
        });
        vBox.setAlignment(Pos.CENTER);

    }
}
