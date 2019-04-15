package game;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioHandler {

    Long currentFrame;
    private Clip clip;
    private String status;
    private String filepath;
    AudioInputStream audioInputStream;

    public AudioHandler(String filepath) {
        try {
            this.filepath = filepath;
            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(-1);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();

        status = "play";
    }

    public void pause() {
        if (status.equals("pause")) {
            return;
        }
        currentFrame = Long.valueOf(clip.getMicrosecondPosition());
        clip.stop();
        status = "paused";
    }

    public void resume() {
        if (status.equals("play")) {
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame.longValue());
        play();
    }

    public void stop() {
        currentFrame = Long.valueOf(0L);
        clip.stop();
        clip.close();
    }

    public void resetAudioStream() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
            clip.open(audioInputStream);
            clip.loop(-1);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
