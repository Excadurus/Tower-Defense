package ir.ac.kntu;

import java.io.Serializable;

public class Map implements Serializable {
    private String name;
    private int size;
    private int[][] domain;
    private int[][] boardValue;
    private Armament[][] armaments;

    public Map(String name, int size, int[][] domain, int[][] boardValue) {
        this.name=name;
        this.size = size;
        this.domain = domain;
        this.boardValue = boardValue;
        this.armaments = new Armament[size][size];
        setArmament();
    }

    public String getName() {
        return name;
    }


    public int getSize() {
        return size;
    }


    public int getDomain(int i, int j) {
        return domain[i][j];
    }


    public int getBoardValue(int i, int j) {
        return boardValue[i][j];
    }


    public Armament getArmaments(int i,int j) {
        return armaments[i][j];
    }

    public void setArmaments(int i,int j,Armament armament) {
        this.armaments[i][j]=armament;
    }

    public Armament getArmament(int i,int j) {
        return armaments[i][j];
    }



    private void setArmament(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                this.armaments[i][j]=null;
            }
        }
    }

}
