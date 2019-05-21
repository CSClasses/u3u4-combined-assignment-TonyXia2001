/*
Unit 3&4 Assignment 1 Question 1
Programmer: Tony Xia
Date: 5/15/2019
Modifications: None
Description: The following program reads data in and displays the weather data
*/

import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Weather {
    private static Input db;
    private static JFrame GUI;
    private static JPanel query;
    private static JPanel result;
    public static void main(String[] args) {
        // read data
        db = new Input("eng-climate-summaries-All-4,2018.csv");

        // GUI
        GUI = new JFrame("Weather");

        // go to the query page
        query = queryPage();
        GUI.setContentPane(query);
        GUI.setSize(640, 360);
        GUI.setVisible(true);
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static JPanel queryPage() {
        // the entire panel -> instruction + inputPane + submit button
        // instruction
        JPanel query = new JPanel(new GridLayout(3, 1, 0, 10));
        JPanel textBox = new JPanel();
        textBox.add(new JLabel("Please specify the statistic and the location you want to look up."));

        // input pane
        JPanel inputPane = new JPanel(new FlowLayout());

        // a new panel to contain the location dropbox
        JPanel locationBox = new JPanel();
        JComboBox locationDrop = new JComboBox(db.locations);
        locationBox.add(new JLabel("Location: "));
        locationBox.add(locationDrop);
        inputPane.add(locationBox);

        // a new panel to contain the statistic dropbox
        JPanel statsBox = new JPanel();
        JComboBox statDrop = new JComboBox(db.headersArr);
        statsBox.add(new JLabel("Statistic: "));
        statsBox.add(statDrop);
        inputPane.add(statsBox);

        JPanel buttonBox = new JPanel();
        JButton submit = new JButton("Submit");
        // when the button is clicked, switch to the result page.
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get the selected items
                String loc = (String) locationDrop.getSelectedItem();
                String stat = (String) statDrop.getSelectedItem();

                // create the result page
                result = resultPage(loc, stat);
                // switch to the result page
                GUI.setContentPane(result);
                GUI.revalidate();
                GUI.repaint();
            }
        });

        buttonBox.add(submit);

        query.add(textBox);
        query.add(inputPane);
        query.add(buttonBox);
        return query;
    }

    private static JPanel resultPage(String loc, String stat) {
        result = new JPanel(new GridLayout(3, 1, 0 ,10));

        // instruction
        JPanel textBox = new JPanel();
        textBox.add(new JLabel("Here's the weather data you requested for."));
        result.add(textBox);

        // result display
        JPanel textBox1 = new JPanel();

        // if the value is "", set it to unknown,
        // if it's "NA", set it to not applicable,
        String value;
        if (db.data.get(loc)[db.headers.get(db.legend1.get(stat))].equals("")) {
            value = "unknown";
        } else if (db.data.get(loc)[db.headers.get(db.legend1.get(stat))].equals("NA")) {
            value = "not applicable";
        } else {
            value = db.data.get(loc)[db.headers.get(db.legend1.get(stat))];
        }
        textBox1.add(new JLabel(loc + "'s " + stat + " is " + value + "."));

        // set up the button for the user to go back.
        JPanel buttonBox = new JPanel();
        JButton goBack = new JButton("Back");
        buttonBox.add(goBack);
        goBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI.setContentPane(query);
                result.removeAll();
                GUI.revalidate();
                GUI.repaint();
            }
        });

        // add all the components to the result page
        result.add(textBox1);
        result.add(buttonBox);
        return result;
    }
}

// stores all the data input from the user
// legend: short->full
// legend1: full->short
// headers: short->indexOfTheData
// headersArr: array of header(full)
// data: all the data excluding the headers
// locations: a vector of locations
class Input {
    private final int columnNum = 25;
    public HashMap<String, String> legend = new HashMap<String, String>();
    public HashMap<String, String> legend1 = new HashMap<String, String>();
    public HashMap<String, Integer> headers = new HashMap<String, Integer>();
    public String[] headersArr;
    public HashMap<String, String[]> data = new HashMap<String, String[]>();
    public Vector<String> locations = new Vector<String>();
    private Scanner sc;

    public Input(String fileName) {
        try {
            sc = new Scanner(new File(fileName));
            readFile();
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("nonono");
        }
    }

    private void readFile() {

        // skip to useful info
        String nextLine = "foobarbaz";
        while (!nextLine.equals("\"Legend\""))  {
            nextLine = sc.nextLine();
        }
        readLegends();
        readHeaders();
        readData();
    }

    private void readLegends() {
        String nextLine = sc.nextLine();    // skip the empty line
        while (!nextLine.equals("")) {
            int mid = nextLine.indexOf(',');    // find the middle

            //store both the full->short version and the short->full version
            legend.put(nextLine.substring(1, mid - 1), nextLine.substring(mid + 2, nextLine.length() - 1));
            legend1.put(nextLine.substring(mid + 2, nextLine.length() - 1), nextLine.substring(1, mid - 1));

            nextLine = sc.nextLine();
        }
    }

    private void readHeaders() {
        String lnOfHeaders = sc.nextLine(); // read in the line of headers
        String[] arrHeaders = lnOfHeaders.split(",", 0);    // split it into individual items
        // since we don't need the first header(Location), we give it an index of -1
        for (int i = -1; i < columnNum - 1; i++) {
            headers.put(stripQ(arrHeaders[i + 1]), i);
        }
        // make an array that stores all the headers in their full form
        headersArr = new String[columnNum - 1];
        for (int i = 0; i < columnNum - 1; i++) {
            headersArr[i] = legend.get(stripQ(arrHeaders[i + 1]));  // +1 to skip the first header
        }
        Arrays.sort(headersArr);
    }

    private void readData() {
        String ln;
        while (sc.hasNextLine()) {
            ln = sc.nextLine();

            // find the first comma to separate the location info from the rest
            int firstcomma = ln.indexOf(",");
            String key = stripQ(ln.substring(0, firstcomma));

            // the locations are the keys of the maps
            locations.add(key);

            // split the rest of the line into individual items.
            ln = ln.substring(firstcomma + 1);
            String[] arrLn = ln.split(",", 0);

            // strip the quotation marks off each data value and store them in the map
            for (int i = 0; i < arrLn.length; i++) {
                arrLn[i] = stripQ(arrLn[i]);
            }
            data.put(key, arrLn);
        }
        Collections.sort(locations);
    }

    // this function strips quotation marks off data values
    private String stripQ(String str) {
        if (str.length() != 0 && str.charAt(0) == '"') {
            return str.substring(1, str.length() - 1);
        } else {
            return str;
        }
    }
}
