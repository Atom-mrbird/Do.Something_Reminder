/**
 * @(#)Alarm_Clock.java
 *
 * Alarm Object that interacts with the playWave class
 * Now supports 12 and 24 hour clock alarms
 *
 * @author Chris Kurhan
 * @version 1.4 2012/2/22
 */
package pkgdo.something.reminder;
import java.io.File;
public class Alarm
{
	//declare class variables
	private static String alarmTime = "-1";
	private String formatAlarmTime = "-1";
	//uses playWave.java to open and play .wav files
	private static playWave trigger = new playWave("default_alarms" + File.separator + "alarm1.wav");
	private boolean playFile;

	public Alarm(String h, String m)
	{
		//set alarm time given hours and minutes
		alarmTime = h + ":" + m;
		playFile = false;
	}
	public Alarm(int h, int m, boolean ampm, String amorpm)
	{
		playFile = false;
		String hour = "";
		String minute = "";
		if ( amorpm.equals("pm") && ampm)
		{
			//if it's pm
			if ( h != 12 )
				h += 12;
			hour = h + "";
			if ( m < 10 )
				minute = "0" + m;
			else
				minute = m + "";
			if ( h > 23 || m > 59 || h < 0 || m < 0)
				alarmTime = "-1";
			else
			{
				alarmTime = h + ":" + minute;
				formatAlarmTime = h-12 + ":" + minute + " pm";
			}
		}
		else if ( amorpm.equals("am") && ampm )
		{
			hour = h + "";
			if ( m < 10 )
				minute = "0" + m;
			else
				minute = m + "";
			if ( h > 23 || m > 59 || h < 0 || m < 0)
				alarmTime = "-1";
			else
			{
				alarmTime = hour + ":" + minute;
				formatAlarmTime = hour + ":" + minute + " am";
			}
		}
		else
		{
			if ( h < 10 )
			{
				hour = "0" + h;
			}
			else
			{
				hour = h + "";
			}
			if ( m < 10 )
			{
				minute = "0" + m;
			}
			else
			{
				minute = m + "";
			}
			alarmTime = h + ":" + minute;
			formatAlarmTime = alarmTime;
		}

	}
	public Alarm(String time, boolean ampm, String amorpm)
	{
		playFile = false;
		//set alarm time given a string of format HH:MM otherwise it will not work
		if (ampm && amorpm.equalsIgnoreCase("pm") && (Integer.parseInt(time.substring(0,2)) != 12) )
		{
			int h = Integer.parseInt(time.substring(0,2));
			int m = Integer.parseInt(time.substring(3,5));
			h += 12;
			alarmTime = "" + h + ":" + m;
			String a;
			if ( m < 10 )
			{
				formatAlarmTime = "" + (h-12) + ":" + m + "0 " + amorpm;
			}
			else
			{
				formatAlarmTime = "" + (h-12) + ":" + m + " " + amorpm;
			}

		}
		else
		{
			alarmTime = time;
			formatAlarmTime = alarmTime;
		}
	}
	public Alarm()
	{
		//sets default alarm time of -1
		alarmTime = "-1";
	}
	/* The new way to check if alarm should be going off instead of using theTime.equals(Alarm.alarmTime)
	 * This function checks the current time (in 24 or 12 hour clock) and checks it against
	 * the current alarm that is set. This allows for checking 12 and 24 hour clocks without needing to
	 * make the user enter in the format of the clock they are currently looking
	 *
	 *@param currentTime	The current time in String format
	 *
	 */
	public static boolean checkAlarm(String currentTime)
	{
		//strings to get information
		String h;
		String m;
		String amorpm = "";
		String alarmz = "";
		int hour;
		int minute;
		//get the index of colon so we can check
		int index = currentTime.indexOf(":");
		h = currentTime.substring(0,index);
		int index2 = currentTime.indexOf(':', index+1);
		m = currentTime.substring(index+1, index2);
		if ( currentTime.indexOf('p') != -1 )
		{
			amorpm = "pm";
		}
		//now we have hours, minutes, and the am/pm number
		hour = Integer.parseInt(h);
		if ( amorpm.equals("pm") && hour != 12 )
		{
			hour += 11;
			h = hour + "";
		}
		alarmz = h + ":" + m;
		//System.out.println("Alarm Time: " + alarmTime);
		//System.out.println("Alarmz: " + alarmz);
		if ( alarmz.equals(alarmTime) )
		{
			return true;
		}
		return false;
	}
/**
 * Triggers the alarm to go off (plays the file at fileLocation
 * unless it is already playing, in which case nothing happens
 *
@param  fileLocation	the location of the .wav file to play
 *
 */
	public void triggerAlarm(String fileLocation)
	{
		//create new playWave using the file given in Alarm_Clock
		trigger = new playWave(fileLocation);
		//tell playWave to play the file
		trigger.run();
		//end if the file is done playing
		if ( !trigger.isPlaying() )
			return;
	}
	public String getAlarm()
	{
		return alarmTime;
	}
	public String getFormatAlarm()
	{
		return formatAlarmTime;
	}
/**
 * Tells the playWave object to stop playing the file
 *
 */
	public void stopAlarm()
	{
		trigger.stopPlaying();
		playFile = false;
		return;
	}
/**
 * Tells the playWave object to mute or unmute
@param  mute if true, mute the playWave object, if false, unmute the playWave object
 *
 */
	public void mute(boolean mute)
	{
		if (mute)
		{
			trigger.mute();
		}
		else
			trigger.unmute();
	}

/**
 * Tells the Alarm_Clock if there is sound playing currently on trigger
 *
 */
	public boolean isPlaying()
	{
		return trigger.isPlaying();
	}
 }
