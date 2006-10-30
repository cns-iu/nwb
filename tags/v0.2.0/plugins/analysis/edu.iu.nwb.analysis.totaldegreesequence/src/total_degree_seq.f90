       program total_degree_seq

  
!
!      It calculates the degree distribution of a network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,n_vert,n_edges,i1,i2,maxind,minind
       integer, allocatable, dimension (:) :: degree
       character*256 filename,fileout1,str1,str2,sn_bins
       
!      Here the program reads the input parameters
       
       call GETARG(2,filename)

       fileout1='degree_sequence.dat'

       n_edges=0
       maxind=1
       minind=1000000

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8103,end=8103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
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
          write(*,*)'Error! The program should be applied on undirected networks'
          stop
       endif
       if(minind/=1)then
          write(*,*)'Error! The minimal node index is not 1'
          stop
       endif

       n_vert=maxind

       allocate(degree(1:n_vert))
       degree=0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             do j=1,n_edges
                read(20,*)i1,i2
                degree(i1)=degree(i1)+1
                degree(i2)=degree(i2)+1
             enddo
             exit
          endif
       enddo
       close(20)

!      Here we write out the final degree sequence 

       open(20,file=fileout1,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Node     |     Degree'
       write(20,*)
       do k=1,n_vert
          write(20,101)k,degree(k)
       enddo
       close(20)

101    format(i10,8x,i10)
103    format(a8,i10)
104    format(i10,9x,e15.6)
105    format(4x,e15.6,4x,e15.6)
106    format(a256)

9001   continue       
       
       stop
     end program total_degree_seq
