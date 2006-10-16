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
       character*256 filename,fileout,fileout1,fileout2,sn_bins,str1,str2
       real*8 binsize

!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout='indegree_outdegree_onepoint.dat'
       fileout1='in_out_degree.dat'
       fileout2='indegree_outdegree_onepoint_binned.dat'

!      Here the arrays are allocated

       n_edges=0
       maxind=1
       minind=1000000

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
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

       if(minind/=1)then
          write(*,*)'The minimal node index is not 1'
          stop
       endif

       n_vert=maxind

       allocate(indegree(1:n_vert))
       allocate(outdegree(1:n_vert))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       allocate(avoutbin(1:n_bins))
       allocate(interv(0:n_bins))

!      Here we initialize to zero the degrees of the nodes

       indegree=0
       outdegree=0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             do j=1,n_edges
                read(20,*)i1,i2
                outdegree(i1)=outdegree(i1)+1
                indegree(i2)=indegree(i2)+1
             enddo
             exit
          endif
       enddo
       close(20)

!      Here we update the degrees of each node
!      directly from the reading of the edges

!      Here we print out the in- and the out-degrees of all nodes

       open(20,file=fileout1,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,101)'#        Node  |   Indegree  |  Outdegree'
       write(20,*)

       do i=1,n_vert
          write(20,102)i,indegree(i),outdegree(i)
       enddo

       close(20)

!      Here we calculate the minimum and maximum in-degree, allocate and 
!      initialize the histogram of the correlations 
!      (arrays knn_his)

       minindeg=MINVAL(indegree)
       maxindeg=MAXVAL(indegree)
       
       allocate(knn_his(minindeg:maxindeg))
       allocate(count_in(minindeg:maxindeg))

       knn_his=0.0d0
       count_in=0

!      Here we average the correlation coefficients among nodes with equal degree
!      (array knn_his)
!      and write out the final averages

       do k=1,n_vert
          knn_his(indegree(k))=knn_his(indegree(k))+real(outdegree(k))
          count_in(indegree(k))=count_in(indegree(k))+1
       enddo

       open(21,file=fileout,status='unknown')

       write(21,103)'# Nodes ',n_vert
       write(21,*)'#   Indegree |   Outdegree'
       write(21,*)
       do k=minindeg, maxindeg
          if(count_in(k)>0)then
             knn_his(k)=knn_his(k)/count_in(k)
             write(21,107)k,knn_his(k)
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
       
       do i=1,n_vert
          do j=1,n_bins
             if(real(indegree(i))<interv(j))then
                np(j)=np(j)+1
                avdegbin(j)=avdegbin(j)+real(indegree(i))
                avoutbin(j)=avoutbin(j)+real(outdegree(i))
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
       
       stop
     end program One_point_correlations
