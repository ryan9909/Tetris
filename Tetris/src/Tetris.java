/***********************************************************
Tetris
	このクラスは、テトリスの開始クラスであり、テトリス全体を
	取りまとめる外殻的クラスでもあります。
	また、起動時にiniファイルから設定情報を読み込みます。
***********************************************************/
import java.io.*;

public class Tetris {
	//テトリス表示サイズ
	static public int BlockSize;	//ブロック一つのドットサイズ
	static public int FieldCols;	//フィールドの横マス数(左右は壁)
	static public int FieldRows;	//フィールドの縦マス数(下は底壁)

	//ウィンドウ表示サイズ（テトリス表示サイズで決まる）
	static public int ScreenW;		//ウィンドウ幅
	static public int ScreenH;		//ウィンドウ高さ

	//ウィンドウ表示位置
	static public int ScreenX;		//ウィンドウ表示位置左上座標Ｘ
	static public int ScreenY;		//ウィンドウ表示位置左上座標Ｙ

	//色
	static int[][] color = new int[4][3];

	//関連するクラスの宣言
	private TetrisField		fld;
	private TetrisBlock		blk;
	private TetrisKey		key;
	private TetrisGUI		gui;
	private TetrisProc		prc;
	
	
	/********************************************
	コンストラクタ
	********************************************/
	public Tetris() {
		iniRead();
		
		fld		= new TetrisField();					//フィールド
		blk		= new TetrisBlock();					//ブロック
		key 	= new TetrisKey();						//キー入力
		gui		= new TetrisGUI(fld, blk, key);			//ＧＵＩ
		prc		= new TetrisProc(fld, blk, key, gui);	//プロシージャ(処理)


		//ウィンドウ表示
		gui.windowShow();

		
		//sleep0.5秒
		try {
			Thread.sleep(500);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		/*
		//ループ(AWT, Swing仕様)
	    while (true){
			//実行
			prc.tetrisExec();
	    }
		*/
		
		
		//ループ(SWT仕様)
	    while (!gui.shell.isDisposed()){
			if(!gui.display.readAndDispatch()){
				//実行
				prc.tetrisExec();
			}
	    }
		

	}//end Tetris()

	
	/********************************************
	iniファイル読込
	********************************************/
	protected void iniRead(){
		String	str, item, value;
		int		p;
		
		try{
			FileReader		fr	= new FileReader("tetris.ini");
			BufferedReader	buf	= new BufferedReader(fr);
            while((str = buf.readLine()) != null) {
				
				p = str.indexOf("=", 0);	//p:"="の位置
				if(p == -1)continue;
                item  = str.substring(0, p);
				value = str.substring(p+1);
				
				if(item.equals("BLOCKSIZE")){
					BlockSize = Integer.parseInt(value);
				}else if(item.equals("FIELDCOLS")){
					FieldCols = Integer.parseInt(value) + 2;
				}else if(item.equals("FIELDROWS")){
					FieldRows = Integer.parseInt(value) + 1;
        		}else if(item.equals("SCREENX")){
					ScreenX   = Integer.parseInt(value);
        		}else if(item.equals("SCREENY")){
					ScreenY   = Integer.parseInt(value);
				}else if(item.equals("COLOR0")){
					colorSet(item, value);
				}else if(item.equals("COLOR1")){
					colorSet(item, value);
				}else if(item.equals("COLOR2")){
					colorSet(item, value);
				}else if(item.equals("COLOR3")){
					colorSet(item, value);
        		}
            }//end while
            buf.close();
		}catch(Exception ex){	//iniファイルに不備があるとき
			BlockSize = 18;
			FieldCols = 10+2;
			FieldRows = 20+1;
			ScreenX   = 300;
			ScreenY   = 100;
			color = new int[][] {
				{90,90,90},		//空白
				{0,0,255},		//ブロック[壁]
				{0,255,0},		//ブロック[山]
				{255,0,0},		//ブロック[落下]
			};
		}
		
		ScreenW = BlockSize * FieldCols;
		ScreenH = BlockSize * FieldRows;

	}//end iniRead()
	
	
	/********************************************
	色のセット
	********************************************/
	public void colorSet(String item, String value){
		int n = Integer.parseInt(item.substring(item.length()-1));
		int	p1, p2;
		
		p1 = value.indexOf(",", 0);		//p1:１つ目の","の位置
		p2 = value.indexOf(",", p1+1);	//p2:２つ目の","の位置
		color[n][0]  = Integer.parseInt(value.substring(0,p1));		//Red
		color[n][1]  = Integer.parseInt(value.substring(p1+1,p2));	//Green
		color[n][2]  = Integer.parseInt(value.substring(p2+1));		//Blue
	}//end colorSet()
	
	
	/********************************************
	main()
	********************************************/	
	public static void main(String[] args){
		new Tetris();
	}

}//end class Tetris
