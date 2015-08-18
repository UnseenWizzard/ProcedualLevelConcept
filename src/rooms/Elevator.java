package rooms;

/**
 * Special kind of room, with special handling by the game (entrance to a level)
 */
public class Elevator extends Room {

    public Elevator(){
        super();
        this.setLeftDoor(1);
        this.setRightDoor(1);
        this.setUpperDoor(1);
        this.setLowerDoor(1);
    }

}
