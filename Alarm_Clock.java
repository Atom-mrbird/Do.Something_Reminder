package pkgdo.something.reminder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.util.Calendar;
import java.awt.Color;
import java.io.File;

public class Alarm_Clock extends JFrame
{
	//UI components used in inner methods (like actionPerformed(ActionEvent))
	private static final JFrame backFrame = new JFrame("Alarm Clock");
    protected static final JLabel time = new JLabel("00:00:00");
    protected static JButton set = new JButton("Set Alarm");
    private static JButton snooze = new JButton("Snooze");
    //Color variables used so don't have to make them several times (ram save)
	protected static Color alarmIsSet = new Color(0, 255, 25);
	protected static Color alarmNotSet = new Color(0,0,0);
	protected static Color alarmGoing = new Color(255,25,25);
	//booleans used in loops to make sure the time adjustment doesn't call Alarm methods several times
    protected static boolean alarmGoOff = false;
    protected static boolean playingAlready = false;
    private static boolean military = true;
    //declare Alarm object for use in inner methods
	private static Alarm anAlarm;
	//hold string location for use in inner methods, set default to be alarm1.wav in the default_alarms folder
	private static String fileLocation = "default_alarms" + File.separator + "newalarm1.wav";
	//hold time when snooze button was pressed
	private static String timeSnoozePressed = "-1";
	private static boolean snoozePressed = false;
    private static long SLEEP_TIME;

    public Alarm_Clock()
    {
    	//set up UI
		backFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//make JPanels
		JPanel backPane = new JPanel();
    	JPanel timeHold = new JPanel();
    	JPanel bottom = new JPanel();
    	JPanel vBottom = new JPanel();
    	//configure layout managers for panels
    	backPane.setLayout(new BoxLayout(backPane, BoxLayout.Y_AXIS));
    	bottom.setLayout(new GridLayout(1,2,5,0));
    	vBottom.setLayout(new GridLayout(1,1,3,3));
    	//create non static buttons
    	JButton off = new JButton("Turn Off");
		//Set mnemonics
		off.setMnemonic(KeyEvent.VK_O);
		set.setMnemonic(KeyEvent.VK_A);
		snooze.setMnemonic(KeyEvent.VK_S);
		//Configure JLabel holding the time
		time.setFont(new Font("DejaVu Sans", Font.BOLD, 36));
    	timeHold.add(time);
    	//set up menu bar and menu items
    	JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu;
		JMenu helpMenu;
		JMenuItem helpMenuItem;
		JMenuItem fileMenuItem;
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		//set Mnemonics
		fileMenu.setMnemonic('F');
		helpMenu.setMnemonic('H');
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		helpMenuItem = new JMenuItem("About");
		helpMenuItem.addActionListener(new ActionListener()
		{
                    private String VERSION_NUMBER;
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(backFrame,
					"This program was created by Chris Kurhan and Lance Storey\n"
					+"It can only open .wav files to use as Ringers for the alarm\n"
					+"Current Version: " + VERSION_NUMBER + "\n"
					+"View the readme.txt for updates and changes\n"
					+"The original audio reader can be found at\nhttp://www.anyexample.com/programming/java/java_play_wav_sound_file.xml \n"
					+"This program uses a modified version of that audio player\n\n"
					+"Copyright (c) 2011 Chris Kurhan, Lance Storey\n"
					+"Permission is hereby granted, free of charge, to any person obtaining a copy of this software\n"
					+"and associated documentation files (the \"Software\"), to deal in the Software without restriction,\n"
					+"including without limitation the rights to use, copy, modify, merge, publish, distribute,\n"
					+"sublicense, and/or sell copies of the Software, and to permit persons to whom the Software\n"
					+"is furnished to do so, subject to the following conditions:\n"
					+"The above copyright notice and this permission notice shall be included in all copies or\n"
					+"substantial portions of the Software.\n\n"
					+"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,\n"
					+"INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR\n"
					+"PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE\n"
					+"FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,\n"
					+"ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n");
			}
		});
		helpMenu.add(helpMenuItem);
		helpMenuItem = new JMenuItem("Help");
		helpMenuItem.addActionListener(new ActionListener()
		{
                    private String VERSION_NUMBER;
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(backFrame,
					"List of what each button does and how to use it:\n"
					+"  -  Set Alarm/Change Alarm: Press this button and a new\n"
					+"window will appear. Enter in the time you want the alarm\n"
					+"to go off in an HH:MM format, that is, if you want it to go\n"
					+"off at 8:00am enter 08:00 into the field then press the Set Alarm button\n"
					+"Note that it is a 24 hour clock so 22:00 is 10:00pm\n"
					+"\n"
					+"  -  Turn Off button: Turns off the Alarm. This completely disables the\n"
					+"alarm and makes the program forget that it exists. Use this after\n"
					+"you don't want that alarm to go off again until you reset it\n"
					+"\n  -  Snooze button: Turns off the Alarm for 5 minutes. Once 5 minutes\n"
					+"have passed the alarm will begin playing again\n"
					+"\n  -  File>Cancel Alarm: This makes it so the alarm is no longer set\n"
					+"before the alarm goes off. This is used if you made a mistake\n"
					+"entering the alarm or you no longer need the alarm\n"
					+"\n  -  File>Set Alarm Ringer: This opens a new window that allows you\n"
					+"to choose which file you want to play as a ringer. There are\n"
					+"two default ringers in the folder you will see once it opens.\n"
					+"However, you can navigate to any directory and open any .wav\n"
					+"file to use. The program WILL NOT ALLOW YOU TO OPEN ANY OTHER\n"
					+"KIND OF FILE OTHER THAN .wav\n"
					+"\n  -  File>Exit Program: Will exit the program and cancel any alarm\n"
					+"that is currently set.\n\n"
					+"\nWhat do the text colors mean?\n"
					+"  -  Black: No alarm is set, just displaying the current system time\n"
					+"  -  Green: An alarm is set (look at the title bar for the alarm set time\n"
					+"  -  Red: The alarm is going off, press Snooze or Turn off to stop\n"
					+"  -  Dark Red: The alarm is snoozed, it will play again soon.\n"
					+"Current Alarm Clock version: " + VERSION_NUMBER);
			}
		});
		helpMenu.add(helpMenuItem);
		fileMenuItem = new JMenuItem("Cancel Alarm");
		fileMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				backFrame.setTitle("Alarm Clock");
				anAlarm = new Alarm();
			}
		});
		fileMenu.add(fileMenuItem);
		JMenuItem toggleFormat = new JMenuItem("Toggle 'Military time'");
		toggleFormat.setMnemonic('T');
		toggleFormat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				military = !military;
			}
		});
		fileMenuItem = new JMenuItem("Set Alarm Ringer");
		fileMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//find "ringers" .wav files to use
				File randomFile;
				final JFileChooser chooser = new JFileChooser();
				try
				{
					//create a file to get current directory and navigate to the default_alarms page
					randomFile = new File(new File(".").getCanonicalPath() + File.separator +"default_alarms");
					chooser.setCurrentDirectory(randomFile);
					chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				}
				catch(Exception ex)
				{
					//catch if you can't make the file or something went wrong
					System.err.println("Could not create file at current path");
					ex.printStackTrace();
				}
				int number = chooser.showOpenDialog(null);
				if (number == JFileChooser.APPROVE_OPTION)
				{
					//if you select a file
					File loadMe = chooser.getSelectedFile();
					//set fileLocation to that file's location if it is a valid format
					if (loadMe.getName().contains(".wav"))
						fileLocation = loadMe.getPath();
					else
					{
						//display a dialog saying that we can't open that file if it is not a .wav
						JOptionPane.showMessageDialog(backFrame, "Please select a .wav (Wave) file to set as your alarm.");
					}
					//System.out.println(loadMe.getPath());
				}
			}
		});
		//add the set alarm ringer fileMenuItem to the file Menu
		fileMenu.add(fileMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(toggleFormat);
		fileMenu.addSeparator();
		fileMenuItem = new JMenuItem("Exit Program");
		fileMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//if you click Exit program... the program exits
				System.exit(0);
			}
		});
		//add everything together
    	backPane.add(timeHold);
		fileMenu.add(fileMenuItem);
    	bottom.add(set);
    	bottom.add(off);
    	vBottom.add(snooze);
    	backPane.add(timeHold);
    	backPane.add(bottom);
    	backPane.add(vBottom);
    	backFrame.add(backPane);
        
    	//add the JMenuBar so you can use it
		backFrame.setJMenuBar(menuBar);
		//set size to be the default desired size
    	backFrame.setPreferredSize(new Dimension(290, 175));
    	//pack it up so it's actually that size
    	backFrame.pack();
    	//set location to the middle of the screen
    	backFrame.setLocationRelativeTo(null);
    	//trying to set look and feel
    	//make it all visible so it's interactable
    	backFrame.setVisible(true);
        
    	backFrame.setResizable(false);
        
    	//end set up UI
        
    	//add action listener(s)
    	set.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    			//set up the set alarm window
    			final JFrame back = new JFrame("Set Alarm");
    			//make it so the jframe is disposed on close instead of ending program
    			back.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    			JPanel backPanel = new JPanel();
    			JPanel top = new JPanel();
    			JPanel bottom = new JPanel();
    			backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
    			final JLabel info = new JLabel("Input the alarm time below.<br>Either 12 hour clock(including am/pm)<br>or 24 hour clock times are valid");
    			JButton submit = new JButton("Set Alarm");
    			JButton cancel = new JButton("Cancel");
    			final JTextField input = new JTextField("");
				bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
				top.setLayout(new GridLayout(2,1,0,5));
				//add everything
    			top.add(info);
    			top.add(input);
    			bottom.add(submit);
    			bottom.add(cancel);
    			backPanel.add(top);
    			backPanel.add(bottom);
    			back.add(backPanel);
    			//grab location of the main JFrame
    			Point p = new Point(backFrame.getLocationOnScreen());
    			back.pack();
    			//set this new window's location to be on top of the old window's location
    			back.setLocation(p);
    			back.setResizable(false);
    			back.setVisible(true);
    			//set Mnemonic
    			submit.setMnemonic(KeyEvent.VK_S);
    			cancel.setMnemonic(KeyEvent.VK_C);
				//end set up set alarm window
				if (alarmGoOff)
				{
					info.setText("Please stop the alarm that is going off before you try to set a new one");
					back.pack();
					return;
				}
				//add action listeners
				cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						back.dispose();
					}
				});
				submit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent a)
					{

						if (!setAlarm(input.getText()) == true)
						{
							//if they messed up on entering it, display this new text reminding them to put it in the HH:MM format
							info.setText("Please make sure you enter a valid time. (i.e. 13:00pm is not a valid time)");
							//re pack to make sure the size is right for the new text
							back.pack();
						}
						else
						{
							//when they get it right, dispose of this window, we don't need it anymore
							back.dispose();
						}
					}
				});
    		}
    	});
    	off.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    			//when they click Turn off Alarm
    			//if one's not set, don't do anything because you don't need to turn it off
    			if (!alarmGoOff)
    				return;
    			alarmGoOff = false;
    			//call stopAlarm (more OOP-y)
    			stopAlarm();
    		}
    	});
    	snooze.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    			//when they press snooze
    			//make sure the alarm is actually playing
    			if (alarmGoOff)
    			{
    				//say they pressed snooze
    				snoozePressed = true;
    				//get the current time
    				//make sure we're not playing anymore
    				playingAlready = false;
    				timeSnoozePressed = time.getText().substring(0,5);
    				anAlarm.mute(true);
    			}
    		}
    	});

		//main meat of program
		anAlarm = new Alarm();
                this.getContentPane().add(backFrame);
    	try
    	{
    		while(true)
    		{
    			//try to adjust the time then sleep 1 second so it doesn't eat up all processing power finding out what time it is
				new Thread(new Runnable()
				{
					public void run()
					{
						adjustTime();
					}
				}).start();
				Thread.sleep(SLEEP_TIME);
    		}
    	}catch(InterruptedException e)
		{
			//if it messes up on the sleep, throw the exception
			System.err.println("" + e.toString());
    	}

    } //end main

/**
 * Adjusts the JLabel's text to be the current system time
 * It displays in an HH:MM:SS format or, now has a 12 hour clock available. so HH:MM:SS am/pm is available
 * Used by Alarm_Clock only
 *
 */
	private static void adjustTime()
	{
		String s = "";
		String h = "";
		String m = "";
		String theTime = "";
		boolean yes = false;
	    Calendar rightNow = Calendar.getInstance();
		//format text to show HH:MM:SS instead of maybe (worst case) H:M:S (i.e. 5:2:9 5am 2 minutes 9 seconds)
		//if the seconds are less than 10, add a 0 in front of it
		if (military)
		{
			if ( rightNow.get(Calendar.SECOND) < 10 )
			{
				s = ":0" + rightNow.get(Calendar.SECOND);
			  	yes = true;
			}
			else
				s = ":" + rightNow.get(Calendar.SECOND);
			//if the minutes are less than 10, add a 0 in front of it
			if (rightNow.get(Calendar.MINUTE) < 10)
			{
			    m = ":0" + rightNow.get(Calendar.MINUTE);
			    yes = true;
			}
			else
			    m = ":" + rightNow.get(Calendar.MINUTE);
			//if the hours are less than 10, add a 0 in front of it
			if (rightNow.get(Calendar.HOUR_OF_DAY) < 10)
			{
			 	h = "0" + rightNow.get(Calendar.HOUR_OF_DAY);
			   	yes = true;
			}
			else
			    h = "" + rightNow.get(Calendar.HOUR_OF_DAY);
			//if everything is beautiful and > 10, display just the numbers the system clock says
			if (yes == false)
			{
			   	theTime = rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE) + ":" + rightNow.get(Calendar.SECOND);
			}
			else
				theTime = h + m + s;
		}
		else
		{
			String ampm = " am";
			int time = rightNow.get(Calendar.HOUR_OF_DAY);
			if ( time > 12 )
			{
				time -= 12;
				ampm = " pm";
				if ( time < 10)
					h = "0" + time;
				else
					h = "" + time;
			}
			else
			{
				if ( time == 12 )
					ampm = " pm";
				if ( time < 10)
					h = "0" + time;
				else
					h = "" + time;
			}
			time = rightNow.get(Calendar.MINUTE);
			if ( time < 10 )
				m = ":0" + time;
			else
				m = ":" + time;
			time = rightNow.get(Calendar.SECOND);
			if ( time < 10)
				s = ":0" + time;
			else
				s = ":" + time;
			theTime = h + m + s + ampm;
		}
		//update the time on the JLabel
		time.setText(theTime);
		//convert theTime back to military so we can use it in these checks
		if ( rightNow.get(Calendar.SECOND) < 10 )
		{
			s = ":0" + rightNow.get(Calendar.SECOND);
		  	yes = true;
		}
		else
			s = ":" + rightNow.get(Calendar.SECOND);
		//if the minutes are less than 10, add a 0 in front of it
		if (rightNow.get(Calendar.MINUTE) < 10)
		{
		    m = ":0" + rightNow.get(Calendar.MINUTE);
		    yes = true;
		}
		else
		    m = ":" + rightNow.get(Calendar.MINUTE);
		//if the hours are less than 10, add a 0 in front of it
		if (rightNow.get(Calendar.HOUR_OF_DAY) < 10)
		{
		 	h = "0" + rightNow.get(Calendar.HOUR_OF_DAY);
		   	yes = true;
		}
		else
		    h = "" + rightNow.get(Calendar.HOUR_OF_DAY);
		//if everything is beautiful and > 10, display just the numbers the system clock says
		if (yes == false)
		{
		   	theTime = rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE) + ":" + rightNow.get(Calendar.SECOND);
		}
		else
			theTime = h + m + s;

		//continue with checks
		//if there is an alarm set and an alarm is not going off
		//anAlarm.checkAlarm(theTime);
		if ( !anAlarm.getAlarm().equals("-1") && !theTime.equals(anAlarm.getAlarm() + ":00") && alarmGoOff == false)
		{
			//set the JButton's text to be "Change Alarm" instead of "Set Alarm" since there already is an alarm
			time.setForeground(alarmIsSet);
			set.setText("Change Alarm");
		}
		else if (anAlarm.getAlarm().equals("-1") )
		{
			time.setForeground(alarmNotSet);
			set.setText("Set Alarm");
		}
		if (alarmGoOff == true)
		{
			if ( !anAlarm.isPlaying())
			{
				anAlarm.triggerAlarm(fileLocation);
			}
			playingAlready = true;
			return;
		}
		if ( Alarm.checkAlarm(theTime) && alarmGoOff == false)
		{
			alarmGoOff = true;
			time.setForeground(alarmGoing);
			anAlarm.triggerAlarm(fileLocation);
		}
		else
				return;
		return;
	}
/**
 * NEW
 * Now accepts any valid time (i.e. 3pm sets alarm to 15:00 and displays 3:00pm as alarm set)
 * Sets the alarm time (in 24 hour format for use and 12 hour [for display] depending on how it was input)
 * Returns false if we couldn't determine what time they really meant.

@param  alarmTime	a string that holds the desired alarm time in HH:MM time
 *
 *
@return      if the operation was completed successfully
 *
 */
 	private static boolean setAlarm(String alarmTime)
 	{
 		boolean ampm = false;
 		String amorpm = "am";
 		String h = "";
 		String m = "";
 		boolean colon = false;
 		char[] ar = alarmTime.toCharArray();
 		//get the character array and search for what we want
 		for(int i = 0; i<ar.length; i+=1)
 		{
 			//System.out.println(Character.isDigit(ar[i]) + ", character " + ar[i] + " is digit" );
 			if ( ar[i] == ':')
 			{
 				//flip flag for colon
 				colon = true;
 				continue;
 			}
 			if ( Character.isDigit(ar[i]) && !colon )
 			{
 				if ( Character.isDigit(ar[i+1]) )
 				{
 					h = ar[i] + "" +  ar[i+1];
 					i += 1;
 				}
 				else
 				{
 					h = ar[i] + "";
 				}
 				//System.out.println(h);
 				continue;
 			}
 			if ( Character.isDigit(ar[i]) && colon)
 			{
 				if ( Character.isDigit(ar[i+1]) )
 				{
 					m = ar[i] + "" +  ar[i+1];
 					i += 1;
 				}
 				else
 				{
 					m = ar[i] + "";
 				}
 				//System.out.println(m);
 				continue;
 			}
 			if ( ar[i] == 'a' || ar[i] == 'A' || ar[i] == 'p' || ar[i] == 'P')
 			{
 				ampm = true;
 				amorpm = "" + ar[i] + ar[i+1];
 				break;
 			}
 		}
 		//if we don't have a minute given, then we set it to be 0 (i.e. they input 5am)
 		int minute;
 		int hour;
 		try
 		{
 			minute = Integer.parseInt(m);
 		}
 		catch (Exception e)
 		{
 			minute = 00;
 		}
 		try
 		{
 			hour = Integer.parseInt(h);
 		}
 		catch (Exception e)
 		{
 			hour = 00;
 		}
 		if ( hour > 12 && ampm )
 		{
			return false;
 		}
 		if ( hour > 23 )
 		{
 			return false;
 		}

 	//	System.out.println(hour + " hours, " + minute + " minutes, " + ampm + " am or pm was read, " + amorpm);

 		anAlarm = new Alarm(hour, minute, ampm, amorpm);
 		backFrame.setTitle("Alarm: " + anAlarm.getFormatAlarm());
 		return true;

 	}
/**
 * Returns all the UI components to their default "alarm not set" positions
 * and sets the "anAlarm" variable to be "-1", the default value for no alarm set
 *
 *
 */
	public static void stopAlarm()
	{
		//reset the JLabel's text to the default (black) color
		time.setForeground(new Color(0,0,0));
		//reset the backFrame's title to say Alarm Clock
		backFrame.setTitle("Alarm Clock");
                
		//reset the Set Alarm button's text to reflect that no alarm is set
		set.setText("Set Alarm");
		set.setEnabled(true);
		anAlarm.mute(true);
		//reset the anAlarm to be = "-1"
		anAlarm = new Alarm();
		playingAlready = false;
		//reset snoozePressed flag
		snoozePressed = false;
		return;
	}

	public static String getMediaFile()
	{
		return fileLocation;
	}

}