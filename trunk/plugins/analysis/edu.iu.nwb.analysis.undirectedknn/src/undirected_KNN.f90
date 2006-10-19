       program undirected_KNN

  
!
!      It calculates the degree-degree correlations 
!      for an undirected network 
!      whose matrix is given as input as list of all edges
!      The correlation coefficient is knn
!      

       implicit none

       integer i,j,k,maxdeg,mindeg,n_vert,n_edges,n_bins,i1,i2,minind,maxind
       integer, allocatable, dimension (:) :: degree,np
       integer, allocatable, dimension (:) :: count_deg
       real*8, allocatable, dimension (:) :: knn,avdegbin,interv,avoutbin
       real*8, allocatable, dimension (:) :: knn_his
       real*8 binsize,avk,avk2
       character*256 filename,fileout1,fileout2,sn_bins,str1,str2
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout1='knn.dat'
       fileout2='knn_binned.dat'

!      Here the arrays are allocated

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
       allocate(knn(1:n_vert))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       allocate(avoutbin(1:n_bins))
       allocate(interv(0:n_bins))
       
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
       
!      Here we initialize to zero the degrees of the nodes


!      Here we calculate the minimum and maximum degree, allocate and 
!      initialize the histogram of the degree-degree correlations 
!      (array knn_his)

       mindeg=MINVAL(degree)
       maxdeg=MAXVAL(degree)
       
       allocate(knn_his(mindeg:maxdeg))
       allocate(count_deg(mindeg:maxdeg))

!      Here we calculate the correlation coefficients

       knn=0.0d0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             do j=1,n_edges
                read(20,*)i1,i2
                knn(i2)=knn(i2)+real(degree(i1))/degree(i2)
                knn(i1)=knn(i1)+real(degree(i2))/degree(i1)
             enddo
             exit
          endif
       enddo

       close(20)

!      Here we average the correlation coefficients among nodes with equal degree
!      (array knn_his)
!      and write out the final averages 

       knn_his=0.0d0
       count_deg=0
       avk=0.0d0
       avk2=0.0d0

       do k=1,n_vert
          knn_his(degree(k))=knn_his(degree(k))+knn(k)
          count_deg(degree(k))=count_deg(degree(k))+1
          avk=avk+real(degree(k))/n_vert
          avk2=avk2+(real(degree(k))/n_vert)*degree(k)
       enddo

       open(21,file=fileout1,status='unknown')
       write(21,103)'# Nodes ',n_vert
       write(21,*)'#   Degree |   knn'
       write(21,*)

       do k=mindeg, maxdeg
          if(count_deg(k)>0)then
             write(21,107)k,(knn_his(k)*avk)/(count_deg(k)*avk2)
          endif
       enddo

       close(21)

       if(mindeg==0)then
          interv(0)=real(mindeg+1)
       else
          interv(0)=real(mindeg)
       endif

       binsize=(log(real(maxdeg)+0.1d0)-log(interv(0)))/n_bins
       
       do i=1,n_bins
          interv(i)=exp(log(interv(i-1))+binsize)
       enddo

       avdegbin=0.0d0
       avoutbin=0.0d0
       np=0
       
       do i=1,n_vert
          do j=1,n_bins
             if(real(degree(i))<interv(j))then
                np(j)=np(j)+1
                avdegbin(j)=avdegbin(j)+real(degree(i))
                avoutbin(j)=avoutbin(j)+knn(i)
                exit
             endif
          enddo
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
             avoutbin(i)=avoutbin(i)/np(i)
          endif
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Degree |   knn'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),(avoutbin(i)*avk)/avk2
          endif
       enddo
       close(20)

101    format(a41)
102    format(3i12)
103    format(a8,i10)
104    format(8x,e15.6,6x,e15.6)
105    format(a40,e15.6)
106    format(a256)
107    format(i12,e15.6)

       stop
     end program undirected_KNN
