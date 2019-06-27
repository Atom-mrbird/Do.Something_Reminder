/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgdo.something.reminder;
import java.awt.Desktop;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

/**
 *
 * @author ASUS
 */
public class DoSomethingReminder extends JFrame {
JPanel pnl;
JButton add;
JTextArea area;
JTextArea area2;
JButton alarm;
JButton yeninotyaz;
JFileChooser fc;
private static int sayac = 0;
JLabel alarmstat;
JLabel alarmresult;
JLabel hour;
JLabel minute;
JLabel second;
    public DoSomethingReminder(){
        pnl = new JPanel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        area = new JTextArea(20,20);
        JScrollPane pane = new JScrollPane(area);
        area2 = new JTextArea(20,20);
        area2.setEditable(false);
        JScrollPane pane2 = new JScrollPane(area2);
        add = new JButton("kaydet");
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                StringBuilder strbldr = new StringBuilder();
                strbldr.append("------------------------------\n");
                strbldr.append(" ").append(area.getText()).append("\n");
                area2.setText(strbldr.toString());
                String oku = String.valueOf(area2.getText());
                
                File f = new File("C:\\Users\\ASUS\\Desktop\\belgeler\\Do.Something Reminder\\save place",sayac+"kayıt.rtf");
                try
                {
                    try (FileWriter yazdir = new FileWriter(f)) {
                        yazdir.write(oku);
                    }
                    char yazi[] = new char[(int) oku.length()];
                    FileReader f2 = new FileReader(f);
                    f2.read();
                    String okunan = new String(yazi);
                    System.out.println(okunan);
                    sayac++;
                }
                catch (IOException e)
                {
                }
            }
        });
        fc = new JFileChooser("C:\\Users\\ASUS\\Desktop\\belgeler\\Do.Something Reminder\\save place");
        fc.addActionListener((ActionEvent ae) -> {
            File file = new File("C:\\Users\\ASUS\\Desktop\\\\belgeler\\Do.Something Reminder\\save place\\");
            Desktop desk = Desktop.getDesktop();
            try {
                desk.open(fc.getSelectedFile());
            } catch (IOException ex) {
                Logger.getLogger(DoSomethingReminder.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        alarm = new JButton("alarm");
        alarm.addActionListener((ActionEvent ae) -> {
            Alarm_Clock frme = new Alarm_Clock();
            frme.setVisible(true);

        });
        yeninotyaz = new JButton("yeni not yaz");
        yeninotyaz.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                area.setText("");
            }
        });
        pnl.add(pane);
        pnl.add(add);
        pnl.add(yeninotyaz);
        pnl.add(pane2);
        pnl.add(alarm);
        pnl.add(fc);
        this.getContentPane().add(pnl);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        DoSomethingReminder frm = new DoSomethingReminder();
        frm.setSize(1000,1000);
        frm.setVisible(true);
    }
}