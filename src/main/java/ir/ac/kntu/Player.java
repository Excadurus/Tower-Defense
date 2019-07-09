package ir.ac.kntu;

import java.util.ArrayList;

public class Player {

    enum Cards{
        SWORDSMAN,GOBLIN,
        SHIELD,KNIGHT,ARCHER,DRAGON,HEALER,
        BLACK_TOWER,ELECTRIC_TOWER,
        HOSPITAL_TOWER,SPECIAL_TOWER;


    }

    private String name;
    private int health;
    private int mana;
    private int domain;
    private ArrayList<Cards> cards;
    private ArrayList<Coordinate> bases;
    private Cards selectedCard;

    public Player(String name,int domain ,ArrayList<Cards> cards) {
        this.name = name;
        this.health = 5;
        this.mana = 100;
        this.domain =domain;
        this.cards=cards;
        this.bases=new ArrayList<>();
        //Default selected card
        selectedCard=cards.get(0);
    }


    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<Cards> getCards() {
        return new ArrayList<Cards>(cards);
    }

    public ArrayList<Coordinate> getBases() {
        return new ArrayList<Coordinate>(bases);
    }

    public void setBases(ArrayList<Coordinate> bases) {
        this.bases = bases;
    }

    public Cards getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Cards selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void createArmament(Database database, Cards card, Coordinate coordinate, int divider){
        Armament armament;
        switch (card){
            case SWORDSMAN:
                int manaCost=Swordsman.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Swordsman Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Swordsman(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case GOBLIN:
                manaCost=Goblin.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Goblin Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Goblin(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case SHIELD:
                manaCost=Shield.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Shield Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Shield(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case KNIGHT:
                manaCost=Knight.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Knight Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Knight(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case ARCHER:
                manaCost=Archer.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Archer Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Archer(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case DRAGON:
                manaCost=Dragon.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Dragon Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Dragon(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case HEALER:
                manaCost=Healer.getManaCost()/divider;
                if(this.mana >= manaCost){
                    System.out.println("Healer Mana Cost for domain " + this.domain + "is" + manaCost);
                    this.mana = this.mana - manaCost;
                    database.addArmament(new Healer(coordinate.getX(),coordinate.getY(),this.domain));
                    System.out.println("Added Soldier to " + coordinate.getX() + ":" + coordinate.getY() );
                }
                break;
            case BLACK_TOWER:
                manaCost=BlackTower.getManaCost()/divider;
                if(this.mana >= manaCost){
                    this.mana = this.mana - manaCost;
                    database.addArmament(new BlackTower(coordinate.getX(),coordinate.getY(),this.domain));
                }
                break;
            case SPECIAL_TOWER:
                manaCost=SpecialTower.getManaCost()/divider;
                if(this.mana >= manaCost){
                    this.mana = this.mana - manaCost;
                    database.addArmament(new SpecialTower(coordinate.getX(),coordinate.getY(),this.domain));
                }
                break;
            default:
                break;
        }
    }
}
