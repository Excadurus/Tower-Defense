package ir.ac.kntu;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Database {
    private ArrayList<Map> maps;
    private boolean finished;
    private Map map;
    private Stage prmaryStage;
    private GridPane inGame;
    private Rectangle[][] rectangles;
    private StackPane[][] cell;
    private Player[] players;
    private Player.Cards cards;
    private ArrayList<Armament> armaments;
    private ArrayList<SupportArmament> supportArmaments;
    private ArrayList<SpecialTower> specialTowers;
    private ArrayList<AttackArmament> attackArmaments;
    private ArrayList<Tower> towers;
    private ArrayList<BlackTower> blackTowers;
    private ArrayList<ElectricTower> electricTowers;
    private ArrayList<HospitalTower> hospitalTowers;
    private ArrayList<Soldier> soldiers;
    private ArrayList<MeleeSoldier> meleeSoldiers;
    private ArrayList<Swordsman> swordsmen;
    private ArrayList<Goblin> goblins;
    private ArrayList<Shield> shields;
    private ArrayList<Knight> knights;
    private ArrayList<RangedSoldier> rangedSoldiers;
    private ArrayList<Archer> archers;
    private ArrayList<Dragon> dragons;
    private ArrayList<Healer> healers;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Stage getPrmaryStage() {
        return prmaryStage;
    }

    public void setPrmaryStage(Stage prmaryStage) {
        this.prmaryStage = prmaryStage;
    }


    public void setInGame(GridPane inGame) {
        this.inGame = inGame;
    }


    public void setRectangles(Rectangle[][] rectangles) {
        this.rectangles = rectangles;
    }

    public StackPane getCell(int i, int j) {
        return cell[i][j];
    }


    public void setCell(StackPane[][] cell) {
        this.cell = cell;
    }

    public Player getPlayers(int i) {
        return players[i];
    }

    public void setPlayers(int i,Player player) {
        this.players[i] = player;
    }


    //This method is only used when we add an element to the game FOR THE FIRST TIME
    public void addArmament(Armament armament) {
        this.armaments.add(armament);
        this.getMap().setArmaments(armament.getX(),armament.getY(),armament);
        Platform.runLater(() ->{
            cell[armament.getY()][armament.getX()].getChildren().add(armament.getShape());
        });
        addArmamentReferences(armament);
    }

    public ArrayList<Armament> getArmaments() {
        return new ArrayList<>(armaments);
    }

    //This Method only used when we COMPLETELY REMOVE an element from the game
    public void removeArmament(Armament armament) {
        this.getMap().setArmaments(armament.getX(),armament.getY(),null);
        armaments.remove(armament);
        Platform.runLater(() ->{
            cell[armament.getY()][armament.getX()].getChildren().remove(armament.getShape());
        });
        removeArmamentReferences(armament);
    }



    //Defining Target ArrayLists


    //adds a new Armament to all possible TargetLists(Imagine this as sort of a construction)
    private void addArmamentReferences(Armament armament){
        //starting from top of hierarchy
        if(armament instanceof SupportArmament){
            this.supportArmaments.add((SupportArmament) armament);
            if(armament instanceof SpecialTower){
                this.specialTowers.add((SpecialTower) armament);
            }
        }else if (armament instanceof AttackArmament){
            this.attackArmaments.add((AttackArmament) armament);
            if(armament instanceof Tower){
                this.towers.add((Tower)armament);
                if(armament instanceof BlackTower){
                    this.blackTowers.add((BlackTower)armament);
                }else if(armament instanceof ElectricTower){
                    this.electricTowers.add((ElectricTower) armament);
                }else if(armament instanceof HospitalTower){
                    this.hospitalTowers.add((HospitalTower) armament);
                }
            }else if(armament instanceof Soldier){
                this.soldiers.add((Soldier) armament);
                if(armament instanceof MeleeSoldier) {
                    this.meleeSoldiers.add((MeleeSoldier) armament);
                    if (armament instanceof Swordsman) {
                        this.swordsmen.add((Swordsman) armament);
                    }else if(armament instanceof Goblin){
                        this.goblins.add((Goblin) armament);
                    }else if(armament instanceof Shield){
                        this.shields.add((Shield) armament);
                    }else if(armament instanceof Knight){
                        this.knights.add((Knight) armament);
                    }
                } else if(armament instanceof RangedSoldier){
                    this.rangedSoldiers.add((RangedSoldier) armament);
                    if(armament instanceof Archer){
                        this.archers.add((Archer) armament);
                    }else if(armament instanceof Dragon){
                        this.dragons.add((Dragon) armament);
                    }else if(armament instanceof Healer){
                        this.healers.add((Healer) armament);
                    }
                }
            }
        }
    }


    //removes an Armament from all possible TargetLists(Imagine this as sort of a Deconstruction)
    private void removeArmamentReferences(Armament armament){
        //starting from top of hierarchy
        if(armament instanceof SupportArmament){
            this.supportArmaments.remove(armament);
            if(armament instanceof SpecialTower){
                this.specialTowers.remove(armament);
            }
        }else if(armament instanceof AttackArmament){
            attackArmaments.remove(armament);
            if(armament instanceof Tower){
                this.towers.remove(armament);
                if(armament instanceof BlackTower){
                    this.blackTowers.remove(armament);
                }else if(armament instanceof ElectricTower){
                    this.electricTowers.remove(armament);
                }else if(armament instanceof HospitalTower){
                    this.hospitalTowers.remove(armament);
                }
            }else if(armament instanceof Soldier){
                this.soldiers.remove(armament);
                if(armament instanceof MeleeSoldier) {
                    this.meleeSoldiers.remove(armament);
                    if (armament instanceof Swordsman) {
                        this.swordsmen.remove(armament);
                    }else if(armament instanceof Goblin){
                        this.goblins.remove(armament);
                    }else if(armament instanceof Shield){
                        this.shields.remove(armament);
                    }else if(armament instanceof Knight){
                        this.knights.remove(armament);
                    }
                } else if(armament instanceof RangedSoldier){
                    this.rangedSoldiers.remove(armament);
                    if(armament instanceof Archer){
                        this.archers.remove(armament);
                    }else if(armament instanceof Dragon){
                        this.dragons.remove(armament);
                    }else if(armament instanceof Healer){
                        this.healers.remove(armament);
                    }
                }
            }
        }
    }

    public ArrayList<SupportArmament> getSupportArmaments() {
        return new ArrayList<SupportArmament>(supportArmaments);
    }

    public ArrayList<SpecialTower> getSpecialTowers() {
        return new ArrayList<SpecialTower>(specialTowers);
    }

    public ArrayList<AttackArmament> getAttackArmaments() {
        return new ArrayList<AttackArmament>(attackArmaments);
    }

    public ArrayList<Tower> getTowers() {
        return new ArrayList<>(towers);
    }

    public ArrayList<BlackTower> getBlackTowers() {
        return new ArrayList<>(blackTowers);
    }

    public ArrayList<Soldier> getSoldiers() {
        return new ArrayList<>(soldiers);
    }

    public ArrayList<MeleeSoldier> getMeleeSoldiers() {
        return new ArrayList<>(meleeSoldiers);
    }

    public ArrayList<Swordsman> getSwordsmen() {
        return new ArrayList<>(swordsmen);
    }

    public Database() {
        maps=new ArrayList<>();
        finished =false;
        inGame = new GridPane();
        players = new Player[2];
        armaments = new ArrayList<>();
        supportArmaments = new ArrayList<>();
        specialTowers = new ArrayList<>();
        attackArmaments = new ArrayList<>();
        towers = new ArrayList<>();
        blackTowers = new ArrayList<>();
        electricTowers = new ArrayList<>();
        hospitalTowers = new ArrayList<>();
        soldiers = new ArrayList<>();
        meleeSoldiers = new ArrayList<>();
        swordsmen = new ArrayList<>();
        goblins = new ArrayList<>();
        shields = new ArrayList<>();
        knights = new ArrayList<>();
        rangedSoldiers = new ArrayList<>();
        archers = new ArrayList<>();
        dragons = new ArrayList<>();
        healers = new ArrayList<>();
    }

    public ArrayList<Map> getMaps() {
        return new ArrayList<>(maps);
    }

    public void addMaps(Map map) {
        maps.add(map);
    }


}
