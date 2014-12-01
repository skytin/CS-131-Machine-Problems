title Dutdut
.model small
.data
scoreArr db 0, 0, 0, 0
delaytime db 2
scoreMess db "Score: $"
hscoreMess db "High Score: $"
score db 0
aMess db "New Game $"
bMess db "High Scores $"
cMess db "Instructions $"
dMess db "Credits $"
eMess db "Quit $"
tMess db "Thank you for playing! :) $"
creators db "AWESOME CREATORS!!$"
bella db "Ma. Isabella Dominique N. Inosantos$"
stud1 db "2012-23607$"
color db "Favorite Color: $"
hobbies db "Hobbies: $"
tin db "Christine Joyce O. Arzadon$"
stud2 db "2012-XXXXX$"
bCol db "Red ;)$"
tCol db "Blue $"
bHob db "GP LOOOOOL$"
tHob db "Subarashi desu. *wink wink*$"
wCol db 13
wRow db 17
choiceCol db 13, 12, 12, 14, 15
choiceRow db 17, 18, 19, 20, 21
col db ?
row db ?
starCol db 9,8,7,28,29,30
starRow db 13,14,13,15,14,15
backMess db "[Back]$"

.stack 100h
.code

clr proc
		mov ax, 0600h
		mov bh, 07h
		xor cx, cx
		mov dx, 184fh
		int 10h
		ret
clr endp
sidebar proc

	mov dl, 26
	mov dh, 1
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov dx, offset scoreMess
	mov ah, 09h
	int 21h
	
	mov si, 0
	scoreLoop:
		mov dl, scoreArr[si]
		add dl, 30h
		mov ah, 02h
		int 21h
		inc si
		cmp si, 4
	jl scoreLoop
	
	mov dl, 26
	mov dh, 2
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov dl, 3
	mov ah, 02h
	int 21h
	mov dl, ':'
	mov ah, 02h
	int 21h
	
	mov dl, 26
	mov dh, 20
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov dx, offset hscoreMess
	mov ah, 09h
	int 21h
ret
sidebar endp

welcome proc
	call clr
	mov dl, 15
	mov dh, 17
	xor bh, bh
	mov ah, 02h
	int 10h
	mov dx, offset aMess
	mov ah, 09h
	int 21h
	mov dl, 14
	mov dh, 18
	xor bh, bh
	mov ah, 02h
	int 10h
	mov dx, offset bMess
	mov ah, 09h
	int 21h
	mov dl, 14
	mov dh, 19
	xor bh, bh
	mov ah, 02h
	int 10h
	mov dx, offset cMess
	mov ah, 09h
	int 21h
	mov dl, 16
	mov dh, 20
	xor bh, bh
	mov ah, 02h
	int 10h
	mov dx, offset dMess
	mov ah, 09h
	int 21h
	mov dl, 17
	mov dh, 21
	xor bh, bh
	mov ah, 02h
	int 10h
	mov dx, offset eMess
	mov ah, 09h
	int 21h
	
ret
welcome endp

welScan proc
	mov si, 0
	mov wCol, 13
	mov wRow, 17
	mov dl, wCol
	mov dh, wRow
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov al, 16
	mov bl, 0Ch
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
	
	wScan:
		mov ah, 01h
		int 21h
		
		cmp al, 72 
		je wUp
		
		cmp al, 80
		je wDown

		cmp al, 0dh
		je x
	jmp wScan
x:	
ret

wUp:
	call clr
	call welcome

	cmp wRow, 17
	je wU
	
	dec si
	mov dl, choiceCol[si]
	mov wCol, dl
	mov dh, choiceRow[si]
	mov wRow, dh
	xor bh, bh
	mov ah, 02h
	int 10h
	mov al, 16
	mov bl, 0Ch
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
	jmp wScan
	
wDown:
	call clr
	call welcome
	cmp wRow, 21
	je wD
	
	inc si
	mov dl, choiceCol[si]
	mov wCol, dl
	mov dh, choiceRow[si]
	mov wRow, dh
	xor bh, bh
	mov ah, 02h
	int 10h
	mov al, 16
	mov bl, 0Ch
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
	jmp wScan
	
	wU:
		inc si
		mov dl, choiceCol[si]
		mov wCol, dl
		mov dh, choiceRow[si]
		mov wRow, dh
		xor bh, bh
		mov ah, 02h
		int 10h
		jmp wUp
	
	wD:
		dec si
		mov dl, choiceCol[si]
		mov wCol, dl
		mov dh, choiceRow[si]
		mov wRow, dh
		xor bh, bh
		mov ah, 02h
		int 10h
		jmp wDown
		
 welScan endp
 
compareChoice proc
	cmp wRow, 17
	je a
	cmp wRow, 18
	je b
	cmp wRow, 19
	je f
	cmp wRow, 20
	je d
	cmp wRow, 21
	je e
x:
call gameProper
ret
	a:
		call newGame
		jmp x
	b:
		call highScores
		jmp x
	f:
		call inst
		jmp x
	d:
		call creds
		jmp x
	e:
		call exitGame

compareChoice endp

creds proc
	
	call back
	
	mov dl, 10
	mov dh, 14
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, creators
	mov ah, 09h
	int 21h
		
	mov si, 0
	starLoop:
		mov dl, starCol[si]
		mov dh, starRow[si]
		xor bh, bh
		mov ah, 02h
		int 10h
		
		mov al, '*'
		mov bl, 0Eh
		mov bh, 0
		xor cx, cx
		mov cx, 1
		mov ah, 09h
		int 10h
	
	inc si
	cmp si, 6
	jne starLoop
	
	mov dl, 2
	mov dh, 16
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov al, 16
	mov bl, 0Ch
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
	
	mov dl, 3
	mov dh, 16
	xor bh, bh
	mov ah, 02h
	int 10h

	lea dx, bella
	mov ah, 09h
	int 21h
	
	mov dl, 10
	mov dh, 17
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, stud1
	mov ah, 09h
	int 21h
	
	mov dl, 10
	mov dh, 18
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, color
	mov ah, 09h
	int 21h
		
	lea dx, bCol
	mov ah, 09h
	int 21h	
	
	mov dl, 10
	mov dh, 19
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, hobbies
	mov ah, 09h
	int 21h
	
	lea dx, bHob
	mov ah, 09h
	int 21h

	mov dl, 7
	mov dh, 20
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov al, 16
	mov bl, 0Ch
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
	
	mov dl, 8
	mov dh, 20
	xor bh, bh
	mov ah, 02h
	int 10h

	lea dx, tin
	mov ah, 09h
	int 21h
	
	mov dl, 10
	mov dh, 21
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, stud2
	mov ah, 09h
	int 21h
	
	mov dl, 10
	mov dh, 22
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, color
	mov ah, 09h
	int 21h
		
	lea dx, tCol
	mov ah, 09h
	int 21h	
	
	mov dl, 2
	mov dh, 23
	xor bh, bh
	mov ah, 02h
	int 10h
	
	lea dx, hobbies
	mov ah, 09h
	int 21h
	
	lea dx, tHob
	mov ah, 09h
	int 21h
	
	scan:
		 mov ah, 01h
		 int 21h	
		 cmp al, 8
		 je x	
		jmp scan
x:
ret
creds endp

inst proc
	call back
	
	scan:
		 mov ah, 01h
		 int 21h	
		 cmp al, 8
		 je x	
		jmp scan
x:
ret
inst endp

highScores proc
	call back
	
	
	scan:
		 mov ah, 01h
		 int 21h	
		 cmp al, 8
		 je x	
		jmp scan
x:
ret
highScores endp

newGame proc
	call back
	call sidebar
	mov dl, '1'
	mov ah, 02h
	int 21h
	
	scan:
		 mov ah, 01h
		 int 21h	
		 cmp al, 8
		 je x	
		jmp scan
x:
ret
newGame endp

gameProper proc
	call welcome
	call welScan
	call clr
	call compareChoice
ret
gameProper endp

exitGame proc
	call clr
	mov dl, 10
	mov dh, 16
	xor bh, bh
	mov ah, 02h
	int 10h
	lea dx, tMess
	mov ah, 09h
	int 21h
	mov ax, 4c00h
	int 21h
exitGame endp

back proc
	mov dl, 31
	mov dh, 24
	xor bh, bh
	mov ah, 02h
	int 10h
	
	mov al, 27
	mov bl, 0Ch
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
	
	mov dl, 33
	mov dh, 24
	xor bh, bh
	mov ah, 02h
	int 10h
	lea dx, backMess
	mov ah, 09h
	int 21h
ret
back endp

    main    proc
		
		mov ax, @data
		mov ds, ax
		
		mov al, 01h
		mov ah, 00h
		int 10h
		mov cx, 3200h
		mov ah, 01h
		int 10h
		call gameProper
		
	main    endp
    end     main
