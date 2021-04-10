import java.util.Date;
import java.text.SimpleDateFormat;

class PlayerScore{
	
	int score;
	int time;
	int difficulty;
	Date date;
	Player player;
	SimpleDateFormat inputFormatter;
	SimpleDateFormat outputFormatter;
	
	public PlayerScore(){
		inputFormatter = new SimpleDateFormat();
	}
	
	public PlayerScore(Player player,int difficulty,int score,int time){
		this.player = player;
		this.difficulty = difficulty;
		this.score = score;
		this.time = time;
		
		inputFormatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		outputFormatter = new SimpleDateFormat( "MMM d,Y HH:mm");
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getTime(){
		return time;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
	
	public void setDifficulty(int difficulty){
		this.difficulty = difficulty;
	}
	
	public String getDate(){
		return outputFormatter.format(date);
	}
	public String getDate2(){
		return inputFormatter.format(date);
	}
	public void setDate(Date date){
		this.date = date;
	}
	public void setDateStr(String date){
		try{
			this.date = inputFormatter.parse(date);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}