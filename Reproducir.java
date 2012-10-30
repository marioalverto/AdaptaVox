
import javax.sound.sampled.*;
import java.io.*;
 
 
 
/**
 *
 * @author Jorge Valverde
 */


public class Reproducir {
  
    
    private AudioFileFormat aff;
    private AudioInputStream ais;
    private File sf;   
 


    public Reproducir(String ruta)
    {       
        sf = new File(ruta);
    }
    
       
    /*Ejecución de sonido*/
    public void playAudio()
    {
     try
     {
       aff = AudioSystem.getAudioFileFormat(sf); 
       ais = AudioSystem.getAudioInputStream(sf);
            
       AudioFormat af = aff.getFormat(); 
            
       DataLine.Info info=new DataLine.Info(Clip.class,ais.getFormat(), 
                        ((int) ais.getFrameLength()*af.getFrameSize())); 
            
       Clip audio = (Clip)AudioSystem.getLine(info);
       audio.open(ais);
       audio.loop(0);//Clip.LOOP_CONTINUOUSLY
     }   
      
   catch(UnsupportedAudioFileException ee){} 
   catch(IOException ea){} 
   catch(LineUnavailableException LUE){};                
 }
 
} 