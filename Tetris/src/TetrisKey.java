/***********************************************************
TetrisKey
	このクラスは、キー入力を検知するためのクラスです。
	TetrisGUI の canvas に結びついています。
	検知したキー入力の情報は、TetrisProc が参照します。
***********************************************************/

import org.eclipse.swt.events.*;
import org.eclipse.swt.SWT;

public class TetrisKey implements KeyListener {

	public boolean	UP;
	public boolean	DOWN;
	public boolean	LEFT;
	public boolean	RIGHT;
	public boolean	TURN_L;
	public boolean	TURN_R;
	public boolean	START;

	//キーボードが押されたときの処理
	public void keyPressed(KeyEvent e){
		switch(e.keyCode){
			case SWT.ARROW_UP :
				UP = true;
				break;
			case SWT.ARROW_DOWN :
				DOWN = true;
				break;
			case SWT.ARROW_LEFT :
				LEFT = true;
				break;
			case SWT.ARROW_RIGHT :
				RIGHT = true;
				break;
		}
		switch(e.character){
			case 'z' :
			case 'Z' :
				TURN_L = true;
				break;
			case 'x' :
			case 'X' :
				TURN_R = true;
				break;
			case ' ' :	//Space
			case 13 :	//Enter
				START = true;
				break;
		}
		
		//System.out.println((int)e.character+" : "+e.keyCode);
	}//end keyPressed


	//キーボードが離されたときの処理
	public void keyReleased(KeyEvent e){
		switch(e.keyCode){
			case SWT.ARROW_UP :
				UP = false;
				break;
			case SWT.ARROW_DOWN :
				DOWN = false;
				break;
			case SWT.ARROW_LEFT :
				LEFT = false;
				break;
			case SWT.ARROW_RIGHT :
				RIGHT = false;
				break;
		}
		switch(e.character){
			case 'z' :
			case 'Z' :
				TURN_L = false;
				break;
			case 'x' :
			case 'X' :
				TURN_R = false;
				break;
			case ' ' :	//Space
			case 13 :	//Enter
				START = false;
				break;
		}
	}//end keyReleased()

}