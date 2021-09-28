import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;

public class Hockey extends Applet implements Runnable {
	private Ball ball;	

	private Paddle client1, client2; // client
	private Paddle server1, server2; // server

	public int localtion;

	int score1, score2, SCORE;	

	Thread gameThread;

	Image imgHockey, imgBall;
  Image imgRScore, imgBScore;
  Image imgDigit[];
	Graphics gBall;

	AudioClip acGo;	
	AudioClip acLose;		

	boolean blnPlay, blnOver, blnShow;

	public void init () {
		try {
			SCORE = Integer.parseInt(getParameter("score"));
    }
    catch(Exception e) {
      e.printStackTrace();
    }

		setBackground (Color.black);

		ClassLoader cl = this.getClass().getClassLoader();
		Toolkit tk = Toolkit.getDefaultToolkit();

		imgBall = tk.createImage(cl.getResource("images/ball.gif"));

    imgDigit = new Image[10];

    for(int i = 0; i < 10; i++) {
			imgDigit[i] = tk.createImage(cl.getResource("images/d" + i + ".gif"));
    }

		imgRScore = tk.createImage(cl.getResource("images/rscore.gif"));
		imgBScore = tk.createImage(cl.getResource("images/bscore.gif"));
		
		acGo = getAudioClip(cl.getResource("audio/go.au"));

		acLose = getAudioClip(cl.getResource("audio/lose.au"));

		ball = new Ball (imgBall, this) ;

		client1 = new Paddle (false, 15, 175);
		client2 = new Paddle (false, 375, 175);

		server1 = new Paddle (true, 475, 175);
		server2 = new Paddle (true, 115, 175);

		blnPlay = false;
		blnOver = false;
		blnShow = false;

		score1 = 0;
		score2 = 0;

		// 註冊 MouseListener
		addMouseListener(new MouseAdapter() {
			// 釋放滑鼠按鍵
			public void mouseReleased(MouseEvent e) {
				if (!blnPlay){
					blnPlay = true;
					blnOver = false;
					blnShow = false;
					ball.vx = 4;
					ball.vy = 4;
					score1 = 0;
					score2 = 0;
					ball.isStoped = false;
				}
			}
		});

		// 註冊 MouseMotionListener
		addMouseMotionListener(new MouseMotionAdapter() {
			// 在物件上方移動滑鼠
			public void mouseMoved(MouseEvent e) {
				//int x = e.getX();
				int y = e.getY();
				
				if (y > 25 & y < 325)	{
					client1.movePaddle (y);
					client2.movePaddle (y);
				}
			}
		});
	}

	public void start () {
		gameThread = new Thread (this);
		gameThread.start ();
	}

	public void stop ()	{
		gameThread.yield();
	}

	public void run () {
		while (true) {
			repaint();

			ball.moveBall();

			server1.movePaddle (ball);

			localtion = ball.getLocation();

			if (localtion == 1) {
				score1 += 1;
				acLose.play();
				ball.pos_x = 250;
				ball.vx = 4;
				ball.vy = -4;

				if (score1 == SCORE)	{
					blnPlay = false;
					blnOver = true;
					blnShow = false;
					ball.isStoped = true;
					ball.vx = 0;
					ball.vy = 0;
				}
			}
			else if (localtion == 2)	{
				score2 += 1;
				acLose.play();
				ball.pos_x = 250;
				ball.vx = 4;
				ball.vy = 4;

				if (score2 == SCORE)	{
					blnPlay = false;
					blnOver = true;
					blnShow = false;
					ball.isStoped = true;
					ball.vx = 0;
					ball.vy = 0;
				}
			}

			if (ball.vx < 0) {
				ball.touchPlayerPaddle (client1, client2, acGo);
			}
			else if (ball.vx > 0)	{
				ball.touchComputerPaddle (server1, server2, acGo);
			}

			try {
				Thread.sleep (10);
			}
			catch (InterruptedException ex)	{
				break;
			}
		}
	}

	public void paint (Graphics g)	{
		g.setColor(new Color(0,128,128));
		g.fillRect(0, 0, 500, 8);		
		g.fillRect(0, 342, 500, 8);	
		g.fillRect(0, 0, 8, 75);		
		g.fillRect(0, 275, 8, 75);	
		g.fillRect(492, 0, 8, 75);	
		g.fillRect(492, 275, 8, 75);	
		g.fillOval(180, 105, 140, 140);		

		g.setColor(Color.black);
		g.fillOval(184, 109, 132, 132);		

		g.setColor(new Color(0,128,128));
		g.fillRect(248, 0, 4, 342);		

		ball.drawBall (g);

		client1.drawPaddle (g);
		client2.drawPaddle (g);

		server1.drawPaddle (g);
		server2.drawPaddle (g);

		// Computer
    g.drawImage(imgBScore, 390, 365, this);
    
    String bScore = String.valueOf(score1) ;
    
    for (int i=0; i < bScore.length() ; i++) {
    	int j = Integer.parseInt(bScore.substring(i, i+1));
      g.drawImage(imgDigit[j], 440 + 15 * i, 361, this);
    }

		// Player
    g.drawImage(imgRScore, 30, 365, this);
      
    String rScore = String.valueOf(score2) ;

    for (int i=0; i < rScore.length() ; i++) {
    	int j = Integer.parseInt(rScore.substring(i, i+1));
      g.drawImage(imgDigit[j], 80 + 15 * i, 361, this);
    }

		if (blnOver) {
			if (score1 == SCORE && !blnShow)	{
				blnShow = true;
				JOptionPane.showMessageDialog((Component) this, "電腦勝.", "曲棍球(Hockey)", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (score2 == SCORE && !blnShow) {
				blnShow = true;
				JOptionPane.showMessageDialog((Component) this, "玩家勝.", "曲棍球(Hockey)", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public void update (Graphics g)	{
		if (imgHockey == null) {
			imgHockey = createImage (this.getSize().width, this.getSize().height);
			gBall = imgHockey.getGraphics ();
		}

		gBall.setColor (getBackground ());
		gBall.fillRect (0, 0, this.getSize().width, this.getSize().height);

		gBall.setColor (getForeground());
		paint (gBall);

		g.drawImage (imgHockey, 0, 0, this);
	}
}