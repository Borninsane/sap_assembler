import java.io.*;
import java.util.*;

class sasm
{
	static Scanner sc=new Scanner(System.in);
	
	
	static HashMap<String, Integer> opcode=new HashMap<>();
	static HashMap<String, Integer> putlabel=new HashMap<>();
	static HashMap<Integer, String> getlabelm=new HashMap<>();
	static HashSet<Integer> getlabels=new HashSet<>();
	
	
	public static void main(String args[])
	{
		
		
		if(args.length==0)
		{
			System.out.println("\n@iriis Labs sap_assembler v0.1\n<Usage>: java sasm <options> <sourceFile.asm> <outputFile>");
			System.out.println("\nWhere <options> include:\n  -c2f <outputFile> \t\t\t\t use your Console as input and Dump the results in HEX out file.\n  -f2f <sourceFile.asm> <outputFileName> \t use the source file as input and HEX file as output.\n  -hexdump <sourceFile> \t\t\t use this command to view a file in raw HEX.");
			return;
		}			
		
		putOpcodes();
		
		try
		{
			
		if(args[0].equals("-c2f"))
		console2file(args[1]);
		
		else if(args[0].equals("-hexdump"))
		hexDump(args[1]);
		
		else if(args[0].equals("-f2f"))
		file2file(args[1],args[2]);
		
		else
		{
			System.out.println("\n<Usage>: java sasm <options> <sourceFile.asm> <outputFile>");
			System.out.println("\nWhere <options> include:\n  -c2f <outputFile> \t\t\t\t use your Console as input and Dump the results in HEX out file.\n  -f2f <sourceFile.asm> <outputFileName> \t use the source file as input and HEX file as output.\n  -hexdump <sourceFile> \t\t\t use this command to view a file in raw HEX.");
			return;
		}
		
		
		}
		
		
		catch(Exception E)
		{
			
			System.out.println("fatal error, file not found or no file specified");
			System.out.println("\n<Usage>: java sasm <options> <sourceFile.asm> <outputFile>");
			System.out.println("\nWhere <options> include:\n  -c2f <outputFile> \t\t\t\t use your Console as input and Dump the results in HEX out file.\n  -f2f <sourceFile.asm> <outputFileName> \t use the source file as input and HEX file as output.\n  -hexdump <sourceFile> \t\t\t use this command to view a file in raw HEX.");
			return;
			
		}
		
	}
	
	
	
	
	
	
	static void console2file(String file)
	{
		
		int data[]=new int[256];
		int addr=0;
		
		System.out.println("\nADDR : MNEMONIC");
		System.out.println("---------------");
		
		String ass="";
		
		while(true)
		{
			
			if(addr<16)
			System.out.print("0"+Integer.toHexString(addr)+" : ");
			else
			System.out.print(Integer.toHexString(addr)+" : ");
			
			String s=sc.nextLine().trim();
			
				if(s.length()!=0)
				{
				if(s.charAt(0)==';')
				break;
				}
				
				else
				continue;
				
			
			
			Integer x=opcode.get(s);
			
			if(x!=null)
			{
				if(x==0x01||x==0x02||x==0x03||x==0x04||x==0x05||x==0x06||x==0x07||x==0x08||x==0x09||x==0x0a||x==0x11||x==0x12||x==0x13)
				{
					System.out.println("\nOperand  Missing in Address "+Integer.toHexString(addr)+" !\n");
					continue;
				}
				data[addr++]=x;
			}
			
				
			else
			{
				try
				{
					
				int li=s.lastIndexOf(' ');
				int t=Integer.parseInt(s.substring(li+1),16);
				
				if(t>255)
					{
					System.out.println("\nBad Operand in Adderss "+Integer.toHexString(addr)+" !\n");
					continue;
					}
					
				x=opcode.get(s.substring(0,li));
				
				if(x!=null)
				{
					
				if(!(x==0x01||x==0x02||x==0x03||x==0x04||x==0x05||x==0x06||x==0x07||x==0x08||x==0x09||x==0x0a||x==0x11||x==0x12||x==0x13))
				{
					System.out.println("\nBad Instruction in Address "+Integer.toHexString(addr)+" !\n");
					continue;
				}
				
					data[addr++]=x;					
					data[addr++]=t;
				}
				
					
				else
				System.out.println("\nBad Instruction in Address "+Integer.toHexString(addr)+" !\n");
				}
				catch(Exception E){
					System.out.println("\nBad Operand Or Instruction in Adderss "+Integer.toHexString(addr)+" !\n");
				}
			}
			
			
			if(addr>255)
			break;
		}
		
		System.out.println("\nAssembled "+addr+" bytes");
		
		bytedump(data);

		try
		{
		BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file+".o"), "ISO8859_1"));
		
		for(int i:data)
		fw.write(i);
		
		fw.close();
		}
		
		catch(Exception E)
		{
			System.out.println("\nFatal Error, File not supported !");
		}
		
	}
	
	
	
	
	static void file2file(String file,String ofile)
	{
		
		int data[]=new int[256];
		int addr=0;
		
			String s="";
			int line=0;
			String ml="";
			
		try
		{
		BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO8859_1"));
		
			
		while((s=fr.readLine())!=null)
		{
			line++;
			s=s.trim();
			
			if(s.length()!=0)
			{
				char x=s.charAt(0);
				if(x==';')
				break;
				else if(x=='$')
				continue;
				else if(x=='*')
				{
					int in=s.indexOf(':');
					if(in==-1)
					{
					System.out.println("Error: "+file+": \""+s+"\""+"\nbad label, missing \':\' in line "+line); 
					return;
					}
					
					putlabel.put(s.substring(1,in), addr);
					s=s.substring(in+1).trim();
				}
			}
				
				
			else
			continue;
				
			
			
			Integer x=opcode.get(s);
			
			if(x!=null)
			{
				if(x==0x01||x==0x02||x==0x03||x==0x04||x==0x05||x==0x06||x==0x07||x==0x08||x==0x09||x==0x0a||x==0x11||x==0x12||x==0x13)
				{
					System.out.println("Error: "+file+": \""+s+"\"");
					System.out.println("operand  missing in line "+line+".\n");
					return;
				}
				data[addr++]=x;
			}
			
				
			else
			{
				try
				{
					
				int li=s.lastIndexOf(' ');
				
					
				x=opcode.get(s.substring(0,li));
				
				if(x!=null)
				{
					
				if(!(x==0x01||x==0x02||x==0x03||x==0x04||x==0x05||x==0x06||x==0x07||x==0x08||x==0x09||x==0x0a||x==0x11||x==0x12||x==0x13))
				{
					System.out.println("Error: "+file+": \""+s+"\"");
					System.out.println("bad instruction in line "+line+".\n");
					return;
				}
				
					data[addr++]=x;		
					
					int t=Integer.parseInt(s=s.substring(li+1),16);
				
					if(t>255)
					{
						System.out.println("Error: "+file+": \""+s+"\"");
						System.out.println("bad operand in line "+line+".\n");
						return;
					}					
					data[addr++]=t;
				}
				
					
				else
				{
				System.out.println("Error: "+file+": \""+s+"\"");
				System.out.println("bad instruction in line "+line+".\n");
				return;
				}
				}
				catch(Exception E){
					
					int st=s.indexOf('*');
					
					if(st>-1)
					{
					s=s.substring(st+1);
					getlabelm.put(addr,s);
					getlabels.add(addr);
					addr++;
					continue;
					}
					System.out.println(s);
					System.out.println("Error: "+file+": \""+s+"\"");
					System.out.println("bad operand or instruction in line "+line+".\n");
					return;
				}
			}
			if(addr>255)
			{
				System.out.println("not enough memory Exiting");
				return;
			}
			
			
		}
		
		
		for(int i:getlabels)
		data[line=i]=putlabel.get(ml=getlabelm.get(i));
		
		
		System.out.println("\nAssembled "+addr+" bytes");
		
		bytedump(data);
		
		BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ofile+".o"), "ISO8859_1"));
		
		for(int i:data)
		fw.write(i);
		
		fw.close();
		
		}
		catch(UnsupportedEncodingException Er)
		{
			System.out.println("fatal error, file format not supported"); 
		}
		catch(FileNotFoundException E)
		{	
			System.out.println("fatal error, file not found or no file specified");
		}
		catch(NullPointerException E)
		{
			System.out.println("Error: "+file);
			System.out.println("missing label \""+ml+"\" in line "+(line+1)+".");
		}
		catch(IOException E)
		{
			System.out.println("unknown error occured -1");
		}
		
		
	}
			
			
	
	
	
	static void bytedump(int data[])
	{
		int n=data.length/16;
		
		System.out.print("\n\t     ");
		for(int i=0;i<n;i++)
		System.out.print(" 0"+Integer.toHexString(i));
		
		System.out.println("\n\t      _______________________________________________\n");
		
		for(int i=0;i<n;i++)
		{
			System.out.print("\t"+Integer.toHexString(i)+"0 | ");
			
			for(int j=0;j<16;j++)
			{
				
				if(data[i*n+j]<n)
				System.out.print(" 0"+Integer.toHexString(data[i*n+j]));
				else
				System.out.print(" "+Integer.toHexString(data[i*n+j]));
			}
			System.out.println();
		}
	}

		
		
		
		
	
	
	static void hexDump(String file)
	{
		
		try
		{
		
		BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO8859_1"));
		
		System.out.print("\n \t|");
		for(int i=0;i<16;i++)
		System.out.print(" 0"+Integer.toHexString(i));
		
		System.out.println(" |      DUMP      |\n\t|_________________________________________________|________________|");

  
		int i=0,j=1;
		String s="";
		
		
		
		while ((i=fr.read())!=-1) 
		{
			
			if(i>32)
			s+=(char)i;
			else
			s+=".";
			
			if((j-1)%16==0)
			{
				
				
				String t=Integer.toHexString(j-1);
				
				System.out.print("  ");
			for(int k=t.length();k<5;k++)
			System.out.print("0");
			
			System.out.print(t+" | ");
			}
			
			String t=Integer.toHexString(i);
			if(i<16)
			t="0"+t;
			System.out.print(t+" "); 
	
			if(j%16==0)
			{
				
				System.out.print("["+s+"]");
				System.out.println();				
				s="";
			}
	
		j++;
			
		}
		
		
		j=16-(j-1)%16;
		if(j%16!=0)
		{
		for(byte x=0;x<j;x++,s+=".")
		System.out.print("   ");
		
		System.out.println("["+s+"]");
		}
		
		}
		
		catch(Exception E)
		{
			System.out.println("Error, No such file found.");
		}
  
	}

	
	
	
	
	
	
	static void putOpcodes()
	{
opcode.put("nop",0x0);
opcode.put("lda",0x1);
opcode.put("sta",0x2);
opcode.put("ldi",0x3);
opcode.put("addz",0x4);
opcode.put("jmp",0x5);
opcode.put("subz",0x6);
opcode.put("adi",0x7);
opcode.put("subi",0x8);
opcode.put("jz",0x9);
opcode.put("jc",0xa);
opcode.put("ldax b",0xb);
opcode.put("ldax c",0xc);
opcode.put("ldax d",0xd);
opcode.put("stax b",0xe);
opcode.put("stax c",0xf);
opcode.put("stax d",0x10);
opcode.put("mvi b",0x11);
opcode.put("mvi c",0x12);
opcode.put("mvi d",0x13);
opcode.put("mov a,b",0x14);
opcode.put("mov a,c",0x15);
opcode.put("mov a,d",0x16);
opcode.put("mov b,a",0x17);
opcode.put("mov b,c",0x18);
opcode.put("mov b,d",0x19);
opcode.put("mov c,a",0x1a);
opcode.put("mov c,b",0x1b);
opcode.put("mov c,d",0x1c);
opcode.put("mov d,a",0x1d);
opcode.put("mov d,b",0x1e);
opcode.put("mov d,c",0x1f);
opcode.put("add a",0x20);
opcode.put("add b",0x21);
opcode.put("add c",0x22);
opcode.put("add d",0x23);
opcode.put("sub a",0x24);
opcode.put("sub b",0x25);
opcode.put("sub c",0x26);
opcode.put("sub d",0x27);
opcode.put("inr a",0x28);
opcode.put("inr b",0x29);
opcode.put("inr c",0x2a);
opcode.put("inr d",0x2b);
opcode.put("hlt",0xff);

}
	

}
	
