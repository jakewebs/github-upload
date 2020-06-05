import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
/*
    The Board class draws the board and selects a puzzle based off of the specified difficulty
    Sources for puzzles: https://dingo.sbs.arizona.edu/~sandiway/sudoku/examples.html (Easy 1-2, Medium 1, Hard 1-2)
                         https://krazydad.com/sudoku/sfiles/KD_Sudoku_IM_8_v1.pdf (Medium 2-5)
                         http://www.sudokuessentials.com/free_sudoku.html (Easy 3-5, Hard 3-5)
*/
public class Board{
	private int x, y, difficulty, spot;
	private Color black, gray, blue, red;
	private Font font;
	private int[][] wrong_spots;
	private int[][] selected_grid;
	private String[][] unsolved;
	private HashMap<Integer,int[][]> easy_maps, medium_maps, hard_maps;
	private HashMap<Integer,String[][]> easy_unsolved, medium_unsolved, hard_unsolved;

	//Constructor, initializes variables/the grid based off the inputted difficulty
	public Board(int x, int y, int difficulty){
		this.x = x;
		this.y = y;
		this.difficulty = difficulty;
		black = new Color(0,0,0);
		gray = new Color(192,192,192);
		blue = new Color(30,30,255);
		red = new Color(232,142,142);
		font = new Font("Times New Roman",Font.BOLD,36);
		wrong_spots = new int[9][9];
		easy_maps = new HashMap<Integer,int[][]>();
		medium_maps = new HashMap<Integer,int[][]>();
		hard_maps = new HashMap<Integer,int[][]>();
		easy_unsolved = new HashMap<Integer,String[][]>();
		medium_unsolved = new HashMap<Integer,String[][]>();
		hard_unsolved = new HashMap<Integer,String[][]>();
		if (difficulty == 1) {
			initializeEasy();
			spot = (int)(Math.random() * easy_maps.size() + 1);
			selected_grid = easy_maps.get(spot);
			unsolved = easy_unsolved.get(spot);
		} else if (difficulty == 2){
			initializeMedium();
			spot = (int)(Math.random() * medium_maps.size() + 1);
			selected_grid = medium_maps.get(spot);
			unsolved = medium_unsolved.get(spot);
		} else {
			initializeHard();
			spot = (int)(Math.random() * hard_maps.size() + 1);
			selected_grid = hard_maps.get(spot);
			unsolved = hard_unsolved.get(spot);
		}
	}

	//The main visualization function for the sudoku grid
	public void drawMe(Graphics g){
		//Incorrect squares get colored red
		g.setColor(red);
		for (int c = 0; c < wrong_spots.length; c++){
			for (int r = 0; r < wrong_spots[c].length; r++){
				if (wrong_spots[c][r] == 1)
					g.fillRect(x + (60*r),y + (60*c),60,60);
			}
		}

		//Sets up the physical board
		g.setColor(black);
		g.drawLine(x,y,x+540,y);
		g.drawLine(x,y,x,y+540);
		g.drawLine(x,y+540,x+540,y+540);
		g.drawLine(x+540,y,x+540,y+540);
		g.drawLine(x+180,y,x+180,y+540);
		g.drawLine(x+360,y,x+360,y+540);
		g.drawLine(x,y+180,x+540,y+180);
		g.drawLine(x,y+360,x+540,y+360);
		g.setColor(gray);
		g.drawLine(x+60,y,x+60,y+540);
		g.drawLine(x+120,y,x+120,y+540);
		g.drawLine(x+240,y,x+240,y+540);
		g.drawLine(x+300,y,x+300,y+540);
		g.drawLine(x+420,y,x+420,y+540);
		g.drawLine(x+480,y,x+480,y+540);
		g.drawLine(x,y+60,x+540,y+60);
		g.drawLine(x,y+120,x+540,y+120);
		g.drawLine(x,y+240,x+540,y+240);
		g.drawLine(x,y+300,x+540,y+300);
		g.drawLine(x,y+420,x+540,y+420);
		g.drawLine(x,y+480,x+540,y+480);

		//Drawing the numbers
		g.setFont(font);
		g.setColor(blue);
		for (int c = 0; c < unsolved.length; c++){
    		for(int r = 0; r< unsolved[c].length;r++){
    			g.drawString(unsolved[c][r],20 + x + (60*r), 40 + y + (60*c));
    		}
    	}
	}

	//Checks the answer grid to see if the guess is correct
	public boolean guess(int r, int c, int guess){
		unsolved[c][r] = Integer.toString(guess);
		return selected_grid[c][r] == guess;
	}

	//If any spot is incorrect (blanks do not count), they are highlighted. If all correct, then it returns True.
	public boolean checkAnswers() {
		boolean finished = true;
		for (int c = 0; c < unsolved.length; c++) {
    		for(int r = 0; r< unsolved[c].length;r++) {
    			//No answer gets -1, Correct gets a 0, incorrect gets labelled 1
    			if(unsolved[c][r].equals("")){
    				wrong_spots[c][r] = -1;
    				finished = false;
    			}
    			else if (Integer.parseInt(unsolved[c][r]) == selected_grid[c][r]) {
    				wrong_spots[c][r] = 0;
    			}
    			else {
    				wrong_spots[c][r] = 1;
    				finished = false;
    			}
    		}
    	}
    	return finished;
	}

	//Adds all the grids to the easy HashMap
	public void initializeEasy(){
		easy_maps.put(1, new int[][] {{4,3,5,2,6,9,7,8,1}, {6,8,2,5,7,1,4,9,3}, {1,9,7,8,3,4,5,6,2}, {8,2,6,1,9,5,3,4,7}, {3,7,4,6,8,2,9,1,5}, {9,5,1,7,4,3,6,2,8},
		      						  {5,1,9,3,2,6,8,7,4}, {2,4,8,9,5,7,1,3,6}, {7,6,3,4,1,8,2,5,9} });
		easy_maps.put(2, new int[][] {{1,5,2,4,8,9,3,7,6}, {7,3,9,2,5,6,8,4,1}, {4,6,8,3,7,1,2,9,5}, {3,8,7,1,2,4,6,5,9}, {5,9,1,7,6,3,4,2,8}, {2,4,6,8,9,5,7,1,3},
			                          {9,1,4,6,3,7,5,8,2}, {6,2,5,9,4,8,1,3,7}, {8,7,3,5,1,2,9,6,4} });
		easy_maps.put(3, new int[][] {{8,4,6,9,3,7,1,5,2}, {3,1,9,6,2,5,8,4,7}, {7,5,2,1,8,4,9,6,3}, {2,8,5,7,1,3,6,9,4}, {4,6,3,8,5,9,2,7,1}, {9,7,1,2,4,6,3,8,5},
			                          {1,2,7,5,9,8,4,3,6}, {6,3,8,4,7,1,5,2,9}, {5,9,4,3,6,2,7,1,8} });
		easy_maps.put(4, new int[][] {{1,5,7,2,3,6,4,8,9}, {4,6,9,1,8,5,3,7,2}, {3,8,2,7,4,9,5,1,6}, {5,9,1,8,6,7,2,3,4}, {2,4,6,3,5,1,7,9,8}, {7,3,8,4,9,2,1,6,5},
			                          {6,1,4,5,7,8,9,2,3}, {8,2,3,9,1,4,6,5,7}, {9,7,5,6,2,3,8,4,1} });
		easy_maps.put(5, new int[][] {{8,3,6,5,2,9,1,7,4}, {2,9,1,7,4,3,8,6,5}, {4,7,5,8,1,6,2,9,3}, {5,4,8,6,3,2,7,1,9}, {3,6,9,1,7,5,4,8,2}, {1,2,7,9,8,4,3,5,6},
			                          {9,1,4,2,6,8,5,3,7}, {7,5,3,4,9,1,6,2,8}, {6,8,2,3,5,7,9,4,1} });
		easy_unsolved.put(1, new String[][] {{"","","","2","6","","7","","1"}, {"6","8","","","7","","","9",""}, {"1","9","","","","4","5","",""}, {"8","2","","1","","","","4",""},
						                     {"","","4","6","","2","9","",""}, {"","5","","","","3","","2","8"}, {"","","9","3","","","","7","4"}, {"","4","","","5","","","3","6"},
						                     {"7","","3","","1","8","","",""} });
		easy_unsolved.put(2, new String[][] {{"1","","","4","8","9","","","6"}, {"7","3","","","","","","4",""}, {"","","","","","1","2","9","5"}, {"","","7","1","2","","6","",""},
			                                 {"5","","","7","","3","","","8"}, {"","","6","","9","5","7","",""}, {"9","1","4","6","","","","",""}, {"","2","","","","","","3","7"},
			                                 {"8","","","5","1","2","","","4"} });
		easy_unsolved.put(3, new String[][] {{"8","","","9","3","","","","2"}, {"","","9","","","","","4",""}, {"7","","2","1","","","9","6",""}, {"2","","","","","","","9",""},
			                                 {"","6","","","","","","7",""}, {"","7","","","","6","","","5"}, {"","2","7","","","8","4","","6"}, {"","3","","","","","5","",""},
			                                 {"5","","","","6","2","","","8"} });
		easy_unsolved.put(4, new String[][] {{"","","","","","6","","8",""}, {"","","9","1","","5","3","7","2"}, {"","8","","7","","","","1","6"}, {"","","","","","","","3","4"},
			                                 {"","","","3","5","1","","",""}, {"7","3","","","","","","",""}, {"6","1","","","","8","","2",""}, {"8","2","3","9","","4","6","",""},
			                                 {"","7","","6","","","","",""} });
		easy_unsolved.put(5, new String[][] {{"8","3","","","2","9","","",""}, {"","9","","7","","","","6",""}, {"4","","","","1","","2","",""}, {"","4","8","","","2","","1","9"},
			                                 {"","","9","","","","4","",""}, {"1","2","","9","","","3","5",""}, {"","","4","","6","","","","7"}, {"","5","","","","1","","2",""},
			                                 {"","","","3","5","","","4","1"} });
	}

	//Adds all the grids to the medium HashMap
	public void initializeMedium(){
		medium_maps.put(1, new int[][] {{1,2,3,6,7,8,9,4,5}, {5,8,4,2,3,9,7,6,1}, {9,6,7,1,4,5,3,2,8}, {3,7,2,4,6,1,5,8,9}, {6,9,1,5,8,3,2,7,4}, {4,5,8,7,9,2,6,1,3},
			                            {8,3,6,9,2,4,1,5,7}, {2,1,9,8,5,7,4,3,6}, {7,4,5,3,1,6,8,9,2} });
		medium_maps.put(2, new int[][] {{6,2,4,3,8,7,5,9,1}, {1,3,9,4,5,6,8,2,7}, {7,5,8,1,9,2,4,3,6}, {4,9,6,8,1,3,7,5,2}, {2,8,3,7,6,5,1,4,9}, {5,1,7,9,2,4,3,6,8},
			                            {9,4,1,2,3,8,6,7,5}, {3,6,2,5,7,1,9,8,4}, {8,7,5,6,4,9,2,1,3} });
		medium_maps.put(3, new int[][] {{8,2,1,7,4,6,9,5,3}, {5,9,6,8,1,3,7,2,4}, {7,4,3,2,5,9,8,6,1}, {4,6,5,9,8,7,1,3,2}, {9,1,7,3,2,5,4,8,6}, {2,3,8,4,6,1,5,9,7},
			                            {6,5,4,1,3,8,2,7,9}, {3,7,2,5,9,4,6,1,8}, {1,8,9,6,7,2,3,4,5} });
		medium_maps.put(4, new int[][] {{1,6,8,9,5,4,2,7,3}, {3,9,5,2,7,8,4,6,1}, {2,7,4,3,6,1,9,8,5}, {8,1,6,4,3,9,7,5,2}, {4,2,3,7,8,5,6,1,9}, {7,5,9,1,2,6,8,3,4},
			                            {9,8,7,5,4,3,1,2,6}, {5,4,2,6,1,7,3,9,8}, {6,3,1,8,9,2,5,4,7} });
		medium_maps.put(5, new int[][] {{1,9,2,3,7,4,6,5,8}, {5,7,4,1,8,6,2,9,3}, {8,3,6,9,2,5,4,1,7}, {9,8,7,5,6,2,1,3,4}, {4,2,3,8,1,9,5,7,6}, {6,5,1,4,3,7,9,8,2},
			                            {7,6,8,2,5,1,3,4,9}, {3,4,5,6,9,8,7,2,1}, {2,1,9,7,4,3,8,6,5} });
		medium_unsolved.put(1, new String[][] {{"","2","","6","","8","","",""}, {"5","8","","","","9","7","",""}, {"","","","","4","","","",""}, {"3","7","","","","","5","",""},
											   {"6","","","","","","","","4"}, {"","","8","","","","","1","3"}, {"","","","","2","","","",""}, {"","","9","8","","","","3","6"},
			                                   {"","","","3","","6","","9",""} });
		medium_unsolved.put(2, new String[][] {{"","2","4","3","8","","","",""}, {"","","","","","6","","","7"}, {"","5","8","","","","4","",""}, {"4","","","","1","","","",""},
			                                   {"","","","7","","5","","",""}, {"","","","","2","","","","8"}, {"","","1","","","","6","7",""}, {"3","","","5","","","","",""},
			                                   {"","","","","4","9","2","1",""} });
		medium_unsolved.put(3, new String[][] {{"8","","","","","","","","3"}, {"5","","","8","","","7","","4"}, {"","","","","","","","6",""}, {"","6","","9","8","","1","",""},
			                                   {"","","7","","","","4","",""}, {"","","8","","6","1","","9",""}, {"","5","","","","","","",""}, {"3","","2","","","4","","","8"}, 
			                                   {"1","","","","","","","","5"} });
		medium_unsolved.put(4, new String[][] {{"","","8","","","4","","","3"}, {"","9","","","","","","6","1"}, {"","","","3","","","","",""}, {"","","","4","","","7","5",""},
			                                   {"","","3","7","","5","6","",""}, {"","5","9","","","6","","",""}, {"","","","","","3","","",""}, {"5","4","","","","","","9",""},
			                                   {"6","","","8","","","5","",""} });
		medium_unsolved.put(5, new String[][] {{"","","2","","","4","","","8"}, {"","","","","","","","9","3"}, {"","","","9","2","5","","",""}, {"","","","","","","1","","4"},
			                                   {"","","3","8","","9","5","",""}, {"6","","1","","","","","",""}, {"","","","2","5","1","","",""}, {"3","4","","","","","","",""},
			                                   {"2","","","7","","","8","",""} });
	}

	//Adds all the grids to the hard HashMap
	public void initializeHard(){
		hard_maps.put(1, new int[][] {{5,8,1,6,7,2,4,3,9}, {7,9,2,8,4,3,6,5,1}, {3,6,4,5,9,1,7,8,2}, {4,3,8,9,5,7,2,1,6}, {2,5,6,1,8,4,9,7,3}, {1,7,9,3,2,6,8,4,5},
			                          {8,4,5,2,1,9,3,6,7}, {9,1,3,7,6,8,5,2,4}, {6,2,7,4,3,5,1,9,8} });
		hard_maps.put(2, new int[][] {{2,7,6,3,1,4,9,5,8}, {8,5,4,9,6,2,7,1,3}, {9,1,3,8,7,5,2,6,4}, {4,6,8,1,2,7,3,9,5}, {5,9,7,4,3,8,6,2,1}, {1,3,2,5,9,6,4,8,7},
			                          {3,2,5,7,8,9,1,4,6}, {6,4,1,2,5,3,8,7,9}, {7,8,9,6,4,1,5,3,2} });
		hard_maps.put(3, new int[][] {{2,3,7,4,8,1,6,5,9}, {6,9,4,5,2,7,1,3,8}, {8,5,1,3,6,9,2,4,7}, {4,1,9,8,7,3,5,6,2}, {7,8,5,6,1,2,4,9,3}, {3,2,6,9,5,4,8,7,1},
			                          {1,4,3,2,9,5,7,8,6}, {5,7,8,1,3,6,9,2,4}, {9,6,2,7,4,8,3,1,5} });
		hard_maps.put(4, new int[][] {{1,5,7,3,6,9,2,8,4}, {6,3,4,8,2,5,9,1,7}, {2,8,9,7,1,4,6,5,3}, {7,9,2,4,5,6,1,3,8}, {3,6,1,9,8,7,5,4,2}, {8,4,5,1,3,2,7,9,6},
			                          {4,1,6,2,9,3,8,7,5}, {9,2,3,5,7,8,4,6,1}, {5,7,8,6,4,1,3,2,9} });
		hard_maps.put(5, new int[][] {{1,7,6,9,2,4,3,5,8}, {4,2,8,7,5,3,1,9,6}, {5,9,3,8,6,1,4,7,2}, {3,5,2,4,7,9,8,6,1}, {7,6,1,5,3,8,9,2,4}, {9,8,4,6,1,2,5,3,7},
			                          {8,4,5,2,9,7,6,1,3}, {2,1,9,3,8,6,7,4,5}, {6,3,7,1,4,5,2,8,9} });
		hard_unsolved.put(1, new String[][] {{"","","","6","","","4","",""}, {"7","","","","","3","6","",""}, {"","","","","9","1","","8",""}, {"","","","","","","","",""},
			                                 {"","5","","1","8","","","","3"}, {"","","","3","","6","","4","5"}, {"","4","","2","","","","6",""}, {"9","","3","","","","","",""},
			                                 {"","2","","","","","1","",""} });
		hard_unsolved.put(2, new String[][] {{"2","","","3","","","","",""}, {"8","","4","","6","2","","","3"}, {"","1","3","8","","","2","",""}, {"","","","","2","","3","9",""},
			                                 {"5","","7","","","","6","2","1"}, {"","3","2","","","6","","",""}, {"","2","","","","9","1","4",""}, {"6","","1","2","5","","8","","9"},
			                                 {"","","","","","1","","","2"} });
		hard_unsolved.put(3, new String[][] {{"","3","","4","8","","6","","9"}, {"","","","","2","7","","",""}, {"8","","","3","","","","",""}, {"","1","9","","","","","",""},
			                                 {"7","8","","","","2","","9","3"}, {"","","","","","4","8","7",""}, {"","","","","","5","","","6"}, {"","","","1","3","","","",""},
			                                 {"9","","2","","4","8","","1",""} });
		hard_unsolved.put(4, new String[][] {{"","","7","","","","2","8",""}, {"","","4","","2","5","","",""}, {"2","8","","","","4","6","",""}, {"","9","","","","6","","",""},
			                                 {"3","","","","","","","","2"}, {"","","","1","","","","9",""}, {"","","6","2","","","","7","5"}, {"","","","5","7","","4","",""},
			                                 {"","7","8","","","","3","",""} });
		hard_unsolved.put(5, new String[][] {{"","","","","2","","","",""}, {"","","","7","","3","","","6"}, {"","","","","","","4","7","2"}, {"","","2","","7","9","","6",""},
			                                 {"7","","1","5","","8","9","","4"}, {"","8","","6","1","","5","",""}, {"8","4","5","","","","","",""}, {"2","","","3","","6","","",""},
			                                 {"","","","","4","","","",""} });
	}
}