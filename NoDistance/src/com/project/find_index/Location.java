package com.project.find_index;


/*
 * 
 * By Mrli			last update:2013.10.06  11:00
 *
 *��ȡ�鱾λ�õ���
 *
 *����ش�BUG��ÿ����ĸ�ӱ���С���ӱȵ�������ʱ�����ȵ�������һ�����ӵĵ�һ���飬�϶������
 *��ʼʱ���ÿ����ĸ�����һ�����ӵ����һ������������У����ȵ�������ʱ����ͨ�����ú���ȡ�������бȽϡ�
 *�Ȳ�˵Ҫ��52���������������ڷ��غ������ж����Ŀ⣨��Щ��ĸ�������ⶼ�У���������̫�鷳
 *�����������ŷ��֣��ȵ����һ�����ӾͿ϶�����������ˣ������ٱȡ������ҺܺõĴ��������Խ������⡣ʧ��ʧ�ߣ��װ��˷���ʱ��   9.30
 *
 *
 *���̣�
 *1����CutString���ȡ����ŵĵ�һ����ĸ�������������֡�����ĸ֪�����ĸ���    8����
 *2����Shelfʵ�����л�ȡ�����ĸ��Ӧ����ܺ����䣨int begin, end)  ÿ���ⶼ��30������
 *3����+���䣬��С�˱ȽϷ�Χ��								��240������ȡ��Լ3--10��
 *4����ȡ�õ���ÿ�����ӵ�һ����������
 *5����1�е��õ�������4�еıȽϣ����С�ڵĻ��Ϳ���ȷ����������Ӳ����أ�������ڣ�˵���������Ӷ��У��������������ӣ����ڱ�����һ���Ƚ�
 *
 * */





public class Location {	

	char firstLet;
	char firstLet2;
	char tailLet;
	char specialT;
	
	float headNum;
	float shelfHeadNo;
	float next_shelfHeadNo=0;
	
	int shelf_Let_begin;
	int shelf_Let_end;	
	int room=-1;
	int whichshelf=0;
	int tailNum=0;
	
	//boolean nextShelf=false;
	String location=null;	
	Shelf s=new Shelf();
	
	
	/*���캯������ֵ*/
	public Location(CutString cs){
		
		
		firstLet=cs.getFirst_Let();
		firstLet2=cs.getFirst_Let2();
		headNum=cs.getHead_Num();
		
		tailLet=cs.getTail_Let();
		tailNum=cs.getTail_Num();
		
		

	}
	
	public int getRoom()
	{
		if(firstLet>='A' && firstLet<'D' || firstLet=='D' && headNum<631)
		{
			room=0;//һ¥����
		}
		else if(firstLet>='D' && firstLet<'F' || firstLet=='F' && headNum<500)
		{
			room=1;//һ¥����
		}	
		else if(firstLet=='H' && headNum>=319 || firstLet=='I')
		{
			room=2;//��¥����
		}
		else if(firstLet>='F' && firstLet<='H' )
		{
			room=3;//��¥����
		}
		
		else if(firstLet>='J' && firstLet<='O')
		{
			room=4;//��¥����
		}
		else if(firstLet>='P' && firstLet<'T'|| firstLet=='T'&& headNum==0|| firstLet=='T' && firstLet2<'P' && firstLet2>='A' || firstLet=='T' && firstLet2=='P' && headNum<=311.5)
		{
			room=5;//��¥���� 
		}
		else if(firstLet>'T' && firstLet<='Z'|| firstLet=='T' && firstLet2=='V' || firstLet=='T'&&firstLet2=='U' && headNum>241)
		{
			room=6;//��¥����
		}
		else if(firstLet=='T' && firstLet2<'U' && firstLet2>='P' || firstLet=='T' &&firstLet2=='U' && headNum<=241)
		{
			room=7;//��¥����
		}
		return room;
	}
	
	/**
	  *���鱾���Ӻ�
	  *@param 
	  *@param 
	  *@return �鱾���Ӻ�
	  */
	public int getShelf(){
		
		/*���жϴ�������������ٴ����ӵ��������Щ�������ֻ��һ�����ӻ��������ӵ�*/
		if(firstLet=='A')									//����û��+1,ֱ�Ӿ��Ƕ�Ӧ�ļ��Ӻ�
		{
			whichshelf=1;
		}
		if(firstLet=='G' && headNum<120)
		{					
			//last_shelf_Let_end=7;							//F��G��ͬһ�����ӣ�����F�������ⶼ�У����Ӻ�Ҳ��ͬ��������һ���������
			whichshelf=8;
		}
		else if(firstLet=='N')
		{
			whichshelf=25;
		}
		else if(firstLet=='P')
		{
			whichshelf=1;
		}
		else if(firstLet=='Q')
		{
			whichshelf=2;
		}
		else if(firstLet=='S')
		{
			whichshelf=1;
		}
		else if(firstLet=='T')
		{
			if(headNum==0)
			{
				whichshelf=6;
			}
			else if(firstLet2=='B')
			{
				if(tailLet<'Y')
					whichshelf=7;
				else
					whichshelf=8;				//����������
			}
			else if(firstLet2=='E')
			{
				if(tailLet<'Y')
					whichshelf=8;
				else
					whichshelf=9;
			}
			else if(firstLet2=='F')
			{
				whichshelf=9;
			}
			else if(firstLet2=='J'||firstLet2=='K'||firstLet2=='L')
			{
				whichshelf=14;
			}
			else if(firstLet2=='P' && headNum==312 )
			{	
				specialT=CutString.getSpecialForTP312();
				if(specialT<'C' || specialT=='C' && tailLet<'G' || specialT=='C' && tailLet=='G' && tailNum<120.9)
					whichshelf=1;
				else if(specialT<'J' || specialT=='J' && tailLet=='A' && tailNum<770)
					whichshelf=2;
				else if(specialT<'X' || specialT=='X' && tailLet<'H' || specialT=='X' && tailLet=='H' && tailNum<472)
					whichshelf=3;
				else
					whichshelf=4;
			}
			else if(firstLet2=='Q')
			{
				if(tailLet=='A' || tailLet=='B' && tailNum<958)
					whichshelf=17;
				else
					whichshelf=18;
			}
			
			/*������T�����������ֻ��T���滹����ĸ  TA----TZ�� ��*/
			else
			{	
				shelf_Let_begin=s.getTbegin(firstLet2, room);
				shelf_Let_end=s.getTend(firstLet2, room);
				compare();
			}
		}
		else if(firstLet=='V')
		{
			whichshelf=11;
		}
		else if(firstLet=='X')
		{
			
			if(tailLet<'G')
				whichshelf=12;
			else
				whichshelf=13;
		}
		else if(firstLet=='Z')
		{
			if(tailLet<'Y' || tailLet=='Y' && tailNum<358)
				whichshelf=13;
			else
				whichshelf=14;
		}
			
		//����Ķ�������ģ�ֻ��һ�����������ӣ�����T��
		else{
			////////////////////////********************
				shelf_Let_begin=s.getShelf_Let_begin(firstLet, room);
				shelf_Let_end=s.getShelf_Let_end(firstLet, room);
				compare();
			}
////////////////////////********************
		return whichshelf;
	}
	
	public void compare()
	{
		for(int i=shelf_Let_begin;i<=shelf_Let_end;i++)
		{

			if(room==-1)
			{
				whichshelf=0;
				break;
			}
			shelfHeadNo=s.getShelfHeadNo(room, i);				
			if(i<30)
			{
				if(i<29)
					next_shelfHeadNo=s.getShelfHeadNo(room, i+1);
				if(shelf_Let_end==i)			//�ȵ������ĸ�����һ�����ӣ��������ˣ�������һ���ӵĵ�һ����϶�����С�������пɱ��ԣ��������
				{
					whichshelf=i+1;
					break;
				}
				
			}
			
			if(headNum<next_shelfHeadNo)
			{
				whichshelf=i+1;
				break;						
			}
			
			if(headNum==next_shelfHeadNo && i<29)
			{
				if(tailLet<s.getShelfTailLet(room, i+1) || tailLet==s.getShelfTailLet(room, i+1) && tailNum<s.getShelfTailNum(room, i+1))
				{	
					whichshelf=i+1;
					break;
				}
				else 
				{
					if(i<27)
					{	
						if(headNum!=s.getShelfHeadNo(room, i+2) )
						{
							whichshelf=i+2;
							System.out.println("sssssssssssss");
							break;
						}
					}
				}
			}
			
			System.out.println("����ĸ  "+firstLet+"  �õ����Ϊ    :"+room+"----����ĸ   "+firstLet+"�Ϳ�� "+room+"   ��ܺŴ�   "+shelf_Let_begin+  "  ��    "+shelf_Let_end+ "-----�����ǵ�   "+i+  "  ��           "+"   -----���ΪΪ��"+headNum+"   ��һ���ӵĵ�һ�����ΪΪ��= "+next_shelfHeadNo);
	
		}
	}
	public String getLocation(){
		
		//String next="";
		String direction="����ͼ��ݴ��ŷ��� ";
		String floor[]=new String[8];
		floor[0]="һ¥���";//0��
		floor[1]="һ¥�Ҳ�";//1��
		floor[2]="��¥���";//2��
		floor[3]="��¥�Ҳ�";//3��
		floor[4]="��¥���";//4��
		floor[5]="��¥�Ҳ�";//5��
		floor[6]="��¥���";//6��
		floor[7]="��¥�Ҳ�";//7��
		
		room=getRoom();
		shelf_Let_begin=s.getShelf_Let_begin(firstLet,room);
		shelf_Let_end=s.getShelf_Let_end(firstLet,room);
		
		int shelf=getShelf();
		if(shelf==0)
		{
			if(room==-1)
			{
				location="��Ǹ��δ��¼���飡";
			}
			location=direction+floor[room]+"�����Ҳ������ĸ���ܣ���������������δ�ϼܣ�����Ҫ��ѯ�ʹ���Ա��";
		}
		else
		{
			location=direction+floor[room]+" �� �� "+String.valueOf(shelf)+" �������";
		}
		return location;
	}
	
}

