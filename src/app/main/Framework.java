
package app.main;

import app.input.KeyboardInput;
import app.state.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


public class Framework extends JFrame implements Runnable {

    private static final DisplayMode POSSIBLE_MODES[] = {
            new DisplayMode(800, 600, 16, 0),
            new DisplayMode(800, 600, 32, 0),
            new DisplayMode(800, 600, 24, 0),
            new DisplayMode(640, 480, 16, 0),
            new DisplayMode(640, 480, 32, 0),
            new DisplayMode(640, 480, 24, 0),
            new DisplayMode(1024, 768, 16, 0),
            new DisplayMode(1024, 768, 32, 0),
            new DisplayMode(1024, 768, 24, 0),
            new DisplayMode(1280, 768, 16, 0),
            new DisplayMode(1280, 768, 32, 0),
            new DisplayMode(1280, 768, 24, 0),
            new DisplayMode(1360, 768, 16, 0),
            new DisplayMode(1360, 768, 24, 0),
            new DisplayMode(1360, 768, 32, 0),
    };

    public static KeyboardInput keyboard;       //Class used for getting keyboard input

    public static int width;                    //Screen Width
    public static int height;                   //Screen height

    protected String title = "";                //Title of Game
    protected Color background = new Color(42,106,255);   //Background

    private static volatile boolean running;    //Use for the game loop
    private static Thread gameThread;           //The thread the game is running on
    private BufferStrategy bs;                  //Use for page flipping or double

                                               //Use for the display mode and enabling full screen
    private GraphicsDevice graphicsDevice;
    private DisplayMode currentDisplayMode;
    private Canvas  canvas;

    public static State gameState;                    //The state of the game
    public static State gameOverState;
    public static State menuState;                   //The menu of the game
    public static State aboutState;
    public static State completedGameState;


    public void start(){

        width = 640;
        height = 512;

        //Create states
        gameState          = new GameState();
        menuState          = new MenuState();
        gameOverState      = new GameOver();
        aboutState         = new AboutState();
        completedGameState = new CompletedGameState();

        //Set the total enemies and total money in the map
        ((CompletedGameState) completedGameState).setTotalEnemy(9);
        ((CompletedGameState) completedGameState).setTotalMoney(21);

        //Set state
        State.setState(menuState);

        //Create a size window
        createSizeWindow();

        //Start game thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        running = true;

        int fps = 60;
        double timePerTick= 1.0E9/fps;
        double delta = 0;

        long currentTime = System.nanoTime();
        long lastTime    = currentTime;
        double nsPerFrame;

        long timer = 0;
        long ticks = 0;

        while (running) {
            currentTime = System.nanoTime();
            nsPerFrame = currentTime-lastTime;

            delta += nsPerFrame/timePerTick;    //Calculate target 60 fps
            timer += nsPerFrame;
            lastTime = currentTime;


            //Make sure to run the program 60 fps
            if(delta >= 1) {
                processInput();
                updateObject();
                renderFrame();
                ticks++;
                delta--;
            }

            if(timer >= 1.0E9 ){
                System.out.println("Ticks: " + ticks);
                ticks = 0;
                timer = 0;
            }
        }
    }

    //Process user input from keyboard
    protected void processInput() {
        keyboard.poll();
        State.getState().processInput();
    }

    //update objects
    protected void updateObject() {
        if(State.getState() != null){
            State.getState().update();
        }
    }

    protected void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ex) {
        }
    }

    protected void renderFrame() {
        do {
            do {
                Graphics g = null;
                try {
                    g = bs.getDrawGraphics();
                    g.clearRect(0, 0, getWidth(), getHeight());
                    render(g);
                } finally {
                    if (g != null) {
                        g.dispose();
                    }
                }
            } while (bs.contentsRestored());
            bs.show();
        } while (bs.contentsLost());
    }

    protected void render(Graphics g) {
        if(State.getState() != null)
             State.getState().draw(g);
        else {
            Graphics2D g2 = (Graphics2D) g;

            //Use anti-aliasing to make the text look sharper
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            //Set the font and color
            g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
            g2.setColor(Color.WHITE);
            g2.drawString("GAME OVER",(Framework.width/2)-50,Framework.height/2);
        }
    }

    public void loadFullScreen(){
        setIgnoreRepaint(true);
        setUndecorated(true);
        setBackground(background);

        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();

        graphicsDevice = ge.getDefaultScreenDevice();

        currentDisplayMode = graphicsDevice.getDisplayMode();

        if( !graphicsDevice.isFullScreenSupported() ) {
            System.err.println( "ERROR: Not Supported!!!" );
            System.exit( 0 );
        }

        graphicsDevice.setFullScreenWindow(this);

        graphicsDevice.setDisplayMode( getDisplayMode() );
    }

    public DisplayMode getDisplayMode(){
        DisplayMode[] dm = graphicsDevice.getDisplayModes();
        DisplayMode temp = null;

        for(int i = 0; i<dm.length; i++){
            for(int j = POSSIBLE_MODES.length-1; j>= 0; j--){
               int w = POSSIBLE_MODES[j].getWidth();
               int h = POSSIBLE_MODES[j].getHeight();
               int b = POSSIBLE_MODES[j].getBitDepth();

               if(dm[i].getWidth() == w &&
                  dm[i].getHeight() == h &&
                  dm[i].getBitDepth() == b) {

                   temp = dm[i];
                   width = w;
                   height = h;
                   break;
               }
            }
        }
        return  temp;
    }

    public void onExit(){
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
                    shutDown();
                }
            }
        });
    }

    protected void shutDown() {
        try {
            running = false;
            gameThread.join();

            graphicsDevice.setDisplayMode( currentDisplayMode );
            graphicsDevice.setFullScreenWindow( null );
        } catch( InterruptedException e ) {
           System.out.println(e.getMessage());
        }
        System.exit( 0 );
    }

    protected void createSizeWindow(){

        //Setup the canvas
        canvas = new Canvas();
        canvas.setSize(width,height);
        canvas.setBackground(background);
        canvas.setIgnoreRepaint(true);

        //Add keyboard listener to the canvas
        keyboard = new KeyboardInput();
        canvas.addKeyListener(keyboard);

        getContentPane().add(canvas);
        setTitle(title);
        setIgnoreRepaint(true);
        setResizable(false);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        canvas.requestFocus();
    }

    protected void createFullWindow(){
        loadFullScreen();

        keyboard = new KeyboardInput();
        addKeyListener(keyboard);

        //Allow page flipping or double buffering
        createBufferStrategy( 2 );
        bs = getBufferStrategy();

        onExit(); //Finished program if user pressed the escape button
    }

    public static void onWindowClosing() {
        try {
            running = false;
            gameThread.join();
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
        System.exit( 0 );
    }

    public static Graphics2D get2DGraphics(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        //Use anti-aliasing to make the text look sharper
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        return g2;
    }

    public static void main(String[] args) {

        final  Framework game = new Framework();
        game.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.onWindowClosing();
            }
        });

        SwingUtilities.invokeLater(() -> {
            game.start();
        });
    }



}
