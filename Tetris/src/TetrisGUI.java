/***********************************************************
TetrisGUI
	このクラスは、テトリスのＧＵＩを担当するクラスです。
	スクリーンへの描画も行います。
	TetrisField, TetrisBlock, TetrisKeyの各クラスと連携して
	います。
	(このソースでは、ＧＵＩに SWT を採用)
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
	コンストラクタ
	********************************************/
	TetrisGUI(TetrisField fld, TetrisBlock blk, TetrisKey key){

		display = new Display();		//ディスプレイ

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
		
		//色
		color[0] = new Color(null, Tetris.color[0][0], Tetris.color[0][1], Tetris.color[0][2]);	//空白
		color[1] = new Color(null, Tetris.color[1][0], Tetris.color[1][1], Tetris.color[1][2]);	//ブロック[壁]
		color[2] = new Color(null, Tetris.color[2][0], Tetris.color[2][1], Tetris.color[2][2]);	//ブロック[山]
		color[3] = new Color(null, Tetris.color[3][0], Tetris.color[3][1], Tetris.color[3][2]);	//ブロック[落下]
		
		//display = new Display();		//ディスプレイ ※この位置で生成するとエラー

		//ウィンドウの生成
	    shell   = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.SYSTEM_MODAL);
	    shell.setText("Tetris");
		shell.setLocation(ScreenX, ScreenY);
		shell.setSize(shell.computeSize(ScreenW, ScreenH));

		shell.addShellListener(new ShellListener() {//ShellAdapter()だと下記メソッドを全て実装する必要はない
			public void shellClosed(ShellEvent e){		//クローズ
				fg.dispose();		//表GCの破棄
				bg.dispose();		//裏GCの破棄
				display.dispose();	//displayの破棄
				System.exit(0);		//終了
			}
			public void shellActivated(ShellEvent e){	//アクティブ化
			}
			public void shellDeactivated(ShellEvent e){	//非アクティブ化
			}
			public void shellDeiconified(ShellEvent e){	//最小化からの復帰
			}
			public void shellIconified(ShellEvent e){	//最小化
			}
		});

		//キャンバス(表画面)の生成
		canvas = new Canvas(shell, SWT.NULL);
		canvas.setLocation(0, 0);
		canvas.setSize(ScreenW, ScreenH);
		canvas.setBackground(new Color(null, 0, 0, 0) );
		//キー処理クラスを登録
		canvas.addKeyListener(key);	

		//イメージ(裏画面)の生成
		image 	= new Image(display, ScreenW, ScreenH);

		fg = new GC(canvas);
		bg = new GC(image);
		bg.setBackground(new Color(null,0,0,0));
		bg.fillRectangle(0, 0, ScreenW, ScreenH);
	}

	
	/********************************************
	画面を表示
	********************************************/
	public void windowShow(){
		//frame.show();
		shell.open();

	}
	
	
	/********************************************
	フィールドを描画:裏画面(bg)
	********************************************/
	public void fieldDraw(){

		for(int row=0; row <= FldRows-1; row++){
		for(int col=0; col <= FldCols-1; col++){
			if(fld.field[row][col] == 0){
				//ブロックなし
				bg.setBackground(color[0]);
				bg.fillRectangle(aSIZE*col+1, aSIZE*row+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*i+1, aSIZE*j+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*i, aSIZE*j, aSIZE, aSIZE);
				
			}
			else if(fld.field[row][col] == 9){
				//ブロック(壁)
				bg.setBackground(color[1]);
				bg.fillRectangle(aSIZE*col+1, aSIZE*row+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*i+1, aSIZE*j+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*i, aSIZE*j, aSIZE, aSIZE);
			}
			else{
				//ブロック(山)
				bg.setBackground(color[2]);
				bg.fillRectangle(aSIZE*col+1, aSIZE*row+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*i+1, aSIZE*j+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*i, aSIZE*j, aSIZE, aSIZE);
			}
		}
		}
	}//end fieldDraw()
	

	/********************************************
	ブロックを描画:裏画面(bg)
	********************************************/	
	public void blockDraw(){
		
		for(int row=0; row < blk.block.length; row++){
		for(int col=0; col < blk.block.length; col++){
			if(blk.block[row][col] != 0){
				//ブロックあり
				bg.setBackground(color[3]);
				bg.fillRectangle(aSIZE*(col+blk.x)+1, aSIZE*(row+blk.y)+1, aSIZE-2, aSIZE-2);
				//bg.fillRoundRectangle(aSIZE*(col+blk.x)+1, aSIZE*(row+blk.y)+1, aSIZE-1, aSIZE-1, 8, 8);
				//bg.fillOval(aSIZE*(col+blk.x), aSIZE*(row+blk.y), aSIZE, aSIZE);
				
			}
		}
		}
	}//end blockDraw()
	
	
	/********************************************
	裏画面(bg) → 表画面(fg)
	********************************************/	
	public void BGtoFG(){
	    GC fg = new GC(canvas);
		fg.drawImage(image, 0, 0);
		fg.dispose();			
	}//end BGtoFG()


}//end class TetrisGUI
