package com.project.find_index;

/*
 * 
 * By Mrli    last update:2013.10.06  11：00
 * 
 * shelf的实体类
 * 
 * 1）存储 每个字母对应的书架号，如D有19---26，分别记录下19和26在不同的数组中
 * 2）存储每个库每个架子的第一本书的索书号，存在一个二维数组中
 * 3）对同一个字母在不同的库的条件进行判断，是的话重新赋值对应的书架号
 * 4）提供获取书架号区间和获取每一个书架号第一本书的索书号的函数
 * 
 * */


public class Shelf {
	
	private int shelf_Let_begin[];
	private int shelf_Let_end[];
	private int Tbegin;
	private int Tend;	
	private char shelfTailLet[][];
	private int shelfTailNum[][];

	private float shelfHeadNo[][];
	

	
	public Shelf(){

		//每个字母的架子		从0开始，与书架的HEAD TAIL 保持一至		
		shelf_Let_begin=new int[26];	
		shelf_Let_end=new int[26];	
		
		shelf_Let_begin[0]=0;//A的书架只有1号//先不考虑TA，TP，TS情况
		shelf_Let_end[0]=0;//
		shelf_Let_begin[1]=1;//b的书架从2-----13
		shelf_Let_end[1]=12;
		shelf_Let_begin[2]=12;//c
		shelf_Let_end[2]=20;
		shelf_Let_begin[3]=21;//d
		shelf_Let_end[3]=25;//D    这是一楼东的      按断点前的写这里，断点后的结合ROOM的不同重新赋值
		shelf_Let_begin[4]=14;//E
		shelf_Let_end[4]=16;
		shelf_Let_begin[5]=16;//F  这是一楼西的F
		shelf_Let_end[5]=25;
		shelf_Let_begin[6]=7;//G
		shelf_Let_end[6]=18;
		shelf_Let_begin[7]=19;//H  这是二楼西库里的H
		shelf_Let_end[7]=29;
		shelf_Let_begin[8]=7;//I
		shelf_Let_end[8]=29;
		shelf_Let_begin[9]=0;//J
		shelf_Let_end[9]=4;
		shelf_Let_begin[10]=5;//K
		shelf_Let_end[10]=23;
		shelf_Let_begin[11]=0;//L
		shelf_Let_end[11]=0;
		shelf_Let_begin[12]=0;//M
		shelf_Let_end[12]=0;	
		shelf_Let_begin[13]=0;//N
		shelf_Let_end[13]=0;
		shelf_Let_begin[14]=24;//O
		shelf_Let_end[14]=29;
		shelf_Let_begin[15]=0;//P only 1 shelf
		shelf_Let_end[15]=0;
		shelf_Let_begin[16]=0;//Q only 1 too
		shelf_Let_end[16]=0;
		shelf_Let_begin[17]=1;//R
		shelf_Let_end[17]=5;
		shelf_Let_begin[18]=0;//S  only 1
		shelf_Let_end[18]=0;
		shelf_Let_begin[19]=5;//T  没带字母的T只有一个架子
		shelf_Let_end[19]=5;
		shelf_Let_begin[20]=4;//U
		shelf_Let_end[20]=10;
		shelf_Let_begin[21]=0;//V  only 1
		shelf_Let_end[21]=0;
		shelf_Let_begin[22]=0;//W
		shelf_Let_end[22]=0;
		shelf_Let_begin[23]=11;//X    only 2
		shelf_Let_end[23]=12;
		shelf_Let_begin[24]=0;//Y
		shelf_Let_end[24]=0;
		shelf_Let_begin[25]=12;//Z    only 2
		shelf_Let_end[25]=13;
		

		
		shelfHeadNo=new float[8][30];	//每个库每个架的区间数。	
		//一楼东库
		shelfHeadNo[0][0]=(float)0;//一楼东库
		shelfHeadNo[0][1]=(float)0;
		shelfHeadNo[0][2]=(float)221.5;
		shelfHeadNo[0][3]=(float)228.05;
		shelfHeadNo[0][4]=(float)516.35;
		shelfHeadNo[0][5]=(float)820.5;
		shelfHeadNo[0][6]=(float)821;
		shelfHeadNo[0][7]=(float)830.6;
		shelfHeadNo[0][8]=(float)842.6;
		shelfHeadNo[0][9]=(float)848.4;
		shelfHeadNo[0][10]=(float)848.4;/////////////////
		shelfHeadNo[0][11]=(float)933;
		shelfHeadNo[0][12]=(float)992.4;
		shelfHeadNo[0][13]=(float)520;
		shelfHeadNo[0][14]=(float)520;
		shelfHeadNo[0][15]=(float)800;
		shelfHeadNo[0][16]=(float)912.1;
		shelfHeadNo[0][17]=(float)912.81;
		shelfHeadNo[0][18]=(float)913.7;
		shelfHeadNo[0][19]=(float)930.5;
		shelfHeadNo[0][20]=(float)933.2;
		shelfHeadNo[0][21]=(float)0;
		shelfHeadNo[0][22]=(float)8;////
		shelfHeadNo[0][23]=(float)260;
		shelfHeadNo[0][24]=(float)500;
		shelfHeadNo[0][25]=(float)618;

		//一楼西
		shelfHeadNo[1][0]=(float)631;
		shelfHeadNo[1][1]=(float)669.3;//
		shelfHeadNo[1][2]=(float)733.81;
		shelfHeadNo[1][3]=(float)813.4;
		shelfHeadNo[1][4]=(float)900;
		shelfHeadNo[1][5]=(float)913.04;
		shelfHeadNo[1][6]=(float)920.4;		
		shelfHeadNo[1][7]=(float)922.164;
		shelfHeadNo[1][8]=(float)923.2815;
		shelfHeadNo[1][9]=(float)922.2945;
		shelfHeadNo[1][10]=(float)923;
		shelfHeadNo[1][11]=(float)924;
		shelfHeadNo[1][12]=(float)926;
		shelfHeadNo[1][13]=(float)926.7;
		shelfHeadNo[1][14]=(float)954.14;
		shelfHeadNo[1][15]=(float)195.2;
		shelfHeadNo[1][16]=(float)925.6;
		shelfHeadNo[1][17]=(float)113.7;
		shelfHeadNo[1][18]=(float)131.3;
		shelfHeadNo[1][19]=(float)232;
		shelfHeadNo[1][20]=(float)270;
		shelfHeadNo[1][21]=(float)272.9;
		shelfHeadNo[1][22]=(float)274;
		shelfHeadNo[1][23]=(float)279.1;
		shelfHeadNo[1][24]=(float)284;
		shelfHeadNo[1][25]=(float)320;

		
		
		
		
		
		//二楼东 room2
		shelfHeadNo[2][0]=(float)319;
		shelfHeadNo[2][1]=(float)319.4;
		shelfHeadNo[2][2]=(float)319.4;
		shelfHeadNo[2][3]=(float)319.6;
		shelfHeadNo[2][4]=(float)319.9;
		shelfHeadNo[2][5]=(float)339;
		shelfHeadNo[2][6]=(float)369.9;
		shelfHeadNo[2][7]=(float)120;
		shelfHeadNo[2][8]=(float)206.7;
		shelfHeadNo[2][9]=(float)207.41;
		shelfHeadNo[2][10]=(float)211;
		shelfHeadNo[2][11]=(float)217.61;
		shelfHeadNo[2][12]=(float)227.7;
		shelfHeadNo[2][13]=(float)246.53;
		shelfHeadNo[2][14]=(float)247.53;
		shelfHeadNo[2][15]=(float)247.53;////
		shelfHeadNo[2][16]=(float)247.53;
		shelfHeadNo[2][17]=(float)247.57;
		shelfHeadNo[2][18]=(float)247.57;
		shelfHeadNo[2][19]=(float)247.57;
		shelfHeadNo[2][20]=(float)247.8;
		shelfHeadNo[2][21]=(float)253;
		shelfHeadNo[2][22]=(float)260;
		shelfHeadNo[2][23]=(float)267;
		shelfHeadNo[2][24]=(float)267;
		shelfHeadNo[2][25]=(float)267.1;
		shelfHeadNo[2][26]=(float)313.45;
		shelfHeadNo[2][27]=(float)521.15;
		shelfHeadNo[2][28]=(float)561.65;
		shelfHeadNo[2][29]=(float)712.45;

		
		
		
		
		//二楼西 room3
		shelfHeadNo[3][0]=(float)500;
		shelfHeadNo[3][1]=(float)713.5;
		shelfHeadNo[3][2]=(float)715.4;
		shelfHeadNo[3][3]=(float)740.4;
		shelfHeadNo[3][4]=(float)812.2;
		shelfHeadNo[3][5]=(float)830.46;
		shelfHeadNo[3][6]=(float)831.2;
		shelfHeadNo[3][7]=(float)840;
		shelfHeadNo[3][8]=(float)120;
		shelfHeadNo[3][9]=(float)206.2;
		shelfHeadNo[3][10]=(float)222.2;
		shelfHeadNo[3][11]=(float)256.22;
		shelfHeadNo[3][12]=(float)400.1;
		shelfHeadNo[3][13]=(float)471.2;
		shelfHeadNo[3][14]=(float)641;
		shelfHeadNo[3][15]=(float)643.7;
		shelfHeadNo[3][16]=(float)647.38;
		shelfHeadNo[3][17]=(float)780;
		shelfHeadNo[3][18]=(float)852.11;
		shelfHeadNo[3][19]=(float)000;
		shelfHeadNo[3][20]=(float)33;
		shelfHeadNo[3][21]=(float)109.4;
		shelfHeadNo[3][22]=(float)152.3;
		shelfHeadNo[3][23]=(float)195.4;
		shelfHeadNo[3][24]=(float)310;
		shelfHeadNo[3][25]=(float)310;///////////////
		shelfHeadNo[3][26]=(float)310;
		shelfHeadNo[3][27]=(float)313.1;
		shelfHeadNo[3][28]=(float)313.1;
		shelfHeadNo[3][29]=(float)315;
		
		
				
		
		
		
		//三楼东room 4
		shelfHeadNo[4][0]=(float)0;
		shelfHeadNo[4][1]=(float)212.25;
		shelfHeadNo[4][2]=(float)228.1;
		shelfHeadNo[4][3]=(float)429.1;
		shelfHeadNo[4][4]=(float)607.257;
		shelfHeadNo[4][5]=(float)152;
		shelfHeadNo[4][6]=(float)0;
		shelfHeadNo[4][7]=(float)152;
		shelfHeadNo[4][8]=(float)248.045;
		shelfHeadNo[4][9]=(float)265.2106;
		shelfHeadNo[4][10]=(float)295.1;
		shelfHeadNo[4][11]=(float)810.2;
		shelfHeadNo[4][12]=(float)820.7;
		shelfHeadNo[4][13]=(float)825.38;
		shelfHeadNo[4][14]=(float)825.67;//825.6=74
		shelfHeadNo[4][15]=(float)825.76;
		shelfHeadNo[4][16]=(float)825.76;
		shelfHeadNo[4][17]=(float)827.6;//827=6
		shelfHeadNo[4][18]=(float)827.7;/////////835.467=2
		shelfHeadNo[4][19]=(float)837.467;
		shelfHeadNo[4][20]=(float)837.1261;//837.126.11
		shelfHeadNo[4][21]=(float)876.32;
		shelfHeadNo[4][22]=(float)892.24;//"o" and N,N has one shelf.so just "o"
		shelfHeadNo[4][23]=(float)928.91;
		shelfHeadNo[4][24]=(float)0;
		shelfHeadNo[4][25]=(float)121.5;
		shelfHeadNo[4][26]=(float)151.2;///O4
		shelfHeadNo[4][27]=(float)212;
		shelfHeadNo[4][28]=(float)400;
		shelfHeadNo[4][29]=(float)630;//O6-3

		
		
		
		//三楼西 ROOM=5
		shelfHeadNo[5][0]=(float)0;//only  p
		shelfHeadNo[5][1]=(float)0;//Q 
		shelfHeadNo[5][2]=(float)161;
		shelfHeadNo[5][3]=(float)282.7107;//282.710.7
		shelfHeadNo[5][4]=(float)540.4 ;
		shelfHeadNo[5][5]=(float)0;//s HAS ONE SHELF
		shelfHeadNo[5][6]=(float)6;//Tb d e f g
		shelfHeadNo[5][7]=(float)470;//
		shelfHeadNo[5][8]=(float)385.2;//
		shelfHeadNo[5][9]=(float)0;//TG
		shelfHeadNo[5][10]=(float)659;
		shelfHeadNo[5][11]=(float)0;
		shelfHeadNo[5][12]=(float)126;
		shelfHeadNo[5][13]=(float)6;//tj06
		shelfHeadNo[5][14]=(float)0.42;//tm-43
		shelfHeadNo[5][15]=(float)7;//tm07
		shelfHeadNo[5][16]=(float)100;
		shelfHeadNo[5][17]=(float)520;
		shelfHeadNo[5][18]=(float)730;
		shelfHeadNo[5][19]=(float)1;//tn01
		shelfHeadNo[5][20]=(float)432;
		shelfHeadNo[5][21]=(float)800;
		shelfHeadNo[5][22]=(float)912.3;
		shelfHeadNo[5][23]=(float)920;
		shelfHeadNo[5][24]=(float)946.5;//tn and tp
		shelfHeadNo[5][25]=(float)180;
		shelfHeadNo[5][26]=(float)300;
		shelfHeadNo[5][27]=(float)301.6;
		shelfHeadNo[5][28]=(float)311.13;
		shelfHeadNo[5][29]=(float)311.5;

		
		
		//四楼东room=6
		shelfHeadNo[6][0]=(float)742;
		shelfHeadNo[6][1]=(float)767;
		shelfHeadNo[6][2]=(float)850;
		shelfHeadNo[6][3]=(float)986.3;
		shelfHeadNo[6][4]=(float)523;
		shelfHeadNo[6][5]=(float)415.12;
		shelfHeadNo[6][6]=(float)463.6;
		shelfHeadNo[6][7]=(float)469.1102;
		shelfHeadNo[6][8]=(float)471.3;
		shelfHeadNo[6][9]=(float)472.41;
		shelfHeadNo[6][10]=(float)494;
		shelfHeadNo[6][11]=(float)196;
		shelfHeadNo[6][12]=(float)550;
		shelfHeadNo[6][13]=(float)126;
		//这个库只有14个架子
		
		//四楼西库  room=7
		
		shelfHeadNo[7][0]=(float)312;
		shelfHeadNo[7][1]=(float)312;
		shelfHeadNo[7][2]=(float)312;
		shelfHeadNo[7][3]=(float)312;
		shelfHeadNo[7][4]=(float)316.7;
		shelfHeadNo[7][5]=(float)316.86;
		shelfHeadNo[7][6]=(float)360;
		shelfHeadNo[7][7]=(float)370;
		shelfHeadNo[7][8]=(float)391.41;
		shelfHeadNo[7][9]=(float)391.41;
		shelfHeadNo[7][10]=(float)391.41;
		shelfHeadNo[7][11]=(float)391.41;
		shelfHeadNo[7][12]=(float)393;
		shelfHeadNo[7][13]=(float)393.09;
		shelfHeadNo[7][14]=(float)393.092;
		shelfHeadNo[7][15]=(float)393.1;
		shelfHeadNo[7][16]=(float)393.4;
		shelfHeadNo[7][17]=(float)320;///change here 
		shelfHeadNo[7][18]=(float)235.1;
		shelfHeadNo[7][19]=(float)935.5;
		shelfHeadNo[7][20]=(float)97;
		shelfHeadNo[7][21]=(float)974.1;
		shelfHeadNo[7][22]=(float)98;
		shelfHeadNo[7][23]=(float)201.1;
		shelfHeadNo[7][24]=(float)241;
		shelfHeadNo[7][25]=(float)318;
		shelfHeadNo[7][26]=(float)400;
		shelfHeadNo[7][27]=(float)532;
		shelfHeadNo[7][28]=(float)712;
		shelfHeadNo[7][29]=(float)723.2;
		
		
		shelfTailLet=new char[8][30];//只初始化要判断的
		shelfTailNum=new int [8][30];
		
		//一楼东
		shelfTailLet[0][3]='D';
		shelfTailNum[0][3]=661;
		
		shelfTailLet[0][5]='L';
		shelfTailNum[0][5]=61;
		shelfTailLet[0][9]='D';
		shelfTailNum[0][9]=120;
		shelfTailLet[0][10]='S';
		shelfTailNum[0][10]=709;
		shelfTailLet[0][14]='S';
		shelfTailNum[0][14]=7;
		shelfTailLet[0][17]='B';
		shelfTailNum[0][17]=433;
		shelfTailLet[0][19]='Z';
		shelfTailNum[0][19]=130;
		shelfTailLet[0][20]='F';
		shelfTailNum[0][20]=483;
		shelfTailLet[0][22]='Y';
		shelfTailNum[0][22]=618;
		
		//一楼西
		shelfTailLet[1][1]='X';
		shelfTailNum[1][1]=258;
		shelfTailLet[1][6]='X';
		shelfTailNum[1][6]=822;
		shelfTailLet[1][7]='W';
		shelfTailNum[1][7]=83;
		shelfTailLet[1][10]='W';
		shelfTailNum[1][10]=997;
		shelfTailLet[1][12]='Z';
		shelfTailNum[1][12]=102;
		shelfTailLet[1][19]='Y';
		shelfTailNum[1][19]=198;
		shelfTailLet[1][22]='Z';
		shelfTailNum[1][22]=220;
		shelfTailLet[1][23]='L';
		shelfTailNum[1][23]=201;
		shelfTailLet[1][24]='Z';
		shelfTailNum[1][24]=558;
		
		
		//二楼东
		shelfTailLet[2][1]='Z';
		shelfTailNum[2][1]=808;
		shelfTailLet[2][2]='M';
		shelfTailNum[2][2]=975;
		shelfTailLet[2][4]='Q';
		shelfTailNum[2][4]=17;//Q0171  只取前三位，即17
		shelfTailLet[2][6]='W';
		shelfTailNum[2][6]=647;
		shelfTailLet[2][8]='H';
		shelfTailNum[2][8]=325;
		shelfTailLet[2][9]='P';
		shelfTailNum[2][9]=141;
		shelfTailLet[2][11]='G';
		shelfTailNum[2][11]=804;
		shelfTailLet[2][12]='Z';
		shelfTailNum[2][12]=810;
		shelfTailLet[2][13]='L';//
		shelfTailNum[2][13]=715;
		shelfTailLet[2][14]='G';//  IT 'S ALL I247.53
		shelfTailNum[2][14]=200;
		shelfTailLet[2][15]='S';//
		shelfTailNum[2][15]=367;
		shelfTailLet[2][16]='Z';//
		shelfTailNum[2][16]=317;
		shelfTailLet[2][17]='J';//
		shelfTailNum[2][17]=242;
		shelfTailLet[2][18]='W';//
		shelfTailNum[2][18]=206;
		shelfTailLet[2][19]='Z';//
		shelfTailNum[2][19]=890;
		shelfTailLet[2][20]='S';//
		shelfTailNum[2][20]=672;
		shelfTailLet[2][21]='W';
		shelfTailNum[2][21]=630;
		shelfTailLet[2][22]='H';
		shelfTailNum[2][22]=167;
		shelfTailLet[2][23]='L';
		shelfTailNum[2][23]=133;
		shelfTailLet[2][24]='Z';
		shelfTailNum[2][24]=872;
		shelfTailLet[2][25]='Z';
		shelfTailNum[2][25]=879;
		shelfTailLet[2][26]='L';
		shelfTailNum[2][26]=561;
		shelfTailLet[2][27]='C';	
		shelfTailNum[2][27]=941;
		shelfTailLet[2][28]='D';
		shelfTailNum[2][28]=200;
		
		//二楼西
		shelfTailLet[3][1]='L';
		shelfTailNum[3][1]=845;
		shelfTailLet[3][2]='W';
		shelfTailNum[3][2]=959;
		shelfTailLet[3][3]='L';
		shelfTailNum[3][3]=600;
		shelfTailLet[3][4]='Z';
		shelfTailNum[3][4]=113;
		shelfTailLet[3][5]='L';
		shelfTailNum[3][5]=678;
		shelfTailLet[3][6]='D';
		shelfTailNum[3][6]=593;
		shelfTailLet[3][7]='X';
		shelfTailNum[3][7]=873;
		shelfTailLet[3][9]='T';
		shelfTailNum[3][9]=144;
		shelfTailLet[3][10]='Z';
		shelfTailNum[3][10]=938;
		shelfTailLet[3][13]='L';
		shelfTailNum[3][13]=472;
		shelfTailLet[3][14]='L';
		shelfTailNum[3][14]=523;
		shelfTailLet[3][15]='C';
		shelfTailNum[3][15]=442;
		shelfTailLet[3][16]='S';
		shelfTailNum[3][16]=996;
		shelfTailLet[3][17]='M';
		shelfTailNum[3][17]=213;
		shelfTailLet[3][18]='Z';//这里字母与上一个架子没有连续，所以不用记录数字
		shelfTailLet[3][20]='W';
		shelfTailNum[3][20]=285;
		shelfTailLet[3][21]='H';
		shelfTailLet[3][23]='Z';
		shelfTailNum[3][23]=775;
		shelfTailLet[3][24]='L';
		shelfTailNum[3][24]=75;
		shelfTailLet[3][25]='W';
		shelfTailNum[3][25]=274;
		shelfTailLet[3][26]='Z';
		shelfTailNum[3][26]=744;
		shelfTailLet[3][27]='C';
		shelfTailNum[3][27]=868;
		shelfTailLet[3][28]='Y';
		shelfTailNum[3][28]=156;
		shelfTailLet[3][29]='G';
		shelfTailNum[3][29]=139;
		
		//三楼东
		shelfTailLet[4][1]='W';
		shelfTailNum[4][1]=685;
		shelfTailLet[4][2]='Q';
		shelfTailNum[4][2]=637;
		shelfTailLet[4][13]='M';
		shelfTailNum[4][13]=226;
		shelfTailLet[4][15]='S';
		shelfTailNum[4][15]=921;
		
		//三楼西
		shelfTailLet[5][4]='F';
		shelfTailNum[5][4]=531;
		shelfTailLet[5][19]='G';
		shelfTailNum[5][19]=147;
		shelfTailLet[5][28]='S';
		shelfTailNum[5][28]=27;
		
		//四楼东
		
		shelfTailLet[6][1]='Y';
		shelfTailNum[6][1]=288;
		shelfTailLet[6][2]='W';
		shelfTailNum[6][2]=265;
		shelfTailLet[6][3]='C';
		shelfTailNum[6][3]=968;
		//shelfTailLet[6][4]='';   tV
		//shelfTailNum[6][4]=;
		shelfTailLet[6][6]='C';
		shelfTailNum[6][6]=869;
		shelfTailLet[6][7]='L';
		shelfTailLet[6][8]='H';
		shelfTailNum[6][8]=220;
		shelfTailLet[6][9]='X';
		shelfTailNum[6][9]=324;
		
		
		//四楼西
		shelfTailLet[7][4]='X';
		shelfTailNum[7][4]=642;
		shelfTailLet[7][5]='T';
		shelfTailNum[7][5]=163;
		shelfTailLet[7][7]='G';
		shelfTailNum[7][7]=82;
		shelfTailLet[7][8]='B';
		shelfTailNum[7][8]=146;
		shelfTailLet[7][9]='L';
		shelfTailNum[7][9]=238;
		shelfTailLet[7][10]='W';
		shelfTailNum[7][10]=32;
		shelfTailLet[7][11]='Z';
		shelfTailNum[7][11]=611;
		shelfTailLet[7][12]='S';
		shelfTailNum[7][12]=780;
		shelfTailLet[7][14]='Z';
		shelfTailNum[7][14]=23;
		shelfTailLet[7][15]='M';
		shelfTailNum[7][15]=34;
		shelfTailLet[7][16]='J';
		shelfTailNum[7][16]=36;
		shelfTailLet[7][18]='L';
		shelfTailNum[7][18]=707;
		shelfTailLet[7][21]='Z';
		shelfTailNum[7][21]=110;
		shelfTailLet[7][23]='Q';
		shelfTailNum[7][23]=962;
		shelfTailLet[7][25]='L';
		shelfTailNum[7][25]=314;
		shelfTailLet[7][26]='S';
		shelfTailNum[7][26]=950;		
		shelfTailLet[7][27]='Z';
		shelfTailNum[7][27]=733;		
		shelfTailLet[7][28]='L';
		shelfTailNum[7][28]=233;		
		shelfTailLet[7][29]='B';
		shelfTailNum[7][29]=807;	
		
	}
	
	public int getShelf_Let_begin(char firstLet,int room) 
	{
		int numForLetter=(int)firstLet-'A';
		if(firstLet=='D' && room==1)
		{	
			shelf_Let_begin[numForLetter]=0;
		}
		else if(firstLet=='F' && room==3)
		{
			shelf_Let_begin[numForLetter]=0;
		}
		else if(firstLet=='H' && room==2)
		{
			shelf_Let_begin[numForLetter]=0;

		}
		return shelf_Let_begin[numForLetter];
	}
	
	public int getShelf_Let_end(char firstLet,int room) 
	{
		int numForLetter=(int)firstLet-'A';
		
		if(firstLet=='D' && room==1)
		{
			shelf_Let_end[numForLetter]=13;
		}
		else if(firstLet=='F' && room==3)
		{
			shelf_Let_end[numForLetter]=7;
		}
		else if(firstLet=='H' && room==2)
		{
			shelf_Let_end[numForLetter]=6;
		}
		return shelf_Let_end[numForLetter];
	}
	

	public int getTbegin(char firstLet2,int room) 
	{
		if(firstLet2=='G')
		{
			Tbegin=8;
		}
		else if(firstLet2=='M')
		{
			Tbegin=13;
		}
		else if(firstLet2=='N')
		{
			Tbegin=19;
		}
		else if(firstLet2=='P')
		{
			Tbegin=24;
			if(room==7){
				Tbegin=0;
			}
		}
		else if(firstLet2=='S')
		{
			Tbegin=17;
		}
		else if(firstLet2=='U')
		{
			Tbegin=21;
			if(room==6)
			{
				Tbegin=0;
			}
		}
		return Tbegin;
	}
	
	public int getTend(char firstLet2,int room) 
	{
		if(firstLet2=='G')
		{
			Tend=10;
		}
		else if(firstLet2=='M')
		{
			Tend=18;
		}
		else if(firstLet2=='N')
		{
			Tend=24;
		}
		else if(firstLet2=='P')
		{
			Tend=29;
			if(room==7)
			{
				Tend=16;
			}
		}
		else if(firstLet2=='S')
		{
			Tend=21;
		}
		else if(firstLet2=='U')
		{
			Tend=29;
			if(room==6){
				Tend=4;
			}
		}
		return Tend;
	}
	
	public float getShelfHeadNo(int room,int shelfNo)
	{
		return shelfHeadNo[room][shelfNo];
	}
	
	
	public char getShelfTailLet(int room,int shelfNo)
	{
		return shelfTailLet[room][shelfNo];
	}
	
	public int getShelfTailNum(int room,int shelfNo)
	{
		return shelfTailNum[room][shelfNo];
	}

	
}
