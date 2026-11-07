package com.hancha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Pane extends JPanel implements KeyListener {

    private enum DIRECTION{
        RIGHT,LEFT,UP,DOWN
    }

    private final int FPS=10;
    private final int CELL_SIZE=20;
    private final int WIDTH=20;
    private final int HEIGHT=20;

    private final long dt=1_000_000_000/FPS;

    private final Color SNAKE_COLOR =Color.green;
    private final Color APPLE_COLOR=Color.red;
    private final Color GRID_COLOR=Color.darkGray;

    private final LinkedList<Point> snake=new LinkedList<>();
    private final Random appleGen=new Random();

    private volatile DIRECTION dir=DIRECTION.RIGHT;
    private volatile DIRECTION nextDir=DIRECTION.RIGHT;

    private Point appleLoc =null;

    public Pane(){

        init();

        loop();

    }

    private void init(){
        snake.add(new Point(WIDTH/2,HEIGHT/2));

        spawnApple();

        setPreferredSize(new Dimension(CELL_SIZE*WIDTH,CELL_SIZE*HEIGHT));
        addKeyListener(this);
        setOpaque(true);
        setFocusable(true);
        setBackground(Color.black);
    }

    private void loop(){
        new Thread(()->{
        long now=System.nanoTime();
        long next=now+dt;

        while(true){
            now=System.nanoTime();
            long delta=next-now;
            if(delta<=0) {
                update();
                repaint();

                next=now+dt;
            }
            else{
                try {
                    Thread.sleep(delta/1_000_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        }).start();

    }

    private void update(){

        dir=nextDir;

        int headX=snake.getFirst().x;
        int headY =snake.getFirst().y;

        headX+=dir.equals(DIRECTION.RIGHT)?1:dir.equals(DIRECTION.LEFT)?-1:0;
        headY +=dir.equals(DIRECTION.DOWN)?1:dir.equals(DIRECTION.UP)?-1:0;

        headX+=(headX<0)?WIDTH:0;
        headY +=(headY <0)?HEIGHT:0;

        headX%=WIDTH;
        headY %=HEIGHT;

        Point head=new Point(headX, headY);

        synchronized (snake) {

            boolean ate=head.equals(appleLoc);

            if (detectCollision(head,ate)) return;

            snake.addFirst(head);

            if (ate)
                spawnApple();
            else
                snake.removeLast();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(GRID_COLOR);
        for(int i=0;i<WIDTH;i++){
            for(int j=0;j<HEIGHT;j++){
                g.drawRect(i*CELL_SIZE,j*CELL_SIZE,CELL_SIZE,CELL_SIZE);
            }
        }

        synchronized (snake) {
            g.setColor(SNAKE_COLOR);
            for (Point p : snake)
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

            g.setColor(APPLE_COLOR);
            if (appleLoc != null)
                g.fillOval(appleLoc.x * CELL_SIZE, appleLoc.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void spawnApple(){
        do {
            int x = appleGen.nextInt(0, WIDTH);
            int y = appleGen.nextInt(0, HEIGHT);
            appleLoc = new Point(x, y);
        }while (snake.contains(appleLoc));
    }
    private boolean detectCollision(Point head,boolean ate) {
        List<Point> tempSnake=snake;
        if(!ate)
            tempSnake=snake.subList(0,snake.size()-1);
        return tempSnake.contains(head);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> nextDir = (dir==DIRECTION.LEFT) ?dir:DIRECTION.RIGHT;
            case KeyEvent.VK_LEFT  -> nextDir = (dir==DIRECTION.RIGHT)?dir:DIRECTION.LEFT;
            case KeyEvent.VK_UP    -> nextDir = (dir==DIRECTION.DOWN) ?dir:DIRECTION.UP;
            case KeyEvent.VK_DOWN  -> nextDir = (dir==DIRECTION.UP)   ?dir:DIRECTION.DOWN;
        }
    }

    //Unused
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

}
