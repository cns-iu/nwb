       program One_point_correlations
  
!
!      It calculates the one-point correlations 
!      for a directed network 
!      whose matrix is given as input as list of all edges
!      The correlation is between the in- and the out-degree of the same node
!

       implicit none

       integer i,j,k,maxindeg,minindeg,n_vert,n_edges,minind,maxind,n_bins,i1,i2
       integer, allocatable, dimension (:) :: indegree,outdegree,np
       real*8, allocatable, dimension (:) :: knn_his,avdegbin,interv,avoutbin
       integer, allocatable, dimension (:) :: count_in
       logical, allocatable, dimension(:):: nodelist
       character*256 filename,fileout,fileout1,fileout2,sn_bins,str1,str2
       real*8 binsize,avdeg,avkinkout
       character*2000 headerdump

!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout='indegree_outdegree_onepoint.dat'
       fileout2='indegree_outdegree_onepoint_binned.dat'

!      Here the arrays are allocated

       n_edges=0
       maxind=1
       minind=10000000

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8103,end=8103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             read(20,108,err=8103,end=8103)headerdump
             n_vert=0
             do 
                read(20,*,err=8103,end=8103)i1
                if(minind>i1)minind=i1
                if(maxind<i1)maxind=i1
                n_vert=n_vert+1  
             enddo
          else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             read(20,108,err=9103,end=9103)headerdump
             do 
                read(20,*,err=9103,end=9103)i1,i2
                if(minind>i1)minind=i1
                if(minind>i2)minind=i2
                if(maxind<i2)maxind=i2
                if(maxind<i1)maxind=i1
                n_edges=n_edges+1   
             enddo
          endif
       enddo
8103   continue
       backspace(20)
       do
          read(20,106,err=9103,end=9103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             read(20,108,err=9103,end=9103)headerdump
             do 
                read(20,*,err=9103,end=9103)i1,i2
                if(minind>i1)minind=i1
                if(minind>i2)minind=i2
                if(maxind<i2)maxind=i2
                if(maxind<i1)maxind=i1
                n_edges=n_edges+1   
             enddo
          endif
       enddo
9103   continue
       close(20)
       allocate(nodelist(minind:maxind))
       allocate(indegree(minind:maxind))
       allocate(outdegree(minind:maxind))
       indegree=0
       outdegree=0
       nodelist=.false.
       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=9203,end=9203)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
            read(20,108,err=9203,end=9203)headerdump
            n_vert=0
             do 
                read(20,*,err=9203,end=9203)i1
                nodelist(i1)=.true.
                n_vert=n_vert+1
             enddo
          else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             read(20,108,err=9203,end=9203)headerdump
             n_vert=0
             do i=1,n_edges
                read(20,*)i1,i2
                if(nodelist(i1).eqv..false.)then
                   nodelist(i1)=.true.
                   n_vert=n_vert+1
                endif
                if(nodelist(i2).eqv..false.)then
                   nodelist(i2)=.true.
                   n_vert=n_vert+1
                endif
                indegree(i2)=indegree(i2)+1
                outdegree(i1)=outdegree(i1)+1
             enddo
             goto 9303
          endif
       enddo
9203   continue
       backspace(20)
       do
          read(20,106,err=9303,end=9303)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             read(20,108,err=9303,end=9303)headerdump
             do i=1,n_edges
                read(20,*)i1,i2
                if(nodelist(i1).eqv..false.)then
                   nodelist(i1)=.true.
                   n_vert=n_vert+1
                endif
                if(nodelist(i2).eqv..false.)then
                   nodelist(i2)=.true.
                   n_vert=n_vert+1
                endif
                indegree(i2)=indegree(i2)+1
                outdegree(i1)=outdegree(i1)+1
             enddo
          endif
       enddo
9303   continue
       close(20)

       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on directed networks'
          stop
       endif

       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       allocate(avoutbin(1:n_bins))
       allocate(interv(0:n_bins))

!      Here we update the degrees of each node
!      directly from the reading of the edges

!      Here we print out the in- and the out-degrees of all nodes

!      Here we calculate the minimum and maximum in-degree, allocate and 
!      initialize the histogram of the correlations 
!      (arrays knn_his)

       minindeg=1000000
       maxindeg=0

       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             if(indegree(i)>maxindeg)maxindeg=indegree(i)
             if(indegree(i)<minindeg)minindeg=indegree(i)
          endif
       enddo

       allocate(knn_his(minindeg:maxindeg))
       allocate(count_in(minindeg:maxindeg))

       knn_his=0.0d0
       avkinkout=0.0d0
       count_in=0
       avdeg=0.0d0

!      Here we average the correlation coefficients among nodes with equal degree
!      (array knn_his)
!      and write out the final averages

       do k=minind,maxind
          if(nodelist(k).eqv..true.)then
             avkinkout=avkinkout+(real(indegree(k))/n_vert)*outdegree(k)
             knn_his(indegree(k))=knn_his(indegree(k))+real(outdegree(k))
             count_in(indegree(k))=count_in(indegree(k))+1
             avdeg=avdeg+real(outdegree(k))/n_vert
          endif
       enddo

       write(*,*)'**************************************************************************'
       write(*,*)'The crossed one-point correlation equals ',avkinkout/(avdeg*avdeg)
       write(*,*)'**************************************************************************'

       open(21,file=fileout,status='unknown')

       write(21,103)'# Nodes ',n_vert
       write(21,*)'#   Indegree |   Outdegree'
       write(21,*)
       do k=minindeg, maxindeg
          if(count_in(k)>0)then
             write(21,107)k,knn_his(k)/count_in(k)
          endif
       enddo

       close(21)

       if(minindeg==0)then
          interv(0)=real(minindeg+1)
       else
          interv(0)=real(minindeg)
       endif

       binsize=(log(real(maxindeg)+0.1d0)-log(interv(0)))/n_bins
       
       do i=1,n_bins
          interv(i)=exp(log(interv(i-1))+binsize)
       enddo

       avdegbin=0.0d0
       avoutbin=0.0d0
       np=0
       
       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             do j=1,n_bins
                if(real(indegree(i))<interv(j))then
                   np(j)=np(j)+1
                   avdegbin(j)=avdegbin(j)+real(indegree(i))
                   avoutbin(j)=avoutbin(j)+real(outdegree(i))
                   exit
                endif
             enddo
          endif
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
             avoutbin(i)=avoutbin(i)/np(i)
          endif
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Indegree |   Outdegree'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),avoutbin(i)
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
108    format(a2000)
       
       stop
     end program One_point_correlations
