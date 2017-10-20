/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Classe aprimorada por RUAN GABRIEL G. BARROS - KOULIKOV SYSTEMS
 */
package mild.katyusha.system.ctr;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Koulikov
 */
public class CTRAudio {

    public static void play(String filename) {
        try {
            Clip clip = AudioSystem.getClip(null);
            clip.open(AudioSystem.getAudioInputStream(new CTRAudio().getClass().getResource(filename)));
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException exc) {
            exc.printStackTrace(System.out);
        }
    }

    public static void playExternal(String filename) {
        try {
            Clip clip = AudioSystem.getClip(null);
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException exc) {
            exc.printStackTrace(System.out);
        }
    }

    public static int getDurationOf(String filename) {
        try {
            Clip clip = AudioSystem.getClip(null);
            clip.open(AudioSystem.getAudioInputStream(new CTRAudio().getClass().getResource(filename)));
            return (((int) clip.getMicrosecondLength()) / 1000);

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException exc) {
            exc.printStackTrace(System.out);
        }
        return 0;
    }
}
