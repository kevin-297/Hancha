package com.hancha;

import javax.swing.*;

public class Main extends JFrame {

    private Pane pane=new Pane();

    public Main(){

        add(pane);
        pack();

        pane.requestFocusInWindow();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()->new Main().setVisible(true));
    }

}
