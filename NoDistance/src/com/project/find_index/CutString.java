package com.project.find_index;



/*
 * 
 * By Mrli     update:2013.10.06  11:00 
 * û����������У� 
�����ַ�������
�������̣�
1���ָ��ַ�����ȡ��/��ǰ���һ���ַ���
2����ȡ��һ���ַ�������ĸ�������Ϳ����жϴ�������һ������
3�������ĸ����ģ���/��ǰ����ַ����������T,��ȡT�������ĸ���ٻ�ȡ�����ĸ����ģ���/��ǰ����ַ���
4)���ݴ��ַ����ĳ��Ƚ����жϡ�����<=2ʱ������0��С���� ��//////����Ϊ2�ķ������������ַ�A-4--->A0.4 
5����¼С����λ��
6���������ַ����ɵ���ֻʣ������
7�����¼ӻ�С����
8�����ַ���ת�ɸ�����
*/


public class CutString {
	
	public static char first_Let=0;//��һ����ĸ
	public static char first_Let2=0;//T������µڶ�����ĸ	
	public static float head_Num;//8.1	
	public static char tail_Let;
	public static int tail_Num;
	public static char specialForTP312;
	

	public CutString(String src_Str){
		dealStr(src_Str);
	}
	
	public void dealStr(String src_Str){
		
		int strLength;
		int dotIndex=-1;										//С�����λ�ã���ʼ������Ϊ0����I245��
		String tempStr = ""; 									//��ʱ�ַ���1
		String tempStr1 = "";									//��ʱ�ַ���2
		String strCuted[]=new String[2];						//����װ�ָ����ַ���
		
		
		strCuted=src_Str.split("/");							//���ݡ�/���ָ��ַ���
		first_Let=(char)strCuted[0].charAt(0);	
		tail_Let=(char)strCuted[1].charAt(0);
		tail_Num=Integer.valueOf(strCuted[1].substring(1, 4));
		
		//��ȡ����ĸ
		tempStr=strCuted[0].substring(1);						//��ȡ��ĸ���ֺ�����ַ���
		System.out.println("tempStr-----:"+tempStr);												
		if(first_Let=='T' && strCuted[0].length()>1)
		{													//��T��ʼ��ȡT�������ĸ                 T110
			first_Let2=(char)strCuted[0].charAt(1);			//��ȡ��ĸ���ֺ�����ַ���
			tempStr=strCuted[0].substring(2);
		}
		
		strLength=tempStr.length();

		/*��һ�������ַ���*/
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
		
		/*�ڶ��������ַ���*/
		else
		{ 	
			/*���С���㣬ð�ţ��ܺŵ����ã�����С����Ϊ-1  45-->'-'  46-->'.'  58-->':'*/
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
			/*����ε��ַ����������ַ���ȥ������?,=,-,".",��,ʣ�����ִ�*/
			for(int i=0;i<tempStr.length();i++)
			{
				if(tempStr.charAt(i)>=48 && tempStr.charAt(i)<=57)
				{
					tempStr1+=String.valueOf(tempStr.charAt(i)-48);				
				}
			}			
			tempStr=tempStr1;			
			
			/*���¸���ǰ���С����λ�üӻ�С����*/
			if(dotIndex>0)
			{
				tempStr=tempStr.substring(0,dotIndex)+"."+tempStr.substring(dotIndex);
			}
			else if(dotIndex==0)
			{
				tempStr="0."+tempStr;
			}
				
		}
			
		head_Num=Float.parseFloat(tempStr);				//���ַ���ת�ɸ����� 
		System.out.println("�����------->"+src_Str+"ת���� ------>"+tempStr+"ת����ת��С�� ----->"+head_Num);
		
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

