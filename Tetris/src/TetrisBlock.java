/***********************************************************
TetrisBlock
	このクラスは、テトリスのブロックに関するクラスです。
***********************************************************/

import java.math.*;

public class TetrisBlock {

	public int 			x;
	public int 			y;
	public int[][] 		block;

	protected int	FldRows;
	protected int	FldCols;

	//コンストラクタ（通常版）
	public TetrisBlock(){
		//newBlock();
		FldRows	= Tetris.FieldRows;
		FldCols	= Tetris.FieldCols;
	}//end of TetrisBlock()
	
	
	//コンストラクタ（コピー機能版）
	public TetrisBlock(TetrisBlock b){
		x = b.x;
		y = b.y;
		block = new int[b.block.length][b.block.length];
		
		block = b.copy();

	}
	
	//新規ブロック
	public void newBlock(){
		//１～７の乱数を発生させて、ブロックの種類を決めます
		int n = (int)(7 * Math.random() + 1.0);
		
		switch(n){
			case 1 :
				block = new int[][] { {0, n, 0},
									  {n, n, n},
									  {0, 0, 0} };
				x=(int)((FldCols - block.length)/2);
				y=0;
				break;
				
			case 2 :
				block = new int[][] { {n, n, 0},
									  {0, n, n},
									  {0, 0, 0} };
				x=4;
				y=0;
				break;
				
			case 3 :
				block = new int[][] { {0, n, n},
									  {n, n, 0},
									  {0, 0, 0} };
				x=4;
				y=0;
				break;
				
			case 4 :
				block = new int[][] { {0, 0, n},
									  {n, n, n},
									  {0, 0, 0} };
				x=4;
				y=0;
				break;
				
			case 5 :
				block = new int[][] { {n, 0, 0},
									  {n, n, n},
									  {0, 0, 0} };
				x=4;
				y=0;
				break;
				
			case 6 :
				block = new int[][] { {n, n},
									  {n, n} };
				x=4;
				y=0;
				break;
				
			case 7 :
				block = new int[][] { {0, 0, 0, 0},
									  {n, n, n, n},
									  {0, 0, 0, 0},
									  {0, 0, 0, 0} };
				x=3;
				y=0;
				break;
		
		}//end of switch()

		x=(int)((FldCols - block.length)/2);
		y=0;
	}//end of new()

	
	//下移動
	public void moveDown(){
			y++;
	}
	
	//右移動
	public void moveRight(){
			x++;
	}
	
	//左移動
	public void moveLeft(){
			x--;
	}
	
	//右回転
	public void turnRight(){
		int[][] block2 = new int[block.length][block.length];

		block2 = copy();
		
		for(int row = 0; row < block.length; row++){
			for(int col = 0; col < block.length; col++){
				block[col][block.length - 1 - row] = block2[row][col];	//右回転：定式
			}
		}		
	}
	
	//左回転
	public void turnLeft(){
		int[][] block2 = new int[block.length][block.length];
		
		block2 = copy();
		
		for(int row = 0; row < block.length; row++){
			for(int col = 0; col < block.length; col++){
				block[block.length - 1 - col][row] = block2[row][col];	//左回転：定式
			}
		}
	}

	//blockをクリア
	public void clear(){
		//block = new int[0][0];
		block = new int[][] {{0}};
	}
	
	//blockをコピーして返す
	public int[][] copy(){
		int[][] block2 = new int[block.length][block.length];
		
		for(int row=0; row < block.length; row++){
		for(int col=0; col < block.length; col++){
			block2[row][col] = block[row][col];
		}
		}
		
		return block2;
	}

	/*
	protected void finalize(){
		try{
			super.finalize();
			System.out.println("finalize:" + this);
		}
		catch(Throwable ex){
		}
	}
	*/

}//end class TetrisBlock


