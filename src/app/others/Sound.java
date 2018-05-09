package app.others;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Sound {

    private Clip sound;

    public Sound(String filePath){

        try {
            sound = AudioSystem.getClip();
            File file = new File(filePath);
            sound.open(AudioSystem.getAudioInputStream(file));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void play(){

        if(sound.isRunning())
            sound.stop();

        sound.setFramePosition(0);
        sound.start();

    }

    public void loop(){
        sound.setFramePosition(0);
        sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        sound.stop();
    }

    public void setVolume(float volume){
        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }

    public boolean isSoundRunning(){
        return sound.isRunning();
    }

}
