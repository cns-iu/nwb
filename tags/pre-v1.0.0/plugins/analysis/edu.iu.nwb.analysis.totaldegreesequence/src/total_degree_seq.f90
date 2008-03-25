       program total_degree_seq

       
!
!      It calculates the degree sequence of a network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,n_vert,n_edges,i1,i2,maxind,minind,nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
       integer, allocatable, dimension (:) :: degree,ind,indc,nodes
       logical, allocatable, dimension(:):: nodelist
       character*256 filename,fileout1,fileout2
       character*50 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
       character*25,allocatable,dimension(:,:):: attrN,attrE
       
       
       logical quoteit
       character*(25) addquotes

!      Here the program reads the input parameters
       
       
       
       
       call GETARG(2,filename)

       fileout1='degree_sequence.dat'
       fileout2='network_total_degree_seq.nwb'

       n_edges=0
       n_vert0=0
       n_vert1=0
       maxind=1
       minind=10000000
       ch=0
       n_edges_N=0
       n_edges_E=0
       nattrN=0

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8103,end=8103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             read(20,*)str3
             if(str3(1:1)=='*')then
                ch=1
                goto 8103
             endif
             read(20,*)str2
             backspace(20)
             backspace(20)
             do k=1,20
                read(20,*)(str(j),j=1,k)
                if(str(k)(1:1)==str2(1:1))exit
                backspace(20)
             enddo
             backspace(20)
             nattrN=k-1
             do 
                read(20,*,err=8103,end=8103)str2
                if(str2(1:1)=='*')goto 8103
                n_edges_N=n_edges_N+1  
             enddo
          else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
             read(20,*)
             read(20,*)str2
             backspace(20)
             backspace(20)
             do k=1,20
                read(20,*)(str(j),j=1,k)
                if(str(k)(1:1)==str2(1:1))exit
                backspace(20)
             enddo
             backspace(20)
             nattrE=k-1
             do 
                read(20,*,err=9103,end=9103)
                n_edges_E=n_edges_E+1   
             enddo
          endif
       enddo
8103   continue
       backspace(20)
       do
          read(20,106,err=9103,end=9103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             read(20,*)
             read(20,*)str2
             backspace(20)
             backspace(20)
             do k=1,20
                read(20,*)(str(j),j=1,k)
                if(str(k)(1:1)==str2(1:1))exit
                backspace(20)
             enddo
             backspace(20)
             nattrE=k-1
             do 
                read(20,*,err=9103,end=9103)
                n_edges_E=n_edges_E+1   
             enddo
          endif
       enddo
9103   continue
       close(20)
       allocate(nodes(1:n_edges_N))
       allocate(ind(1:n_edges_E))
       allocate(indc(1:n_edges_E))
       allocate(attrE(1:nattrE-2,1:n_edges_E))
       if(nattrN>1)then
          allocate(attrN(1:nattrN-1,1:n_edges_N))
       endif

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8104,end=8104)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             if(ch==1)then
                read(20,*)
                goto 8104
             endif
             read(20,*)(headattrN(i),i=1,nattrN)
             do k=1,n_edges_N
                read(20,*,err=8114,end=8114)nodes(n_vert0+1),(attrN(j,n_vert0+1),j=1,nattrN-1)
                do i = 1, nattrN - 1, 1
                   if(quoteit(headattrN(i+1),attrN(i, n_vert0+1))) then
                       attrN(i, n_vert0+1) = addquotes(attrN(i, n_vert0+1))
                   endif
                enddo
                n_vert0=n_vert0+1
                if(minind>nodes(n_vert0))minind=nodes(n_vert0)
                if(maxind<nodes(n_vert0))maxind=nodes(n_vert0)  
8114            continue
             enddo
             goto 8104
          else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
             read(20,*)(headattrE(i),i=1,nattrE)
             do k=1,n_edges_E
                read(20,*,err=9114,end=9114)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
                do i = 1, nattrE -1, 1
                	if(quoteit(headattrE(i+2), attrE(i, n_edges+1))) then
                		attrE(i, n_edges+1) = addquotes(attrE(i, n_edges+1))
                	endif
                enddo
                n_edges=n_edges+1
                if(minind>indc(n_edges))minind=indc(n_edges)
                if(minind>ind(n_edges))minind=ind(n_edges)
                if(maxind<ind(n_edges))maxind=ind(n_edges)
                if(maxind<indc(n_edges))maxind=indc(n_edges)
9114            continue
             enddo
             goto 9104
          endif
       enddo
8104   continue
       backspace(20)
       do
          read(20,106,err=9104,end=9104)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             read(20,*)(headattrE(i),i=1,nattrE)
             do k=1,n_edges_E
                read(20,*,err=9214,end=9214)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
                do i = 1, nattrE -1, 1
                	if(quoteit(headattrE(i+2), attrE(i, n_edges+1))) then
                		attrE(i, n_edges+1) = addquotes(attrE(i, n_edges+1))
                	endif
                enddo
                n_edges=n_edges+1
                if(minind>indc(n_edges))minind=indc(n_edges)
                if(minind>ind(n_edges))minind=ind(n_edges)
                if(maxind<ind(n_edges))maxind=ind(n_edges)
                if(maxind<indc(n_edges))maxind=indc(n_edges)
9214            continue
             enddo
          endif
       enddo
9104   continue
       close(20)

       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on undirected networks'
          stop
       endif
       
       allocate(nodelist(minind:maxind))
       allocate(degree(minind:maxind))
       degree=0
       nodelist=.false.
       do i=1,n_vert0
          nodelist(nodes(i))=.true.
       enddo
       n_vert=n_vert0
       do i=1,n_edges
          if(nodelist(ind(i)).eqv..false.)then
             nodelist(ind(i))=.true.
             n_vert=n_vert+1
          endif
          if(nodelist(indc(i)).eqv..false.)then
             nodelist(indc(i))=.true.
             n_vert=n_vert+1
          endif
          degree(indc(i))=degree(indc(i))+1
          degree(ind(i))=degree(ind(i))+1
       enddo
       if(n_vert0<n_vert)then
          print*,'The nwb file is not properly formatted: not all nodes/labels are listed'
          stop
       endif

!      Here we construct a logical array corresponding to the node headers
       
       !logical headstringN(1:20)

!      Here we write out the final degree sequence 

       n_vert1=n_vert0
       n_vert0=0
       open(20,file=fileout1,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Node     |     Degree'
       write(20,*)
       do k=minind,maxind
          if(nodelist(k).eqv..true.)then
             n_vert0=n_vert0+1
             nodes(n_vert0)=k
             write(20,101)k,degree(k)
          endif
       enddo
       close(20)
       open(20,file=fileout2,status='unknown')
       write(20,112)'*Nodes'
       write(20,109)(headattrN(i),i=1,nattrN),'degree*int'
       if(nattrN-1==1)then
          do i=1,n_vert
             write(20,120)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==2)then
          do i=1,n_vert
             write(20,121)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==3)then
          do i=1,n_vert
             write(20,122)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==4)then
          do i=1,n_vert
             write(20,123)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==5)then
          do i=1,n_vert
             write(20,124)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==6)then
          do i=1,n_vert
             write(20,125)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==7)then
          do i=1,n_vert
             write(20,126)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==8)then
          do i=1,n_vert
             write(20,127)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==9)then
          do i=1,n_vert
             write(20,128)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       else if(nattrN-1==10)then
          do i=1,n_vert
             write(20,129)nodes(i),(attrN(j,i),j=1,nattrN-1),degree(nodes(i))
          enddo
       endif
       write(20,113)'*UndirectedEdges'
       write(20,109)(headattrE(i),i=1,nattrE)
       do i=1,n_edges
          write(20,110)indc(i),ind(i),(attrE(j,i),j=1,nattrE-2)
       enddo
       close(20)
    
101    format(i10,8x,i10)
103    format(a8,i10)
104    format(i10,9x,e15.6)
105    format(4x,e15.6,4x,e15.6)
106    format(a25)
107    format(a16,a18,a10)      
109    format(20a50)      
110    format(i10,8x,i10,1x,18a25)
111    format(i10,10x,20a25)
112    format(a6)
113    format(a16)
120    format(i10,2x,a25,1x,i10)
121    format(i10,2x,2a25,1x,i10)
122    format(i10,2x,3a25,1x,i10)
123    format(i10,2x,4a25,1x,i10)
124    format(i10,2x,5a25,1x,i10)
125    format(i10,2x,6a25,1x,i10)
126    format(i10,2x,7a25,1x,i10)
127    format(i10,2x,8a25,1x,i10)
128    format(i10,2x,9a25,1x,i10)
129    format(i10,2x,10a25,1x,i10)


9001   continue       
       
       stop
     end program total_degree_seq
     
	logical function quoteit(header, value)
		character*(*) header, value
		if(index(header, '*string') > 0 .AND. '*' /= value) then
			quoteit = .TRUE.
		else
			quoteit = .FALSE.
		endif
		return
	end 
	
	character*(25) function addquotes(value)
		character*(*) value
		character*22 longname
		
		if(len(TRIM(ADJUSTL(value))) >= 23) then
			longname = ADJUSTL(value)
			addquotes = '"' // longname // '"'
		else
			addquotes = '"' // TRIM(ADJUSTL(value)) // '"'
		endif
		return
	end