                    title Pacman Game
.model small
.data
col db 20
row db 20
wrow db 0
wcol db 0
w1Cola db 11,11,11,12,12,12,13,13,13,11,12,13,4,4,4,5,5,5,7,7,7,8,8,8,9,9,9,15,15,15,16,16,16,17,17,17,19,19,19,20,20,20,3,3,3,3,3
w1Rowa db 3,4,5,3,4,5,3,4,5,6,6,6,4,5,6,4,5,6,4,5,6,4,5,6,4,5,6,4,5,6,4,5,6,4,5,6,4,5,6,4,5,6,11,12,13,14
w1Colb db 21,21,21,21,7,8,9,10,11,7,8,9,10,11,7,8,9,10,11,13,14,15,16,17,13,14,15,16,17,13,14,15,16,17,3,4,20,21,11,12,13,11,12,13
w1Rowb db 11,12,13,14,8,8,8,8,8,9,9,9,9,9,10,10,10,10,10,8,8,8,8,8,9,9,9,9,9,10,10,10,10,10,16,16,16,16,20,20,20,21,21,21
w1Colc db 4,5,4,5,19,20,19,20,5,5,5,5,5,19,19,19,19,19,6,6,6,18,18,18,8,9,10,11,12,13,14,15,16,8,9,10,11,12,13,14,15,16,8,9,10,11,12,13,14,15,16
w1Rowc db 8,8,9,9,8,8,9,9,10,11,12,13,14,10,11,12,13,14,12,13,14,12,13,14,12,12,12,12,12,12,12,12,12,13,13,13,13,13,13,13,13,13,14,14,14,14,14,14,14,14,14
scoreArr db 0, 0, 0, 0
;scoreArr db 4 dup(?)
scoreMess db "Score: $"
hscoreMess db "High Score: $"
lifeMess db "<3: $"
score db 0
.stack 100h
.code
wall1 proc
	mov si, 0
	wall1loopA:
		mov dl, w1Cola[si]
		mov dh, w1Rowa[si]
		call printwall
		inc si
		cmp si,46
	jl wall1loopA
	mov si, 0
	wall1loopB:
		mov dl, w1Colb[si]
		mov dh, w1Rowb[si]
		call printwall
		inc si
		cmp si,44
	jl wall1loopB
	mov si, 0
	wall1loopC:
		mov dl, w1Colc[si]
		mov dh, w1Rowc[si]
		call printwall
		inc si
		cmp si,45
	jl wall1loopC
	ret
wall1 endp

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
	
	mov dx, offset lifeMess	
	mov ah, 09h
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

printwall proc
	xor bh, bh
	mov ah, 02h
	int 10h
	mov ah, 0Ah
	mov al, 178
	mov bl, 09h
	mov bh, 0
	xor cx, cx
	mov cx, 1
	mov ah, 09h
	int 10h
ret
printwall endp

clr proc
		mov ax, 0600h
		mov bh, 07h
		xor cx, cx
		mov dx, 184fh
		int 10h
		ret
clr endp
outline proc
	mov si, 0
	outlloop1:
		mov dl, 0
		mov dh, wrow
		call printwall
		mov dl, 1
		mov dh, wrow
		call printwall
		mov dl, 2
		mov dh, wrow
		call printwall
		
		mov dl, 24
		mov dh, wrow
		call printwall
		mov dl, 23
		mov dh, wrow
		call printwall
		mov dl, 22
		mov dh, wrow
		call printwall
		
		inc wrow
		inc si
		cmp si, 24
	jl outlloop1
	
	mov si, 0
	outlloop2:
		mov dl, wcol
		mov dh, 0
		call printwall
		mov dl, wcol
		mov dh, 1
		call printwall
		mov dl, wcol
		mov dh, 2
		call printwall
		
		mov dl, wcol
		mov dh, 24
		call printwall
		mov dl, wcol
		mov dh, 23
		call printwall
		mov dl, wcol
		mov dh, 22
		call printwall
		
		inc wcol
		inc si
		cmp si, 25
	jl outlloop2
	mov wrow, 0
	mov wcol, 0
	ret
outline endp


scored proc
	inc score
	mov si, 3
	cmp scoreArr[si], 9
	jge inc1
	
	inc scoreArr[si]
	jmp ex

inc1:
	mov si, 2
	cmp scoreArr[si], 9
	jge inc2
	
	inc scoreArr[si]
	mov si, 3
	mov scoreArr[si], 0
	jmp ex

inc2:
	mov si, 1
	cmp scoreArr[si], 9
	jge inc3
	
	inc scoreArr[si]
	mov si, 2
	mov scoreArr[si], 0
	jmp ex
	
	inc scoreArr[si]
	mov si, 2
	mov scoreArr[si], 0
	mov si, 3
	mov scoreArr[si], 0
	jmp ex

inc3:
	mov si, 0
	inc scoreArr[si]
	mov si, 1
	mov scoreArr[si], 0
	mov si, 2
	mov scoreArr[si], 0
	mov si, 3
	mov scoreArr[si], 0	
ex:
	
ret
scored endp

    main proc
		
		mov ax, @data
		mov ds, ax
		
		mov al, 01h
		mov ah, 00h
		int 10h
		
		mov cx, 3200h
		mov ah, 01h
		int 10h
		call sidebar
		call outline
		call wall1
		
		
		
	scan:
						
			mov ah, 01h
			int 21h
			
			cmp al, 1Bh
			je exit	
			
			cmp al, 75 
			je left
			cmp al, 'a'
			je left
			
			cmp al, 77
			je right
			cmp al, 'd'
			je right
			
			cmp al, 72 
			je up
			cmp al, 'w'
			je up
			
			cmp al, 80
			je down				
			cmp al, 's'
			je down				
			
		jmp scan
					
		exit:
			call clr
			mov ax, 4c00h
			int 21h	

		left:
			call clr
			call sidebar
			call outline
			call wall1
			
			cmp col, 3
			je l
			
			mov bp, 0
			loop1:
				mov dh, w1Cola[bp]
				inc dh
				cmp col, dh
				je blockLeft
				inc bp
				cmp bp, 9
			jne loop1
	
		cont1:
			dec col		
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, '>'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
		
		right:
			call clr
			call sidebar
			call outline
			call wall1
			
			cmp col, 21
			je r
			
			mov bp, 0
			loop2b:
				mov dh, w1Cola[bp]
				dec dh
				cmp col, dh
				je blockRight
				inc bp
				cmp bp, 9
			jne loop2b

		cont2:
			inc col
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, '<'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
		
		up:
			call clr
			call sidebar
			call outline			
			call wall1
				
			cmp row, 3
			je u
			
		loop3b:
				mov dh, w1Rowa[bp]
				inc dh
				cmp row, dh
				je blockUp
				inc bp
				cmp bp, 9
			jne loop3b

		cont3:	
			dec row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, 'v'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
			
		down:
			call clr
			call sidebar
			call outline			
			call wall1
			
			cmp row, 21
			je d
			
		mov bp, 0
			loop4b:
				mov dh, w1Rowa[bp]
				dec dh
				cmp row, dh
				je blockDown
				inc bp
				cmp bp, 9
			jne loop4b

		cont4:
			inc row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			
			mov ah, 0Ah
			mov al, '^'
			mov bl, 0Eh
			mov bh, 0
			xor cx, cx
			mov cx, 1
			mov ah, 09h
			int 10h
			jmp scan
		
		l:
			inc col
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp left
	
		r:
			dec col
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp right
		
		u:
			inc row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp up
		
		d:
			dec row
			mov dl, col
			mov dh, row
			xor bh, bh
			mov ah, 02h
			int 10h
			jmp down
			
		blockLeft:
			mov dh, w1Rowa[bp]
			cmp row, dh
			je l
			jmp cont1
		blockRight:
			mov dh, w1Rowa[bp]
			cmp row, dh
			je r
			jmp cont2
		blockUp:
			mov dh, w1Cola[bp]
			cmp col, dh
			je u
			jmp cont3
		blockDown:
			mov dh, w1Cola[bp]
			cmp col, dh
			je d
			jmp cont4
	main    endp
    end     main
