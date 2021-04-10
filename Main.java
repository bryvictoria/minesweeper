import java.awt.*;  
import java.awt.event.*;  
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.util.Date;
class Main extends Frame {  

	
	Random rand;
	
	int rows;
	int cols;
	int bombCount;
	Box[][] boxes;
	Bomb[] bombs;
	FileHandler fileHandler;
	boolean playing = false;
	int remainingFlagCount;
	Label flagCountLabel;
	Label timerLabel;
	Timer timer;
	int time;
	
	Panel boxPanel;
	MenuBar menuBar;
	Menu settingsMenu, levelsMenu,scoreMenu,aboutMenu;
	Menu levelsMenuItem;  
	MenuItem showScoreMenuItem,resetScoreMenuItem,showAboutMenuItem;
	CheckboxMenuItem easyMenuItem;
	CheckboxMenuItem mediumMenuItem;
	CheckboxMenuItem hardMenuItem;
	
	GridLayout boxLayout;
	
	int[][] difficulties;
	int difficulty;
	int pristineBoxCount = 0;
	
	PlayerScore[][] playerHighscores;
	
	String highscorefilename;
	int maxRank;
	
	Main(){  
	
		fileHandler = new FileHandler();
		maxRank = 10;
		playerHighscores = new PlayerScore[3][maxRank];
		
		highscorefilename = "highscores.txt";
		readHighscores();
		
		difficulties = new int[3][3];
		difficulties[0][0] = 9;
		difficulties[0][1] = 8;
		difficulties[0][2] = 10;
		difficulties[1][0] = 12;
		difficulties[1][1] = 10;
		difficulties[1][2] = 20;
		difficulties[2][0] = 14;
		difficulties[2][1] = 12;
		difficulties[2][2] = 29;
		
		buildGUI();
		
		timer = new Timer(1000,new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTimer();  
			}
        });
		
		easyMenuItem.setState(true);
		difficulty = 0;
		setGame();
		
		setVisible(true);
	
	}
	public void buildGUI(){
		
		setTitle("Minesweeper");
		setSize(400,600);
		setLayout(new BorderLayout());
		
		menuBar = new MenuBar();
		settingsMenu = new Menu("Settings");
		scoreMenu = new Menu("High Score");
		aboutMenu = new Menu("About");
		levelsMenuItem=new Menu("Difficulty"); 
		
		easyMenuItem = new CheckboxMenuItem("Easy");
		mediumMenuItem = new CheckboxMenuItem("Medium");
		hardMenuItem = new CheckboxMenuItem("Hard");
		
		showAboutMenuItem = new MenuItem("Show About");
		showScoreMenuItem = new MenuItem("Show HighScores");
		resetScoreMenuItem = new MenuItem("Reset Scores");
		aboutMenu.add(showAboutMenuItem);
		scoreMenu.add(showScoreMenuItem);
		scoreMenu.add(resetScoreMenuItem);
		
		easyMenuItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				difficulty = 0;
				setGame();  
				
				easyMenuItem.setState(true);
				mediumMenuItem.setState(false);
				hardMenuItem.setState(false);
			}
		});  
		mediumMenuItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				difficulty = 1;
				setGame();  
				easyMenuItem.setState(false);
				mediumMenuItem.setState(true);
				hardMenuItem.setState(false);
			}
		});  
		hardMenuItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				difficulty = 2;
				setGame();  
				
				
				easyMenuItem.setState(false);
				mediumMenuItem.setState(false);
				hardMenuItem.setState(true);
			
			}
		});
		showAboutMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {            
				String message = "";
				message += "Minesweeper version 2.0.1";
				message += "\r\n";
				message += "Author: Milagros Marie G Pasco";
				showMessage(message);
			}    
		});
		showScoreMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {            
				String message = "";
				message += "HIGHSCORES";
				
				message += "\r\n";
				
				
				String[] difficultyNames = {"Easy","Medium","Hard"};
				int index= 0;
				
				for(PlayerScore[] playerHighscore: playerHighscores){
					
					message += "\r\n";
					message += difficultyNames[index];
					for(PlayerScore score: playerHighscore){
						if(score != null){
							message += "\r\n";
							message += score.getPlayer().getName();
							message += "|";
							message += score.getScore();
							message += "|";
							message += score.getTime();
							message += "|";
							message += score.getDate();
						}
					}
					index++;
				}
				showMessage(message);
			}    
		});
		levelsMenuItem.add(easyMenuItem);
		levelsMenuItem.add(mediumMenuItem);
		levelsMenuItem.add(hardMenuItem);
		
		menuBar.add(settingsMenu);
		menuBar.add(scoreMenu);
		menuBar.add(aboutMenu);
		settingsMenu.add(levelsMenuItem);
		setMenuBar(menuBar);
		
		
		boxLayout = new GridLayout(1,1);
		boxPanel = new Panel();
		boxPanel.setLayout(boxLayout);
		
		Panel topPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
		
		add(topPanel, BorderLayout.NORTH);
		add(boxPanel, BorderLayout.CENTER);
		
		Button startButton = new Button("NEW GAME");
		flagCountLabel = new Label();
		timerLabel = new Label("00:00");
		topPanel.add(flagCountLabel);
		topPanel.add(startButton);
		topPanel.add(timerLabel);
		
		startButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				startGame();  
			}  
		});  
		
		addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {

                System.exit(0);
            }
        });
	}
	
	
	public void readHighscores(){
		try{
			
			String content = fileHandler.readFile(highscorefilename);
			String[] difficultyPlayerScores = content.split("\r\n");
			int difficultyIndex = 0;
			int playerScoreIndex = 0;
			
			for(String score:difficultyPlayerScores){

				String[] playerScores = score.split(",");
				playerScoreIndex = 0;
				for(String playerScore : playerScores){
					String[] playerScoreDetails = playerScore.split("\\|");
					if(playerScoreDetails.length == 4){
						Player nPlayer = new Player(playerScoreDetails[0]);
						PlayerScore nPlayerScore = new PlayerScore(nPlayer,difficultyIndex,Integer.parseInt(playerScoreDetails[1]),Integer.parseInt(playerScoreDetails[2]));
						nPlayerScore.setDateStr(playerScoreDetails[3]);
						playerHighscores[difficultyIndex][playerScoreIndex] = nPlayerScore;
						
						playerScoreIndex++;
					}
				}
				difficultyIndex++;
			}	
		}catch(Exception e){
			
		}
		
	}
	public void saveHighscores(){
		String content = "";
		
		for(PlayerScore[] playerHighscore: playerHighscores){
					
			for(PlayerScore score: playerHighscore){
				if(score != null){
					content += score.getPlayer().getName();
					content += "|";
					content += score.getScore();
					content += "|";
					content += score.getTime();
					content += "|";
					content += score.getDate2();
					content += ",";
				}
			}
			
			content += "\r\n";
		}
		
		fileHandler.writeFile(highscorefilename,content);
		
	}
	public void setGame(){
		rows = difficulties[difficulty][0];
		cols = difficulties[difficulty][1];
		bombCount = difficulties[difficulty][2];
		boxes = new Box[rows][cols];
		bombs = new Bomb[bombCount];
		
		boxLayout.setRows(rows);
		boxLayout.setColumns(cols);
		
		boxPanel.removeAll();
		
		rand = new Random();
		for(int i = 0; i<bombCount;i++){
			bombs[i] = new Bomb();
		}
		
		for(int y =0; y< rows;y++){
			for(int x =0; x < cols;x++){
				
				boxes[y][x] = new Box(x,y);  
				boxes[y][x].setMain(this);
				boxPanel.add(boxes[y][x]);
			}
		}
		validate();
		
	}
	
	public void startGame(){
		
		setRemainingFlag(bombCount);
		pristineBoxCount = rows*cols;
		
		playing = true;
		time = 0;
		timer.start();
		int bx,by;
		for(int i = 0; i<bombCount;i++){
			
			bx = rand.nextInt(cols);
			by = rand.nextInt(rows);
			bombs[i].setX(bx);
			bombs[i].setY(by);
		
		}
		
		for(int y =0; y< rows;y++){
			for(int x =0; x < cols;x++){
				boxes[y][x].reset();
				if(checkBomb(x,y)){
					boxes[y][x].setHasBomb(true);
				}
			}
		}
		
		for(int y =0; y< rows;y++){
			for(int x =0; x < cols;x++){
				if(!boxes[y][x].getHasBomb()){
					int bombMates = getBombMates(x,y);
					boxes[y][x].setMates(bombMates);
				}
			}
		}
		
	}
	
	
	
	public void stopGame(boolean won){
		playing = false;
		timer.stop();
		
		int score = 0;
		
		for(int y = 0; y< rows;y++){
			for(int x = 0; x < cols;x++){
				boxes[y][x].setClickable(false);
				if(boxes[y][x].getHasBomb()){
					boxes[y][x].showBomb();
					
					if(boxes[y][x].getFlagged()){
						score += 100;
					}else{
						score -= 10;
					}
					
				}else{
					if(boxes[y][x].getFlagged()){
						boxes[y][x].showFlag(-1);
						score -= 100;
					}
				}
			}
		}
		
		// easy 1 minute, medium 2 minutes, hard 3minutes
		int maxtime = (difficulty+1) * 60; 
		
		if(time < maxtime){
			score += (maxtime-time) * 10;
		}
		
		String message = "";
		if(won){
			message += "You WON!!!";
			message += "\r\n";
			message += "Your Time:"+formatTime();
			message += "\r\n";
			message += "Your Score:"+score;
			
			int rank = getRank(score);
			if(rank >= 0){
				message += "\r\n";
				message += "You were rank #"+(rank+1);
				message += "\r\n";
				message += "Please enter your name:";
				message += "\r\n";
				
				String name = showInputMessage(message);
				if(name.length() > 0){
					
					Player nPlayer = new Player(name);
					PlayerScore nPlayerScore = new PlayerScore(nPlayer,difficulty,score,time);
					nPlayerScore.setDate(new Date());
					
					for(int i = maxRank -1;i >= rank; i--){
						if(playerHighscores[difficulty][i] != null)
							playerHighscores[difficulty][i+1] = playerHighscores[difficulty][i];
					}
					playerHighscores[difficulty][rank] = nPlayerScore;
					saveHighscores();
				}
			}else{
				showMessage(message);
			}
		}
		
		
	}
	public int getRank(int score){
		int rank = -1;
		if(playerHighscores[difficulty] != null){
			int index = 0;
			for(PlayerScore ps:playerHighscores[difficulty]){
				if(ps != null){
					if(score > ps.getScore()){
						rank = index;
						break;
					}
					index++;

				}else{
					rank = index;
					break;
				}
			}
			
		}
		
		return rank;
		
	}
	public void showMessage(String message){
		JOptionPane.showMessageDialog(null, message);
	}	
	
	public String showInputMessage(String message){
		String input = JOptionPane.showInputDialog(null, message);
		return input;
	}
	public String formatTime(){
		String timeString = "";
		timeString += String.format("%02d", (int)Math.floor(time/60));
		timeString += ":";
		timeString += String.format("%02d", time%60);
		return timeString;
	}
	public void setTimer(){
		time++;
		timerLabel.setText(formatTime());
	}
	
	
	public void revealAdjucentBoxes(int x,int y){
		for(int yy = (y-1);yy < (y+2);yy++){
			for(int xx = (x-1);xx < (x+2);xx++){
				if(yy >= 0 && yy < rows && xx >= 0 && xx < cols && !(x == xx && y ==yy)){
					if(!boxes[yy][xx].getFlagged())
						if(!boxes[yy][xx].getRevealed())
							boxes[yy][xx].reveal();
				}
			}
		}
	}
	
	
	
	public int getBombMates(int x, int y){
		
		int bombMates = 0;
		
		for(int yy = (y-1);yy < (y+2);yy++){
			for(int xx = (x-1);xx < (x+2);xx++){
				if(yy >= 0 && yy < rows && xx >= 0 && xx < cols ){
					if(boxes[yy][xx].getHasBomb()){
						bombMates++;
					}
				}
			}
		}
		
		return bombMates;
	}
	
	public boolean checkBomb(int x,int y){
		boolean hasBomb = false;
		for(Bomb bomb:bombs){
			if(bomb.getX() == x && bomb.getY() == y){
				hasBomb = true;
				break;
			}
		}
		return hasBomb;
	}
	
	
	public void setRemainingFlag(int remainingFlagCount){
		this.remainingFlagCount = remainingFlagCount;
		flagCountLabel.setText("REMAINING FLAG: "+remainingFlagCount);
		validate();
	}
	
	public int getRemainingFlag(){
		return remainingFlagCount;
	}
	public void setPristineBox(int pristineBoxCount){
		this.pristineBoxCount = pristineBoxCount;
	}
	
	public void decrementPristineBox(){
		this.pristineBoxCount -= 1;
	}
	
	public void incrementPristineBox(){
		this.pristineBoxCount += 1;
	}
	
	public int getPristineBox(){
		return pristineBoxCount;
	}
}