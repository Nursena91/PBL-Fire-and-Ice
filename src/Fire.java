
public class Fire {
	private CircularQueue cq;
	private Point p;

	Fire(Point p, int [][] mazeTimer) {
		this.p = p;
		cq = new CircularQueue(10000000);
		cq.enqueue(p);
		
		mazeTimer[p.getx()][p.gety()] = 99;
	}

	public void update(enigma.console.Console cn, char[][]maze, int [][] mazeTimer) {
		boolean check = false;
		while(!check) {
			if (!cq.isEmpty()) {
				p = (Point) cq.peek();

				if (maze[p.getx()][p.gety() + 1] == ' ') { // sağı
					Point newp = new Point(p.getx(), p.gety() + 1);
					cq.enqueue(newp);
				}
				if (maze[p.getx() + 1][p.gety()] == ' ') { // altı
					Point newp = new Point(p.getx() + 1, p.gety());
					cq.enqueue(newp);
				}
				if (maze[p.getx()][p.gety() - 1] == ' ') {// solı
					Point newp = new Point(p.getx(), p.gety() - 1);
					cq.enqueue(newp);
				}
				if (maze[p.getx() - 1][p.gety()] == ' ') {// üstü
					Point newp = new Point(p.getx() - 1, p.gety());
					cq.enqueue(newp);
				}
			}
			if (maze[p.getx()][p.gety()] == ' ' && !cq.isEmpty()) {
				check = true;
				mazeTimer[p.getx()][p.gety()] = 100;				
			}
			if (!(cq.isEmpty())) {
				if (maze[p.getx()][p.gety()] == ' ')
				{
				cn.getTextWindow().setCursorPosition(p.gety(), p.getx());
				maze[p.getx()][p.gety()] = '-'; // maze'in değerini güncellemiyor

				cn.getTextWindow().output(maze[p.getx()][p.gety()]);	}
				
				cq.dequeue();
			}
			else check=true;
		}
	}

	public boolean isDone(int counter) {
		if (counter != 49) {
			return false;
		} else {
			return true;
		}
	}
	
	

}
