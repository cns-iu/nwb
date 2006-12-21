       program outdegree_sequence

  
!
!      It calculates the out-degree sequences 
!

       implicit none

       integer i,j,k,n_vert,n_edges,maxind,minind,i1,i2
       integer, allocatable, dimension (:) :: outdegree
       character*256 filename,fileout1,str1,str2
       
!      Here the program reads the input parameters
       
       call GETARG(2,filename)
       
       fileout1='out-degree_sequence.dat'

       n_edges=0
       maxind=1
       minind=1000000

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8103,end=8103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             do 
                read(20,*,err=8103,end=8103)i1,i2
                if(minind>i1)minind=i1
                if(minind>i2)minind=i2
                if(maxind<i2)maxind=i2
                if(maxind<i1)maxind=i1
                n_edges=n_edges+1   
             enddo
          endif
       enddo
8103   continue
       close(20)

       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on directed networks'
          stop
       endif
       if(minind/=1)then
          write(*,*)'Error! The minimal node index is not 1'
          stop
       endif

       n_vert=maxind

       allocate(outdegree(1:n_vert))

       outdegree=0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             do j=1,n_edges
                read(20,*)i1,i2
                outdegree(i1)=outdegree(i1)+1
             enddo
             exit
          endif
       enddo
8203   continue
       close(20)

!      Here we write out the final degree sequences 

       open(20,file=fileout1,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Node     |   In-degree'
       write(20,*)
       do k=1,n_vert
          write(20,101)k,outdegree(k)
       enddo
       close(20)

101    format(i10,8x,i10)
103    format(a8,i10)
104    format(2x,i10,9x,e15.6)
105    format(4x,e15.6,6x,e15.6)
106    format(a256)

9001   continue

       stop
     end program outdegree_sequence
