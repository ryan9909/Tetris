/***********************************************************
TetrisField
	���̃N���X�́A�e�g���X�̃t�B�[���h�Ɋւ���N���X�ł��B
	'��'�̕�����9�A'��'�̕�����0�ł��B
	�V��ނ̃u���b�N��1�`7�̐��l�Ƃ��Ċi�[����܂��B
***********************************************************/

public class TetrisField {

	public  int[][]		field;

	private int			delNum;		//�����郉�C���̐�
	private int[]		delRow;		//�����郉�C���̍s�ԍ����i�[����z��
	private final int	FldRows	= Tetris.FieldRows;
	private final int	FldCols	= Tetris.FieldCols;

	
	/********************************************
	�R���X�g���N�^
	********************************************/
	TetrisField(){
		//newField();
	}


	/********************************************
	�t�B�[���h�̏�����
	********************************************/
	public void newField(){
		field = new int[FldRows][FldCols];
		
		for(int row=0; row < FldRows-1; row++){
			//���̕�
			field[row][0] = 9;
			//���
			for(int col=1; col < FldCols-1; col++){
				field[row][col] = 0;
			}
			//�E�̕�
			field[row][FldCols-1] = 9;
		}
		
		//�t�B�[���h�̒�
		for(int col=0; col <= FldCols-1; col++){
			field[FldRows-1][col] = 9;
		}
		
		delRow = new int[FldRows];
	}//end of newField()
	
	
	/********************************************
	�������C���̃`�F�b�N����
	********************************************/
	public int checkLine(){
		delNum = 0;
		
		int cells;
		
		for(int row=0; row < FldRows-1; row++){
			cells = 0;
			
			//�����C���̃u���b�N�����J�E���g
			for(int col=1; col < FldCols-1; col++){
				if(field[row][col]!=0){
					cells++;
				}else{
					break;
				}
			}//next col
			
			//�����C���������Ă���Ȃ�A���̍s�ԍ���delRow[]�Ɋi�[
			if(cells == FldCols-2){
				delRow[delNum++] = row;
			}

		}//next row
		
		return delNum;
	
	}//end checkLine()
	
	
	
	/********************************************
	���C��������(�󔒉�)
	********************************************/
	public void deleteLine(){
		//�����郉�C���ɂO����
		for(int n  =0; n   < delNum;    n++){
		for(int col=1; col < FldCols-1; col++){
			field[delRow[n]][col] = 0;
		}
		}
	}
	
	
	
	/********************************************
	���������C�����l�߂�
	********************************************/
	public void killLine(){
		
		for(int n=0; n < delNum; n++){
			//�P�i�����ւ��炷�i���̂P�j�o�O
			/*
			System.arraycopy(field, 0, field, 1, delRow[n]);
			System.out.println(n+"���:������"+delRow[n]);
			/* arraycopy()���\�b�h���ƁA�v���C���Ă��邤����
			   �Ȃ����o�O��B�����z��ɃR�s�[���Ă邩��H
			   �� �Q�����z��Ɏg�p����̂͂m�f���ۂ��A�Ɣ��� */
			
			
			//�P�i�����ւ��炷�i���̂Q�j�n�j
			/*
			for(int row=delRow[n]; row > 0; row--){
			for(int col=0; col < FldCols-1; col++){
				field[row][col] = field[row-1][col];
			}
			}
			*/
			
			
			//�P�i�����ւ��炷�i���̂R�j�o�O
			/*
			for(int row=delRow[n]; row > 0; row--){
				System.arraycopy(field, row-1, field, row, 1);
			}
			*/

			//�P�i�����ւ��炷�i���̂S�j�o�O
			/*
			int[][] field2 = new int[FldRows][FldCols];
			System.arraycopy(field, 0, field2, 0, delRow[n]);
			System.arraycopy(field2, 0, field, 1, delRow[n]);
			*/
			
			
			//�P�i�����ւ��炷�i���̂T�j�n�j
			for(int row=delRow[n]; row > 0; row--){
				System.arraycopy(field[row-1], 0, field[row], 0, FldCols);
			}
			
			
			
			//�ŏ�i�N���A
			for(int col=1; col < FldCols-1; col++){
				field[0][col] = 0;
			}
			
		}
	}//end killLine()
	
}//end class TetrisField


