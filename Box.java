import java.awt.*;  
import java.awt.event.*;  
class Box extends Button implements MouseListener  {  
	
	int x;
	int y;
	int mates;
	boolean hasBomb;
	boolean revealed;
	boolean flagged;
	Main main;
	boolean clickable;
	
	public Box(int x,int y){
		this.x = x;
		this.y = y;
		this.revealed = false;
		this.clickable = false;
		setFont(new Font("Arial",Font.BOLD,18));
		addMouseListener(this);
	}
	
	public void setMain(Main main){
		this.main = main;
	}
	
	public void setMates(int mates){
		this.mates = mates;
	}
	
	public void reset(){
		mates = 0;
		hasBomb = false;
		flagged = false;
		revealed = false;
		clickable = true;
		setLabel(" ");
		setBackground(null);
		setForeground(Color.black);
	}
	
	public void setHasBomb(boolean hasBomb){
		this.hasBomb = hasBomb;
	}
	public void setClickable(boolean clickable){
		this.clickable = clickable;
	}
	public boolean getHasBomb(){
		return this.hasBomb;
	}
	
	public void showFlag(int status){ // -1 wrong flag(red) 0 new flag (black) 1 correct flag blue
		if(status < 0){
			setForeground(Color.red);
		}else if(status > 0){
			setForeground(Color.blue);
		}else{
			setForeground(Color.black);
		}
		setLabel("F");
	}
	
	public void showBomb(){
		if(flagged){
			showFlag(1);
		}else{
			setForeground(Color.red);
			setLabel("O");
		}
	}
	public void reveal(){
		revealed = true;
		setBackground(Color.lightGray);
		repaint();
		if(hasBomb){
			showBomb();
		}else if(mates > 0){
			setLabel(""+mates);
		}else{
			main.revealAdjucentBoxes(x,y);
		}
		main.decrementPristineBox();
	}
	public boolean getRevealed(){
		return this.revealed;
	}
	
	public boolean getFlagged(){
		return this.flagged;
	}
	
	public void mouseClicked(MouseEvent e){  
		if(this.clickable){
			if(e.getModifiers() == MouseEvent.BUTTON3_MASK){
				if(main.getRemainingFlag() > 0){
					if(!revealed){
						if(flagged){
							flagged = false;
							setForeground(Color.black);
							setLabel(" ");
							main.setRemainingFlag(main.getRemainingFlag()+1);
							main.incrementPristineBox();
						}else{
							flagged = true;
							showFlag(0);
							main.setRemainingFlag(main.getRemainingFlag()-1);
							main.decrementPristineBox();
							
							if (main.getPristineBox() == 0){
								main.stopGame(true);
							}
						}
					}
				}
			}else{
				if(!flagged){
					reveal();
					if(hasBomb){
						main.stopGame(false);
					}else if (main.getPristineBox() == 0){
						main.stopGame(true);
					}
				}
			}
		}
	}  
	
	public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}