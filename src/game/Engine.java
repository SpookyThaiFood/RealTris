package game;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Engine extends JFrame implements Runnable {

    private Grid grid = new Grid(500.0D, 1000.0D, 10, 20);

    private TPanel panel = new TPanel(grid);
    private final int MOVE_BUFFER_TIME = 5;
    private final int ROTATE_BUFFER_TIME = 15;
    private static boolean paused = false;

    private static final String SAVE_FILE = "assets\\savefile.txt";

    private static final String AUDIO_FILE = "assets\\Tetris_theme.wav";
    private static FileHandler filehandler = new FileHandler();
    private static AudioHandler audiohandler = new AudioHandler("assets\\Tetris_theme.wav");

    Clip clip;

    KeyHandler keyhandler = new KeyHandler();
    boolean[] keys;
    int[] keyBuffer = new int['ü'];

    public Engine() {
    }

    public void run() {
        add(panel);
        addKeyListener(keyhandler);
        setSize(1017, 1040);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);

        grid.fillQueue();
        grid.spawnFromQueue();
        grid.setGhostBlocks();

        audiohandler.play();

        boolean isRunning = true;
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0D;
        double ns = 1.0E9D / amountOfTicks;
        double delta = 0.0D;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1.0D) {
                if (grid.getFail()) {
                    isRunning = false;
                } else if (paused) {
                    checkPause();
                } else {
                    update();
                }
                delta -= 1.0D;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000L) {
                timer += 1000L;
                frames = 0;
            }
        }
        close();
    }

    private void close() {
        String name = JOptionPane.showInputDialog(null, "Enter nickname", "", -1);
        name = removeSpaces(name);
        int score = grid.getScore();
        String save = name + ": " + score;
        filehandler.append(save, "assets\\savefile.txt");
        grid.reset();
        keyhandler.reset();
        run();
    }

    public static String[] getScores() {
        String[] loadedArray = filehandler.openArray("assets\\savefile.txt");
        loadedArray = sortScores(loadedArray);
        return loadedArray;
    }

    private static String[] sortScores(String[] array) {
        String[] b = array;
        for (int i = 0; i < array.length - 1; i++) {
            if (getScoreValue(b[i]) < getScoreValue(b[(i + 1)])) {
                String r = b[i];
                b[i] = b[(i + 1)];
                b[(i + 1)] = r;
                return sortScores(b);
            }
        }
        return b;
    }

    private static int getScoreValue(String text) {
        boolean found = false;
        String score = "";
        for (int i = 0; i < text.length(); i++) {
            if ((i > 0)
                    && (text.charAt(i - 1) == ':')) {
                found = true;
            }

            if (found) {
                score = score + text.charAt(i);
            }
        }
        score = removeSpaces(score);
        score = validateScore(score);

        return Integer.valueOf(score).intValue();
    }

    public static String validateScore(String text) {
        if (text.length() > 9) {
            int d = text.length() - 9;
            String text1 = "";
            for (int i = 0; i < text.length() - d; i++) {
                text1 = text1 + text.charAt(i);
            }
            return text1;
        }
        return text;
    }

    public static String removeSpaces(String text) {
        String text1 = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') {
                text1 = text1 + text.charAt(i);
            }
        }
        return text1;
    }

    private String arrayToString(String[] array) {
        String text = "";
        for (int i = 0; i < array.length; i++) {
            text = text + array[i] + "\n";
        }
        return text;
    }

    private void render() {
        panel.repaint();
    }

    private void update() {
        keys = keyhandler.getKeys();

        if (keys[65] != false) {
            if (keyBuffer[65] == 0) {
                grid.moveLeft();
            } else if (keyBuffer[65] >= 5) {
                grid.moveLeft();
                keyBuffer[65] = 0;
            }
            keyBuffer[65] += 1;
        } else {
            keyBuffer[65] = 0;
        }
        if (keys[68] != false) {
            if (keyBuffer[68] == 0) {
                grid.moveRight();
            } else if (keyBuffer[68] >= 5) {
                grid.moveRight();
                keyBuffer[68] = 0;
            }
            keyBuffer[68] += 1;
        } else {
            keyBuffer[68] = 0;
        }
        if (keys[83] != false) {
            if (keyBuffer[83] == 0) {
                grid.moveDown();
            } else if (keyBuffer[83] >= 5) {
                grid.moveDown();
                keyBuffer[83] = 0;
            }
            keyBuffer[83] += 1;
        } else {
            keyBuffer[83] = 0;
        }
        if (keys[44] != false) {
            if (keyBuffer[44] == 0) {
                grid.rotateLeft();
            } else if (keyBuffer[44] >= 15) {
                grid.rotateLeft();
                keyBuffer[44] = 0;
            }
            keyBuffer[44] += 1;
        } else {
            keyBuffer[44] = 0;
        }
        if (keys[46] != false) {
            if (keyBuffer[46] == 0) {
                grid.rotateRight();
            } else if (keyBuffer[46] >= 15) {
                grid.rotateRight();
                keyBuffer[46] = 0;
            }
            keyBuffer[46] += 1;
        } else {
            keyBuffer[46] = 0;
        }
        if (keys[32] != false) {
            if (keyBuffer[32] == 0) {
                grid.fastDrop();
            } else if (keyBuffer[32] >= 15) {
                grid.fastDrop();
                keyBuffer[32] = 0;
            }
            keyBuffer[32] += 1;
        } else {
            keyBuffer[32] = 0;
        }
        if (keys[16] != false) {
            if (keyBuffer[16] == 0) {
                grid.hold();
            } else if (keyBuffer[16] >= 15) {
                grid.hold();
                keyBuffer[16] = 0;
            }
            keyBuffer[16] += 1;
        } else {
            keyBuffer[16] = 0;
        }

        checkPause();

        grid.update();
    }

    private void checkPause() {
        keys = keyhandler.getKeys();
        if (keys[27] != false) {
            if (keyBuffer[27] == 0) {
                pause();
                audiohandler.pause();
            } else if (keyBuffer[27] >= 15) {
                pause();
                audiohandler.pause();
                keyBuffer[27] = 0;
            }
            keyBuffer[27] += 1;
        } else {
            keyBuffer[27] = 0;
        }
    }

    private void pause() {
        if (paused) {
            paused = false;
        } else {
            paused = true;
        }
    }

    public static boolean isPaused() {
        return paused;
    }
}
