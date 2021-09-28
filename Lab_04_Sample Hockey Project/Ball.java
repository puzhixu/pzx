import java.awt.*;
import java.applet.*;

public class Ball {
	public int pos_x, pos_y;
	public int vx, vy;
	public int radius;
	public boolean isStoped;

	private int ball_top, ball_bottom, ball_left, ball_right;
	private int player_top, player_bottom, player_right;
	private int comp_top, comp_bottom, comp_left;

	private final int max_vx, max_vy;	

	Image ballImg;
	Applet parent;

	public Ball (Image ballImg, Applet parent)	{
		pos_x = 250;
		pos_y = 175;
		radius = 8;	
		vx = 0;
		vy = 0;
		max_vx = 5;	
		max_vy = 5;	
		isStoped = true;

		this.ballImg = ballImg;
		this.parent = parent;
	}

	public void drawBall (Graphics g)	{
		g.setColor (Color.black);
		g.fillOval (pos_x - radius, pos_y - radius, 2 * radius, 2 * radius);
		g.drawImage (ballImg, pos_x - 10, pos_y - 10, parent);
	}

	public void moveBall ()	{
		if (vx > max_vx) 
			vx = max_vx;
		else if (vx < -max_vx) 
			vx = -max_vx;
		else if (vy > max_vy) 
			vy = max_vy;
		else if (vy < -max_vy) 
			vy = -max_vy;
		else if (vx == 0 && !isStoped) 
			vx = 1;

		pos_x += vx;
		pos_y += vy;
	}

	public int getLocation () {
		if (vy > 0)	{
			if (pos_y > 330) {
				vy = -vy;
				return 0;
			}
		}
		else if (vy < 0) {
			if (pos_y < 20)	{
				vy = -vy;
				return 0;
			}
		}

		if (vx < 0)	{
			if (pos_x < 15 && pos_y < 75)	{
				vx = -vx;
				return 0;
			}
			else if (pos_x < 15 && pos_y > 275) {
				vx = -vx;
				return 0;
			}
			else if (pos_x < 15) {
				vx = -vx;
				return 1;
			}
			else 
				return 0;
		}
		else if (vx > 0) {
			if (pos_x > 485 && pos_y < 75) {
				vx = -vx;
				return 0;
			}
			else if (pos_x > 485 && pos_y > 275) {
				vx = -vx;
				return 0;
			}
			else if (pos_x > 485)	{
				vx = -vx;
				return 2;
			}
			else 
				return 0;
		}
		else 
			return 0;
	}

	public void touchPlayerPaddle (Paddle paddle1, Paddle paddle2, AudioClip sound) {
		ball_top = pos_y - radius;
		ball_bottom = pos_y + radius;
		ball_left = pos_x - radius;
		ball_right = pos_x + radius;

		player_top = paddle1.pos_y2;
		player_bottom = paddle1.pos_y2 + paddle1.size_y;

		if ((ball_top >= player_top - 10) && (ball_bottom <= player_bottom + 10))	{
			if (ball_left <= 25 || (ball_left <= 380 && ball_left >= 370)){
				sound.play();

				vx = - vx;

				if (paddle1.vy2 < 0)	{
					vy --;
				}
				else if (paddle1.vy2 > 0) {
					vy += paddle1.vy2;
				}
			}
		}
	}

	public void touchComputerPaddle (Paddle paddle3, Paddle paddle4, AudioClip sound) {
		ball_top = pos_y - radius;
		ball_bottom = pos_y + radius;
		ball_left = pos_x - radius;
		ball_right = pos_x + radius;

		comp_top = paddle3.pos_y1;
		comp_bottom = paddle3.pos_y1 + paddle3.size_y;

		if ((ball_top >= comp_top - 10) && (ball_bottom <= comp_bottom + 10))	{
			if (ball_right >= 475 || (ball_right <= 125 && ball_right >= 115)) {
				sound.play();

				vx = - vx;

				if (paddle3.vy1 < 0)	{
					vy --;
				}
				else if (paddle3.vy1 > 0){
					vy += paddle3.vy1;
				}
			}
		}
	}
}