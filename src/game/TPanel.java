package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;
import object.Block;

public class TPanel
        extends JPanel {

    Grid grid;
    private Queue<Integer> queue;
    private LinkedList<Block> ghosts;
    private String[] highscores;
    private int ghostx;
    private int ghosty;
    private int pxoffset = -50;
    private int pyoffset = -50;
    private int hxoffset = 100;
    private int hyoffset = 50;
    private int totalBlocks = 0;
    private int linesCleared = 0;

    private int rows;
    private int columns;
    private double width;
    private double height;

    private boolean darkMode = true;

    private final Color PURPLE = new Color(192, 94, 201);
    private final Color RED = new Color(226, 90, 90);
    private final Color GREEN = new Color(132, 206, 119);
    private final Color BLUE = new Color(166, 167, 234);
    private final Color YELLOW = new Color(252, 255, 109);
    private final Color LIGHT_BLUE = new Color(164, 252, 249);
    private final Color ORANGE = new Color(255, 181, 71);

    Color background;

    Color lines;

    Color text;
    private int level;
    private int combo = 0;

    public TPanel(Grid grid) {
        this.grid = grid;
        queue = grid.getQueue();
        ghosts = grid.getGhostBlocks();
        ghostx = grid.getGhostX();
        ghosty = grid.getGhostY();
        highscores = Engine.getScores();
        level = grid.getLevel();
        
        rows = grid.rows;
        columns = grid.columns;
        width = grid.width;
        height = grid.height;

        if (darkMode) {
            background = new Color(36, 52, 71);
            lines = Color.GRAY;
            text = Color.LIGHT_GRAY;
        } else {
            background = Color.WHITE;
            lines = Color.LIGHT_GRAY;
            text = Color.BLACK;
        }
    }

    public void paintComponent(Graphics g) {
        if (darkMode) {
            g.setColor(background);
            g.fillRect(0, 0, 1000, 1000);
        } else {
            g.clearRect(0, 0, 1000, 1000);
        }
        renderBoard(grid, g);
    }

    public void renderBoard(Grid grid, Graphics g) {
        ghosts = grid.getGhostBlocks();
        ghostx = grid.getGhostX();
        ghosty = grid.getGhostY();
        double drawy = height / rows;
        double drawx = width / columns;

        for (int i = 0; i < ghosts.size(); i++) {
            Block block = (Block) ghosts.get(i);
            g.setColor(lines);
            g.fillRect((int) ((ghostx + block.getRx()) * drawx), (int) ((ghosty + block.getRy()) * drawy), (int) drawx, (int) drawy);
        }
        for (int i = 0; i < rows + 1; i++) {
            g.setColor(lines);
            g.drawLine(0, (int) (i * drawy), (int) width, (int) (i * drawy));
            for (int j = 0; j < columns + 1; j++) {
                g.setColor(lines);
                g.drawLine((int) (j * drawx), 0, (int) (j * drawx), (int) height);

                Block block = grid.getBlock(j, i);
                if (block != null) {
                    g.setColor(block.getColor());
                    g.fillRect((int) (j * drawx), (int) (i * drawy) + 1, (int) drawx, (int) drawy - 1);
                }
            }
        }
        paintPreview(g);
        paintHeld(g);
        paintScore(g);
        paintHighScores(g);
        paintCurrentLevel(g);
        paintCombo(g);
        paintLevels(g);
        paintPause(g);
    }

    private void paintPause(Graphics g) {
        if (Engine.isPaused()) {
            g.setColor(background);
            g.fillRect(220, 490, 60, 35);
            g.setColor(text);
            g.drawString("PAUSED", 227, 510);
        }
    }

    private void paintLevels(Graphics g) {
        g.setColor(text);
        linesCleared = grid.getLines();
        g.drawString("Lines cleared: " + linesCleared, 550, 775);
    }

    private void paintCombo(Graphics g) {
        g.setColor(text);
        combo = grid.getCombo();
        g.drawString("Combo: " + combo, 550, 750);
    }

    private void paintCurrentLevel(Graphics g) {
        level = grid.getLevel();
        totalBlocks = grid.getBlocksDropped();
        g.drawString("Current level: " + level, 550, 700);
        g.drawString("Total blocks: " + totalBlocks, 550, 725);
    }

    private void paintHighScores(Graphics g) {
        g.setColor(this.text);
        g.drawRect(525, 275, 200, 600);
        g.drawString("HighScores", 528, 273);
        if (highscores.length < 10) {
            for (int i = 0; i < highscores.length; i++) {
                String text = highscores[i];
                g.drawString(text, 550, 300 + i * 25);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                String text = highscores[i];
                g.drawString(text, 550, 300 + i * 25);
            }
        }
    }

    private void paintScore(Graphics g) {
        String score = Integer.toString(grid.getScore());
        g.setColor(text);
        g.drawString("Current score: " + score, 700, 100);
    }

    private void paintHeld(Graphics g) {
        g.setColor(text);
        g.drawRect(575 + hxoffset, 75 + hyoffset, 150, 100);
        g.drawString("Held", 578 + hxoffset, 173 + hyoffset);
        if (grid.getHeld() == 5) {
            g.setColor(LIGHT_BLUE);
            g.fillRect(600 + hxoffset, 100 + hyoffset, 100, 25);
        } else if (grid.getHeld() == 4) {
            g.setColor(YELLOW);
            g.fillRect(600 + hxoffset, 100 + hyoffset, 50, 50);
        } else if (grid.getHeld() == 3) {
            g.setColor(GREEN);
            g.fillRect(625 + hxoffset, 100 + hyoffset, 50, 25);
            g.fillRect(600 + hxoffset, 125 + hyoffset, 50, 25);
        } else if (grid.getHeld() == 2) {
            g.setColor(new Color(226, 90, 90));
            g.fillRect(625 + hxoffset, 125 + hyoffset, 50, 25);
            g.fillRect(600 + hxoffset, 100 + hyoffset, 50, 25);
        } else if (grid.getHeld() == 1) {
            g.setColor(PURPLE);
            g.fillRect(625 + hxoffset, 100 + hyoffset, 25, 25);
            g.fillRect(600 + hxoffset, 125 + hyoffset, 75, 25);
        } else if (grid.getHeld() == 6) {
            g.setColor(BLUE);
            g.fillRect(600 + hxoffset, 125 + hyoffset, 75, 25);
            g.fillRect(600 + hxoffset, 100 + hyoffset, 25, 25);
        } else if (grid.getHeld() == 7) {
            g.setColor(ORANGE);
            g.fillRect(600 + hxoffset, 125 + hyoffset, 75, 25);
            g.fillRect(650 + hxoffset, 100 + hyoffset, 25, 25);
        }
    }

    public void paintPreview(Graphics g) {
        g.setColor(text);
        g.drawRect(575 + pxoffset, 75 + pyoffset, 150, 100);
        g.drawString("Up next", 578 + pxoffset, 173 + pyoffset);
        if (queue.peek() != null) {
            if (null != (queue.peek())) switch (queue.peek()) {
                case 5:
                    g.setColor(LIGHT_BLUE);
                    g.fillRect(600 + pxoffset, 100 + pyoffset, 100, 25);
                    break;
                case 4:
                    g.setColor(YELLOW);
                    g.fillRect(600 + pxoffset, 100 + pyoffset, 50, 50);
                    break;
                case 3:
                    g.setColor(GREEN);
                    g.fillRect(625 + pxoffset, 100 + pyoffset, 50, 25);
                    g.fillRect(600 + pxoffset, 125 + pyoffset, 50, 25);
                    break;
                case 2:
                    g.setColor(new Color(226, 90, 90));
                    g.fillRect(625 + pxoffset, 125 + pyoffset, 50, 25);
                    g.fillRect(600 + pxoffset, 100 + pyoffset, 50, 25);
                    break;
                case 1:
                    g.setColor(PURPLE);
                    g.fillRect(625 + pxoffset, 100 + pyoffset, 25, 25);
                    g.fillRect(600 + pxoffset, 125 + pyoffset, 75, 25);
                    break;
                case 6:
                    g.setColor(BLUE);
                    g.fillRect(600 + pxoffset, 125 + pyoffset, 75, 25);
                    g.fillRect(600 + pxoffset, 100 + pyoffset, 25, 25);
                    break;
                case 7:
                    g.setColor(ORANGE);
                    g.fillRect(600 + pxoffset, 125 + pyoffset, 75, 25);
                    g.fillRect(650 + pxoffset, 100 + pyoffset, 25, 25);
                    break;
                default:
                    break;
            }
        }
    }

    public void paintScores() {
    }
}
