package com.project.find_index;



/*
 * 
 * By Mrli     update:2013.10.06  11:00 
 * 没解决的问题有： 
处理字符串的类
处理流程：
1）分割字符串，取“/”前面的一段字符串
2）获取第一个字符，即字母，有它就可以判断此书在哪一个库中
3）获得字母后面的，“/”前面的字符串，如果是T,获取T后面的字母，再获取这个字母后面的，“/”前面的字符串
4)根据此字符串的长度进行判断。长度<=2时，补充0或小数点 。//////长度为2的符串且有特殊字符A-4--->A0.4 
5）记录小数点位置
6）把特殊字符都干掉，只剩下数字
7）重新加回小数点
8）将字符串转成浮点数
*/


public class CutString {
	
	public static char first_Let=0;//第一个字母
	public static char first_Let2=0;//T的情况下第二个字母	
	public static float head_Num;//8.1	
	public static char tail_Let;
	public static int tail_Num;
	public static char specialForTP312;
	

	public CutString(String src_Str){
		dealStr(src_Str);
	}
	
	public void dealStr(String src_Str){
		
		int strLength;
		int dotIndex=-1;										//小数点的位置，初始化不能为0，如I245，
		String tempStr = ""; 									//临时字符串1
		String tempStr1 = "";									//临时字符串2
		String strCuted[]=new String[2];						//用来装分割后的字符串
		
		
		strCuted=src_Str.split("/");							//根据‘/’分割字符串
		first_Let=(char)strCuted[0].charAt(0);	
		tail_Let=(char)strCuted[1].charAt(0);
		tail_Num=Integer.valueOf(strCuted[1].substring(1, 4));
		
		//获取首字母
		tempStr=strCuted[0].substring(1);						//获取字母数字后面的字符串
		System.out.println("tempStr-----:"+tempStr);												
		if(first_Let=='T' && strCuted[0].length()>1)
		{													//以T开始的取T后面的字母                 T110
			first_Let2=(char)strCuted[0].charAt(1);			//获取字母数字后面的字符串
			tempStr=strCuted[0].substring(2);
		}
		
		strLength=tempStr.length();

		/*第一步处理字符串*/
		if(strLength==0)
		{														//D ----->D0
			tempStr="0";			
		}
		else if(strLength==1 && !tempStr.equals("0"))
		{														//D1 ---->D100
			tempStr=tempStr+"00";
		}
		else if(strLength==2 && tempStr.charAt(0)>48)
		{														//D11----->D110
			tempStr=tempStr+"0";
		}
		else if(strLength==2 && tempStr.charAt(0)==48)
		{														//D01----->D0.1
			tempStr="0."+tempStr.substring(1);
		}
		
		/*第二步处理字符串*/
		else
		{ 	
			/*获得小数点，冒号，杠号的拉置，无数小数则为-1  45-->'-'  46-->'.'  58-->':'*/
			for(int i=0;i<tempStr.length();i++)
			{
				if(tempStr.charAt(i)==46|| tempStr.charAt(i)==58)
				{
					dotIndex=i;									
					break;
				}
				if(tempStr.charAt(i)==45)
				{
					dotIndex=i+1;
					break;
				}
			}
			/*将这段的字符串的特殊字符都去掉，如?,=,-,".",等,剩下数字串*/
			for(int i=0;i<tempStr.length();i++)
			{
				if(tempStr.charAt(i)>=48 && tempStr.charAt(i)<=57)
				{
					tempStr1+=String.valueOf(tempStr.charAt(i)-48);				
				}
			}			
			tempStr=tempStr1;			
			
			/*重新根据前面的小数点位置加回小数点*/
			if(dotIndex>0)
			{
				tempStr=tempStr.substring(0,dotIndex)+"."+tempStr.substring(dotIndex);
			}
			else if(dotIndex==0)
			{
				tempStr="0."+tempStr;
			}
				
		}
			
		head_Num=Float.parseFloat(tempStr);				//将字符串转成浮点数 
		System.out.println("索书号------->"+src_Str+"转化后 ------>"+tempStr+"转化后转成小数 ----->"+head_Num);
		
		if(first_Let2=='P' && head_Num==312)//for TP312C or TP312XM JA
		{
			specialForTP312=src_Str.charAt(5);
		}
	}	

	public  char getFirst_Let()
	{
		return first_Let;
	}
	
	public  char getFirst_Let2() 
	{
		return first_Let2;
	}
	
	public  float getHead_Num()
	{
		return head_Num;
	}	
	

	public  char getTail_Let()
	{
		return tail_Let;
	}
	
	public  int getTail_Num()
	{
		return tail_Num;
	}	
	
	public static char getSpecialForTP312() 
	{
		return specialForTP312;
	}
}

