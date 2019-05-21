/*
Unit 3&4 Assignment 1 Question 5
Programmer: Tony Xia
Date:
Modifications: None
Description: The following program converts a file into a txt file.
*/

import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class CodeCopier {
    private static JFrame GUI;
    public static void main(String[] args) {

        GUI = new JFrame();
        init();
        GUI.setSize(640, 360);
        GUI.setVisible(true);
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void init() {
        JPanel firstpage = new JPanel(new GridLayout(3, 1, 0, 10));

        JPanel textBox = new JPanel();
        textBox.add(new JLabel("Please provide the name of the file you want to copy."));

        // input pane
        JTextField sourceInput = new JTextField();
        sourceInput.setPreferredSize(new Dimension(300, sourceInput.getPreferredSize().height));
        JPanel nameBox = new JPanel();
        nameBox.add(new JLabel("Name: "));
        nameBox.add(sourceInput);

        JPanel warningBox = new JPanel();
        JLabel warning = new JLabel("");
        warningBox.add(warning);
        textBox.add(warningBox);

        JPanel buttonBox = new JPanel();
        JButton copybutton = new JButton("copy");
        copybutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = sourceInput.getText();
                File inFile = new File(fileName);
                if (inFile.exists() && !inFile.isDirectory()) {
                    copy(fileName);
                } else {
                    warning.setText("File not Found!");
                    GUI.revalidate();
                    GUI.repaint();
                }
            }
        });
        buttonBox.add(copybutton);

        firstpage.add(textBox);
        firstpage.add(nameBox);
        firstpage.add(buttonBox);
        GUI.setContentPane(firstpage);
    }

    private static void copy(String fileName) {
        int ex = fileName.lastIndexOf('.');
        File inFile = new File(fileName);
        try {
            PrintWriter writer = new PrintWriter(fileName.substring(0, ex) + ".txt");
            Scanner sc = new Scanner(inFile);
            while (sc.hasNextLine()) {
                writer.write(sc.nextLine() + "\n");
            }
            writer.flush();
            writer.close();
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("nono");
            return;
        }
    }
}
