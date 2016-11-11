/***********************************************************
TetrisField
	このクラスは、テトリスのフィールドに関するクラスです。
	'壁'の部分が9、'空白'の部分が0です。
	７種類のブロックは1～7の数値として格納されます。
***********************************************************/

public class TetrisField {

	public  int[][]		field;

	private int			delNum;		//消えるラインの数
	private int[]		delRow;		//消えるラインの行番号を格納する配列
	private final int	FldRows	= Tetris.FieldRows;
	private final int	FldCols	= Tetris.FieldCols;

	
	/********************************************
	コンストラクタ
	********************************************/
	TetrisField(){
		//newField();
	}


	/********************************************
	フィールドの初期化
	********************************************/
	public void newField(){
		field = new int[FldRows][FldCols];
		
		for(int row=0; row < FldRows-1; row++){
			//左の壁
			field[row][0] = 9;
			//空間
			for(int col=1; col < FldCols-1; col++){
				field[row][col] = 0;
			}
			//右の壁
			field[row][FldCols-1] = 9;
		}
		
		//フィールドの底
		for(int col=0; col <= FldCols-1; col++){
			field[FldRows-1][col] = 9;
		}
		
		delRow = new int[FldRows];
	}//end of newField()
	
	
	/********************************************
	消去ラインのチェック処理
	********************************************/
	public int checkLine(){
		delNum = 0;
		
		int cells;
		
		for(int row=0; row < FldRows-1; row++){
			cells = 0;
			
			//横ラインのブロック数をカウント
			for(int col=1; col < FldCols-1; col++){
				if(field[row][col]!=0){
					cells++;
				}else{
					break;
				}
			}//next col
			
			//横ラインが揃っているなら、その行番号をdelRow[]に格納
			if(cells == FldCols-2){
				delRow[delNum++] = row;
			}

		}//next row
		
		return delNum;
	
	}//end checkLine()
	
	
	
	/********************************************
	ラインを消す(空白化)
	********************************************/
	public void deleteLine(){
		//消えるラインに０を代入
		for(int n  =0; n   < delNum;    n++){
		for(int col=1; col < FldCols-1; col++){
			field[delRow[n]][col] = 0;
		}
		}
	}
	
	
	
	/********************************************
	消えたラインを詰める
	********************************************/
	public void killLine(){
		
		for(int n=0; n < delNum; n++){
			//１段ずつ下へずらす（その１）バグ
			/*
			System.arraycopy(field, 0, field, 1, delRow[n]);
			System.out.println(n+"回目:長さ＝"+delRow[n]);
			/* arraycopy()メソッドだと、プレイしているうちに
			   なぜかバグる。同じ配列にコピーしてるから？
			   → ２次元配列に使用するのはＮＧっぽい、と判明 */
			
			
			//１段ずつ下へずらす（その２）ＯＫ
			/*
			for(int row=delRow[n]; row > 0; row--){
			for(int col=0; col < FldCols-1; col++){
				field[row][col] = field[row-1][col];
			}
			}
			*/
			
			
			//１段ずつ下へずらす（その３）バグ
			/*
			for(int row=delRow[n]; row > 0; row--){
				System.arraycopy(field, row-1, field, row, 1);
			}
			*/

			//１段ずつ下へずらす（その４）バグ
			/*
			int[][] field2 = new int[FldRows][FldCols];
			System.arraycopy(field, 0, field2, 0, delRow[n]);
			System.arraycopy(field2, 0, field, 1, delRow[n]);
			*/
			
			
			//１段ずつ下へずらす（その５）ＯＫ
			for(int row=delRow[n]; row > 0; row--){
				System.arraycopy(field[row-1], 0, field[row], 0, FldCols);
			}
			
			
			
			//最上段クリア
			for(int col=1; col < FldCols-1; col++){
				field[0][col] = 0;
			}
			
		}
	}//end killLine()
	
}//end class TetrisField


