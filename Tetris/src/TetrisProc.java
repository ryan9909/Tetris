/***********************************************************
TetrisProc
	���̃N���X�́A�e�g���X�̐S�����ł��B�e�g���X���̂̒��j�I
	�ȏ�����S�����܂��B
	TetrisField, TetrisBlock, TetrisKey, TetrisGUI�̃N���X��
	�A�g���Ă��܂��B
***********************************************************/

public class TetrisProc {

	private TetrisField		fld;
	private TetrisBlock		blk;
	private TetrisKey		key;
	private TetrisGUI		gui;

	private final int		FldRows	= Tetris.FieldRows;
	private final int		FldCols	= Tetris.FieldCols;

	
	//�Q�[�����[�h
	public static int	gameMode;
	public static final int GAME_PLAY			= 1;
	public static final int GAME_PLAY_dropBlock	= 2;
	public static final int GAME_PLAY_addBlock	= 3;
	public static final int GAME_PLAY_deleteLine= 4;
	public static final int GAME_PLAY_newBlock	= 5;
	public static final int GAME_PAUSE			= 6;
	public static final int GAME_OVER			= 7;
	public static final int STATE_UP			= 0;

	
	//�Փ˃^�C�v
	private static final int MOVE_DOWN	= 0;
	private static final int MOVE_RIGHT	= 1;
	private static final int MOVE_LEFT	= 2;
	private static final int TURN_RIGHT	= 3;
	private static final int TURN_LEFT	= 4;
	private static final int BLOCK_NEW	= 5;
	
	//�u���b�N��������
	private long waitTime = 1000;


	/********************************************
	�R���X�g���N�^
	********************************************/
	public TetrisProc(TetrisField fld, TetrisBlock blk, TetrisKey key, TetrisGUI gui){
		this.fld	= fld;
		this.blk	= blk;
		this.key	= key;
		this.gui	= gui;
		
		init();
		
	}//end TetrisProc()

	
	/********************************************
	�Q�[��������
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
	�e�g���X���s
	********************************************/
	public void tetrisExec(){
		//�Q�[�����[�h�ݒ�
		gameMode = outputMode();
		//�L�[���͏���
		keyBoard(gameMode);
		//�Q�[���i�s����
		gameRun(gameMode);
		//�`�揈��
		screenDraw(gameMode);
		//�X���[�v
		sleep(15);
	}


	
	/********************************************
	�L�[���͏���
	********************************************/
	int t1, t2, t3, t4, t5, t6;
	
	private void keyBoard(int mode){
		switch(mode){
		case GAME_PLAY :
			/* ��L�[ */
			if(key.UP && !isCollision(TURN_LEFT)){
				blk.turnLeft();
				sleep(160);
			}
			/* ���L�[ */
			if(key.DOWN){
				if(t2==0 || t2>=18){
					if(!isCollision(MOVE_DOWN)){//�����n
						blk.moveDown();
						if(t2>=18)t2=16;
						timerReset();	//(��������)�^�C�}�[���Z�b�g
					}else{						//���n
						inputMode(GAME_PLAY_addBlock);
					}
				}
				t2++;
			}else{//key.DOWN==false
				t2 = 0;
			}
			/* �E�L�[ */
			if(key.RIGHT && !isCollision(MOVE_RIGHT)){
				blk.moveRight();
				sleep(120);
			}
			/* ���L�[ */
			if(key.LEFT && !isCollision(MOVE_LEFT)){
				blk.moveLeft();
				sleep(120);
			}
			/* �E��]�L�[ */
			if(key.TURN_R && !isCollision(TURN_RIGHT)){
				blk.turnRight();
				sleep(160);
			}
			/* ����]�L�[ */
			if(key.TURN_L && !isCollision(TURN_LEFT)){
				blk.turnLeft();
				sleep(160);
			}
			//berak;  mode==GAME_PLAY�̎������̏����֗����
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
	�Q�[���i�s����
	********************************************/
	//private long nothingTime;

	private void gameRun(int mode){
		switch(mode){
		//�Q�[���v���C
		case GAME_PLAY :
		 	if(timer(waitTime)){
				inputMode(GAME_PLAY_dropBlock);
		 	}
			break;

		//�u���b�N��i����
		case GAME_PLAY_dropBlock :
	 		if(!isCollision(MOVE_DOWN)){
				blk.moveDown();
				inputMode(GAME_PLAY);
			
			}else{
				inputMode(GAME_PLAY_addBlock);
	 		}
			break;
		
		//�u���b�N���t�B�[���h�ɒǉ�
		case GAME_PLAY_addBlock :
			addBlock();					//�u���b�N���t�B�[���h�ɉ�����
			blk.clear();				//�u���b�N�̃N���A
			if(fld.checkLine() > 0){	//�����Ă��郉�C���̗L�����`�F�b�N
				inputMode(GAME_PLAY_deleteLine);
			}else{
				inputMode(GAME_PLAY_newBlock);
			}
			break;
				
		//���C��������
		case GAME_PLAY_deleteLine :
			switch(state){
			case 0 :
				timerReset();
				inputMode(STATE_UP);
				break;
			case 1 :
				if(timer(300)){
					fld.deleteLine();//���C��������
					timerReset();
					inputMode(STATE_UP);
				}
				break;
			case 2 :
				if(timer(600)){
					fld.killLine();	//���������C�����l�߂�
					inputMode(GAME_PLAY_newBlock);
				}
				break;
			}//end switch(state)
			break;

		//�V�����u���b�N����
		case GAME_PLAY_newBlock :
			switch(state){
			case 0 :
				timerReset();
				inputMode(STATE_UP);
				break;
			case 1 :
				if(timer(800)){
					blk.newBlock();			//�V�K�u���b�N
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
		
		//�ꎞ���f
		case GAME_PAUSE :
		 	break;
		
		//�Q�[���I�[�o�[
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
	�X�N���[���ւ̕`��
	********************************************/
	public void screenDraw(int mode){
		switch(mode){
		case GAME_PLAY :
		case GAME_PLAY_dropBlock :
		case GAME_PLAY_addBlock :
		case GAME_PLAY_deleteLine :
		case GAME_PLAY_newBlock :
			//�t�B�[���h�`��
			gui.fieldDraw();
			//�u���b�N�`��
			gui.blockDraw();
			//�����(bg) �� �\���(fg)
			gui.BGtoFG();
			break;
			
		case GAME_PAUSE :
			break;
			
		case GAME_OVER :
			break;
		}
		
		
	}//end screenDraw()
	
	
	
	/********************************************
	�Փ˃`�F�b�N����
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
		
		//�Փ˔���
		for(int BlkRow=0; BlkRow < copyBlk.block.length; BlkRow++){
			if(copyBlk.y + BlkRow < 0)continue;
			if(copyBlk.y + BlkRow > FldRows-1)break;
			
			for(int BlkCol=0; BlkCol < copyBlk.block.length; BlkCol++){
				if(copyBlk.x + BlkCol < 0)continue;
				if(copyBlk.x + BlkCol > FldCols-1)break;

				//�����̃u���b�N�����݂���ӏ��ŁA����
				//�t�B�[���h�u���b�N�����݂���Ȃ�Փ�
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
	�u���b�N���t�B�[���h�ɉ����鏈��
	********************************************/
	public void addBlock(){
		//block �� field �R�s�[
		for(int row=0; row < blk.block.length; row++){
		for(int col=0; col < blk.block.length; col++){
			if(blk.block[row][col] != 0){
				fld.field[blk.y + row][blk.x + col] = blk.block[row][col];
			}
		}
		}
	}//end addBlock()

	
	/********************************************
	GAMEOVER���菈��
	********************************************/
	public boolean isGameOver(){
		if(isCollision(BLOCK_NEW)){
			return true;
		}else{
			return false;
		}
	}//end isGameOver()
	
	
	
	/********************************************
	�X���[�v
	********************************************/
	private void sleep(long ms){
		try {
			Thread.sleep(ms);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/********************************************
	�^�C�}�[
	********************************************/
	private long nowTime  = 0;
	private long preTime  = 0;
	private long pastTime = 0;
	
	private boolean timer(long waitTime){
		if(preTime==0){		//�^�C�}�[���Z�b�g���
			preTime  = System.currentTimeMillis();
			pastTime = 0;
			return false;
		}
		
		nowTime 	= System.currentTimeMillis();
		pastTime 	= pastTime + (nowTime - preTime);
		preTime 	= nowTime;
		
		//�w�莞�Ԃ��o�߂����Ȃ� true ��Ԃ�
		if(pastTime > waitTime){
			preTime = 0;	//�^�C�}�[���Z�b�g
			return true;
		} else {
			return false;
		}
	}//end timer()


	/********************************************
	�^�C�}�[�����Z�b�g
	********************************************/
	private void timerReset(){
		preTime=0;
	}//end timerReset()

	
	/********************************************
	�Q�[�����[�h�Ɋւ��鏈��
	�@�@�Q�[�����[�h�ɕύX������������ MODE[]�z��
	�@�@�Ɏ���̃��[�h���i�[�����B
	********************************************/
	private int		modeNum = -1;
	private int[]	MODE = new int[4];
	private int		state = 0;
	private int		saveMode;
	private int		saveState;
	//private boolean modeChangeFLG;
	
	//MODE�i�[
	private void inputMode(int mode){
		//STATE_UP�Ȃ�Astate�����̂�
		if(mode == STATE_UP){
			state++;
			return;
		}
		//�i�[�z�񂪖��t�Ȃ�A�������Ȃ�
		if(modeNum >= MODE.length-1) return;

		//���񃂁[�h���i�[
		MODE[++modeNum] = mode;
	}
	
	//MODE��o
	private int outputMode(){
		int rtnMode;
		
		//���񃂁[�h��������΁A���݃��[�h�p��
		if(modeNum < 0) return gameMode;
		
		//���[�h����o
		rtnMode = MODE[0];
		System.arraycopy(MODE, 1, MODE, 0, MODE.length-1);
		modeNum--;

		//GAME_PAUSE�̏ꍇ�͈ȑO�̃��[�h���L�����Ă���
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
