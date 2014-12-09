package Disassembler;
import java.io.*;
import java.util.*;

import javax.swing.*;
public class Try2 {

	//checks whether the parameter is a number or not
	public static boolean isNumber(String num){
		try{
			int number = Integer.parseInt(num);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static void main(String[] args)   {
		String fileName = JOptionPane.showInputDialog("Enter filename: ");
		String fileName1 = null;
		int messCount = 0;
		int[] array = new int[10];
		int[] openCurly = new int[10];					// 1 - inside main; 2 - inside if; 3 - inside forLoop; 4 - inside whileLoop; 5 - inside doWhile
		int[] closeCurly = new int[10];					// 1 - end main; 2 - end if; 3 - end forLoop; 4 - end whileLoop; 5 - end doWhile
		int counter = 0;
		closeCurly[0] = 0;
	    PrintWriter outputStream = null;
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            fileName = fileName.substring(0, fileName.length()-4);
            fileName1 = fileName + "_Converted.asm";
            outputStream = new PrintWriter(new FileOutputStream(fileName1));
            String line = inputStream.readLine();
            StringBuilder b = new StringBuilder();
            ArrayList<String> problemsName = new ArrayList<String>();
            ArrayList<String> problemsValue = new ArrayList<String>();
            
            outputStream.println("title "+ fileName);
            outputStream.println(";Christine Joyce O. Arzadon");
            outputStream.println(";CS 131");
            outputStream.println(";2012-13554");
            outputStream.println(";Ma. Isabella Dominique Inosantos");
            outputStream.println(";CS 131");
            outputStream.println(";2012-23607");
            outputStream.println();
            outputStream.println(".model small");
            outputStream.println(".data");
            outputStream.println("	ten db 10");
            outputStream.println("	decima db 0,0,0,0,0,'$'");
            
            while (line != null) {
            	b.append(line);
            	b.append("\n");
            	line = inputStream.readLine();
            }
            inputStream.close();
            
            // loop para sa declaration ng variables
            StringTokenizer token = new StringTokenizer(b.toString(), "\n");
            while(token.hasMoreTokens()){
            	String token1 = token.nextToken().replaceAll("\t", "");
            	if(token1.contains("main(")){
            		openCurly[0] = 1;
            		counter++;
            	}else if(token1.contains("for(")){
            		openCurly[counter] = 3;
            		counter++;
            	}else if(token1.contains("while(")){
            		openCurly[counter] = 4;
            		counter++;
            	}else if(token1.contains("do")){
            		openCurly[counter] = 5;
            		counter++;
            	}else if((token1.contains("if(")) || ((token1.contains("if (")))){
        			openCurly[counter] = 2;
            		counter++;
        		}else if((token1.contains("else")) && (!token1.contains("if"))){
        			openCurly[counter] = 6;
        			counter++;
        		}else if(token1.contains("}")){
            		int count = 0;
            		int num = 0;
            		for(int i = 0;i < 10;i++){
            			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
            				count = i;
            				num = openCurly[i];
            			}
            		}
            		closeCurly[count] = num;
            	}else if((token1.contains("char")) && (openCurly[0] == 1) && (closeCurly[0] != 1)){
            		if(token1.contains("=")){
            			outputStream.println("	"+ token1.substring(0, token1.indexOf("=")).replaceAll("char", "").replaceAll(" ", "").replaceAll("\t", "") +" db "+ token1.substring(token1.indexOf("=")+1, token1.indexOf(";")));
            		}else{
            			if(token1.contains("[")){
            				outputStream.println("	"+ token1.substring(0, token1.indexOf("[")).replaceAll("char", "").replaceAll(" ", "").replaceAll("\t", "") +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            			}else{
            				outputStream.println("	"+ token1.substring(0, token1.indexOf(";")).replaceAll("char", "").replaceAll(" ", "").replaceAll("\t", "") +" db ?");
            			}
            		}
        			System.out.println(token1);
            	}else if((token1.contains("int")) && (openCurly[0] == 1) && (closeCurly[0] != 1)){
            		String varName = null;
            		String value = null;
            		if(token1.contains(",")){
            			StringTokenizer echos = new StringTokenizer(token1,",;");
            			while(echos.hasMoreTokens()){
            				String echos1 = echos.nextToken();
            				if(echos1.contains("=")){
            					if(echos1.contains("int")){
                					varName = echos1.substring(0, echos1.indexOf("=")).replaceAll(" ", "").replaceAll("int", "").replaceAll("\t", "");
                					value = echos1.substring(echos1.indexOf("=")+1, echos1.length()).replaceAll(" ", "");
                					outputStream.println("	"+ varName +" db "+ value);
                				}else{
                					varName = echos1.substring(0, echos1.indexOf("=")).replaceAll(" ", "");
                					value = echos1.substring(echos1.indexOf("=")+1, echos1.length()).replaceAll(" ", "");
                					outputStream.println("	"+ varName +" db "+ value);
                				}
            				}else{
            					if(echos1.contains("int")){
                					varName = echos1.substring(0, echos1.length()).replaceAll(" ", "").replaceAll("int", "").replaceAll("\t", "");
                					outputStream.println("	"+ varName +" db ?");
                				}else{
                					outputStream.println("	"+ echos1.replaceAll(" ", "") +" db ?");
                				}
            				}
            			}
            		}else{
            			if(token1.contains("=")){
            				varName = token1.substring(0, token1.indexOf("=")).replaceAll(" ", "").replaceAll("int", "").replaceAll("\t", "");
            				value = token1.substring(token1.indexOf("=")+1, token1.indexOf(";")).replaceAll(" ", "");
            				if((value.contains("+")) || (value.contains("-"))){
            					problemsName.add(varName);
            					problemsValue.add(value);
            					outputStream.println("	"+ varName +" db ?");
            				}else{
            					outputStream.println("	"+ varName +" db "+ value);
            				}
            			}else{
            				varName = token1.substring(0, token1.indexOf(";")).replaceAll(" ", "").replaceAll("int", "").replaceAll("\t", "");
            				outputStream.println("	"+ varName +" db ?");
            			}
            		}
            	}else if((token1.contains("float")) && (openCurly[0] == 1) && (closeCurly[0] != 1)){
            		String lol = token1.replaceAll(" ", "").replaceAll("\t", "");
            		if(token1.contains("=")){
            			if(lol.contains("[")){
            				if(!lol.substring(lol.indexOf("{")+1, lol.indexOf("}")).contains(",")){
            					outputStream.println("	"+ lol.substring(0, lol.indexOf("[")).replaceAll("float", "").replaceAll(" ", "") +" db "+ lol.substring(lol.indexOf("[")+1, lol.indexOf("]")) +" dup ("+ lol.substring(lol.indexOf("{")+1, lol.indexOf("}")) +")");
            				}else{
            					outputStream.println("	"+ lol.substring(0, lol.indexOf("=", 5)).replaceAll("float", "").replaceAll(" ", "") +" db "+ lol.substring(lol.indexOf("{")+1, lol.indexOf("}")));
            				}
            			}else{
            				outputStream.println("	"+ lol.substring(0, lol.indexOf("=")).replaceAll("float", "").replaceAll(" ", "") +" db "+ lol.substring(lol.indexOf("=")+1, lol.indexOf(";")));
            			}
            		}else {
            			if(lol.contains("[")){
            				outputStream.println("	"+ lol.substring(0, lol.indexOf("[")).replaceAll("float", "").replaceAll(" ", "") +" db "+ lol.substring(lol.indexOf("[")+1, lol.indexOf("]")) +" dup (?)");
            			}else{
            				outputStream.println("	"+ lol.substring(0, lol.indexOf(";")).replaceAll("float", "").replaceAll(" ", "") +" db ?");
            			}
            			System.out.println(lol);
            		}
            	}else if((token1.contains("string")) && (openCurly[0] == 1) && (closeCurly[0] != 1)){
            		if(token1.contains("=")){
            			System.out.println(token1);
            			outputStream.println("	"+ token1.substring(0, token1.indexOf("=")).replaceAll("string", "").replaceAll(" ", "") +" db \""+ token1.substring(token1.indexOf("=")+1, token1.indexOf(";")) +"\", '$'");
            		}else {
            			if(token1.contains("[")){
            				outputStream.println("	"+ token1.substring(0, token1.indexOf("[")).replaceAll("string", "").replaceAll(" ", "").replaceAll("\t", "") +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            			}else {
            				outputStream.println("	"+ token1.substring(0, token1.indexOf(";")).replaceAll("string", "").replaceAll(" ", "").replaceAll("\t", "") +" db ?");
            			}
            			System.out.println(token1);
            		}	
            	}else if(token1.contains("cout")){
               		int num = 0;
               		for(int i = 0;i < 10;i++){
               			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
               				num = openCurly[i];
               			}
               		}
               		int sym = 0;
            		ArrayList<Integer> place = new ArrayList<Integer>();
            		int i = 0;
            		while(i < token1.length()){
            			if(token1.charAt(i) == '"'){
            				sym++;
            				place.add(i);
            			}else if(token1.charAt(i) == ';'){
            				place.add(i);
            			}
            			i++;
            		}
            		if((token1.contains("\"")) && (sym <= 2)){
            			array[messCount] = num;
            			outputStream.println("	message"+ messCount +" db "+ token1.substring(token1.indexOf("\""), token1.lastIndexOf("\"")+1) +",'$'");
            			messCount++;
            		}else if(sym > 2){
            			int j = 0;
            			while(j < sym){
            				String sub = token1.substring(place.get(j)+1, place.get(j+1));
            				System.out.println(place.get(j));
            				System.out.println(place.get(j+1));
            				System.out.println(sub);
            				if(sub.contains("\"")){
            					array[messCount] = num;
            					outputStream.println("	message"+ messCount +" db \""+ sub +"\",'$'");
            					messCount++;
            				}else if(!sub.equals("")){
            					array[messCount] = num;
            					outputStream.println("	message"+ messCount +" db \""+ sub +"\",'$'");
            					messCount++;
            				}
            				j+=2;
            			}
            		}
            	}
            }
            
            for(int i = 0;i < 10;i++){
            	openCurly[i] = 0;
            	closeCurly[i] = 0;
            }
            
            outputStream.println(".stack 100h");
            outputStream.println(".code");
            outputStream.println();
            outputStream.println("writeDec proc");
            outputStream.println("mov bx,4");
            outputStream.println("loop1:");
            outputStream.println("	div ten");
            outputStream.println("	mov decima[bx],ah");
            outputStream.println("	mov ah,0");
            outputStream.println("	add decima[bx],48");
            outputStream.println("	dec bx");
            outputStream.println("	cmp bx,0");
            outputStream.println("jne loop1");
            outputStream.println("ret");
            outputStream.println("writeDec endp");
            outputStream.println();
            outputStream.println("	main proc");
            outputStream.println();
            outputStream.println("	mov ax,@data");				
            outputStream.println("	mov ds,ax");
            outputStream.println();
            
            if(!problemsName.isEmpty()){
            	for(int i = 0;i < problemsName.size();i++){
            		String get = problemsValue.get(i);
            		String name = problemsName.get(i);
            		String one = null;					// first operator
            		String two = null;					// second operator
            		if(get.contains("+")){
            			one = get.substring(0, get.indexOf("+")).replaceAll("  ", "");
            			two = get.substring(get.indexOf("+")+1, get.length()).replaceAll("  ", "");
            			if((isNumber(one)) || (isNumber(two))){
            				outputStream.println("	mov al, "+ two);
            				outputStream.println("	add al, "+ one);
            				outputStream.println("	mov "+ name +", al");
            			}else if((!isNumber(one)) && (!isNumber(two))){
            				outputStream.println("	mov al, "+ one);
            				outputStream.println("	mov bl, "+ two);
            				outputStream.println("	add al, bl");
            				outputStream.println("	mov "+ name +", al");
            			}
            		}else if(get.contains("-")){
            			one = get.substring(0, get.indexOf("-")).replaceAll("  ", "");
            			two = get.substring(get.indexOf("-"), get.length()).replaceAll("  ", "");
            			if((isNumber(one)) || (isNumber(two))){
            				outputStream.println("	mov al, "+ one);
            				outputStream.println("	sub al, "+ two);
            				outputStream.println("	mov "+ name +", al");
            			}else if((!isNumber(one)) && (!isNumber(two))){
            				outputStream.println("	mov al, "+ one);
            				outputStream.println("	mov bl, "+ two);
            				outputStream.println("	sub al, bl");
            				outputStream.println("	mov "+ name +", al");
            			}
            		}
            		
            	}
            }
            
            counter = 0;
            messCount = 0;
            StringTokenizer token2 = new StringTokenizer(b.toString(), "\n");
            String condition = null;
            String forCondition = null;
            String ment = null;								// increment or decrement lol
            int whileCounter = 0;
            int forCounter = 0;
            int ifCounter = 0;
            int doCounter = 0;
            while(token2.hasMoreTokens()){
            	String token3 = token2.nextToken().replaceAll(" ", "");
            	if(token3.contains("main(")){
            		openCurly[0] = 1;
            		counter++;
            	}else if(token3.contains("if(") || token3.contains("if (")){
            		String lol = token2.nextToken();
            		if(lol.contains("{")){
            			openCurly[counter] = 2;
                		counter++;
            			ifCounter++;
            			String comp1 = null;
            			String comp2 = null;
            			if(token3.contains("==")){
            				comp1 = token3.substring(token3.indexOf("(")+1, token3.indexOf("=")).replaceAll(" ", "");
            				comp2 = token3.substring(token3.lastIndexOf("=")+1, token3.indexOf(")")).replaceAll(" ", "");
            				outputStream.println("	cmp "+ comp1 +", "+ comp2);
                			outputStream.println("	jne label"+ ifCounter);
            			}else if(token3.contains("!=")){
            				comp1 = token3.substring(token3.indexOf("(")+1, token3.indexOf("!")).replaceAll(" ", "");
            				comp2 = token3.substring(token3.indexOf("=")+1, token3.indexOf(")")).replaceAll(" ", "");
            				outputStream.println("	cmp "+ comp1 +", "+ comp2);
                			outputStream.println("	je label"+ ifCounter);
            			}else if(token3.contains("<=")){
            				comp1 = token3.substring(token3.indexOf("(")+1, token3.indexOf("<")).replaceAll(" ", "");
            				comp2 = token3.substring(token3.indexOf("=")+1, token3.indexOf(")")).replaceAll(" ", "");
            				outputStream.println("	cmp "+ comp1 +", "+ comp2);
                			outputStream.println("	jg label"+ ifCounter);
            			}else if(token3.contains(">=")){
            				comp1 = token3.substring(token3.indexOf("(")+1, token3.indexOf(">")).replaceAll(" ", "");
            				comp2 = token3.substring(token3.indexOf("=")+1, token3.indexOf(")")).replaceAll(" ", "");
            				outputStream.println("	cmp "+ comp1 +", "+ comp2);
                			outputStream.println("	jl label"+ ifCounter);
            			}else if(token3.contains("<")){
            				comp1 = token3.substring(token3.indexOf("(")+1, token3.indexOf("<")).replaceAll(" ", "");
            				comp2 = token3.substring(token3.indexOf("<")+1, token3.indexOf(")")).replaceAll(" ", "");
            				outputStream.println("	cmp "+ comp1 +", "+ comp2);
                			outputStream.println("	jge label"+ ifCounter);
            			}else if(token3.contains(">")){
            				comp1 = token3.substring(token3.indexOf("(")+1, token3.indexOf(">")).replaceAll(" ", "");
            				comp2 = token3.substring(token3.indexOf(">")+1, token3.indexOf(")")).replaceAll(" ", "");
            				outputStream.println("	cmp "+ comp1 +", "+ comp2);
                			outputStream.println("	jle label"+ ifCounter);
            			}
            		}else if(lol.contains("cout")){
                		int sym = 0;
                		ArrayList<Integer> place = new ArrayList<Integer>();
                		int i = 0;
                		while(i < lol.length()){
                			if(lol.charAt(i) == '<'){
                				sym++;
                				place.add(i);
                			}else if(lol.charAt(i) == ';'){
                				place.add(i);
                			}
                			i++;
                		}
                		if((lol.contains("\"")) && (sym <= 2)){
                			outputStream.println("		lea dx, message"+ messCount);
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                			messCount++;
                		}else if((!lol.contains("\"")) && (sym <= 2)){
                			outputStream.println("		mov al, "+ lol.substring(lol.lastIndexOf("<")+1, lol.indexOf(";")).replaceAll(" ", ""));
        					outputStream.println("		call writeDec");
        					outputStream.println("		lea dx, decima");
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                		}else if(sym > 2){
                			int j = 0;
                			while(j < sym){
                				String sub = lol.substring(place.get(j+1)+1, place.get(j+2));
                				System.out.println(place.get(j+1));
                				System.out.println(place.get(j+2));
                				System.out.println(sub);
                				if(sub.contains("\"")){
                					outputStream.println("		lea dx, message"+ messCount);
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                        			messCount++;
                				}else if(sub.contains("::")){
                					
                				}else if(!sub.equals("")){
                					outputStream.println("		mov al, "+ sub.replaceAll(" ", "").replaceAll(" ", ""));
                					outputStream.println("		call writeDec");
                					outputStream.println("		lea dx, decima");
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                				}
                				j+=2;
                			}
                		}
            		}
            	}else if((token3.contains("if(") || token3.contains("if (") || token3.contains("else")) && (!token3.contains("{"))){
            		token3 = token2.nextToken().replaceAll(" ", "");
            		if (token3.contains("cout")){
                		int sym = 0;
                		ArrayList<Integer> place = new ArrayList<Integer>();
                		int i = 0;
                		while(i < token3.length()){
                			if(token3.charAt(i) == '<'){
                				sym++;
                				place.add(i);
                			}else if(token3.charAt(i) == ';'){
                				place.add(i);
                			}
                			i++;
                		}
                		if((token3.contains("\"")) && (sym <= 2)){
                			outputStream.println("		lea dx, message"+ messCount);
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                			messCount++;
                		}else if((!token3.contains("\"")) && (sym <= 2)){
                			outputStream.println("		mov al, "+ token3.substring(token3.lastIndexOf("<")+1, token3.indexOf(";")).replaceAll(" ", ""));
        					outputStream.println("		call writeDec");
        					outputStream.println("		lea dx, decima");
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                		}else if(sym > 2){
                			int j = 0;
                			while(j < sym){
                				String sub = token3.substring(place.get(j+1)+1, place.get(j+2));
                				System.out.println(place.get(j+1));
                				System.out.println(place.get(j+2));
                				System.out.println(sub);
                				if(sub.contains("\"")){
                					outputStream.println("		lea dx, message"+ messCount);
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                        			messCount++;
                				}else if(sub.contains("::")){
                					
                				}else if(!sub.equals("")){
                					outputStream.println("		mov al, "+ sub.replaceAll(" ", "").replaceAll(" ", ""));
                					outputStream.println("		call writeDec");
                					outputStream.println("		lea dx, decima");
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                				}
                				j+=2;
                			}
                		}
            		}else if(token3.contains("=")){
            			String result;
            			if((token3.contains("int")) || (token3.contains("float")) || (token3.contains("char")) || (token3.contains("string")) || (token3.contains("if")) || (token3.contains("while")) || (token3.contains("for"))){
            				continue;
            			}else if(token3.contains("+=")){
            				result = token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", "");
            				String addend = token3.substring(token3.indexOf("=")+1, token3.indexOf(";")).replaceAll(" ", "");
            				if(isNumber(addend)){
            					outputStream.println("	add "+ result +", "+ addend);
            				}else{
            					outputStream.println("	mov al, "+ addend);
            					outputStream.println("	add "+ result +", al");
            				}
            				System.out.println("+=");
            			}else if(token3.contains("-=")){
            				result = token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", "");
            				String subtrahend = token3.substring(token3.indexOf("="), token3.length()-1).replaceAll(" ", "");
            				outputStream.println("	sub "+ result +", "+ subtrahend);
            			}else if(token3.contains("+")){
            				token3 = token3.replaceAll(" ", "");
            				result = token3.substring(0, token3.indexOf("=")).replaceAll(" ", "").replaceAll("\t", "");
            				String add1 = token3.substring(token3.indexOf("=")+1, token3.indexOf("+")).replaceAll(" ", "");
            				String add2 = token3.substring(token3.indexOf("+")+1, token3.indexOf(";")).replaceAll(" ", "");;
            				if(isNumber(add1)){
            					outputStream.println("	mov al, "+ add1 );
            					outputStream.println("	mov bl, "+ add2 );
            					outputStream.println("	add al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}else if((isNumber(add2)) && (result.equals(add1))){
            					outputStream.println("	add "+ add1 +", "+ add2);
            				}else if((!isNumber(add2)) && (result.equals(add1))){
            					outputStream.println("	mov al, "+ add2);
            					outputStream.println("	add "+ add1 +", al");
            				}else if((isNumber(add2)) && (!result.equals(add1))){
            					outputStream.println("	mov al, "+ add1);
            					outputStream.println("	add al, "+ add2);
            					outputStream.println("	mov "+ result +", al");
            				}else if((!isNumber(add2)) && (!result.equals(add1))){
            					outputStream.println("	mov al, "+ add1);
            					outputStream.println("	mov bl, "+ add2);
            					outputStream.println("	add al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}
            				
            			}else if(token3.contains("-")){
            				token3 = token3.replaceAll(" ", "");
            				result = token3.substring(0, token3.indexOf("=")).replaceAll(" ", "").replaceAll("\t", "");
            				result = result.replaceAll("\t", "");
            				String sub1 = token3.substring(token3.indexOf("=")+1, token3.indexOf("-")).replaceAll(" ", "");
            				String sub2 = token3.substring(token3.indexOf("-")+1, token3.indexOf(";")).replaceAll(" ", "");;
            				if(isNumber(sub1)){
            					outputStream.println("	mov al, "+ sub1 );
            					outputStream.println("	mov bl, "+ sub2 );
            					outputStream.println("	sub al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}else if((isNumber(sub2)) && (result.equals(sub1))){
            					outputStream.println("	sub "+ sub1 +", "+ sub2);
            				}else if((!isNumber(sub2)) && (result.equals(sub1))){
            					outputStream.println("	mov al, "+ sub2);
            					outputStream.println("	sub "+ sub1 +", al");
            				}else if((isNumber(sub2)) && (!result.equals(sub1))){
            					outputStream.println("	mov al, "+ sub1);
            					outputStream.println("	sub al, "+ sub2);
            					outputStream.println("	mov "+ result +", al");
            				}else if((!isNumber(sub2)) && (!result.equals(sub1))){
            					outputStream.println("	mov al, "+ sub1);
            					outputStream.println("	mov bl, "+ sub2);
            					outputStream.println("	sub al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}
            				
            			}else{
            				String lol = token3.replaceAll("\t", "");
            				lol = lol.replaceAll(" ", "");
            				outputStream.println("		mov "+ lol.substring(0, lol.indexOf("=")) +", "+ lol.substring(lol.indexOf("=")+1, lol.indexOf(";")));
            			}
            		}else if(token3.contains("++")){
            			outputStream.println("	inc "+ token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", ""));
            		}else if(token3.contains("--")){
            			outputStream.println("	dec "+ token3.substring(0, token3.indexOf("-")).replaceAll(" ", "").replaceAll("\t", ""));
            		}
            		outputStream.println("	jmp next"+ ifCounter);
        			outputStream.println("	label"+ ifCounter +":");
            	}else if((token3.contains("else{") || token3.contains("else {")) && (!token3.contains("if"))){
            		openCurly[counter] = 6;
        			counter++;
            	}else if((token3.contains("for(")) || (token3.contains("for ("))){
            		openCurly[counter] = 3;
            		counter++;
            		forCounter++;
            		String init = token3.substring(token3.indexOf("(")+1, token3.indexOf(";")).replaceAll(" ", "");
            		outputStream.println("	mov "+ init.substring(0, init.indexOf("=")) +", "+ init.substring(init.indexOf("=")+1, init.length()));
            		outputStream.println("	for"+ forCounter +":");
            		forCondition = token3.substring(token3.indexOf(";")+1, token3.lastIndexOf(";")).replaceAll(" ", "");
            		ment = token3.substring(token3.lastIndexOf(";")+1, token3.lastIndexOf(")"));
            	}else if(token3.contains("do")){
            		openCurly[counter] = 5;
            		counter++;
            		doCounter++;
            		outputStream.println("	do"+ doCounter +":");
            		outputStream.println();
            	}else if((token3.contains("while(")) || (token3.contains("while ("))){
            		openCurly[counter] = 4;
            		counter++;
            		whileCounter++;
            		outputStream.println("	while"+ whileCounter +":");
            		outputStream.println();
            		condition = token3.substring(token3.indexOf("(")+1, token3.indexOf(")")).replaceAll(" ", "");
            	}else if((token3.contains("else")) && (!token3.contains("if"))){
            		token3 = token2.nextToken().replaceAll(" ", "");
            		if(token3.contains("cout")){
            			int sym = 0;
                		ArrayList<Integer> place = new ArrayList<Integer>();
                		int i = 0;
                		while(i < token3.length()){
                			if(token3.charAt(i) == '<'){
                				sym++;
                				place.add(i);
                			}else if(token3.charAt(i) == ';'){
                				place.add(i);
                			}
                			i++;
                		}
                		if((token3.contains("\"")) && (sym <= 2)){
                			outputStream.println("		lea dx, message"+ messCount);
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                			messCount++;
                		}else if((!token3.contains("\"")) && (sym <= 2)){
                			outputStream.println("		mov al, "+ token3.substring(token3.lastIndexOf("<")+1, token3.indexOf(";")).replaceAll(" ", ""));
        					outputStream.println("		call writeDec");
        					outputStream.println("		lea dx, decima");
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                		}else if(sym > 2){
                			int j = 0;
                			while(j < sym){
                				String sub = token3.substring(place.get(j+1)+1, place.get(j+2));
                				System.out.println(place.get(j+1));
                				System.out.println(place.get(j+2));
                				System.out.println(sub);
                				if(sub.contains("\"")){
                					outputStream.println("		lea dx, message"+ messCount);
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                        			messCount++;
                				}else if(sub.contains("::")){
                					
                				}else if(!sub.equals("")){
                					outputStream.println("		mov al, "+ sub.replaceAll(" ", "").replaceAll(" ", ""));
                					outputStream.println("		call writeDec");
                					outputStream.println("		lea dx, decima");
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                				}
                				j+=2;
                			}
                		}
            		}
            	}else if((openCurly[0] != 0) && (closeCurly[0] == 0)){
            		if(token3.contains("}")){
                		int count = 0;
                		int num = 0;
                		for(int i = 0;i < 10;i++){
                			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
                				count = i;
                				num = openCurly[i];
                			}
                		}
                		closeCurly[count] = num;
                		if(num == 5){
                			if(token3.contains("while(")){
                				if(token3.contains("!=")){
                					outputStream.println("		cmp "+ token3.substring(token3.indexOf("(")+1, token3.indexOf("!")).replaceAll(" ", "") +", "+ token3.substring(token3.indexOf("=")+1, token3.lastIndexOf(")")).replaceAll(" ", ""));
                					outputStream.println("	jne do"+ doCounter);
                				}else if(token3.contains("==")){
                					outputStream.println("		cmp "+ token3.substring(token3.indexOf("(")+1, token3.indexOf("!")).replaceAll(" ", "") +", "+ token3.substring(token3.indexOf("=")+1, token3.lastIndexOf(")")).replaceAll(" ", ""));
                					outputStream.println("	je do"+ doCounter);
                				}
                			}else{
                				token3 = token2.nextToken();
                				if(token3.contains("!=")){
                					outputStream.println("		cmp "+ token3.substring(token3.indexOf("(")+1, token3.indexOf("!")).replaceAll(" ", "") +", "+ token3.substring(token3.indexOf("=")+1, token3.lastIndexOf(")")).replaceAll(" ", ""));
                					outputStream.println("	jne do"+ doCounter);
                				}else if(token3.contains("==")){
                					outputStream.println("		cmp "+ token3.substring(token3.indexOf("(")+1, token3.indexOf("!")).replaceAll(" ", "") +", "+ token3.substring(token3.indexOf("=")+1, token3.lastIndexOf(")")).replaceAll(" ", ""));
                					outputStream.println("	je do"+ doCounter);
                				}
                			}
                		}else if(num == 4){
                			if(condition.contains("!=")){
                				outputStream.println("		cmp "+ condition.substring(0, condition.indexOf("!")) +", "+ condition.substring(condition.indexOf("=")+1, condition.length()));
                				outputStream.println("	jne while"+ whileCounter);
                				outputStream.println();
                			}else if(condition.contains("==")){
                				outputStream.println("		cmp "+ condition.substring(0, condition.indexOf("=")) +", "+ condition.substring(condition.indexOf("=")+1, condition.length()));
                				outputStream.println("	je while"+ whileCounter);
                				outputStream.println();
                			}
                		}else if(num == 2){
                			outputStream.println("	jmp next"+ ifCounter);
                			outputStream.println("	label"+ ifCounter +":");
                		}else if(num == 6){
                			outputStream.println("	next"+ ifCounter +":");
                		}else if(num == 3){
                			if(ment.contains("+")){
                				outputStream.println("		inc "+ ment.substring(0, ment.indexOf("+")).replaceAll(" ", ""));
                			}else if(ment.contains("-")){
                				outputStream.println("		dec "+ ment.substring(0, ment.indexOf("-")).replaceAll(" ", ""));
                			}
                			if(forCondition.contains("<=")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf("<")) +", "+ forCondition.substring(forCondition.indexOf("=")+1, forCondition.length()));
                				outputStream.println("	jle for"+ forCounter);
                				outputStream.println();
                			}else if(forCondition.contains(">=")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf(">")) +", "+ forCondition.substring(forCondition.indexOf("=")+1, forCondition.length()));
                				outputStream.println("	jge for"+ forCounter);
                				outputStream.println();
                			}else if(forCondition.contains("<")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf("<")) +", "+ forCondition.substring(forCondition.indexOf("<")+1, forCondition.length()));
                				outputStream.println("	jl for"+ forCounter);
                				outputStream.println();
                			}else if(forCondition.contains(">")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf(">")) +", "+ forCondition.substring(forCondition.indexOf(">")+1, forCondition.length()));
                				outputStream.println("	jle for"+ forCounter);
                				outputStream.println();
                			}
                		}else if(num == 1){
                			continue;
                		}
                	}else if (token3.contains("cout")){
                		int sym = 0;
                		ArrayList<Integer> place = new ArrayList<Integer>();
                		int i = 0;
                		while(i < token3.length()){
                			if(token3.charAt(i) == '<'){
                				sym++;
                				place.add(i);
                			}else if(token3.charAt(i) == ';'){
                				place.add(i);
                			}
                			i++;
                		}
                		if((token3.contains("\"")) && (sym <= 2)){
                			outputStream.println("		lea dx, message"+ messCount);
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                			messCount++;
                		}else if((!token3.contains("\"")) && (sym <= 2)){
                			outputStream.println("		mov al, "+ token3.substring(token3.lastIndexOf("<")+1, token3.indexOf(";")).replaceAll(" ", ""));
        					outputStream.println("		call writeDec");
        					outputStream.println("		lea dx, decima");
                			outputStream.println("		mov ah, 09h");
                			outputStream.println("		int 21h");
                			outputStream.println();
                		}else if(sym > 2){
                			int j = 0;
                			while(j < sym){
                				String sub = token3.substring(place.get(j+1)+1, place.get(j+2));
                				System.out.println(place.get(j+1));
                				System.out.println(place.get(j+2));
                				System.out.println(sub);
                				if(sub.contains("\"")){
                					outputStream.println("		lea dx, message"+ messCount);
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                        			messCount++;
                				}else if(sub.contains("::")){
                					
                				}else if(!sub.equals("")){
                					outputStream.println("		mov al, "+ sub.replaceAll(" ", "").replaceAll(" ", ""));
                					outputStream.println("		call writeDec");
                					outputStream.println("		lea dx, decima");
                        			outputStream.println("		mov ah, 09h");
                        			outputStream.println("		int 21h");
                        			outputStream.println();
                				}
                				j+=2;
                			}
                		}
            		}else if(token3.contains("=")){
            			String result;
            			if((token3.contains("int")) || (token3.contains("float")) || (token3.contains("char")) || (token3.contains("string")) || (token3.contains("if")) || (token3.contains("while")) || (token3.contains("for"))){
            				continue;
            			}else if(token3.contains("+=")){
            				result = token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", "");
            				String addend = token3.substring(token3.indexOf("=")+1, token3.indexOf(";")).replaceAll(" ", "");
            				if(isNumber(addend)){
            					outputStream.println("	add "+ result +", "+ addend);
            				}else{
            					outputStream.println("	mov al, "+ addend);
            					outputStream.println("	add "+ result +", al");
            				}
            				System.out.println("+=");
            			}else if(token3.contains("-=")){
            				result = token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", "");
            				String subtrahend = token3.substring(token3.indexOf("="), token3.length()-1).replaceAll(" ", "");
            				outputStream.println("	sub "+ result +", "+ subtrahend);
            			}else if(token3.contains("+")){
            				token3 = token3.replaceAll(" ", "");
            				result = token3.substring(0, token3.indexOf("=")).replaceAll(" ", "").replaceAll("\t", "");
            				String add1 = token3.substring(token3.indexOf("=")+1, token3.indexOf("+")).replaceAll(" ", "");
            				String add2 = token3.substring(token3.indexOf("+")+1, token3.indexOf(";")).replaceAll(" ", "");;
            				if(isNumber(add1)){
            					outputStream.println("	mov al, "+ add1 );
            					outputStream.println("	mov bl, "+ add2 );
            					outputStream.println("	add al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}else if((isNumber(add2)) && (result.equals(add1))){
            					outputStream.println("	add "+ add1 +", "+ add2);
            				}else if((!isNumber(add2)) && (result.equals(add1))){
            					outputStream.println("	mov al, "+ add2);
            					outputStream.println("	add "+ add1 +", al");
            				}else if((isNumber(add2)) && (!result.equals(add1))){
            					outputStream.println("	mov al, "+ add1);
            					outputStream.println("	add al, "+ add2);
            					outputStream.println("	mov "+ result +", al");
            				}else if((!isNumber(add2)) && (!result.equals(add1))){
            					outputStream.println("	mov al, "+ add1);
            					outputStream.println("	mov bl, "+ add2);
            					outputStream.println("	add al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}
            				
            			}else if(token3.contains("-")){
            				token3 = token3.replaceAll(" ", "");
            				result = token3.substring(0, token3.indexOf("=")).replaceAll(" ", "").replaceAll("\t", "");
            				result = result.replaceAll("\t", "");
            				String sub1 = token3.substring(token3.indexOf("=")+1, token3.indexOf("-")).replaceAll(" ", "");
            				String sub2 = token3.substring(token3.indexOf("-")+1, token3.indexOf(";")).replaceAll(" ", "");;
            				if(isNumber(sub1)){
            					outputStream.println("	mov al, "+ sub1 );
            					outputStream.println("	mov bl, "+ sub2 );
            					outputStream.println("	sub al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}else if((isNumber(sub2)) && (result.equals(sub1))){
            					outputStream.println("	sub "+ sub1 +", "+ sub2);
            				}else if((!isNumber(sub2)) && (result.equals(sub1))){
            					outputStream.println("	mov al, "+ sub2);
            					outputStream.println("	sub "+ sub1 +", al");
            				}else if((isNumber(sub2)) && (!result.equals(sub1))){
            					outputStream.println("	mov al, "+ sub1);
            					outputStream.println("	sub al, "+ sub2);
            					outputStream.println("	mov "+ result +", al");
            				}else if((!isNumber(sub2)) && (!result.equals(sub1))){
            					outputStream.println("	mov al, "+ sub1);
            					outputStream.println("	mov bl, "+ sub2);
            					outputStream.println("	sub al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}
            				
            			}else{
            				String lol = token3.replaceAll("\t", "");
            				lol = lol.replaceAll(" ", "");
            				outputStream.println("		mov "+ lol.substring(0, lol.indexOf("=")) +", "+ lol.substring(lol.indexOf("=")+1, lol.indexOf(";")));
            			}
            		}else if(token3.contains("cin")){
            			outputStream.println("		mov ah, 01h");
            			outputStream.println("		int 21h");
            			outputStream.println("		mov "+ token3.substring(token3.lastIndexOf(">")+1, token3.indexOf(";")) +", al");
            			outputStream.println();
            		}else if(token3.contains("++")){
            			outputStream.println("	inc "+ token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", ""));
            		}else if(token3.contains("--")){
            			outputStream.println("	dec "+ token3.substring(0, token3.indexOf("-")).replaceAll(" ", "").replaceAll("\t", ""));
            		}
            	}
            }
            
            outputStream.println("	mov ax,4c00h");
            outputStream.println("	int 21h");
            outputStream.println();
            outputStream.println("	main endp");
            outputStream.println("	end main");

            outputStream.close();
            
            System.out.println(forCondition);
            System.out.println(condition);
            System.out.println(ment);
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "The file "+ fileName +" could not be found or could not be opened.");
        }
        catch (IOException e) {
        	JOptionPane.showMessageDialog(null,"Error reading from "+ fileName);
        }
        catch (Exception e){
        	JOptionPane.showMessageDialog(null, "The program detected a/an error/s in your code.");
        	e.printStackTrace();
        }
    }
}
