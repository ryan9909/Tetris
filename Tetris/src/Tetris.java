/***********************************************************
Tetris
	���̃N���X�́A�e�g���X�̊J�n�N���X�ł���A�e�g���X�S�̂�
	���܂Ƃ߂�O�k�I�N���X�ł�����܂��B
	�܂��A�N������ini�t�@�C������ݒ����ǂݍ��݂܂��B
***********************************************************/
import java.io.*;

public class Tetris {
	//�e�g���X�\���T�C�Y
	static public int BlockSize;	//�u���b�N��̃h�b�g�T�C�Y
	static public int FieldCols;	//�t�B�[���h�̉��}�X��(���E�͕�)
	static public int FieldRows;	//�t�B�[���h�̏c�}�X��(���͒��)

	//�E�B���h�E�\���T�C�Y�i�e�g���X�\���T�C�Y�Ō��܂�j
	static public int ScreenW;		//�E�B���h�E��
	static public int ScreenH;		//�E�B���h�E����

	//�E�B���h�E�\���ʒu
	static public int ScreenX;		//�E�B���h�E�\���ʒu������W�w
	static public int ScreenY;		//�E�B���h�E�\���ʒu������W�x

	//�F
	static int[][] color = new int[4][3];

	//�֘A����N���X�̐錾
	private TetrisField		fld;
	private TetrisBlock		blk;
	private TetrisKey		key;
	private TetrisGUI		gui;
	private TetrisProc		prc;
	
	
	/********************************************
	�R���X�g���N�^
	********************************************/
	public Tetris() {
		iniRead();
		
		fld		= new TetrisField();					//�t�B�[���h
		blk		= new TetrisBlock();					//�u���b�N
		key 	= new TetrisKey();						//�L�[����
		gui		= new TetrisGUI(fld, blk, key);			//�f�t�h
		prc		= new TetrisProc(fld, blk, key, gui);	//�v���V�[�W��(����)


		//�E�B���h�E�\��
		gui.windowShow();

		
		//sleep0.5�b
		try {
			Thread.sleep(500);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		/*
		//���[�v(AWT, Swing�d�l)
	    while (true){
			//���s
			prc.tetrisExec();
	    }
		*/
		
		
		//���[�v(SWT�d�l)
	    while (!gui.shell.isDisposed()){
			if(!gui.display.readAndDispatch()){
				//���s
				prc.tetrisExec();
			}
	    }
		

	}//end Tetris()

	
	/********************************************
	ini�t�@�C���Ǎ�
	********************************************/
	protected void iniRead(){
		String	str, item, value;
		int		p;
		
		try{
			FileReader		fr	= new FileReader("tetris.ini");
			BufferedReader	buf	= new BufferedReader(fr);
            while((str = buf.readLine()) != null) {
				
				p = str.indexOf("=", 0);	//p:"="�̈ʒu
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
		}catch(Exception ex){	//ini�t�@�C���ɕs��������Ƃ�
			BlockSize = 18;
			FieldCols = 10+2;
			FieldRows = 20+1;
			ScreenX   = 300;
			ScreenY   = 100;
			color = new int[][] {
				{90,90,90},		//��
				{0,0,255},		//�u���b�N[��]
				{0,255,0},		//�u���b�N[�R]
				{255,0,0},		//�u���b�N[����]
			};
		}
		
		ScreenW = BlockSize * FieldCols;
		ScreenH = BlockSize * FieldRows;

	}//end iniRead()
	
	
	/********************************************
	�F�̃Z�b�g
	********************************************/
	public void colorSet(String item, String value){
		int n = Integer.parseInt(item.substring(item.length()-1));
		int	p1, p2;
		
		p1 = value.indexOf(",", 0);		//p1:�P�ڂ�","�̈ʒu
		p2 = value.indexOf(",", p1+1);	//p2:�Q�ڂ�","�̈ʒu
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
