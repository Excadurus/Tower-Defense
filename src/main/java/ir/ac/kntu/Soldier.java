package ir.ac.kntu;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public abstract class Soldier extends AttackArmament {
    private int speed;
    boolean canMove = true;


    public Soldier(String name, int manaCost, int healthPoint, int x, int y, ImageView shape, int domain, int damage, int range, int speed) {
        super(name, manaCost, healthPoint, x, y, shape, domain, damage, range);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void move(Database database,Map map, int remainingSpeed){

        //Closing Condition 1: Check if the target can move anymore
        if(remainingSpeed==0){
            return;
        }

        //Closing Condition 2: check if the soldier has attacked and can't move anymore;
        if(!canMove){
            canMove=true;
            return;
        }

        //Creating a Priority Map, (very little Bugged, sometimes soldiers might go back because enemy soldiers are a source too
        int[][] priority = setPriorityStepOne(map);
        ArrayList<Coordinate> coordinates = getPossibleMovesStepOne(map,priority,this.getX(),this.getY(),remainingSpeed);
        //Closing Condition 3:
        if (coordinates.size() ==0){
            return;
        }

        //Because nextInt might return negative values we use it's absolute
        int index = Math.abs(RandomHelper.nextInt())%coordinates.size();

        map.setArmaments(this.getX(),this.getY(),null);
        Platform.runLater(()->{
            database.getCell(this.getY(),this.getX()).getChildren().remove(this.getShape());
        });
        System.out.println(this.getName() + "moved from" + this.getX() +":" + this.getY());
        //Remove Shape from old Coordinates
        this.setX(coordinates.get(index).getX());
        this.setY(coordinates.get(index).getY());
        //Add Shape to new Coordinates
        map.setArmaments(this.getX(),this.getY(),this);
        Platform.runLater(()->{
            database.getCell(this.getY(),this.getX()).getChildren().add(this.getShape());
        });

        System.out.println("to" + this.getX() +":" + this.getY());
        //Check if you have landed on Enemy base, also makes sure if you have, since this soldier is removed we return from this function
        if (checkHP(database)){
            return;
        }
        //Recursively Moving
        this.move(database,map,remainingSpeed - 1);

    }

    private int[][] setPriorityStepOne(Map map){
        int[][] priority = new int[map.getSize()][map.getSize()];

        //Set all bases
        for(int i=0;i<map.getSize();i++) {
            for (int j = 0; j < map.getSize(); j++) {
                priority[i][j] = Integer.MAX_VALUE;
            }
        }
        for(int i=0;i<map.getSize();i++){
            for (int j=0;j<map.getSize();j++) {
                if ((map.getBoardValue(i,j) == 1 && map.getDomain(i,j) != this.getDomain()) ||
                        (map.getArmaments(i,j) != null && map.getArmaments(i,j).getDomain()!=this.getDomain())){
                    //Give them priority 0(highest)
                    priority[i][j] = 0;
                    //now to start Recursively giving Yellow rooms priority
                    setPriorityStepTwo(map,priority,i + 1, j, 1);
                    setPriorityStepTwo(map,priority,i - 1, j, 1);
                    setPriorityStepTwo(map,priority,i, j + 1, 1);
                    setPriorityStepTwo(map,priority,i, j - 1, 1);

                }
            }
        }
        return priority;
    }

    private void setPriorityStepTwo(Map map,int[][] priority,int i, int j,int n){
        //Closing Condition 0: Check for indexOutOfBounds
        if(i<0 || i>=map.getSize() || j<0 || j >= map.getSize()){
            return;
        }
        //Closing Condition 1: checks if the cell has been given a priority already
        if(priority[i][j] <n){
            return;
        }
        //Closing Condition2: checks if the cell is Yellow
        if(map.getBoardValue(i,j)!=3 && map.getBoardValue(i,j)!=1){
            return;
        }
        //Closing Condition3: if we reached out base then we give it last priority and close
        if(map.getBoardValue(i,j)==1 && map.getDomain(i,j) == this.getDomain()){
            priority[i][j]=n;
            return;
        }

        //we give priority n and then recursively check the top,bottom,left and right cells
        priority[i][j]=n;
        setPriorityStepTwo(map,priority,i+1,j,n + 1);
        setPriorityStepTwo(map,priority,i-1,j,n + 1);
        setPriorityStepTwo(map,priority,i,j+1,n + 1);
        setPriorityStepTwo(map,priority,i,j-1,n + 1);

    }

    private ArrayList<Coordinate> getPossibleMovesStepOne(Map map,int[][] priority, int x, int y,int remainingSpeed){
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for(int i=-1;i<=1;i++) {
            //don't check starting house
            if(i==0){
                continue;
            }
            //Check a cross section of the original coordinates
            if (getPossibleMovesStepTwo(map, priority, x+i, y, remainingSpeed - 1 )) {
                coordinates.add(new Coordinate(x+i, y));
            }
            if (getPossibleMovesStepTwo(map, priority, x, y+i, remainingSpeed - 1)) {
                coordinates.add(new Coordinate(x, y+i));
            }
        }
        return coordinates;
    }

    private boolean getPossibleMovesStepTwo(Map map,int[][] priority,int i, int j,int remainingSpeed){
        //Closing Condition 0: Checks for ArrayIndexOutOfBounds
        if(i<0 || i>=map.getSize() || j<0 || j >= map.getSize()){
            return false;
        }
        //Closing Condition 1: if this cell has less priority than ours
        if(priority[i][j] > priority[this.getX()][this.getY()]){
            return false;
        }
        //Closing Condition 2: if this cell has no Soldiers in it then it's a target
        if(map.getArmaments(i,j) == null){
            return true;
        }
        //Closing Condition 3:if this cell is enemy Soldier then we can't move
        if(map.getArmaments(i,j).getDomain() != this.getDomain()){
            return false;
        }
        //Closing Condition 4: if this cell is friendly soldier and we can't move anymore
        if(remainingSpeed <= 0){
            return false;
        }
        //Closing Condition 4:if this cell is our Soldier then we need to check for Possible moves(Note this is Recursive)
        if(remainingSpeed >= 0 && getPossibleMovesStepOne(map,priority,i,j,remainingSpeed - 1).size()==0){
            return false;
        }
        //By this line, we are up against a friendly Soldier in our path and can move past them
        return true;
    }
    //Check if must end the game , during move
    private boolean checkHP(Database database){
        for(int i=0;i<database.getMap().getSize();i++){
            for(int j=0;j<database.getMap().getSize();j++){
                //Continue if not red cell
                if(database.getMap().getBoardValue(i,j)!=1){
                    continue;
                }
                //Continue if empty
                if (database.getMap().getArmaments(i,j)==null){
                    continue;
                }
                //Continue if soldier in the house is friendly
                if(database.getMap().getArmaments(i,j).getDomain() == database.getMap().getDomain(i,j)){
                    continue;
                }
                //Lower the Hp by one
                //Remove the Soldier from game
                int domain = database.getMap().getDomain(i,j);
                database.getPlayers(domain).setHealth(database.getPlayers(domain).getHealth() - 1);
                database.removeArmament(database.getMap().getArmaments(i,j));
                //Check if the game must end
                if(database.getPlayers(domain).getHealth()<=0){
                    System.out.println(database.getPlayers(domain).getName());
                    System.out.println("Game Finished");
                    Platform.runLater(() ->{
                        database.getPrmaryStage().close();
                    });
                    database.setFinished(true);
                }
                return true;
            }
        }
        return false;
    }
}
