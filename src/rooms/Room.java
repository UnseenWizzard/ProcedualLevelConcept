package rooms;

import items.Item;

/**
 * A Room in the game
 */
public class Room {

    //0 no door, 1 door, 2 locked door
    private int[] doors = {0,0,0,0};//int leftDoor=0, rightDoor=0, upperDoor=0, lowerDoor=0;
    //rooms that are connected to the room
    private Room leftRoom, rightRoom, upperRoom, lowerRoom;
    //an item that could be in the room
    private Item item;

    /**
     * protected empty super constructor for inheriting classes that represent special types of rooms
     */
    protected Room(){}

    /**
     * Standard constructor for a Room
     * @param incommingRoom the room this Room is connected to
     * @param incommingDoor the door of the incommingRoom the new Room will connect to (0..left,1..right,2..upper,3..lower)
     * @param numDoors number of doors the new room will have
     */
    public Room (Room incommingRoom, int incommingDoor, int numDoors){
        switch (incommingDoor){
            case 0:
                //this.leftDoor=1;
                doors[1]=1;
                this.setRightRoom(incommingRoom);
                incommingRoom.setLeftDoor(1);
                incommingRoom.setLeftRoom(this);
                break;
            case 1:
                //this.rightDoor=1;
                doors[0]=1;
                this.setLeftRoom(incommingRoom);
                incommingRoom.setRightDoor(1);
                incommingRoom.setRightRoom(this);
                break;
            case 2:
                //this.upperDoor=1;
                doors[3]=1;
                this.setLowerRoom(incommingRoom);
                incommingRoom.setUpperDoor(1);
                incommingRoom.setUpperRoom(this);
                break;
            case 3:
                //this.lowerDoor=1;
                doors[2]=1;
                this.setUpperRoom(incommingRoom);
                incommingRoom.setLowerDoor(1);
                incommingRoom.setLowerRoom(this);
                break;
        }
        switch(numDoors){
            case 3:
                doors=new int[]{1,1,1,1};
                break;
            default:
                for(int i=numDoors;i>0;i--){
                    for (int j=0;j<doors.length;j++) {
                        if (doors[j] == 0) {
                            doors[j] = 1;
                            break;
                        }
                    }
                }
        }
    }

    /**
     * Helper returning if a Room has doors that do not lead to a connected room yet
     * @return true if there are doors on a side of the room where no other room connects to it
     */
    public boolean hasUnconnectedDoors(){
        if ((this.doors[0]==1&&this.leftRoom==null)||(this.doors[1]==1&&this.rightRoom==null)||(this.doors[2]==1&&this.upperRoom==null)||(this.doors[3]==1&&this.lowerRoom==null)){
            return true;
        }
        return false;
    }

    public int getDoor(int position){
        return doors[position];
    }
    public void setDoor(int val,int position){
        this.doors[position]=val;
    }

    public int getLeftDoor() {
        return doors[0];
    }

    public void setLeftDoor(int leftDoor) {
        this.doors[0] = leftDoor;
    }

    public int getRightDoor() {
        return doors[1];
    }

    public void setRightDoor(int rightDoor) {
        this.doors[1] = rightDoor;
    }

    public int getUpperDoor() {
        return doors[2];
    }

    public void setUpperDoor(int upperDoor) {
        this.doors[2] = upperDoor;
    }

    public int getLowerDoor() {
        return doors[3];
    }

    public void setLowerDoor(int lowerDoor) {
        this.doors[3] = lowerDoor;
    }

    public Room getLeftRoom() {
        return leftRoom;
    }

    public void setLeftRoom(Room leftRoom) {
        this.leftRoom = leftRoom;
    }

    public Room getRightRoom() {
        return rightRoom;
    }

    public void setRightRoom(Room rightRoom) {
        this.rightRoom = rightRoom;
    }

    public Room getUpperRoom() {
        return upperRoom;
    }

    public void setUpperRoom(Room upperRoom) {
        this.upperRoom = upperRoom;
    }

    public Room getLowerRoom() {
        return lowerRoom;
    }

    public void setLowerRoom(Room lowerRoom) {
        this.lowerRoom = lowerRoom;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getNumDoors(){
        int num=0;
        for (int i:this.doors){
            if(i>0){
                num++;
            }
        }
        return num;
    }
}
