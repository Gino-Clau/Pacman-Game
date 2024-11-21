package App;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;



public class PacMan extends JPanel implements ActionListener, KeyListener{
class Block{
	int x;
	int y;
	int width;
	int height;
	Image image;
	
	int startX;
	int startY;
	char direction = 'U';
	int velocityX = 0;
	int velocityY = 0;
	
	Block(Image image, int x, int y, int width, int height){
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.startX = x;
		this.startY = y;
	}
	
	void characterDirection(char direction) {
		char prevDirection = this.direction;
		this.direction = direction;
		updateVelocity();
		this.x += velocityX;
		this.y += velocityY;
		for(Block wall : walls) {
			if(collision(this,wall)) {
				this.x -= velocityX;
				this.y -= velocityY;
				this.direction = prevDirection;
				updateVelocity();
			}
		}
	}
	
	void updateVelocity() {
		if(this.direction == 'U') {
			this.velocityX = 0;
			this.velocityY = -tileSize/4;
		}else if(this.direction == 'D') {
			this.velocityX = 0;
			this.velocityY = tileSize/4;
		}else if(this.direction == 'R') {
			this.velocityX = tileSize/4;
					this.velocityY = 0;
		}else if(this.direction == 'L') {
			this.velocityX = -tileSize/4;
			this.velocityY = 0;
		}
	}
void reset() {
	this.x =  this.startX;
	this.y = this.startY;
}
}
	private int rowCount = 21;
	private int columnCount = 19;
	private int tileSize = 32;
	private int boardWidth = tileSize * columnCount;
	private int boardHeight = tileSize * rowCount;
	
	private Image wallImage;
	private Image blueghostImage;
	private Image pinkghostImage;
	private Image orangeghostImage;
	private Image redghostImage;
	
	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;
	
	//X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
	 private String[] tileMap = {
		        "XXXXXXXXXXXXXXXXXXX",
		        "X        X        X",
		        "X XX XXX X XXX XX X",
		        "X                 X",
		        "X XX X XXXXX X XX X",
		        "X    X       X    X",
		        "XXXX XXXX XXXX XXXX",
		        "OOOX X       X XOOO",
		        "XXXX X XXrXX X XXXX",
		        "X       bpo       X",
		        "XXXX X XXXXX X XXXX",
		        "OOOX X       X XOOO",
		        "XXXX X XXXXX X XXXX",
		        "X        X        X",
		        "X XX XXX X XXX XX X",
		        "X  X     P     X  X",
		        "XX X X XXXXX X X XX",
		        "X    X   X   X    X",
		        "X XXXXXX X XXXXXX X",
		        "X                 X",
		        "XXXXXXXXXXXXXXXXXXX" 
		    };
	
	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block PacMan;
	
	Timer gameloop;
	
	char[] directions = {'U', 'D', 'L', 'R'};
	Random random = new Random();
	int score = 0;
	int lives  = 3;
	boolean gameOver = false;
	PacMan(){
		setPreferredSize(new Dimension(boardWidth,boardHeight));
		setBackground(Color.BLACK);
		addKeyListener(this);
		setFocusable(true);
		
		//load images
		wallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();
		blueghostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
		pinkghostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
		orangeghostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
		redghostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();
		
		pacmanUpImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
		pacmanDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
		pacmanLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();
		pacmanRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();
		
		loadMap();
		for(Block ghost : ghosts) {
			char newDirection = directions[random.nextInt(4)];
			ghost.characterDirection(newDirection);
		}
		gameloop = new Timer(50,this);
		gameloop.start();
	}
	
	
	public void loadMap() {
		walls = new HashSet<Block>();
		foods = new HashSet<Block>();
		ghosts = new HashSet<Block>();
		
		for(int r = 0; r < rowCount; r++) {
			for(int c = 0; c < columnCount; c++) {
			
				String row = tileMap[r];
				char tilemapChar = row.charAt(c);
				
				int x = c * tileSize;
				int y = r * tileSize;
				
				if(tilemapChar == 'X'){//block wall
					
					Block wall = new Block(wallImage,x,y,tileSize,tileSize);
					walls.add(wall);
				}
				else if(tilemapChar == 'b') { //blue ghost
				
					Block ghost = new Block(blueghostImage,x,y,tileSize,tileSize);
				    ghosts.add(ghost);
				}
				else if(tilemapChar == 'o') {//orange ghost
				
					Block ghost = new Block(orangeghostImage,x,y,tileSize,tileSize);
				    ghosts.add(ghost);
				}
				else if(tilemapChar == 'r'){ //red ghost
				
					Block ghost = new Block(redghostImage,x,y,tileSize,tileSize);
				    ghosts.add(ghost);
				}
				else if(tilemapChar == 'p') {//pink ghost
					Block ghost = new Block(pinkghostImage,x,y,tileSize,tileSize);
				    ghosts.add(ghost);
				}
				else if(tilemapChar == 'P') {//pacman
					PacMan = new Block(pacmanRightImage,x,y,tileSize,tileSize);
				}
				
				else if(tilemapChar == ' ') {
					Block food = new Block(null,x + 14,y + 14, 4,4);
					foods.add(food);
				}

			}
		}
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		g.drawImage(PacMan.image,PacMan.x, PacMan.y, PacMan.width, PacMan.height, null);
		
		for(Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
		}
		for(Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
		}
		
		g.setColor(Color.WHITE);
		for(Block food : foods) {
			g.fillRect(food.x, food.y, food.width, food.height);
		}
		g.setFont(new Font("Arial", Font.PLAIN , 18));
		if(gameOver) {
			g.drawString("Game Over Bozo : " + String.valueOf(score), tileSize/2, tileSize/2);
		}else {
			g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
		}
	}
    public void move() {
	 PacMan.x += PacMan.velocityX;
	 PacMan.y += PacMan.velocityY;
	 
	 for(Block wall : walls) {
		 if(collision(PacMan, wall)) {
			 PacMan.x -= PacMan.velocityX;
			 PacMan.y -= PacMan.velocityY;
			 break;
		 }
	 }
	
	 for(Block ghost : ghosts) {
		 if(collision(ghost, PacMan)) {
			 lives -= 1;
			 if(lives == 0) {
				 gameOver = true;
				 return;
			 }
			 resetPositions();
		 }
		 if(ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
			ghost.characterDirection('U'); 
		 }
		 ghost.x += ghost.velocityX;
		 ghost.y += ghost.velocityY;
		 for(Block wall : walls) {
			 if(collision(ghost, wall)) {
				 ghost.x -= ghost.velocityX;
				 ghost.y -= ghost.velocityY;
				 char newDirection = directions[random.nextInt(4)];
				 ghost.characterDirection(newDirection);
			 }
		 }
	 }
	 
	 Block foodEaten = null;
	 for(Block food : foods) {
		 if(collision(PacMan, food)) {
			 foodEaten = food;
			 score += 10;
		 }
	 }
	 foods.remove(foodEaten);
	 if(foods.isEmpty()) {
		 loadMap();
		 resetPositions();
	 }
    }

    
    
    public boolean collision(Block a, Block b) {
    	 return a.x < b.x + b.width &&
    		       a.x + a.width > b.x &&
    		       a.y < b.y + b.height &&
    		       a.y + a.height > b.y;
    }
    
    void resetPositions() {
      
        PacMan.reset();
        PacMan.velocityX = 0;
        PacMan.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            
         
            char newDirection = directions[random.nextInt(4)];
            ghost.characterDirection(newDirection);
        }
    }

    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	move();
	repaint();
	if(gameOver) {
		gameloop.stop();
	}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(gameOver) {
			loadMap();
			resetPositions();
			lives = 3;
			score = 0;
			gameOver = false;
			gameloop.start();
		}
	if(e.getKeyCode() == KeyEvent.VK_UP) {
		PacMan.characterDirection('U');
	}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
		PacMan.characterDirection('D');
	}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
		PacMan.characterDirection('L');
	}else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
		PacMan.characterDirection('R');
	}
		
	if(PacMan.direction == 'U') {
		PacMan.image = pacmanUpImage;
	}else if(PacMan.direction == 'D') {
		PacMan.image = pacmanDownImage;
	}else if(PacMan.direction == 'L') {
		PacMan.image = pacmanLeftImage;
	}else if(PacMan.direction == 'R') {
		PacMan.image = pacmanRightImage;
	}
	
	}
}
