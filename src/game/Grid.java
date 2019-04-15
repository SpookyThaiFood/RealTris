package game;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import object.Block;

public class Grid {

    private int controlx;
    private int controly;
    private int ghostx;
    private int ghosty;
    private final int DROP_TIME = 72;
    private final int DROP_RESET_TIME = 3;
    private int dropTimer = 0;
    private int dropResetTimer = 0;

    private int startPosx = 4;
    private int startPosy = 1;

    private int level = 1;
    private int lines = 0;

    public double width;
    public double height;
    public int columns;
    public int rows;
    private boolean lost = false;

    private int blocksDropped = 0;

    private int score = 0;

    private LinkedList<Block> pieceConfig = new LinkedList();
    private LinkedList<Block> rotateList = new LinkedList();
    private LinkedList<Block> ghostList = new LinkedList();

    private Queue<Integer> pieceQueue = new LinkedList();

    private int hold = 0;
    private boolean switched = false;
    private int combo = 0;

    public static final int PIECE_T = 1;

    public static final int PIECE_Z = 2;
    public static final int PIECE_S = 3;
    public static final int PIECE_O = 4;
    public static final int PIECE_I = 5;
    public static final int PIECE_L = 6;
    public static final int PIECE_IL = 7;
    private int pieceSpawned;
    private Block[][] grid;

    public void switchLevel() {
        if (blocksDropped <= 25) {
            level = 1;
        } else {
            level = (blocksDropped / 25);
        }
    }

    public int getBlocksDropped() {
        return blocksDropped;
    }

    public void reset() {
        score = (this.level = 0);
        hold = (this.combo = 0);
        controlx = startPosx;
        controly = startPosy;
        ghostx = controlx;
        ghosty = controly;
        blocksDropped = 0;
        switched = false;
        lost = false;
        dropTimer = 0;
        clearList();
        ghostList.clear();
        pieceQueue.clear();
        rotateList.clear();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }
    }

    public Grid(double width, double height, int columns, int rows) {
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.rows = rows;
        grid = new Block[rows][columns];
        controlx = startPosx;
        controly = startPosy;
    }

    public void initialize() {
    }

    public int getLines() {
        return lines;
    }

    public int getLevel() {
        return level;
    }

    public int getHeld() {
        return hold;
    }

    public int getScore() {
        return score;
    }

    public LinkedList<Block> getGhostBlocks() {
        return ghostList;
    }

    public int getGhostX() {
        return ghostx;
    }

    public int getGhostY() {
        return ghosty;
    }

    public void hold() {
        if (!switched) {
            int toHold = pieceSpawned;
            if (hold != 0) {
                clearSpace();
                pieceConfig.clear();
                resetSpot();
                spawn(hold);
            } else {
                clearSpace();
                pieceConfig.clear();
                resetSpot();
                spawnFromQueue();
            }
            hold = toHold;
            switched = true;
        }
        setGhostBlocks();
    }

    public Block getBlock(int x, int y) {
        if (x < 0) {
            return null;
        }
        if (x > columns - 1) {
            return null;
        }
        if (y < 0) {
            return null;
        }
        if (y > rows - 1) {
            return null;
        }
        return grid[y][x];
    }

    public boolean atEdge(int x, int y) {
        if (x < 0) {
            return true;
        }
        if (x > columns - 1) {
            return true;
        }
        if (y < 0) {
            return true;
        }
        if (y > rows - 1) {
            return true;
        }
        return false;
    }

    public Queue<Integer> getQueue() {
        return pieceQueue;
    }

    public void fillQueue() {
        if (pieceQueue.size() < 3) {
            addToQueue();
        }
    }

    public void addToQueue() {
        int rand = queueRandom();
        pieceQueue.add(Integer.valueOf(rand));
    }

    public void spawnFromQueue() {
        int piece = ((Integer) pieceQueue.remove()).intValue();
        spawn(piece);
    }

    public void spawn(int type) {
        pieceSpawned = type;
        if (type == 1) {
            spawnT();
        }
        if (type == 2) {
            spawnZ();
        }
        if (type == 3) {
            spawnS();
        }
        if (type == 4) {
            spawnO();
        }
        if (type == 5) {
            spawnI();
        }
        if (type == 6) {
            spawnL();
        }
        if (type == 7) {
            spawnIL();
        }
    }

    public void checkFail() {
        if ((!checkMoveLeft()) && (!checkMoveRight())) {
            lost = true;
        }
    }

    public boolean getFail() {
        return lost;
    }

    private void spawnIL() {
        Block block = new Block(-1, 0);
        Block block1 = new Block(0, 0);
        Block block2 = new Block(1, 0);
        Block block3 = new Block(1, -1);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(255, 181, 71));
        }
        checkFail();
    }

    private void spawnL() {
        Block block = new Block(-1, 0);
        Block block1 = new Block(0, 0);
        Block block2 = new Block(1, 0);
        Block block3 = new Block(-1, -1);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(166, 167, 234));
        }
        checkFail();
    }

    private void spawnI() {
        Block block = new Block(-1, 0);
        Block block1 = new Block(0, 0);
        Block block2 = new Block(1, 0);
        Block block3 = new Block(2, 0);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(164, 252, 249));
        }
        checkFail();
    }

    private void spawnO() {
        Block block = new Block(1, 1);
        Block block1 = new Block(0, 1);
        Block block2 = new Block(0, 0);
        Block block3 = new Block(1, 0);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(252, 255, 109));
        }
        checkFail();
    }

    private void spawnS() {
        Block block = new Block(1, -1);
        Block block1 = new Block(0, -1);
        Block block2 = new Block(0, 0);
        Block block3 = new Block(-1, 0);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(132, 206, 119));
        }
        checkFail();
    }

    private void spawnZ() {
        Block block = new Block(-1, -1);
        Block block1 = new Block(0, -1);
        Block block2 = new Block(0, 0);
        Block block3 = new Block(1, 0);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(226, 90, 90));
        }
        checkFail();
    }

    private void spawnT() {
        Block block = new Block(0, -1);
        Block block1 = new Block(-1, 0);
        Block block2 = new Block(0, 0);
        Block block3 = new Block(1, 0);

        pieceConfig.add(block);
        pieceConfig.add(block1);
        pieceConfig.add(block2);
        pieceConfig.add(block3);

        for (int i = 0; i < pieceConfig.size(); i++) {
            ((Block) pieceConfig.get(i)).setColor(new Color(192, 94, 201));
        }
        checkFail();
    }

    public void update() {
        fillQueue();
        switchLevel();
        gravity();
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            grid[(controly + block.getRy())][(controlx + block.getRx())] = block;
        }
    }

    public void gravity() {
        if (dropTimer >= 72) {
            moveDown();
            dropTimer = 0;
        }
        dropTimer += level;
    }

    public void clearSpace() {
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            grid[(controly + block.getRy())][(controlx + block.getRx())] = null;
        }
    }

    public void resetSpot() {
        controlx = startPosx;
        controly = startPosy;
    }

    public void moveLeft() {
        if (checkMoveLeft()) {
            clearSpace();
            controlx -= 1;
            setGhostBlocks();
        }
    }

    public void moveRight() {
        if (checkMoveRight()) {
            clearSpace();
            controlx += 1;
            setGhostBlocks();
        }
    }

    public void moveDown() {
        if (checkMoveDown()) {
            clearSpace();
            controly += 1;
        } else {
            dropResetTimer += 1;
            if (dropResetTimer >= 3) {
                clearList();
                resetSpot();
                switched = false;
                spawnFromQueue();
                if (lineNeedClear() != -1) {
                    clearLine();
                } else {
                    combo = 0;
                }
                dropResetTimer = 0;
                blocksDropped += 1;
            }
        }
    }

    public int getCombo() {
        return combo;
    }

    public boolean checkMoveRight() {
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if (controlx + block.getRx() + 1 > columns - 1) {
                return false;
            }
        }

        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if ((getBlock(controlx + block.getRx() + 1, controly + block.getRy()) != null)
                    && (!contains(getBlock(controlx + block.getRx() + 1, controly + block.getRy())))) {
                return false;
            }
        }

        return true;
    }

    public boolean checkMoveLeft() {
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if (controlx + block.getRx() - 1 < 0) {
                return false;
            }
        }

        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if ((getBlock(controlx + block.getRx() - 1, controly + block.getRy()) != null)
                    && (!contains(getBlock(controlx + block.getRx() - 1, controly + block.getRy())))) {
                return false;
            }
        }

        return true;
    }

    public void rotateLeft() {
        if (checkRotateLeft()) {
            clearSpace();
            for (int i = 0; i < pieceConfig.size(); i++) {
                Block block = (Block) pieceConfig.get(i);
                int x = block.getRy();
                int y = -block.getRx();
                block.setRx(x);
                block.setRy(y);
            }
            setGhostBlocks();
        }
    }

    private boolean checkRotateLeft() {
        if (pieceSpawned == 4) {
            return false;
        }
        rotateList = cloneList();
        for (int i = 0; i < rotateList.size(); i++) {
            Block block = (Block) rotateList.get(i);
            int x = block.getRy();
            int y = -block.getRx();
            block.setRx(x);
            block.setRy(y);
            if (atEdge(controlx + x, controly + y) == true) {
                return false;
            }
            if ((getBlock(controlx + x, controly + y) != null)
                    && (!contains(getBlock(controlx + x, controly + y)))) {
                return false;
            }
        }

        return true;
    }

    public void rotateRight() {
        if (checkRotateRight()) {
            clearSpace();
            for (int i = 0; i < pieceConfig.size(); i++) {
                Block block = (Block) pieceConfig.get(i);
                int x = -block.getRy();
                int y = block.getRx();
                block.setRx(x);
                block.setRy(y);
            }
            setGhostBlocks();
        }
    }

    private boolean checkRotateRight() {
        if (pieceSpawned == 4) {
            return false;
        }
        rotateList = cloneList();
        for (int i = 0; i < rotateList.size(); i++) {
            Block block = (Block) rotateList.get(i);
            Block blockcheck = (Block) pieceConfig.get(i);
            int x = -block.getRy();
            int y = block.getRx();
            if (atEdge(controlx + x, controly + y) == true) {
                return false;
            }
            if ((getBlock(controlx + x, controly + y) != null)
                    && (!contains(getBlock(controlx + x, controly + y)))) {
                return false;
            }
        }

        return true;
    }

    private LinkedList<Block> cloneList() {
        LinkedList<Block> list = new LinkedList();
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = ((Block) pieceConfig.get(i)).clone();
            list.add(block);
        }
        return list;
    }

    private void moveUp() {
        clearSpace();
        controly -= 1;
    }

    public boolean checkMoveDown() {
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if (controly + block.getRy() + 1 > rows - 1) {
                return false;
            }
        }

        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if ((getBlock(controlx + block.getRx(), controly + block.getRy() + 1) != null)
                    && (!contains(getBlock(controlx + block.getRx(), controly + block.getRy() + 1)))) {
                return false;
            }
        }

        return true;
    }

    public boolean checkGhostDown() {
        for (int i = 0; i < ghostList.size(); i++) {
            Block block = (Block) ghostList.get(i);
            if (ghosty + block.getRy() + 1 > rows - 1) {
                return false;
            }
        }

        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            if ((getBlock(ghostx + block.getRx(), ghosty + block.getRy() + 1) != null)
                    && (!contains(getBlock(ghostx + block.getRx(), ghosty + block.getRy() + 1)))) {
                return false;
            }
        }

        return true;
    }

    public void clearLine() {
        int line = lineNeedClear();
        if (line != -1) {
            combo += 1;
            lines += 1;
            score += 1000 * combo * level;
            for (int i = line - 1; i >= -1; i--) {
                for (int j = 0; j < columns; j++) {
//                    grid[(i + 1)][j] = grid[i][j];
                    grid[(i + 1)][j] = getBlock(j, i);
                }
            }
            clearLine();
        }
        if (numOfBlocksOnScreen() == 0) {
            score += 10000 * level;
        }
        setGhostBlocks();
    }

    public int lineNeedClear() {
        int check = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (getBlock(j, i) != null) {
                    check++;
                }
            }
            if (check == 10) {
                return i;
            }
            check = 0;
        }
        return -1;
    }

    public boolean contains(Block block) {
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block1 = (Block) pieceConfig.get(i);
            if (block.equals(block1)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRotateList(Block block) {
        for (int i = 0; i < rotateList.size(); i++) {
            Block block1 = (Block) rotateList.get(i);
            if (block.equals(block1)) {
                return true;
            }
        }
        return false;
    }

    public void clearList() {
        for (int i = 0; i < pieceConfig.size(); i++) {
            Block block = (Block) pieceConfig.get(i);
            grid[(controly + block.getRy())][(controlx + block.getRx())] = block;
        }
        pieceConfig.clear();
    }

    public void fastDrop() {
        setGhostBlocks();
        clearSpace();
        controly = ghosty;
        clearList();
        resetSpot();
        switched = false;
        spawnFromQueue();
        dropResetTimer = 0;
        blocksDropped += 1;
        setGhostBlocks();
        if (lineNeedClear() != -1) {
            clearLine();
        } else {
            combo = 0;
        }
    }

    public void setGhostBlocks() {
        ghosty = controly;
        ghostx = controlx;
        ghostList = cloneList();
        while (checkGhostDown()) {
            ghosty += 1;
        }
    }

    public int numOfBlocksOnScreen() {
        int num = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (getBlock(j, i) != null) {
                    num++;
                }
            }
        }
        return num;
    }

    public int queueRandom() {
        int rand = rand(1, 7);
        return rand;
    }

    public static int rand(int l, int h) {
        return (int) ((h - l + 1) * Math.random() + 1.0D);
    }
}
