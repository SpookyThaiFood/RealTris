package object;

import java.awt.Color;

public class Block {

    private int blockid;
    private static int totalBlocks;
    private int rx;
    private int ry;
    private Color color = Color.BLUE;

    public Block(int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
        totalBlocks += 1;
        blockid = totalBlocks;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getRx() {
        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public int getRy() {
        return ry;
    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    public int getId() {
        return blockid;
    }

    public boolean equals(Block block) {
        if (block.getId() == getId()) {
            return true;
        }
        return false;
    }

    public Block clone() {
        return new Block(rx, ry);
    }
}
