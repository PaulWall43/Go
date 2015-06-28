import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.*; 
public class Mat extends JPanel{
	private Board board; 
//	private JButton passButton;
//	private JButton resetButton;
//	private JButton undoButton; 
	//Constructor
	public Mat(Board board){
		//Will add strategy pattern if the need arises for many buttons
		JButton passButton = new JButton("Pass");
		JButton resetButton = new JButton("Reset Board");
		JButton undoButton = new JButton("Undo");
		this.add(passButton);	
		this.add(resetButton);
		this.add(undoButton);
		this.isVisible(); 
		this.board = board; 
		//Will clean this up later when I have full access to Java Documentation
		PassAction passAction = new PassAction();
		ResetAction resetAction = new ResetAction();
		UndoAction undoAction = new UndoAction(); 
		passButton.addActionListener(passAction); 
		resetButton.addActionListener(resetAction);
		undoButton.addActionListener(undoAction); 
	}
	
	public void pass(){
		board.pass(); 
	}
	
	public void resetBoard(){
		board.resetBoard();
	}
	
	public void undo(){
		board.undo(); 
	}
	
	
	public class PassAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Mat.this.pass(); 
		}
	}
	public class ResetAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Mat.this.resetBoard(); 
		}
	}
	public class UndoAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Mat.this.undo(); 
		}
	}
}
