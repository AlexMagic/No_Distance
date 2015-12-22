package com.project.find_index;


/*
 * 
 * By Mrli			last update:2013.10.06  11:00
 *
 *获取书本位置的类
 *
 *解决重大BUG，每个字母从比最小架子比到最大架子时，最后比的数是下一个架子的第一本书，肯定会出错，
 *开始时想把每个字母的最后一个架子的最后一本书放在数组中，到比到最大架子时，再通过调用函数取出，进行比较。
 *先不说要存52个浮点数，还得在返回函数中判断它的库（有些字母在两个库都有）。。。这太麻烦
 *最后存完数组后才发现，比到最后一个架子就肯定是这个架子了，不用再比。。而且很好的处理掉可能越界的问题。失策失策，白白浪费了时间   9.30
 *
 *
 *过程：
 *1）从CutString类获取索书号的第一个字母，及其后面的数字。由字母知道是哪个库    8个库
 *2）从Shelf实体类中获取这个字母对应的书架号区间（int begin, end)  每个库都有30个架子
 *3）库+区间，缩小了比较范围，								从240个架子取到约3--10个
 *4）获取得到的每个架子第一本书的索书号
 *5）用1中得用的数字与4中的比较，如果小于的话就可以确定是这个架子并返回，如果等于，说明两个架子都行，返回这两个架子，大于便与下一个比较
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
	
	
	/*构造函数赋初值*/
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
			room=0;//一楼东库
		}
		else if(firstLet>='D' && firstLet<'F' || firstLet=='F' && headNum<500)
		{
			room=1;//一楼西库
		}	
		else if(firstLet=='H' && headNum>=319 || firstLet=='I')
		{
			room=2;//二楼东库
		}
		else if(firstLet>='F' && firstLet<='H' )
		{
			room=3;//二楼西库
		}
		
		else if(firstLet>='J' && firstLet<='O')
		{
			room=4;//三楼东库
		}
		else if(firstLet>='P' && firstLet<'T'|| firstLet=='T'&& headNum==0|| firstLet=='T' && firstLet2<'P' && firstLet2>='A' || firstLet=='T' && firstLet2=='P' && headNum<=311.5)
		{
			room=5;//三楼西库 
		}
		else if(firstLet>'T' && firstLet<='Z'|| firstLet=='T' && firstLet2=='V' || firstLet=='T'&&firstLet2=='U' && headNum>241)
		{
			room=6;//四楼东库
		}
		else if(firstLet=='T' && firstLet2<'U' && firstLet2>='P' || firstLet=='T' &&firstLet2=='U' && headNum<=241)
		{
			room=7;//四楼西库
		}
		return room;
	}
	
	/**
	  *求书本架子号
	  *@param 
	  *@param 
	  *@return 书本架子号
	  */
	public int getShelf(){
		
		/*先判断处理特殊情况，再处理复杂的情况。这些情况都是只有一个架子或两个架子的*/
		if(firstLet=='A')									//这里没有+1,直接就是对应的架子号
		{
			whichshelf=1;
		}
		if(firstLet=='G' && headNum<120)
		{					
			//last_shelf_Let_end=7;							//F和G在同一个架子，并且F在两个库都有，架子号也不同。所以算一个特殊情况
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
					whichshelf=8;				//有两个架子
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
			
			/*这里是T的特殊情况，只有T后面还有字母  TA----TZ都 有*/
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
			
		//上面的都是特殊的，只有一个或两个架子，还有T的
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
				if(shelf_Let_end==i)			//比到这个字母的最后一个架子，就是它了，它的下一架子的第一本书肯定比它小，不具有可比性，还会出错
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
			
			System.out.println("由字母  "+firstLet+"  得到库号为    :"+room+"----由字母   "+firstLet+"和库号 "+room+"   书架号从   "+shelf_Let_begin+  "  到    "+shelf_Let_end+ "-----这里是第   "+i+  "  架           "+"   -----书号为为："+headNum+"   下一架子的第一本书号为为：= "+next_shelfHeadNo);
	
		}
	}
	public String getLocation(){
		
		//String next="";
		String direction="进入图书馆大门方向 ";
		String floor[]=new String[8];
		floor[0]="一楼左侧";//0东
		floor[1]="一楼右侧";//1西
		floor[2]="二楼左侧";//2东
		floor[3]="二楼右侧";//3西
		floor[4]="三楼左侧";//4东
		floor[5]="三楼右侧";//5西
		floor[6]="四楼左侧";//6东
		floor[7]="四楼右侧";//7西
		
		room=getRoom();
		shelf_Let_begin=s.getShelf_Let_begin(firstLet,room);
		shelf_Let_end=s.getShelf_Let_end(firstLet,room);
		
		int shelf=getShelf();
		if(shelf==0)
		{
			if(room==-1)
			{
				location="抱歉，未收录此书！";
			}
			location=direction+floor[room]+"，但找不到在哪个书架，可能这是新书尚未上架，有需要请询问管理员！";
		}
		else
		{
			location=direction+floor[room]+" 的 第 "+String.valueOf(shelf)+" 号书架上";
		}
		return location;
	}
	
}

