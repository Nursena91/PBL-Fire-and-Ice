import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import enigma.console.TextAttributes;
import enigma.core.Enigma;
import java.util.Random;
import java.util.Scanner;
public class Game {

	public enigma.console.Console cn;
	DoubleLinkedList dll = new DoubleLinkedList();
	Scanner input = new Scanner(System.in);
	public Random rnd = new Random();
	public static char[][] maze = new char[23][53];
	public static int[][] mazeTimer = new int[23][53];
	public static int[][] deleteIceMaze = new int[10000][3];// 1. sütun x, 2. sütun y, 3. sütun sayaç
	public static Stack[] stackEt = new Stack[1000];
	public static int[] control25Ice = new int[1000];
	public static int[][] directionFirstIce = new int[1000][8];
	CircularQueue fires = new CircularQueue(50); // fire'ları depolayacak olan queue.
	CircularQueue firesCounter = new CircularQueue(50); // fire'ların countlarını tutacak olan queue
	// class açılacak
	CircularQueue q = new CircularQueue(10);
	public KeyListener klis;
	// ------ Standard Variables for Keymaze ------
	public int keypr; // Key Pressed ?
	public int rkey; // Key (For Press/Release)
	// ----------------------------------------------------
	public ComputerRobot[] robots = new ComputerRobot[1000];
	public int p_score = 0;
	public int p_life = 0;
	public int c_robots = 0;
	public int totalComputer=0;

	

	public int c_score = 0;

	Game(enigma.console.Console cn) throws Exception {
		this.cn = cn;
		InputQueue(true);
		DisplayMaze();
		HighScoreTable();
		int p_ice = 0;
		int whichIceDelete = -1;
		int deleteMazeStart = 0;
		int sizeEt = 0;
		int px1 = 3;
		int py1 = 3;
		Point xy;
		int directionOfİce = 1;

		int px, py;
		int p_life = 1000;
		int p_score = 0;
		int c_robots = 0;
		int c_score = 0;
		int counted_time = 0;

		// ------ Creating Keymaze Stream Listener ------
		klis = new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (keypr == 0) {
					keypr = 1;
					rkey = e.getKeyCode();
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		};
		cn.getTextWindow().addKeyListener(klis);
		// ----------------------------------------------------
		boolean flag = true;
		px = rnd.nextInt(23);
		py = rnd.nextInt(53);
		while (flag) {
			if (maze[px][py] != ' ') {
				px = rnd.nextInt(23);
				py = rnd.nextInt(53);
			} else {
				maze[px][py] = 'P';
				cn.getTextWindow().setCursorPosition(py, px);
				cn.getTextWindow().output(maze[px][py]);
				flag = false;
			}
		}

		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 53; j++) {
				mazeTimer[i][j] = 0;
			}
		}
		int count =0;
		long prevTime = System.currentTimeMillis() / 10;
		while (true) {
			long currentTime = System.currentTimeMillis() / 10;
			DisplayInfo(p_ice, c_robots, p_life, p_score);
			int time = counted_time / 100;
			cn.getTextWindow().setCursorPosition(65, 6);
			cn.getTextWindow().output("" + time);
			Thread.sleep(Math.max(0, 10 - (currentTime - prevTime)));
			counted_time++;
			// counted_time += Math.floor(currentTime - prevTime);
			prevTime = currentTime;

			// input queue için class oluşturulacak. burada print ediyor
			cn.getTextWindow().setCursorPosition(55, 2);
			for (int i = 0; i < 10; i++) {
				cn.getTextWindow().output((char) q.peek());
				q.enqueue(q.dequeue());
			}

			// iki saniyede bir queue'ya eleman ekliyor.
			if (counted_time % 200 == 0 || count<10) {
				count++;
				int cx = rnd.nextInt(23);
				int cy = rnd.nextInt(53);
				boolean flag2 = true;
				while (flag2) {
					if (maze[cx][cy] == ' ') {
						maze[cx][cy] = (char) q.peek();
						if ((char) q.peek() == 'C') {
							robots[totalComputer] = new ComputerRobot(this, cn, cx, cy);
							c_robots++;
							totalComputer++;
						}
						if (q.peek().equals('-')) { // fireları kontrol edip her fire'ı queueye ekliyor
							Point p = new Point(cx, cy);
							Fire f = new Fire(p, mazeTimer);
							fires.enqueue(f);
							firesCounter.enqueue(0);// karşılığındaki diğer queueye counter'ı atıyor
						}

						q.dequeue();
						InputQueue(false);
						cn.getTextWindow().setCursorPosition(cy, cx);
						cn.getTextWindow().output(maze[cx][cy]);
						flag2 = false;
					} else {
						cx = rnd.nextInt(23);
						cy = rnd.nextInt(53);
					}
				}
			}

			if (counted_time % 40 == 0) {
				for (int i = 0; i < totalComputer; i++) {
					robots[i].movement(maze);
				}
			}

			if (counted_time % 10 == 0) {
				
				for (int i = 0; i < totalComputer; i++) {
					if (robots[i].harming(robots[i].getX(), robots[i].getY(), maze)) {
						c_robots--;
						p_score += 100;
					}
				}

				// DECREASING PLAYER LİFE
				if (maze[px][py + 1] == '-')
					p_life--;
				if (maze[px][py - 1] == '-')
					p_life--;
				if (maze[px + 1][py] == '-')
					p_life--;
				if (maze[px - 1][py] == '-')
					p_life--;
				if (maze[px][py + 1] == 'C')
					p_life -= 50;
				if (maze[px][py - 1] == 'C')
					p_life -= 50;
				if (maze[px + 1][py] == 'C')
					p_life -= 50;
				if (maze[px - 1][py] == 'C')
					p_life -= 50;

				// CREATING FİRE
				if (!(fires.isEmpty())) {
					int count_of_fires = fires.size();
					for (int i = 0; i < count_of_fires; i++) {
						Fire fi = (Fire) fires.peek();
						int counter = (int) firesCounter.peek();
						if (!(fi.isDone(counter))) {
							fi.update(cn, maze, mazeTimer);
							fires.enqueue(fires.dequeue());
							firesCounter.dequeue();
							firesCounter.enqueue(counter + 1);

						} else {
							fires.dequeue();
							firesCounter.dequeue();
						}
					}
				}
				// DELETING FİRE
				for (int i = 0; i < 23; i++) {
					for (int j = 0; j < 53; j++) {
						if (mazeTimer[i][j] != 0) {
							mazeTimer[i][j]--;
							if (mazeTimer[i][j] == 0) {
								maze[i][j] = ' ';
								cn.getTextWindow().output(j, i, maze[i][j]);
							}
						}
					}
				}

				// CREATING ICE
				int numberLoopIce = sizeEt + 1;
				while (numberLoopIce > 0) {
					if (control25Ice[sizeEt - (numberLoopIce - 1)] > 0) {

						while (!stackEt[sizeEt - (numberLoopIce - 1)].isEmpty()) {

							xy = (Point) stackEt[sizeEt - (numberLoopIce - 1)].pop();
							if (maze[xy.getx()][xy.gety()] == ' ') {
								control25Ice[sizeEt - (numberLoopIce - 1)]--;
								whichIceDelete++;
								deleteIceMaze[whichIceDelete][0] = xy.getx();
								deleteIceMaze[whichIceDelete][1] = xy.gety();
								deleteIceMaze[whichIceDelete][2] = 100;
								cn.getTextWindow().setCursorPosition(xy.gety(), xy.getx());
								maze[xy.getx()][xy.gety()] = '+';
								cn.getTextWindow().output(maze[xy.getx()][xy.gety()]);

								fourDirectionAddStack(xy.getx(), xy.gety(), sizeEt - (numberLoopIce - 1));
								break;
							}
						}
					}
					numberLoopIce--;
				}
				// DELETING ICE
				for (int i = deleteMazeStart; i < whichIceDelete + 1; i++) {
					deleteIceMaze[i][2]--;
					if (deleteIceMaze[i][2] == 0) {
						deleteMazeStart = i;
						maze[deleteIceMaze[i][0]][deleteIceMaze[i][1]] = ' ';
						cn.getTextWindow().setCursorPosition(deleteIceMaze[i][1], deleteIceMaze[i][0]);
						cn.getTextWindow().output(' ');
					}
				}

			}

			if (keypr == 1) {
				// Piece Cursor Control
				if (rkey == KeyEvent.VK_RIGHT) {
					if (maze[px][py + 1] == ' ' || maze[px][py + 1] == '@' || maze[px][py + 1] == '1'
							|| maze[px][py + 1] == '2' || maze[px][py + 1] == '3') {
						if (maze[px][py + 1] == '@')
							p_ice++;
						maze[px][py] = ' ';
						p_score += calculatePoint(maze[px][py + 1]);
						maze[px][py + 1] = 'P';
						cn.getTextWindow().setCursorPosition(py, px);
						cn.getTextWindow().output(maze[px][py]);
						cn.getTextWindow().output(maze[px][py + 1]);
						py++;
						directionOfİce = 1;

					}
				}

				if (rkey == KeyEvent.VK_LEFT) {
					if (maze[px][py - 1] == ' ' || maze[px][py - 1] == '@' || maze[px][py - 1] == '1'
							|| maze[px][py - 1] == '2' || maze[px][py - 1] == '3') {
						if (maze[px][py - 1] == '@')
							p_ice++;
						maze[px][py] = ' ';
						p_score += calculatePoint(maze[px][py - 1]);
						maze[px][py - 1] = 'P';
						cn.getTextWindow().setCursorPosition(py, px);
						cn.getTextWindow().output(maze[px][py]);
						cn.getTextWindow().setCursorPosition(py - 1, px);
						cn.getTextWindow().output(maze[px][py - 1]);
						py--;
						directionOfİce = 4;

					}
				}

				if (rkey == KeyEvent.VK_UP) {
					if (maze[px - 1][py] == ' ' || maze[px - 1][py] == '@' || maze[px - 1][py] == '1'
							|| maze[px - 1][py] == '2' || maze[px - 1][py] == '3') {
						if (maze[px - 1][py] == '@')
							p_ice++;
						maze[px][py] = ' ';
						p_score += calculatePoint(maze[px - 1][py]);
						maze[px - 1][py] = 'P';
						cn.getTextWindow().setCursorPosition(py, px);
						cn.getTextWindow().output(maze[px][py]);
						cn.getTextWindow().setCursorPosition(py, px - 1);
						cn.getTextWindow().output(maze[px - 1][py]);
						px--;
						directionOfİce = 2;
					}
				}

				if (rkey == KeyEvent.VK_DOWN) {
					if (maze[px + 1][py] == ' ' || maze[px + 1][py] == '@' || maze[px + 1][py] == '1'
							|| maze[px + 1][py] == '2' || maze[px + 1][py] == '3') {
						if (maze[px + 1][py] == '@')
							p_ice++;
						maze[px][py] = ' ';
						p_score += calculatePoint(maze[px + 1][py]);
						maze[px + 1][py] = 'P';
						cn.getTextWindow().setCursorPosition(py, px);
						cn.getTextWindow().output(maze[px][py]);
						cn.getTextWindow().setCursorPosition(py, px + 1);
						cn.getTextWindow().output(maze[px + 1][py]);
						px++;
						directionOfİce = 3;
					}
				}

				if (rkey == KeyEvent.VK_SPACE) {

					if (p_ice > 0) {
						Boolean flag1 = false;
						px1 = px;
						py1 = py;

						sizeEt++;
						stackEt[sizeEt] = new Stack(4000);
						if (directionOfİce == 1) {

							if (maze[px1][py1 + 1] == ' ') {
								flag1=true;
								Point xyPoint = new Point(px1, py1 + 1);
								stackEt[sizeEt].push(xyPoint);
								directionPriorityIce(sizeEt, 0, -1, 1, 0, -1, 0, 0, 1);
							}
						} else if (directionOfİce == 2) {
							if (maze[px1 - 1][py1] == ' ') {
								flag1=true;
								Point xyPoint = new Point(px1 - 1, py1);
								stackEt[sizeEt].push(xyPoint);
								directionPriorityIce(sizeEt, 1, 0, 0, -1, 0, 1, -1, 0);
							}
						} else if (directionOfİce == 3) {
							if (maze[px1 + 1][py1] == ' ') {
								flag1=true;
								Point xyPoint = new Point(px1 + 1, py1);
								stackEt[sizeEt].push(xyPoint);
								directionPriorityIce(sizeEt, -1, 0, 0, 1, 0, -1, 1, 0);
							}
						} else if (directionOfİce == 4) {
							if (maze[px1][py1 - 1] == ' ') {
								flag1=true;
								Point xyPoint = new Point(px1, py1 - 1);
								stackEt[sizeEt].push(xyPoint);
								directionPriorityIce(sizeEt, 0, 1, -1, 0, 1, 0, 0, -1);
							}
						}

						control25Ice[sizeEt] = 25;
						if(!flag1)p_ice++;
						p_ice--;
					}
				}
				keypr = 0;
			}
			if(p_life<=0) {
		        for (int i = 0; i < 49; i++) {
		            cn.getTextWindow().setCursorPosition(0, i);
		            cn.getTextWindow().output("                                                                           ");
		            cn.getTextWindow().output("                                                ");
		        }
		        
		        cn.getTextWindow().setCursorPosition(0, 0);
	            cn.getTextWindow().output("Game over. What is your name? ");
				String name = input.nextLine();
				dll.add(Integer.valueOf(p_score), name);
				dll.Display();
				break;
			}
		}		
	}

	
	public void HighScoreTable() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\gulte\\Desktop\\HighScoreTable.txt"));
			for (int i = 0; i < 8; i++) {
				String line = reader.readLine();
				String[] word = line.split(" ");
				String a = "";
				for(int j=0;j<word.length-1;j++) {
					a += word[j] + " ";
				}
				dll.add(Integer.valueOf(word[word.length-1]), a);
			}

			reader.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public void DisplayMaze() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\gulte\\Desktop\\Maze.txt"));
			for (int i = 0; i < 23; i++) {
				String line = reader.readLine();
				char[] character = line.toCharArray(); // Satırı karakter dizisine dönüştür
				for (int j = 0; j < 53; j++) {
					maze[i][j] = character[j]; // Karakter dizisinden karakteri alıp maze dizisine ata
					cn.getTextWindow().setCursorPosition(j, i);
					cn.getTextWindow().output(maze[i][j]);
				}
			}

			reader.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void DisplayInfo(int p_ice, int c_robots, int life, int score) {
		cn.getTextWindow().setCursorPosition(55, 0);
		cn.getTextWindow().output("Input");
		cn.getTextWindow().setCursorPosition(55, 1);
		cn.getTextWindow().output("<<<<<<<<<<");
		cn.getTextWindow().setCursorPosition(55, 3);
		cn.getTextWindow().output("<<<<<<<<<<");
		cn.getTextWindow().setCursorPosition(55, 6);
		cn.getTextWindow().output("Time    :");
		cn.getTextWindow().setCursorPosition(55, 9);
		cn.getTextWindow().output("P.Score :            ");
		cn.getTextWindow().setCursorPosition(55, 9);
		cn.getTextWindow().output("P.Score :" + score);
		cn.getTextWindow().setCursorPosition(55, 10);
		cn.getTextWindow().output("P.Life  :" + "      ");
		cn.getTextWindow().setCursorPosition(55, 10);
		cn.getTextWindow().output("P.Life  :" + life);
		cn.getTextWindow().setCursorPosition(55, 11);
		cn.getTextWindow().output("P.Ice   : " + p_ice);
		cn.getTextWindow().setCursorPosition(55, 14);
		cn.getTextWindow().output("C.Score :" + c_score);
		cn.getTextWindow().setCursorPosition(55, 15);
		cn.getTextWindow().output("C.Robots: " + c_robots);

		// console.print(x, y, "");
	}

	public void InputQueue(boolean isAtStart) {
		if (isAtStart) {
			for (int i = 0; i < 10; i++) {
				int num = rnd.nextInt(30) + 1;
				if (0 < num && num < 6)
					q.enqueue('1');
				else if (5 < num && num < 11)
					q.enqueue('2');
				else if (10 < num && num < 16)
					q.enqueue('3');
				else if (15 < num && num < 22)
					q.enqueue('-');
				else if (21 < num && num < 27)
					q.enqueue('@');
				else
					q.enqueue('C');
			}
		} else {
			int num = rnd.nextInt(30) + 1;
			if (0 < num && num < 6)
				q.enqueue('1');
			else if (5 < num && num < 11)
				q.enqueue('2');
			else if (10 < num && num < 16)
				q.enqueue('3');
			else if (15 < num && num < 22)
				q.enqueue('-');
			else if (21 < num && num < 27)
				q.enqueue('@');
			else
				q.enqueue('C');
		}
	}

	public void directionPriorityIce(int sizeEt, int a, int b, int c, int d, int e, int f, int g, int h) {
		directionFirstIce[sizeEt][0] = a;
		directionFirstIce[sizeEt][1] = b;
		directionFirstIce[sizeEt][2] = c;
		directionFirstIce[sizeEt][3] = d;
		directionFirstIce[sizeEt][4] = e;
		directionFirstIce[sizeEt][5] = f;
		directionFirstIce[sizeEt][6] = g;
		directionFirstIce[sizeEt][7] = h;
	}

	public void fourDirectionAddStack(int px1, int py1, int sizeEt) {
		if (maze[px1 + directionFirstIce[sizeEt][0]][py1 + directionFirstIce[sizeEt][1]] == ' ') {
			Point xyPoint4 = new Point(px1 + directionFirstIce[sizeEt][0], py1 + directionFirstIce[sizeEt][1]);
			stackEt[sizeEt].push(xyPoint4);
			directionFirstIce[0][1] = 1;
		}
		if (maze[px1 + directionFirstIce[sizeEt][2]][py1 + directionFirstIce[sizeEt][3]] == ' ') {
			Point xyPoint3 = new Point(px1 + directionFirstIce[sizeEt][2], py1 + directionFirstIce[sizeEt][3]);
			stackEt[sizeEt].push(xyPoint3);
		}
		if (maze[px1 + directionFirstIce[sizeEt][4]][py1 + directionFirstIce[sizeEt][5]] == ' ') {
			Point xyPoint2 = new Point(px1 + directionFirstIce[sizeEt][4], py1 + directionFirstIce[sizeEt][5]);
			stackEt[sizeEt].push(xyPoint2);
		}
		if (maze[px1 + directionFirstIce[sizeEt][6]][py1 + directionFirstIce[sizeEt][7]] == ' ') {
			Point xyPoint1 = new Point(px1 + directionFirstIce[sizeEt][6], py1 + directionFirstIce[sizeEt][7]);
			stackEt[sizeEt].push(xyPoint1);
		}
	}

	public int calculatePoint(char a) {
		if (a == '1')
			return 3;
		else if (a == '2')
			return 10;
		else if (a == '3')
			return 30;
		else
			return 0;
	}
}
