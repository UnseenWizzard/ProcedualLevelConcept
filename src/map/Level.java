package map;

import rooms.Elevator;
import rooms.Room;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

/**
 * A grid of Rooms that is the current game level
 */
public class Level {

    /**
     * private class, used to remeber grid positions in the procedual generation of a level
     */
    private class Position{
        private int x;
        private int y;
        private Position(int x,int y){
            this.x=x;
            this.y=y;
        }
    }

    //Size of the quadratic room grid
    private int gridSize;
    //the matrix that holds the levels rooms
    private Room levelGrid[][];
    //random used for generating a rooms doors
    private Random r;

    /**
     * Constructor creating a new level
     * Stores gridSize (making sure it's an odd number to place the entrance in the middle), creates the grid matrix, and the random num generator
     * Calls buildlevel() function
     * @param size the size (width,height) of the quadratic level grid
     */
    public Level(int size){
        this.gridSize = (size%2!=0)?size:size+1;
        this.levelGrid = new Room[size][size];
        r = new Random(System.currentTimeMillis());
        this.buildLevel();
    }

    /**
     * Generates the level room by room
     * Starting from the entrance (Elevator) in the middle of the level, visits every room with doors that aren't
     * connected to another rooms, and adds new rooms, until all doors are connected.
     */
    private void buildLevel(){
        int middle = (gridSize/2);
        levelGrid[middle][middle]=new Elevator();
        int x=middle, y=middle, moveX=0, moveY=0;
        Stack<Position> route = new Stack<Position>();
        while (levelHasUnconnectedDoors()){
//            System.out.println("X: " + x + ", Y: " + y);
//            System.out.println("Current room has unconnected doors: " + levelGrid[y][x].hasUnconnectedDoors());
//            System.out.println("Rooms en route here: "+route.size());
//            showLevelText();
            if (levelGrid[y][x].getLeftDoor()!=0 && levelGrid[y][x].getLeftRoom()==null){
                if (this.addRoom(levelGrid[y][x], 0, x - 1, y)) {
                    route.push(new Position(x, y));
                    x -= 1;
                } else if (!levelGrid[y][x].hasUnconnectedDoors()){
                    Position t = route.pop();
                    x=t.x;y=t.y;
                }
            } else if (levelGrid[y][x].getRightDoor()!=0 && levelGrid[y][x].getRightRoom()==null){
                if (this.addRoom(levelGrid[y][x], 1, x + 1, y)) {
                    route.push(new Position(x, y));
                    x+=1;
                } else if (!levelGrid[y][x].hasUnconnectedDoors()){
                    Position t = route.pop();
                    x=t.x;y=t.y;
                }
            } else if (levelGrid[y][x].getUpperDoor()!=0 && levelGrid[y][x].getUpperRoom()==null){
                if (this.addRoom(levelGrid[y][x], 2, x, y - 1)){
                    route.push(new Position(x, y));
                    y-=1;
                } else if (!levelGrid[y][x].hasUnconnectedDoors()){
                    Position t = route.pop();
                    x=t.x;y=t.y;
                }
            } else if (levelGrid[y][x].getLowerDoor()!=0 && levelGrid[y][x].getLowerRoom()==null){
                if (this.addRoom(levelGrid[y][x], 3, x, y + 1)){
                    route.push(new Position(x, y));
                    y+=1;
                } else if (!levelGrid[y][x].hasUnconnectedDoors()){
                    Position t = route.pop();
                    x=t.x;y=t.y;
                }
            } else {
                Position t = route.pop();
                x=t.x;y=t.y;
            }
           // System.out.println("nextX: " + x + ", nextY: " + y+ "\nprevX: " + route.peek().x + ", prevY: " + route.peek().y);
        }
    }

    /**
     * Helper function to check if there are rooms in the level that have doors not connected to another room
     * @return true if there are unconnected doors in any room of the level
     */
    private boolean levelHasUnconnectedDoors(){
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                if (levelGrid[i][j] != null && levelGrid[i][j].hasUnconnectedDoors()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a new room in the level in the position specified by the buildLevel() function
     * Makes sure that there are no doors in the new room that hit a wall on a neighbouring room, and that meeting doors
     * are connected
     * @param currentRoom the room on which the new room borders
     * @param doorPosition the position of the door in the current room that will connect to the new room
     * @param x position of the new room on the grid
     * @param y position of the new room on the grid
     * @return false if the new room would be outside the grid
     */
    private boolean addRoom(Room currentRoom, int doorPosition, int x, int y){
        if(x<0||x>=gridSize||y<0||y>=gridSize){
            currentRoom.setDoor(0,doorPosition);
            return false;
        }
        levelGrid[y][x]=new Room(currentRoom,doorPosition,r.nextInt(4));
        if (x-1>=0&&levelGrid[y][x-1]!=null&&levelGrid[y][x].getLeftDoor()!=0){
            if (levelGrid[y][x-1].getRightDoor()!=0){
                levelGrid[y][x-1].setRightRoom(levelGrid[y][x]);
                levelGrid[y][x].setLeftRoom(levelGrid[y][x - 1]);
            } else {
                levelGrid[y][x].setLeftDoor(0);
            }
        }
        if (x+1<gridSize&&levelGrid[y][x+1]!=null&&levelGrid[y][x].getRightDoor()!=0){
            if (levelGrid[y][x+1].getLeftDoor()!=0){
                levelGrid[y][x+1].setLeftRoom(levelGrid[y][x]);
                levelGrid[y][x].setRightRoom(levelGrid[y][x+1]);
            } else {
                levelGrid[y][x].setRightDoor(0);
            }
        }
        if (y-1>=0&&levelGrid[y-1][x]!=null&&levelGrid[y][x].getUpperDoor()!=0){
            if (levelGrid[y-1][x].getLowerDoor()!=0){
                levelGrid[y-1][x].setLowerRoom(levelGrid[y][x]);
                levelGrid[y][x].setUpperRoom(levelGrid[y-1][x]);
            } else {
                levelGrid[y][x].setUpperDoor(0);
            }
        }
        if (y+1<gridSize&&levelGrid[y+1][x]!=null&&levelGrid[y][x].getLowerDoor()!=0){
            if (levelGrid[y+1][x].getUpperDoor()!=0){
                levelGrid[y+1][x].setUpperRoom(levelGrid[y][x]);
                levelGrid[y][x].setLowerRoom(levelGrid[y+1][x]);
            } else {
                levelGrid[y][x].setLowerDoor(0);
            }
        }
        return true;
    }

    /**
     * Could be used to do things with the doors once the level is fully built
     */
    private void fixDoors(){
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                if (levelGrid[i][j]==null){

                    //levelGrid[i][j]
                }
            }
        }
    }

    /**
     * @return the levelGrid matrix of Rooms
     */
    public Room[][] getGrid(){
        return this.levelGrid;
    }

    /**
     * Prints a textual representation of the level
     */
    public void showLevelText(){
        String levelText="";
        for (int i=0;i<gridSize;i++){
            for (int j=0;j<gridSize;j++){
                if (levelGrid[i][j]==null){
                    levelText+="    ";
                } else if (levelGrid[i][j] instanceof Elevator){
                    levelText+="E("+levelGrid[i][j].getNumDoors()+")";
                } else {
                    levelText+="O("+levelGrid[i][j].getNumDoors()+")";
                }
            }
            levelText+="\n";
        }
        System.out.println(levelText);

    }

    /**
     * Creates and returns an image of the level
     * @return a BufferedImage of the level, constructed from the .png files found in the img folder
     */
    public BufferedImage getLevelImage(){
        BufferedImage map = new BufferedImage(gridSize*32,gridSize*32, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2D = map.createGraphics();
        for (int i=0;i<gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                BufferedImage roomImage = new BufferedImage(32,32, BufferedImage.TYPE_INT_BGR);
                if (levelGrid[i][j]!=null) {
                    Room r = levelGrid[i][j];
                    String fileName = "img/1L.png";
                    if (r instanceof Elevator){
                        fileName = "img/E.png";
                    } else if (r.getLeftDoor()!=0){
                        fileName ="img/1L.png";
                        if (r.getRightDoor()!=0){
                            fileName = "img/2H.png";
                            if (r.getLowerDoor()!=0){
                                fileName ="img/3D.png";
                                if (r.getUpperDoor()!=0){
                                    fileName ="img/4.png";
                                }
                            } else if (r.getUpperDoor()!=0){
                                fileName ="img/3U.png";
                            }
                        } else if (r.getUpperDoor()!=0){
                            fileName="img/2LU.png";
                            if (r.getLowerDoor()!=0){
                                fileName ="img/3L.png";
                            }
                        } else if (r.getLowerDoor()!=0){
                            fileName="img/2LD.png";
                        }
                    } else if (r.getLowerDoor()!=0){
                        fileName ="img/1D.png";
                        if (r.getUpperDoor()!=0) {
                            fileName = "img/2V.png";
                            if (r.getRightDoor()!=0){
                                fileName ="img/3R.png";
                            }
                        } else if (r.getRightDoor()!=0){
                            fileName="img/2RD.png";
                        }
                    } else if (r.getUpperDoor()!=0){
                        fileName ="img/1U.png";
                        if (r.getRightDoor()!=0){
                            fileName ="img/2RU.png";
                        }
                    } else if (r.getRightDoor()!=0){
                        fileName ="img/1R.png";
                    }
                    try {
                        roomImage = ImageIO.read(new File(fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                g2D.drawImage(roomImage, j*32,i*32,null);
            }
        }
        g2D.dispose();
        return map;
    }

}
