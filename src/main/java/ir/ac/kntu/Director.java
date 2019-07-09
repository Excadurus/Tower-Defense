package ir.ac.kntu;

import javafx.application.Platform;
import java.util.ArrayList;

public class Director implements Runnable {
    private Database database;

    public Director(Database database) {
        this.database = database;
    }

    @Override
    public void run() {
        while (!database.isFinished()) {
            System.out.println("Towers Starting to Attack");
            attack(database.getTowers());
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
            System.out.println("Removing Soldiers");
            removeSoldier();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
            System.out.println("Soldiers Attacking");
            attack(database.getSoldiers());
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
            System.out.println("Removing Soldiers");
            removeSoldier();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
            System.out.println("Moving");
            move();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
            addMana();
            performFunction();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
            for (int i = 0; i < database.getMap().getSize(); i++) {
                for (int j = 0; j < database.getMap().getSize(); j++) {
                    if (database.getMap().getArmaments(i,j) == null) {
                        System.out.printf("%3s", "**");
                    } else {
                        System.out.printf("%3s", database.getMap().getArmaments(i,j).getName());
                    }
                }
                System.out.println();
            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                //Eat intentionally
            }
        }

    }

    private void removeSoldier(){
        for(int i=0 ; i < database.getMap().getSize() ; i++){
            for(int j=0 ; j < database.getMap().getSize() ; j++){
                //Try Catch for null pointer exception
                try {
                    if(database.getMap().getArmament(i,j).getHealthPoint()<=0){
                        Armament armament= database.getMap().getArmament(i,j);
                        Platform.runLater(()-> database.getCell(armament.getY(),armament.getX())
                                .getChildren().remove(armament.getShape()));

                        System.out.println("Armament at " +
                                armament.getX() +":" + armament.getY() + " Removed");

                        database.removeArmament(armament);
                    }
                }catch (NullPointerException e){
                    //Intentionally eat this Exception
                }
            }
        }
    }




    //We Use generics to avoid duplicate code for towers and soldiers
    private <T extends AttackArmament> void attack(ArrayList<T> armaments){
        //First we initiate armaments at home
        for(int i=0;i<armaments.size();i++){
            T armament = armaments.get(i);
            //break if armament isn't at its home
            if(database.getMap().getDomain(armament.getX(),armament.getY()) != armament.getDomain()){
                continue;
            }
            armament.attackHandler(database);
        }
        //Second we initiate armaments away from home
        for(int i=0;i<armaments.size();i++){
            T armament = armaments.get(i);
            //break if armament is at its home
            if(database.getMap().getDomain(armament.getX(),armament.getY()) == armament.getDomain()){
                continue;
            }
            armament.attackHandler(database);
        }

    }


    private void move(){
        ArrayList<Soldier> home = new ArrayList<>();
        ArrayList<Soldier> away = new ArrayList<>();

        for(int i=0;i<database.getSoldiers().size();i++) {
            Soldier soldier = database.getSoldiers().get(i);
            if (database.getMap().getDomain(soldier.getX(),soldier.getY()) == soldier.getDomain()) {
                home.add(soldier);
            } else {
                away.add(soldier);
            }
        }

        for(int i=0;i<home.size();i++){
            home.get(i).move(database,database.getMap(),home.get(i).getSpeed());
        }
        for(int i=0;i<away.size();i++){
            away.get(i).move(database,database.getMap(),away.get(i).getSpeed());
        }
    }

    private void addMana(){
        database.getPlayers(0).setMana(database.getPlayers(0).getMana()+1);
        System.out.println("Player 1 mana:" + database.getPlayers(0).getMana());
        database.getPlayers(1).setMana(database.getPlayers(1).getMana()+1);
        System.out.println("Player 2 mana:" + database.getPlayers(1).getMana());
    }

    private void performFunction(){
        for(int i=0; i<database.getSupportArmaments().size();i++){
            database.getSupportArmaments().get(i).function(database);
            System.out.println("Special Tower performed at " + database.getSupportArmaments().get(i).getX() + ":" + database.getSupportArmaments().get(i).getY() );
        }
    }

}



