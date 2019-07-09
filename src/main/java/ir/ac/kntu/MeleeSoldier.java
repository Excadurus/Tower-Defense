package ir.ac.kntu;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class MeleeSoldier extends Soldier {

    public MeleeSoldier(String name, int manaCost, int healthPoint, int x, int y, ImageView shape, int domain, int damage, int range, int speed) {
        super(name, manaCost, healthPoint, x, y, shape, domain, damage, range, speed);
    }

    @Override
    public <T extends Armament> void attack(Database database,Map map, ArrayList<T> target,int domain) {
        //Closing Condition 1: if Soldier is dead it can't attack
        if(this.getHealthPoint()<=0){
            return;
        }
        //Closing Condition 2: if Soldier has no target it can't attack
        if(target.size()==0){
            return;
        }
        //Check a cross section of the original coordinates
        for(int i=-this.getRange();i<=this.getRange();i++) {
            //don't check starting house
            if(i==0){
                continue;
            }
            //check for arrayIndexOutofBounds
            Armament armament = null;
            try {
                armament = map.getArmaments(this.getX()+i,this.getY());
            }catch (ArrayIndexOutOfBoundsException e){
                //Eat
                continue;
            }
            if(attackStepTwo(target,armament,domain)){
                armament.setHealthPoint(armament.getHealthPoint()-this.getDamage());
                System.out.println(this.getName() + "at " + this.getX()+":" + this.getY() + "attacked" + armament.getName() + "at " +
                        armament.getX()+":" + armament.getY());
                this.canMove=false;
                return;
            }
            try {
                armament=map.getArmaments(this.getX(),this.getY()+i);
            }catch (ArrayIndexOutOfBoundsException e){
                //Eat
                continue;
            }

            if(attackStepTwo(target,armament,domain)){
                armament.setHealthPoint(armament.getHealthPoint()-this.getDamage());
                System.out.println(this.getName() + "at " + this.getX()+":" + this.getY() + "attacked" + armament.getName() + "at " +
                        armament.getX()+":" + armament.getY());
                this.canMove=false;
                return;
            }
        }
    }
}
