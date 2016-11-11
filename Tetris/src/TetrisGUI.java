/***********************************************************
TetrisGUI
	���̃N���X�́A�e�g���X�̂f�t�h��S������N���X�ł��B
	�X�N���[���ւ̕`����s���܂��B
	TetrisField, TetrisBlock, TetrisKey�̊e�N���X�ƘA�g����
	���܂��B
	(���̃\�[�X�ł́A�f�t�h�� SWT ���̗p)
***********************************************************/

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class TetrisGUI {
	public  Display 		display;
	public  Shell   		shell;
 	private Canvas 			canvas;
	private Image			image;

	private TetrisField	fld;
	private TetrisBlock	blk;
	private TetrisKey	key;
	
	private int aSIZE;
	private int	ScreenX;
	private int	ScreenY;
	private int	ScreenW;
	private int	ScreenH;
	private int	FldRows;
	private int	FldCols;
	
	private Color[] color = new Color[4];
	
	//private Graphics fg;
	//private Graphics bg;
	private GC	fg;
	private GC	bg;

	/********************************************
	�R���X�g���N�^
	********************************************/
	TetrisGUI(TetrisField fld, TetrisBlock blk, TetrisKey key){

		display = new Display();		//�f�B�X�v���C

		this.key 	= key;
		this.fld 	= fld;
		this.blk 	= blk;
		
		aSIZE	= Tetris.BlockSize;
		ScreenX	= Tetris.ScreenX;
		ScreenY	= Tetris.ScreenY;
		ScreenW	= Tetris.ScreenW;
		ScreenH	= Tetris.ScreenH;
		FldRows	= Tetris.FieldRows;
		FldCols	= Tetris.FieldCols;
		
		//�F
		color[0] = new Color(null, Tetris.color[0][0], Tetris.color[0][1], Tetris.color[0][2]);	//��
		color[1] = new Color(null, Tetris.color[1][0], Tetris.color[1][1], Tetris.color[1][2]);	//�u���b�N[��]
		color[2] = new Color(null, Tetris.color[2][0], Tetris.color[2][1], Tetris.color[2][2]);	//�u���b�N[�R]
		color[3] = new Color(null, Tetris.color[3][0], Tetris.color[3][1], Tetris.color[3][2]);	//�u���b�N[����]
		
		//display = new Display();		//�f�B�X�v���C �����̈ʒu�Ő�������ƃG���[

		//�E�B���h�E�̐���
	    shell   = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.SYSTEM_MODAL);
	    shell.setText("Tetris");
		shell.setLocation(ScreenX, ScreenY);
		shell.setSize(shell.computeSize(ScreenW, ScreenH));

		shell.addShellListener(new ShellListener() {//ShellAdapter()���Ɖ��L���\�b�h��S�Ď�������K�v�͂Ȃ�
			public void shellClosed(ShellEvent e){		//�N���[�Y
				fg.dispose();		//�\GC�̔j��
				bg.dispose();		//��GC�̔j��
				display.dispose();	//display�̔j��
				System.exit(0);		//�I��
			}
			public void shellActivated(ShellEvent e){	//�A�N�e�B�u��
			}
			public void shellDeactivated(ShellEvent e){	//��A�N�e�B�u��
			}
			public void shellDeiconified(ShellEvent e){	//�ŏ�������̕��A
			}
			public void shellIconified(ShellEvent e){	//�ŏ���
			}
		});

		//�L�����o�X(�\���)�̐���
		canvas = new Canvas(shell, SWT.NULL);
		canvas.setLocation(0, 0);
		canvas.setSize(ScreenW, ScreenH);
		canvas.setBackground(new Color(null, 0, 0, 0) );
		//�L�[�����N���X��o�^
		canvas.addKeyListener(key);	

		//�C���[�W(�����)�̐���
		image 	= new Image(display, ScreenW, ScreenH);

		fg = new GC(canvas);
		bg = new GC(image);
		bg.setBackground(new Color(null,0,0,0));
		bg.fillRectangle(0, 0, ScreenW, ScreenH);
	}

	
	/********************************************
	��ʂ�\��
	********************************************/
	public void windowShow(){
		//frame.show();
		shell.open();

	}
	
	
	/********************************************
	�t�B�[���h��`��:�����(bg)
	********************************************/
	public void fieldDraw(){

		for(int row=0; row <= FldRows-1; row++){
		for(int col=0; col <= FldCols-1; col++){
			if(fld.field[row][col] == 0){
				//�u���b�N�Ȃ�
				bg.setBackground(color[0]);
				bg.fillRectangle(aSIZE*col+1, aSIZE*row+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*i+1, aSIZE*j+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*i, aSIZE*j, aSIZE, aSIZE);
				
			}
			else if(fld.field[row][col] == 9){
				//�u���b�N(��)
				bg.setBackground(color[1]);
				bg.fillRectangle(aSIZE*col+1, aSIZE*row+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*i+1, aSIZE*j+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*i, aSIZE*j, aSIZE, aSIZE);
			}
			else{
				//�u���b�N(�R)
				bg.setBackground(color[2]);
				bg.fillRectangle(aSIZE*col+1, aSIZE*row+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*i+1, aSIZE*j+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*i, aSIZE*j, aSIZE, aSIZE);
			}
		}
		}
	}//end fieldDraw()
	

	/********************************************
	�u���b�N��`��:�����(bg)
	********************************************/	
	public void blockDraw(){
		
		for(int row=0; row < blk.block.length; row++){
		for(int col=0; col < blk.block.length; col++){
			if(blk.block[row][col] != 0){
				//�u���b�N����
				bg.setBackground(color[3]);
				bg.fillRectangle(aSIZE*(col+blk.x)+1, aSIZE*(row+blk.y)+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*(col+blk.x)+1, aSIZE*(row+blk.y)+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*(col+blk.x), aSIZE*(row+blk.y), aSIZE, aSIZE);
				
			}
		}
		}
	}//end blockDraw()
	
	
	/********************************************
	�����(bg) �� �\���(fg)
	********************************************/	
	public void BGtoFG(){
	    GC fg = new GC(canvas);
		fg.drawImage(image, 0, 0);
		fg.dispose();			
	}//end BGtoFG()


}//end class TetrisGUI
