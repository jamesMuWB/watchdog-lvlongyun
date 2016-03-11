package per.cloudy.compenent;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import per.cloudy.entity.Bucket;

public class GraphicPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4292432024309541366L;
	public GraphicPanel(){
		this.setSize(500,500);
	}
	public void redraw(Bucket bucket) {
		Graphics g = getGraphics();
		if(g==null){
			return;
		}
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		// 画X,Y轴
		int YLeft = 50;
		int lineLength = this.getHeight() - YLeft * 3;
		int lineWidth = this.getWidth() - YLeft * 2;
		g.drawLine(YLeft, YLeft, YLeft, this.getHeight() - YLeft * 2);
		g.drawLine(YLeft, this.getHeight() - YLeft * 2,
				this.getWidth() - YLeft, this.getHeight() - YLeft * 2);
		// 画刻度
		double max=(int)bucket.max*1.2;
		for (int i = 0; i < 10; i++) {
			int y = YLeft + ((lineLength / 10) * i);
			g.drawLine(YLeft, y, YLeft + 10, y);
			int yStr= (int)((max/10.0)*(10-i)+0.5);
			g.drawString(yStr+ "", YLeft - 30, y + 5);
			int x = YLeft + ((lineWidth / 10) * (i + 1));
			int yPoint = this.getHeight() - YLeft * 2;
			g.drawLine(x, yPoint, x, yPoint - 10);
			g.drawString((int)((bucket.list.size()/10.0)*(i+1))+ "", x - 10, yPoint + 20);
		}
		//画数据
		g.setColor(Color.RED);
		double pointWidth=(lineWidth*1.0d/bucket.list.size());
		int lastX=YLeft;
		int lastY=this.getHeight() - YLeft * 2;
		int index=0;
		for(Integer count:bucket.list){
			int newX=(int)(YLeft+pointWidth*(1+index));
			int newY=(int)(this.getHeight() - YLeft * 2.0d-(count*1.0d/max)*lineLength);
			g.drawLine(lastX, lastY, newX,newY);
			lastX=newX;
			lastY=newY;
			index++;
		}
	}
}
