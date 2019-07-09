package ir.ac.kntu;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application implements Serializable {
    private Database database = new Database();

    @Override
    public void init() throws Exception {

        //Create Original Map
        database.addMaps(createFirstMap());

        //Read from file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Maps"))){
            while (true){
                database.addMaps((Map)ois.readObject());
            }
        }catch (EOFException e){
            //We intentionally eat this Exception
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Main Menu
        VBox mainBox = new VBox();
        mainBox.setSpacing(20);
        mainBox.setAlignment(Pos.CENTER);
        Button levelEditorButton = new Button("Level Editor");
        Button newGameButton = new Button("New Game");
        mainBox.getChildren().addAll(levelEditorButton,newGameButton);
        Scene mainMenu = new Scene(mainBox,200,100);
        primaryStage.setScene(mainMenu);
        primaryStage.show();

        //This Button Starts the Level Editor Process
        levelEditorButton.setOnMouseClicked(event -> getLevelSize(primaryStage,mainMenu));

        //This Button starts the game process
        newGameButton.setOnMouseClicked(event -> chooseMap(primaryStage, mainMenu));
    }

    @Override
    public void stop() throws Exception {
        database.setFinished(true);
        super.stop();
    }

    private void chooseMap(Stage primaryStage, Scene mainMenu){
        //if we have no map we can't play the game
        if (database.getMaps().size()==0){
            return;
        }
        //Creating Pane
        VBox mapBox = new VBox();

        //Adding Buttons for Adding cards to card Arraylist
        HBox playerSeparator = new HBox();
        SelectSoldiers selectSoldiersPlayerOne = new SelectSoldiers();
        SelectSoldiers selectSoldiersPlayerTwo = new SelectSoldiers();
        playerSeparator.getChildren().add(selectSoldiersPlayerOne.getVBox());
        playerSeparator.getChildren().add(selectSoldiersPlayerTwo.getVBox());
        playerSeparator.setAlignment(Pos.CENTER);



        //Adding TextField to get player names
        TextField playerOne = new TextField("Player One");
        TextField playerTwo = new TextField("Player Two");
        playerOne.setAlignment(Pos.CENTER);
        playerOne.setMinSize(100,30);
        playerOne.setMaxSize(200,30);
        playerOne.setPrefSize(150,30);
        playerTwo.setAlignment(Pos.CENTER);
        playerTwo.setMinSize(100,30);
        playerTwo.setMaxSize(200,30);
        playerTwo.setPrefSize(150,30);
        mapBox.getChildren().addAll(playerOne,playerTwo);
        mapBox.getChildren().add(playerSeparator);


        //Adding buttons to choose Map
        Button[] buttons = new Button[database.getMaps().size()];
        for(int i=0;i<buttons.length;i++){
            //Initializing button and giving is size
            buttons[i]=new Button();
            buttons[i].setMinSize(100,30);
            buttons[i].setMaxSize(200,30);
            buttons[i].setPrefSize(150,30);

            //We add this so we can use the index
            final int finalI = i;
            buttons[i].setText(database.getMaps().get(i).getName());
            buttons[i].setOnMouseClicked(event -> {
                database.setPlayers(0,new Player(playerOne.getText(),
                        0,selectSoldiersPlayerOne.getPlayerCardList()));
                database.setPlayers(1,new Player(playerTwo.getText(),
                        1,selectSoldiersPlayerTwo.getPlayerCardList()));
                setTowers(primaryStage,database.getMaps().get(finalI));
            } );
            mapBox.getChildren().add(buttons[i]);
        }

        mapBox.setAlignment(Pos.CENTER);
        mapBox.setSpacing(20);

        //Setting Scene
        Scene chooseMapScene = new Scene(mapBox,400,(2* buttons.length + 20)*30);
        primaryStage.setScene(chooseMapScene);
    }


    private void setTowers(Stage primaryStage,Map map){
        //Initializing Database and Fx Elements
        database.setMap(map);
        GridPane inGame = new GridPane();
        database.setInGame(inGame);
        StackPane[][] cell = new StackPane[map.getSize()][map.getSize()];
        database.setCell(cell);
        Rectangle[][] rectangles = new Rectangle[map.getSize()][map.getSize()];
        database.setRectangles(rectangles);
        database.setPrmaryStage(primaryStage);
        //Initializing Tower setting Process
        AtomicInteger towerCounter=new AtomicInteger(0);
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                rectangles[i][j] = new Rectangle(45, 45);
                cell[i][j] = new StackPane(rectangles[i][j]);
                int finalI = i;
                int finalJ = j;
                //Setting up a total of 20 Towers
                rectangles[i][j].setOnMouseClicked(event ->{
                    //have to input i and j reversely
                    settingTowerEvents(map,towerCounter,finalJ,finalI);
                });
            }
        }
        //Coloring the map
        colorGameMap(map,inGame,cell,rectangles);

        //Adding submit button
        Button submit = new Button("ok");
        inGame.add(submit,0,map.getSize());
        Scene inGameScene = new Scene(inGame);

        submit.setOnMouseClicked(event -> {
            inGame.getChildren().remove(submit);
            mainGameScene(primaryStage,map,inGameScene);
        });
        primaryStage.setScene(inGameScene);
    }

    private void settingTowerEvents(Map map,AtomicInteger towerCounter,int finalI,int finalJ){
        if(towerCounter.get()<3
                //IF WE WANT TO DECREASE MANA BY ADDING TOWERS WE CAN EASILY DO IT HERE(EXPLAIN TO THEM)
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==2
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==0){
            database.addArmament(new BlackTower(finalI,finalJ,0));
            towerCounter.set(towerCounter.get()+1);
        }else if(towerCounter.get()>=3 && towerCounter.get()<6
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==2
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==0){
            database.addArmament(new ElectricTower(finalI,finalJ,0));
            towerCounter.set(towerCounter.get()+1);
        }else if(towerCounter.get()>=6 && towerCounter.get()<9
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==2
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==0){
            database.addArmament(new HospitalTower(finalI,finalJ,0));
            towerCounter.set(towerCounter.get()+1);
        }else if(towerCounter.get()==9
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==4
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==0){
            database.addArmament(new SpecialTower(finalI,finalJ,0));
            towerCounter.set(towerCounter.get()+1);

        }else if(towerCounter.get()>=10 && towerCounter.get()<13
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==2
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==1){
            database.addArmament(new BlackTower(finalI,finalJ,1));
            towerCounter.set(towerCounter.get()+1);
        }else if(towerCounter.get()>=13 && towerCounter.get()<16
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==2
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==1){
            database.addArmament(new ElectricTower(finalI,finalJ,1));
            towerCounter.set(towerCounter.get()+1);
        }else if(towerCounter.get()>=16 && towerCounter.get()<19
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==2
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==1){
            database.addArmament(new HospitalTower(finalI,finalJ,1));
            towerCounter.set(towerCounter.get()+1);
        }else if(towerCounter.get()==19
                //if empty
                &&map.getArmaments(finalI,finalJ)==null
                //if Tower Route
                &&map.getBoardValue(finalI,finalJ)==4
                //if Player one Route
                &&map.getDomain(finalI,finalJ)==1){
            database.addArmament(new SpecialTower(finalI,finalJ,1));
            towerCounter.set(towerCounter.get()+1);

        }
    }

    private void mainGameScene(Stage primaryStage,Map map, Scene inGameScene){

        //Finding Bases for Each Player(to identify soldier's Starting Position)
        ArrayList<Coordinate> playerOneCoordinates = SpecialTower.findAvailableRedHouse(map,0);
        ArrayList<Coordinate> playerTwoCoordinates = SpecialTower.findAvailableRedHouse(map,1);
        database.getPlayers(0).setBases(playerOneCoordinates);
        database.getPlayers(1).setBases(playerTwoCoordinates);

        inGameScene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            switch (key.getCode()){
                //Player One Keys : Q,W,E,R Change Selected Card
                //Z,X,C,V,B Summon that cart into the base
                case Q:
                    ArrayList<Player.Cards> cards = database.getPlayers(0).getCards();
                    if(cards.size()>0){
                        database.getPlayers(0).setSelectedCard(cards.get(0));
                    }
                    break;
                case Z:
                    Player.Cards card = database.getPlayers(0).getSelectedCard();
                    ArrayList<Coordinate> coordinates = database.getPlayers(0).getBases();
                    //check if cell is full
                    if(coordinates.size()<=0){
                        break;
                    }
                    Coordinate coordinate = database.getPlayers(0).getBases().get(0);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(0).createArmament(database,card,coordinate,1);
                    break;
                case W:
                    cards = database.getPlayers(0).getCards();
                    if(cards.size()>1){
                        database.getPlayers(0).setSelectedCard(cards.get(1));
                    }
                    break;
                case X:
                    card = database.getPlayers(0).getSelectedCard();
                    coordinates = database.getPlayers(0).getBases();
                    //check if cell is full
                    if(coordinates.size()<=1){
                        break;
                    }
                    coordinate = database.getPlayers(0).getBases().get(1);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(0).createArmament(database,card,coordinate,1);
                    break;
                case E:
                    cards = database.getPlayers(0).getCards();
                    if(cards.size()>2){
                        database.getPlayers(0).setSelectedCard(cards.get(2));
                    }
                    break;
                case C:
                    card = database.getPlayers(0).getSelectedCard();
                    coordinates = database.getPlayers(0).getBases();
                    //check if cell is full
                    if(coordinates.size()<=2){
                        break;
                    }
                    coordinate = database.getPlayers(0).getBases().get(2);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(0).createArmament(database,card,coordinate,1);
                case R:
                    cards = database.getPlayers(0).getCards();
                    if(cards.size()>3){
                        database.getPlayers(0).setSelectedCard(cards.get(3));
                    }
                    break;
                case V:
                    card = database.getPlayers(0).getSelectedCard();
                    coordinates = database.getPlayers(0).getBases();
                    //check if cell is full
                    if(coordinates.size()<=3){
                        break;
                    }
                    coordinate = database.getPlayers(0).getBases().get(3);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(0).createArmament(database,card,coordinate,1);
                    break;
                case B:
                    card = database.getPlayers(0).getSelectedCard();
                    coordinates = database.getPlayers(0).getBases();
                    //check if cell is full
                    if(coordinates.size()<=4){
                        break;
                    }
                    coordinate = database.getPlayers(0).getBases().get(4);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(0).createArmament(database,card,coordinate,1);
                    break;
                //Player Two Scheme: Numpad1,Numpad3,Numpad7,Numpad9 Change Card
                //Numpad2,Numpad4,Numpad5,Numpad6,Numpad8 summon in to base
                case NUMPAD1:
                    cards = database.getPlayers(1).getCards();
                    if(cards.size()>0){
                        database.getPlayers(1).setSelectedCard(cards.get(0));
                    }
                    break;
                case NUMPAD2:
                    card = database.getPlayers(1).getSelectedCard();
                    coordinates = database.getPlayers(1).getBases();
                    //check if cell is full
                    if(coordinates.size()<=0){
                        break;
                    }
                    coordinate = database.getPlayers(1).getBases().get(0);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(1).createArmament(database,card,coordinate,1);
                    break;
                case NUMPAD3:
                    cards = database.getPlayers(1).getCards();
                    if(cards.size()>1){
                        database.getPlayers(1).setSelectedCard(cards.get(1));
                    }
                    break;
                case NUMPAD4:
                    card = database.getPlayers(1).getSelectedCard();
                    coordinates = database.getPlayers(1).getBases();
                    //check if cell is full
                    if(coordinates.size()<=1){
                        break;
                    }
                    coordinate = database.getPlayers(1).getBases().get(1);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(1).createArmament(database,card,coordinate,1);
                    break;
                case NUMPAD7:
                    cards = database.getPlayers(1).getCards();
                    if(cards.size()>2){
                        database.getPlayers(1).setSelectedCard(cards.get(2));
                    }
                    break;
                case NUMPAD5:
                    card = database.getPlayers(1).getSelectedCard();
                    coordinates = database.getPlayers(1).getBases();
                    //check if cell is full
                    if(coordinates.size()<=2){
                        break;
                    }
                    coordinate = database.getPlayers(1).getBases().get(2);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(1).createArmament(database,card,coordinate,1);
                    break;
                case NUMPAD9:
                    cards = database.getPlayers(1).getCards();
                    if(cards.size()>3){
                        database.getPlayers(1).setSelectedCard(cards.get(3));
                    }
                    break;
                case NUMPAD6:
                    card = database.getPlayers(1).getSelectedCard();
                    coordinates = database.getPlayers(1).getBases();
                    //check if cell is full
                    if(coordinates.size()<=3){
                        break;
                    }
                    coordinate = database.getPlayers(1).getBases().get(3);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(1).createArmament(database,card,coordinate,1);
                    break;
                case NUMPAD8:
                    card = database.getPlayers(1).getSelectedCard();
                    coordinates = database.getPlayers(1).getBases();
                    //check if cell is full
                    if(coordinates.size()<=4){
                        break;
                    }
                    coordinate = database.getPlayers(1).getBases().get(4);
                    if(map.getArmaments(coordinate.getX(),coordinate.getY())!=null) {
                        break;
                    }
                    database.getPlayers(1).createArmament(database,card,coordinate,1);
                    break;
                default:
                    break;
            }
        });


        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        //Initializing game;

        //Initializing game Director
        Thread thread = new Thread(new Director(database));
        thread.start();
    }

    private void colorGameMap(Map map,
                             GridPane inGame,
                             StackPane[][] cell,
                             Rectangle[][] rectangles){



        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                rectangles[i][j].setStroke(Color.BLACK);
                switch (map.getBoardValue(i,j)){
                    case 0:
                        if(map.getDomain(i,j)==0){
                            rectangles[j][i].setFill(Color.GRAY);
                        }else {
                            rectangles[j][i].setFill(Color.DIMGRAY);
                        }
                        break;
                    case 1:
                        rectangles[j][i].setFill(Color.RED);
                        break;
                    case 2:
                        rectangles[j][i].setFill(Color.BLUE);
                        break;
                    case 3:
                        rectangles[j][i].setFill(Color.YELLOW);
                        break;
                    case 4:
                        rectangles[j][i].setFill(Color.GREEN);
                        break;
                    default:
                        break;
                }
                cell[i][j] = new StackPane(rectangles[i][j]);
                inGame.add(cell[i][j], i, j);
            }
        }
        inGame.setHgap(2);
        inGame.setVgap(2);
        inGame.setAlignment(Pos.CENTER);
    }

    private void getLevelSize(Stage primaryStage,Scene mainMenu) {
        VBox levelSize = new VBox();
        Scene levelSizeScene = new Scene(levelSize,200,100);

        //Adding Input TextFields
        TextField nameField = new TextField("Map");
        nameField.setAlignment(Pos.CENTER);
        TextField sizeField = new TextField("20");
        sizeField.setAlignment(Pos.CENTER);

        //Adding Submit and Cancel Buttons
        HBox buttonBox = new HBox();
        Button levelEditorButton = new Button("Submit");
        Button mainMenuButton = new Button("Cancel");
        buttonBox.getChildren().addAll(levelEditorButton,mainMenuButton);
        buttonBox.setAlignment(Pos.CENTER);

        //Finalizing the Pane
        levelSize.setAlignment(Pos.CENTER);
        levelSize.getChildren().addAll(nameField,sizeField,buttonBox);

        //Cancel Button returns to mainMenu Scene
        mainMenuButton.setOnMouseClicked(event -> primaryStage.setScene(mainMenu));

        //Submit Button proceeds to Level Editor Scene
        levelEditorButton.setOnMouseClicked(event -> setUpLevelEditor(primaryStage,mainMenu,nameField.getText(),Integer.parseInt(sizeField.getText())));


        //Setting the scene to current process
        primaryStage.setScene(levelSizeScene);
    }


    private void setUpLevelEditor(Stage primaryStage,Scene mainMenu,String name,int size){
        //Set up Pane and Scene
        GridPane levelEditorPane = new GridPane();
        levelEditorPane.setAlignment(Pos.CENTER);
        Scene levelEditor = new Scene(levelEditorPane,(size + 2 )*45 ,(size + 2)*45);

        //Domain of a player TopSide = 0 , DownSide=1
        int[][] domain = new int[size][size];
        //Nothing =0 , Starting Positions =1, TowerPosition=2 , Route=3, Special Tower = 4
        int[][] boardValue=new int[size][size];
        for(int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                //Half the Domain is 0 and half is 1
                if(i<size/2){
                    domain[i][j]=0;
                } else {
                    domain[i][j]=1;
                }
                //For now the board is empty
                boardValue[i][j]=0;
            }
        }
        //Rectangles[i][j] represents domain and boardValue[i][j] at the beginning all houses are white
        Rectangle[][] rectangles = new Rectangle[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rectangles[i][j] = new Rectangle(45, 45);
                Rectangle rectangle = rectangles[i][j];
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.WHITE);
                rectangle.setOnMouseClicked(event -> changeColor(rectangle));
                //in GridPane first input is column, second is row
                levelEditorPane.add(rectangles[i][j], j, i);
            }
        }

        //Save Button starts the saving Process, and saves the representing boards into a Map object binary file
        Button submit = new Button("Save");
        submit.setOnMouseClicked(event -> setAndSaveTable(primaryStage,mainMenu,rectangles,domain,boardValue,name));
        //Cancel Button returns to main menu
        Button cancel = new Button("Back");
        cancel.setOnMouseClicked(event -> closeLevelEditor(primaryStage,mainMenu));

        //Setting the scene to current process
        levelEditorPane.add(submit,0,size+1);
        levelEditorPane.add(cancel,0,size+2);
        primaryStage.setScene(levelEditor);
        primaryStage.setFullScreen(true);


    }
    //Changing a Rectangles color
    private void changeColor(Rectangle rectangle){
        if(rectangle.getFill() ==Color.WHITE){
            rectangle.setFill(Color.RED);
        }else if (rectangle.getFill()==Color.RED){
            rectangle.setFill(Color.BLUE);
        }else if (rectangle.getFill()==Color.BLUE){
            rectangle.setFill(Color.YELLOW);
        } else if (rectangle.getFill()==Color.YELLOW){
            rectangle.setFill(Color.GREEN);
        }else if (rectangle.getFill()==Color.GREEN){
            rectangle.setFill(Color.WHITE);
        }
    }
    //Updating boardValue and then saving the map into an Appendable byte file
    private void setAndSaveTable(Stage primaryStage,Scene mainMenu,Rectangle[][] rectangles,int[][] domain, int[][] boardValue,String name){
        for(int i=0;i<rectangles.length;i++){
            for (int j=0; j<rectangles[i].length;j++){
                if(rectangles[i][j].getFill() ==Color.WHITE){
                    boardValue[i][j] = 0;
                }else if (rectangles[i][j].getFill()==Color.RED){
                    boardValue[i][j] = 1;
                }else if (rectangles[i][j].getFill()==Color.BLUE){
                    boardValue[i][j] = 2;
                }else if (rectangles[i][j].getFill()==Color.YELLOW){
                    boardValue[i][j] = 3;
                }else if (rectangles[i][j].getFill()==Color.GREEN) {
                    boardValue[i][j] = 4;
                }

                if(i<rectangles.length/2){
                    domain[i][j] = 0;
                }else {
                    domain[i][j] = 1;
                }
            }
        }

        //Appending Maps into the Map file using a class that doesn't write a new header for appended bytes
        File file = new File("Maps");
        if(file.exists()){
            try(AppendingObjectOutputStream aoos = new AppendingObjectOutputStream(new FileOutputStream(file,true))){
                aoos.writeObject(new Map(name,rectangles.length,domain,boardValue));
            } catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file,true))){
                oos.writeObject(new Map(name,rectangles.length,domain,boardValue));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        //returning to main menu
        primaryStage.setScene(mainMenu);
        primaryStage.setFullScreen(false);
    }


    private void closeLevelEditor(Stage primaryStage, Scene mainMenu){
        //returning to main menu
        primaryStage.setScene(mainMenu);
        primaryStage.setFullScreen(false);

    }

    //Create Original , Requested Map
    private Map createFirstMap(){
        //Adding Map
        int[][] board = {
                /*
                White = 0
                Red = 1
                Blue = 2
                Yellow = 3
                Green = 4
                 */
                {0,0,0,0,0,2,1,2,0,2,1,2,0,2,1,2,0,0,0,0,},
                {0,2,2,2,2,2,3,2,0,2,3,2,0,2,3,2,2,2,2,2,},
                {0,2,3,3,3,3,3,2,0,2,3,2,0,2,3,3,3,3,3,2,},
                {0,2,3,2,2,2,2,2,2,2,3,2,2,2,2,2,2,2,3,2,},
                {0,2,3,2,0,2,3,3,3,3,3,3,3,3,3,2,0,2,3,2,},
                {0,2,3,2,0,2,3,2,2,2,4,2,2,2,3,2,0,2,3,2,},
                {0,2,3,2,0,2,3,2,0,0,0,0,0,2,3,2,0,2,3,2,},
                {0,2,3,2,0,2,3,2,0,0,0,0,0,2,3,2,0,2,3,2,},
                {0,0,3,0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,3,0,},
                {0,0,3,0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,3,0,},
                {0,0,3,0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,3,0,},
                {0,0,3,0,0,0,3,0,0,0,0,0,0,0,3,0,0,0,3,0,},
                {0,2,3,2,0,2,3,2,0,0,0,0,0,2,3,2,0,2,3,2,},
                {0,2,3,2,0,2,3,2,0,0,0,0,0,2,3,2,0,2,3,2,},
                {0,2,3,2,0,2,3,2,2,2,4,2,2,2,3,2,0,2,3,2,},
                {0,2,3,2,0,2,3,3,3,3,3,3,3,3,3,2,0,2,3,2,},
                {0,2,3,2,2,2,2,2,2,2,3,2,2,2,2,2,2,2,3,2,},
                {0,2,3,3,3,3,3,2,0,2,3,2,0,2,3,3,3,3,3,2,},
                {0,2,2,2,2,2,3,2,0,2,3,2,0,2,3,2,2,2,2,2,},
                {0,0,0,0,0,2,1,2,0,2,1,2,0,2,1,2,0,0,0,0,}
        };
        int[][] domain = {
                /*
                Player One = 0
                Player Two = 1
                 */
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,}
        };
        Map map = new Map("Original Map",20,domain,board);
        return map;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
