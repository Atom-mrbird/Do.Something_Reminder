/**
 * @(#)playWave.java
 *
 * playWave utility to open .wav files
 * Modified the code from http://www.anyexample.com/programming/java/java_play_wav_sound_file.xml
 * which is under free license granted http://www.anyexample.com/license/
 * no direct credit can be given to the actual author since their name is not listed
 * A lot of the file/port i/o is done by the code given by them. The functionality and threading was done by me
 * Took out the threading so the alarm wouldn't have the errors that were coming up in the alarm clock application
 *
 * @author Chris Kurhan
 * version 1.3.1 2012/2/22
 */
package pkgdo.something.reminder;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class playWave extends Thread
{
	private static SourceDataLine auline;
    private String filename;
    private AudioInputStream audioInputStream;
    private int nBytesRead = 0;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
    private byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
    private Position curPosition;
	private BooleanControl bol;
	private static boolean playing = false;

    enum Position
    {
        LEFT, RIGHT, NORMAL
    };

    public playWave(String wavfile)
    {
        filename = wavfile;
        curPosition = Position.NORMAL;
    }

    public playWave(String wavfile, Position p)
    {
        filename = wavfile;
        curPosition = p;
    }
/**
 * the public function called to actually get and play the audio file
 * it can only play .wav files
 * it attempts to get PAN control and MUTE control over the specified audio file
 * then can be ended or muted by other functions
 *
 */
    public void run()
    {
		File soundFile = new File(filename);
		if (!soundFile.exists())
	    {
			System.err.println("Wave file not found: " + filename);
			return;
		}
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}
		catch (UnsupportedAudioFileException e1)
		{
			e1.printStackTrace();
			return;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return;
		}
		AudioFormat format = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try
		{
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		if (auline.isControlSupported(FloatControl.Type.PAN))
		{
			FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
			if (curPosition == Position.RIGHT)
				pan.setValue(1.0f);
			else if (curPosition == Position.LEFT)
				pan.setValue(-1.0f);
		}
		if ( auline.isControlSupported(BooleanControl.Type.MUTE) )
		{
			bol = (BooleanControl) auline.getControl(BooleanControl.Type.MUTE);
			if (bol.getValue())
				bol.setValue(false);
		}
		if ( !isPlaying() )
			play();
    }
/**
 * Tells the auline to stop reading and writing what it is doing
 *
 */
    public void stopPlaying()
    {
    	this.interrupt();
    	playing = false;
    	mute();
    	auline.stop();
    	return;
    }
/**
 * Tells the auline to not play any noise (mute)
 * Particularly useful for "sleep" in the alarm clock
 *
*/
    public void mute()
    {
    	bol.setValue(true);
    }
/**
 * Tells the auline to not start playing again (unmute)
 * Particularly useful for "sleep" in the alarm clock
 *
*/
    public void unmute()
    {
    	bol.setValue(false);
    }
/**
 * Returns whether or not the auline is currently playing a sound
 *
@return true if auline.start() has been called and not stopped
 */
	public boolean isPlaying()
	{
		return playing;
	}
/**
 * Called by run()
 * Tells the auline to start(). It drains and closes the auline when completed
 * Calls Alarm_Clock.adjustTime(h, m, s, false) to keep the time updated as frequently as possible
 * If this is used for other programs those three lines should be commented out
 *
 */
    private void play()
    {
    			auline.start();
			    try
		        {
			    	playing = true;
			    	//used for calling Alarm_Clock.adjustTime(h,m,s,false)
		        	//String s = "";
		        	//String m = "";
		        	//String h = "";
		        	//boolean fal = false;
		        	//loop while the auline is open and has data to be read
		            while (nBytesRead != -1)
		            {
		                //Alarm_Clock.adjustTime(s, m, h, fal);
		            	nBytesRead = audioInputStream.read(abData, 0, abData.length);
		                //Alarm_Clock.adjustTime(s, m, h, fal);
		                if (nBytesRead >= 0)
		                    auline.write(abData, 0, nBytesRead);
		                //Alarm_Clock.adjustTime(s, m, h, fal);
		            }
		        } catch (IOException e)
		        {
		            e.printStackTrace();
		            return;
		        } finally
		        {
		        	//drain and close the auline
		        	playing = false;
		            auline.drain();
		            auline.close();
		        }
    }
}