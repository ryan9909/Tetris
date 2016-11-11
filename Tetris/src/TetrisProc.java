/***********************************************************
TetrisProc
	このクラスは、テトリスの心臓部です。テトリス自体の中核的
	な処理を担当します。
	TetrisField, TetrisBlock, TetrisKey, TetrisGUIのクラスと
	連携しています。
***********************************************************/

public class TetrisProc {

	private TetrisField		fld;
	private TetrisBlock		blk;
	private TetrisKey		key;
	private TetrisGUI		gui;

	private final int		FldRows	= Tetris.FieldRows;
	private final int		FldCols	= Tetris.FieldCols;

	
	//ゲームモード
	public static int	gameMode;
	public static final int GAME_PLAY			= 1;
	public static final int GAME_PLAY_dropBlock	= 2;
	public static final int GAME_PLAY_addBlock	= 3;
	public static final int GAME_PLAY_deleteLine= 4;
	public static final int GAME_PLAY_newBlock	= 5;
	public static final int GAME_PAUSE			= 6;
	public static final int GAME_OVER			= 7;
	public static final int STATE_UP			= 0;

	
	//衝突タイプ
	private static final int MOVE_DOWN	= 0;
	private static final int MOVE_RIGHT	= 1;
	private static final int MOVE_LEFT	= 2;
	private static final int TURN_RIGHT	= 3;
	private static final int TURN_LEFT	= 4;
	private static final int BLOCK_NEW	= 5;
	
	//ブロック落下時間
	private long waitTime = 1000;


	/********************************************
	コンストラクタ
	********************************************/
	public TetrisProc(TetrisField fld, TetrisBlock blk, TetrisKey key, TetrisGUI gui){
		this.fld	= fld;
		this.blk	= blk;
		this.key	= key;
		this.gui	= gui;
		
		init();
		
	}//end TetrisProc()

	
	/********************************************
	ゲーム初期化
	********************************************/
	private void init(){

		fld.newField();
		blk.newBlock();
		
		modeNum = -1;
		state = 0;

		inputMode(GAME_PLAY);
		timerReset();
	}
	
	/********************************************
	テトリス実行
	********************************************/
	public void tetrisExec(){
		//ゲームモード設定
		gameMode = outputMode();
		//キー入力処理
		keyBoard(gameMode);
		//ゲーム進行処理
		gameRun(gameMode);
		//描画処理
		screenDraw(gameMode);
		//スリープ
		sleep(15);
	}


	
	/********************************************
	キー入力処理
	********************************************/
	int t1, t2, t3, t4, t5, t6;
	
	private void keyBoard(int mode){
		switch(mode){
		case GAME_PLAY :
			/* 上キー */
			if(key.UP && !isCollision(TURN_LEFT)){
				blk.turnLeft();
				sleep(160);
			}
			/* 下キー */
			if(key.DOWN){
				if(t2==0 || t2>=18){
					if(!isCollision(MOVE_DOWN)){//未着地
						blk.moveDown();
						if(t2>=18)t2=16;
						timerReset();	//(落下中の)タイマーリセット
					}else{						//着地
						inputMode(GAME_PLAY_addBlock);
					}
				}
				t2++;
			}else{//key.DOWN==false
				t2 = 0;
			}
			/* 右キー */
			if(key.RIGHT && !isCollision(MOVE_RIGHT)){
				blk.moveRight();
				sleep(120);
			}
			/* 左キー */
			if(key.LEFT && !isCollision(MOVE_LEFT)){
				blk.moveLeft();
				sleep(120);
			}
			/* 右回転キー */
			if(key.TURN_R && !isCollision(TURN_RIGHT)){
				blk.turnRight();
				sleep(160);
			}
			/* 左回転キー */
			if(key.TURN_L && !isCollision(TURN_LEFT)){
				blk.turnLeft();
				sleep(160);
			}
			//berak;  mode==GAME_PLAYの時も下の処理へ流れる
		case GAME_PLAY_dropBlock :
		case GAME_PLAY_addBlock :
		case GAME_PLAY_deleteLine :
		case GAME_PLAY_newBlock :
			if(key.START){
				inputMode(GAME_PAUSE);
				sleep(200);
			}
			break;

		case GAME_PAUSE :
			if(key.START){
				inputMode(GAME_PAUSE);
				sleep(200);
			}
			break;
		}//end switch
		
	}//end keyBoard()
	
	
	
	/********************************************
	ゲーム進行処理
	********************************************/
	//private long nothingTime;

	private void gameRun(int mode){
		switch(mode){
		//ゲームプレイ
		case GAME_PLAY :
		 	if(timer(waitTime)){
				inputMode(GAME_PLAY_dropBlock);
		 	}
			break;

		//ブロック一段落下
		case GAME_PLAY_dropBlock :
	 		if(!isCollision(MOVE_DOWN)){
				blk.moveDown();
				inputMode(GAME_PLAY);
			
			}else{
				inputMode(GAME_PLAY_addBlock);
	 		}
			break;
		
		//ブロックをフィールドに追加
		case GAME_PLAY_addBlock :
			addBlock();					//ブロックをフィールドに加える
			blk.clear();				//ブロックのクリア
			if(fld.checkLine() > 0){	//揃っているラインの有無をチェック
				inputMode(GAME_PLAY_deleteLine);
			}else{
				inputMode(GAME_PLAY_newBlock);
			}
			break;
				
		//ラインを消去
		case GAME_PLAY_deleteLine :
			switch(state){
			case 0 :
				timerReset();
				inputMode(STATE_UP);
				break;
			case 1 :
				if(timer(300)){
					fld.deleteLine();//ラインを消す
					timerReset();
					inputMode(STATE_UP);
				}
				break;
			case 2 :
				if(timer(600)){
					fld.killLine();	//消えたラインを詰める
					inputMode(GAME_PLAY_newBlock);
				}
				break;
			}//end switch(state)
			break;

		//新しいブロック生成
		case GAME_PLAY_newBlock :
			switch(state){
			case 0 :
				timerReset();
				inputMode(STATE_UP);
				break;
			case 1 :
				if(timer(800)){
					blk.newBlock();			//新規ブロック
					if(isGameOver()){
						inputMode(GAME_OVER);
					}else{
						inputMode(GAME_PLAY);
						timerReset();
					}
				}
				break;
			}//end switch(state)
			break;
		
		//一時中断
		case GAME_PAUSE :
		 	break;
		
		//ゲームオーバー
		case GAME_OVER :
			switch(state){
			case 0 :
				timerReset();
				inputMode(STATE_UP);
				break;
			case 1 :
				if(timer(1000)){
					init();
				}
				break;
			}//end switch(state)
			break;
		}//end switch

	}//end gameRun()
	
	
	
	/********************************************
	スクリーンへの描画
	********************************************/
	public void screenDraw(int mode){
		switch(mode){
		case GAME_PLAY :
		case GAME_PLAY_dropBlock :
		case GAME_PLAY_addBlock :
		case GAME_PLAY_deleteLine :
		case GAME_PLAY_newBlock :
			//フィールド描画
			gui.fieldDraw();
			//ブロック描画
			gui.blockDraw();
			//裏画面(bg) → 表画面(fg)
			gui.BGtoFG();
			break;
			
		case GAME_PAUSE :
			break;
			
		case GAME_OVER :
			break;
		}
		
		
	}//end screenDraw()
	
	
	
	/********************************************
	衝突チェック処理
	********************************************/
	private boolean isCollision(int type){
		TetrisBlock copyBlk = new TetrisBlock(blk);
		
		switch(type){
		case MOVE_DOWN:
			copyBlk.moveDown();
			break;
			
		case MOVE_RIGHT:
			copyBlk.moveRight();
			break;
			
		case MOVE_LEFT:
			copyBlk.moveLeft();
			break;
			
		case TURN_RIGHT:
			copyBlk.turnRight();
			break;
			
		case TURN_LEFT:
			copyBlk.turnLeft();
			break;
			
		case BLOCK_NEW:
			break;
		}
		
		//衝突判定
		for(int BlkRow=0; BlkRow < copyBlk.block.length; BlkRow++){
			if(copyBlk.y + BlkRow < 0)continue;
			if(copyBlk.y + BlkRow > FldRows-1)break;
			
			for(int BlkCol=0; BlkCol < copyBlk.block.length; BlkCol++){
				if(copyBlk.x + BlkCol < 0)continue;
				if(copyBlk.x + BlkCol > FldCols-1)break;

				//自分のブロックが存在する箇所で、かつ
				//フィールドブロックが存在するなら衝突
				if(copyBlk.block[BlkRow][BlkCol] != 0){
					if(fld.field[copyBlk.y + BlkRow][copyBlk.x + BlkCol] != 0){
						return true;
					}
				}
			}
		}
		return false;
	}//end isCollision()

	
	
	/********************************************
	ブロックをフィールドに加える処理
	********************************************/
	public void addBlock(){
		//block → field コピー
		for(int row=0; row < blk.block.length; row++){
		for(int col=0; col < blk.block.length; col++){
			if(blk.block[row][col] != 0){
				fld.field[blk.y + row][blk.x + col] = blk.block[row][col];
			}
		}
		}
	}//end addBlock()

	
	/********************************************
	GAMEOVER判定処理
	********************************************/
	public boolean isGameOver(){
		if(isCollision(BLOCK_NEW)){
			return true;
		}else{
			return false;
		}
	}//end isGameOver()
	
	
	
	/********************************************
	スリープ
	********************************************/
	private void sleep(long ms){
		try {
			Thread.sleep(ms);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/********************************************
	タイマー
	********************************************/
	private long nowTime  = 0;
	private long preTime  = 0;
	private long pastTime = 0;
	
	private boolean timer(long waitTime){
		if(preTime==0){		//タイマーリセット状態
			preTime  = System.currentTimeMillis();
			pastTime = 0;
			return false;
		}
		
		nowTime 	= System.currentTimeMillis();
		pastTime 	= pastTime + (nowTime - preTime);
		preTime 	= nowTime;
		
		//指定時間が経過したなら true を返す
		if(pastTime > waitTime){
			preTime = 0;	//タイマーリセット
			return true;
		} else {
			return false;
		}
	}//end timer()


	/********************************************
	タイマーをリセット
	********************************************/
	private void timerReset(){
		preTime=0;
	}//end timerReset()

	
	/********************************************
	ゲームモードに関する処理
	　　ゲームモードに変更があった時は MODE[]配列
	　　に次回のモードが格納される。
	********************************************/
	private int		modeNum = -1;
	private int[]	MODE = new int[4];
	private int		state = 0;
	private int		saveMode;
	private int		saveState;
	//private boolean modeChangeFLG;
	
	//MODE格納
	private void inputMode(int mode){
		//STATE_UPなら、state増加のみ
		if(mode == STATE_UP){
			state++;
			return;
		}
		//格納配列が満杯なら、何もしない
		if(modeNum >= MODE.length-1) return;

		//次回モードを格納
		MODE[++modeNum] = mode;
	}
	
	//MODE取出
	private int outputMode(){
		int rtnMode;
		
		//次回モードが無ければ、現在モード継続
		if(modeNum < 0) return gameMode;
		
		//モードを取出
		rtnMode = MODE[0];
		System.arraycopy(MODE, 1, MODE, 0, MODE.length-1);
		modeNum--;

		//GAME_PAUSEの場合は以前のモードを記憶しておく
		if(rtnMode == GAME_PAUSE){
			if(gameMode != GAME_PAUSE){
				saveMode = gameMode;
				saveState = state;
			}else{
				state = saveState;
				return saveMode;
			}
		}
		
		state = 0;
		return rtnMode;
	}
	
	
}//End class TetrisProc
