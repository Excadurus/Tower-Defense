package ir.ac.kntu;

import javafx.scene.image.ImageView;
import java.util.ArrayList;

public abstract class AttackArmament extends Armament {
    private int damage;
    private int range;

    public AttackArmament(String name, int manaCost, int healthPoint, int x, int y, ImageView shape, int domain, int damage, int range) {
        super(name, manaCost, healthPoint, x, y, shape, domain);
        this.damage = damage;
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }


    public int getRange() {
        return range;
    }

    //With Attack Handler we can define the Target type, and the Target owner
    public abstract void attackHandler(Database database);

    public <T extends Armament> void attack(Database database, Map map, ArrayList<T> target, int domain){
        //Closing Condition 1: if Soldier is dead it can't attack
        if(this.getHealthPoint()<=0){
            return;
        }
        //Closing Condition 2: if Soldier has no target it can't attack
        if(target.size()==0){
            return;
        }
        //Check a cross section of the original coordinates
        for(int i=this.getX()-this.getRange();i<=this.getX()+this.getRange();i++) {
            for(int j=this.getY()-this.getRange();j<=this.getY()+this.getRange();j++){
                //don't check starting house
                if(i==this.getX() && j==this.getY()){
                    continue;
                }
                //check for arrayIndexOutofBounds
                Armament armament = null;
                try {
                    armament = map.getArmament(i,j);
                }catch (ArrayIndexOutOfBoundsException e){
                    //Eat
                    continue;
                }
                if(attackStepTwo(target,armament,domain)){
                    armament.setHealthPoint(armament.getHealthPoint()-this.getDamage());
                    System.out.println(this.getName() + "at " + this.getX()+":" + this.getY() + "attacked" + armament.getName() + "at " +
                            armament.getX()+":" + armament.getY());
                    if(this instanceof RangedSoldier){
                        ((RangedSoldier) this).canMove =false;
                    }
                    return;
                }
            }

        }
    }

    public <T extends Armament> boolean attackStepTwo(ArrayList<T> target,Armament armament,int domain){
        //break if cell is empty
        if (armament==null) {
            return false;
        }
        //break if cell is dead
        if(armament.getHealthPoint()<=0){
            return false;
        }
        //break if cell doesn't contain the target
        if(!target.contains(armament)){
            return false;
        }
        //break if target isn't from the player we eant
        if (armament.getDomain()!=domain){
            return false;
        }
        //We can Attack
        return true;
    }
}
