import java.util.Random;

import enigma.core.Enigma;

public class ComputerRobot {
	public Random rnd = new Random();
	private Game game;
	private int x;
	private int y;
	private int lifePoints;
	private int scorePoints;
	boolean isDead=false;
	public enigma.console.Console cn;

	public ComputerRobot(enigma.console.Console cn) {
		this.cn = cn;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public ComputerRobot(Game game, enigma.console.Console cn, int x, int y) {
		this.game = game;
		this.cn = cn;
		this.x = x;
		this.y = y;
		this.lifePoints = 1000;
		this.scorePoints = 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLifePoints() {
		return lifePoints;
	}

	public void setLifePoints(int lifePoints) {
		this.lifePoints = lifePoints;
	}

	public int getScorePoints() {
		return scorePoints;
	}

	public void setScorePoints(int scorePoints) {
		this.scorePoints = scorePoints;
	}

	public void movement(char[][] maze) {
		if (!isDead) {

			int minDistance = 1000;
			int targetSatır = 0;
			int targetSütun = 0;

			// En yakın hazineyi buldu
			for (int i = 0; i < 23; i++) {
				for (int j = 0; j < 53; j++) {
					char hucre = maze[i][j];
					if (hucre == '1' || hucre == '2' || hucre == '3') {
						int distance = Math.abs(x - i) + Math.abs(y - j);
						if (distance < minDistance) {
							minDistance = distance;
							targetSatır = i;
							targetSütun = j;
						}
					}
				}
			}
			int treasure = maze[targetSatır][targetSütun];
			int randomComputer = rnd.nextInt(4);
			if (targetSütun == 0) {
				if (randomComputer == 0) {
					if (maze[x][y + 1] == ' ') {
						maze[x][y] = ' ';
						maze[x][y + 1] = 'C';
						cn.getTextWindow().setCursorPosition(y, x);
						cn.getTextWindow().output(maze[x][y]);
						cn.getTextWindow().output(maze[x][y + 1]);
						y++;
						processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
					}
				} else if (randomComputer == 1) {
					if (maze[x][y - 1] == ' ') {
						maze[x][y] = ' ';
						maze[x][y - 1] = 'C';
						cn.getTextWindow().setCursorPosition(y, x);
						cn.getTextWindow().output(maze[x][y]);
						cn.getTextWindow().setCursorPosition(y - 1, x);
						cn.getTextWindow().output(maze[x][y - 1]);
						y--;
						processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
					}
				} else if (randomComputer == 2) {
					if (maze[x - 1][y] == ' ') {
						maze[x][y] = ' ';
						maze[x - 1][y] = 'C';
						cn.getTextWindow().setCursorPosition(y, x);
						cn.getTextWindow().output(maze[x][y]);
						cn.getTextWindow().setCursorPosition(y, x - 1);
						cn.getTextWindow().output(maze[x - 1][y]);
						x--;
						processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
					}
				} else if (randomComputer == 3) {
					if (maze[x + 1][y] == ' ') {
						maze[x][y] = ' ';
						maze[x + 1][y] = 'C';
						cn.getTextWindow().setCursorPosition(y, x);
						cn.getTextWindow().output(maze[x][y]);
						cn.getTextWindow().setCursorPosition(y, x + 1);
						cn.getTextWindow().output(maze[x + 1][y]);
						x++;
						processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
					}
				}
			}

			else {

				if (x != targetSatır || y != targetSütun) {
					if (y < targetSütun && (maze[x][y + 1] == ' ' || maze[x][y + 1] == treasure)) { // sağa hareket
						if (maze[x][y + 1] != '#') {
							maze[x][y] = ' ';
							maze[x][y + 1] = 'C';
							cn.getTextWindow().setCursorPosition(y, x);
							cn.getTextWindow().output(maze[x][y]);
							cn.getTextWindow().output(maze[x][y + 1]);
							y++;
							processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
						}
					} else if (y > targetSütun && (maze[x][y - 1] == ' ' || maze[x][y - 1] == treasure)) { // sola
																											// hareket
						if (maze[x][y - 1] != '#') {
							maze[x][y] = ' ';
							maze[x][y - 1] = 'C';
							cn.getTextWindow().setCursorPosition(y, x);
							cn.getTextWindow().output(maze[x][y]);
							cn.getTextWindow().setCursorPosition(y - 1, x);
							cn.getTextWindow().output(maze[x][y - 1]);
							y--;
							processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
						}

					} else if (x > targetSatır && (maze[x - 1][y] == ' ' || maze[x - 1][y] == treasure)) { // yukarı
																											// hareket
						if (maze[x - 1][y] != '#') {
							maze[x][y] = ' ';
							maze[x - 1][y] = 'C';
							cn.getTextWindow().setCursorPosition(y, x);
							cn.getTextWindow().output(maze[x][y]);
							cn.getTextWindow().setCursorPosition(y, x - 1);
							cn.getTextWindow().output(maze[x - 1][y]);
							x--;
							processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
						}
					} else if (x < targetSatır && (maze[x + 1][y] == ' ' || maze[x + 1][y] == treasure)) { // aşağı
																											// hareket
						if (maze[x + 1][y] != '#') {
							maze[x][y] = ' ';
							maze[x + 1][y] = 'C';
							cn.getTextWindow().setCursorPosition(y, x);
							cn.getTextWindow().output(maze[x][y]);
							cn.getTextWindow().setCursorPosition(y, x + 1);
							cn.getTextWindow().output(maze[x + 1][y]);
							x++;
							processMovementEffects(x, y, maze, targetSatır, targetSütun, treasure);
						}
					}
				}

			}
		}
	}

	private boolean isPlayerNearby(int cx, int cy, char maze[][]) {
		if (maze[cx + 1][y] == 'P' || maze[cx - 1][y] == 'P' || maze[cx][y + 1] == 'P' || maze[cx][y - 1] == 'P')
			return true;
		else
			return false;
	}

	private boolean isNeighborIceBlock(int cx, int cy, char maze[][]) {
		if (maze[cx + 1][y] == '+' || maze[cx - 1][y] == '+' || maze[cx][y + 1] == '+' || maze[cx][y - 1] == '+')
			return true;
		else
			return false;
	}

	private int countIceBloks(int cx, int cy, char maze[][]) {
		int count = 0;
		if (maze[cx + 1][y] == '+')
			count++;
		if (maze[cx - 1][y] == '+')
			count++;
		if (maze[cx][y + 1] == '+')
			count++;
		if (maze[cx][y - 1] == '+')
			count++;
		return count;
	}

	private void processMovementEffects(int cx, int cy, char[][] maze, int targetSatır, int targetSütun, int treasure) {

		if (cx == targetSatır && cy == targetSütun) {
			if (treasure == '1')
				game.c_score += 9;
			else if (treasure == '2')
				game.c_score += 30;
			else if (treasure == '3')
				game.c_score += 90;
		}
	}

	 boolean harming(int cx, int cy, char[][] maze) throws InterruptedException {
		 if(!isDead){
		if (isNeighborIceBlock(cx, cy, maze)) {
			int count = countIceBloks(cx, cy, maze);
			lifePoints = lifePoints - 50 * count;
			if (lifePoints <= 0) {
				//game.c_robots--;
				maze[cx][cy] = ' ';
				cn.getTextWindow().setCursorPosition(cy, cx);
				cn.getTextWindow().output(maze[cx][cy]);
				isDead=true;
				return true;
			}
		}
		 }
		return false;
	}
}