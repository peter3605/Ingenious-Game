import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


public class GameWindow{
	JFrame frame; 
	StartPanel pan;
	Game game;
	GameBoard gameBoard;
	GameWindow(){
		frame = new JFrame("Ingenious");
		//frame.setPreferredSize(new Dimension(600,300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pan = new StartPanel(true);

		frame.setContentPane(pan);
		frame.pack();
		frame.setVisible(true);
		while(!pan.isGameStart() && !pan.isAnalysisStart()){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(pan.isGameStart()){
			play(0);
		}else if(pan.isAnalysisStart()){
			play(1);
		}
		
	}

	public void play(int a){
		if(a == 0){
			Game game = new Game(pan.getNames(), pan.getPlayerTypes(), pan.getStrategies());
			GameBoard gameBoard = game.getGameBoard();
			new Thread(gameBoard).start();
			

			frame.setContentPane(gameBoard);
			frame.pack();
			frame.setVisible(true);
			try {
				game.play();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.dispose();
			frame = new GameOver(game.sortPlayers(), game.getSortedScores());
			frame.setPreferredSize(new Dimension(1500,800));
			frame.pack();
			frame.setVisible(true);
			while(!((GameOver) frame).cancel()){
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			frame.dispose();
		}else if(a == 1){
			pan.openAnalysisMode();
			frame.pack();
			frame.setVisible(true);
			while(!pan.isContinueClicked()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pan.fastOrSlow() == 0){

				frame.pack();
				frame.setVisible(true);
			}else{
//				frame.setContentPane(gameBoard);
//				frame.pack();
//				frame.setVisible(true);
			}
		

			Game game;
			GameBoard gameBoard;
			int[] wins = new int[pan.numPlayers()];
			for(int player = 0; player < pan.numPlayers(); player ++){
				wins[player] = 0;
			}
			for(int i = 0; i < pan.getGames(); i ++ ){
				game = new Game(pan.getNames(), pan.getPlayerTypes(), pan.getStrategies());
				gameBoard = game.getGameBoard();
				new Thread(gameBoard).start();
				if(pan.fastOrSlow() == 1){
					frame.setContentPane(gameBoard);
					frame.pack();
					frame.setVisible(true);
				}else{
					game.setSleepTimer(0);
				}
				try {
					game.play();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int player = 0; player < game.numPlayers(); player ++){
					if(game.sortPlayers()[0] == game.getPlayers()[player]){
						wins[player] += 1;
					}
					
				}
				game = null;
				gameBoard = null;
				
			}
			for(int player = 0; player < pan.numPlayers(); player ++){
				System.out.println(wins[player]);
			}
			StrategyResults strats = new StrategyResults(wins[0],wins[1]);
//			frame.removeAll();
			frame.setContentPane(strats);
			frame.pack();
			frame.setVisible(true);
			while(!strats.end()){
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			frame.dispose();
			
		}

	}
	
}
