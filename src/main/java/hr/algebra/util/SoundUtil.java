package hr.algebra.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class SoundUtil {
    private SoundUtil(){}

    //https://youtu.be/SyZQVJiARTQ?si=GYgUVmNsIc04f4AH
    public static void playAudioWithStop(String audioPath, Scene scene){
        File file = new File(audioPath);
        if(!file.exists()){
            LoggingUtil.logError("Sound file not found");
            return;
        }
        try{
            AudioInputStream audInput = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audInput);

            clip.start();

            scene.setOnKeyPressed(event->{
                if(event.getCode()== KeyCode.ESCAPE){
                    clip.stop();
                    clip.close(); // resource release
                }
            });
        }
        catch (Exception e){
            LoggingUtil.logError("Error loading audio file", e);
        }
    }

    public static void playAudio(String audioPath){
        File file = new File(audioPath);
        if(!file.exists()){
            LoggingUtil.logError("Sound file not found");
            return;
        }
        try{
            AudioInputStream audInput = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audInput);

            clip.start();
        }
        catch (Exception e){
            LoggingUtil.logError("Error loading audio file", e);
        }
    }

}
