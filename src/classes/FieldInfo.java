package classes;

import java.io.Serializable;

public class FieldInfo implements Serializable {
    public boolean open;
    public boolean ship;

    public boolean isShip() {
        return ship;
    }

    public boolean isOpen() {
        return open;
    }

    public void setShip(boolean ship) {
        this.ship = ship;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
