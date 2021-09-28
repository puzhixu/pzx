import java.awt.*;

// ¦±´Ò²y´Ò
public class Paddle {
	public final int pos_x;
	public static int pos_y1, pos_y2; // 1: server 2: client
	public final int size_x;
	public final int size_y;
	public int vy1, vy2;				
	public int real_y;		
	private Color paddlecolor;
	private boolean blnServer;

	public Paddle (boolean blnServer, int x, int y) {
		this.blnServer = blnServer ;
		pos_x = x;
		size_x = 10;
		size_y = 50;
		
		if (blnServer) { 
			pos_y1 = y;
			paddlecolor = Color.blue;
			vy1 = 3;
		}
		else {
			pos_y2 = y;
			paddlecolor = Color.red;
			vy2 = 0;
		}
	}

	public void movePaddle (Ball b)	{
		real_y = pos_y1 + (size_y / 2);

		if (b.vx < 0)	{
			if (real_y < 175)	{
				pos_y1 += vy1;
			}
			else if (real_y > 175) {
				pos_y1 -= vy1;
			}
		}
		else if (b.vx > 0) {
			if ( real_y != b.pos_y)	{
				if (b.pos_y < real_y)	{
					pos_y1 -= vy1;
				}
				else if (b.pos_y > real_y) {
					pos_y1 += vy1;
				}
			}
		}
	}

	public void movePaddle (int mouse_y)	{
		int new_y_pos;

		new_y_pos = mouse_y - (size_y / 2);

		if (new_y_pos < pos_y2) 
			vy2 = (pos_y2 - new_y_pos) / 2;
		else 
			vy2 = (new_y_pos - pos_y2) / 2;

		pos_y2 = new_y_pos;
	}

	public void drawPaddle (Graphics g)	{
		if (blnServer) {
			g.setColor (paddlecolor);
			g.fillRect (pos_x, pos_y1, size_x, size_y);
		}
		else {
			g.setColor (paddlecolor);
			g.fillRect (pos_x, pos_y2, size_x, size_y);
		}
	}
}
