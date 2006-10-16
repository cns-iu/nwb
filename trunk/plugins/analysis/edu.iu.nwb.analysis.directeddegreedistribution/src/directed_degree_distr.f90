       program directed_degree_distr

  
!
!      It calculates the in-degree and the out-degree 
!      sequences and distributions for a directed network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,maxindeg,minindeg,maxoutdeg,minoutdeg,mindeg,maxdeg,n_vert,n_edges,n_bins,maxind,minind,i1,i2
       integer, allocatable, dimension (:) :: indegree,outdegree,indegdis,outdegdis,npoints,np
       real*8, allocatable, dimension (:) :: interv,degdis_binned,avdegbin
       real*8 binsize,alpha,ratio
       character*256 filename,fileout1,fileout2,fileout3,fileout4,fileout5,fileout6,str1,str2,sn_bins
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout1='in-degree_sequence.dat'
       fileout2='in-degree_distribution.dat'
       fileout3='out-degree_sequence.dat'
       fileout4='out-degree_distribution.dat'
       fileout5='in-degree_distribution_binned.dat'
       fileout6='out-degree_distribution_binned.dat'

       n_edges=0
       maxind=1
       minind=1000000

       open(20,file=filename,status='unknown')
       do 
          read(20,*)str1
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
8203   continue
       close(20)

!      Here the arrays are allocated

       allocate(interv(0:n_bins))
       allocate(npoints(1:n_bins))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       
!      Here we initialize to zero the degrees of the nodes


!      Here we write out the final degree sequences 

       open(20,file=fileout1,status='unknown')
       open(21,file=fileout3,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Node     |   In-degree'
       write(20,*)
       write(21,103)'# Nodes ',n_vert
       write(21,*)'#     Node     |   Out-degree'
       write(21,*)
       do k=1,n_vert
          write(20,101)k,indegree(k)
          write(21,101)k,outdegree(k)
       enddo
       close(20)
       close(21)

!      Here we calculate the minimum and maximum in-degree and out-degree, allocate and 
!      initialize the histogram of the degree distributions (arrays indegdis,outdegdis)

       minindeg=MINVAL(indegree)
       maxindeg=MAXVAL(indegree)
       minoutdeg=MINVAL(outdegree)
       maxoutdeg=MAXVAL(outdegree)
       
       allocate(indegdis(minindeg:maxindeg))
       allocate(outdegdis(minoutdeg:maxoutdeg))

       indegdis=0
       outdegdis=0

!      Here we evaluate the number of nodes having the same (in- or out-) degree;
!      to get the probability we divide by the total number of edges n_edges

       do k=1,n_vert
          indegdis(indegree(k))=indegdis(indegree(k))+1
          outdegdis(outdegree(k))=outdegdis(outdegree(k))+1
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     In-degree    |    Probability'
       write(20,*)
        do k=minindeg,maxindeg
          write(20,104)k,real(indegdis(k))/n_vert
       enddo
       close(20)

       open(20,file=fileout4,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#    Out-degree    |    Probability'
       write(20,*)
        do k=minoutdeg,maxoutdeg
          write(20,104)k,real(outdegdis(k))/n_vert
       enddo
       close(20)

101    format(i10,8x,i10)
103    format(a8,i10)
104    format(2x,i10,9x,e15.6)
105    format(4x,e15.6,6x,e15.6)
106    format(a256)

!      Here we calculate the binned degree distributions

       mindeg=minindeg
       maxdeg=maxindeg

       if(real(mindeg+1)/real(maxdeg)>0.1d0)then
          open(20,file='Message_on_binning_indegree.txt',status='unknown')
          write(20,*)'In-degree varies too little: the logarithmic binning is not useful'
          close(20)
          goto 9001
       endif

       if(mindeg==0)then
          interv(0)=real(mindeg+1)
       else
          interv(0)=real(mindeg)
       endif
       binsize=(log(real(maxdeg)+0.1d0)-log(interv(0)))/n_bins
       
       do i=1,n_bins
          interv(i)=exp(log(interv(i-1))+binsize)
       enddo

       npoints=0
       do i=mindeg,maxdeg
          do j=1,n_bins
             if(real(i)<interv(j))then
                npoints(j)=npoints(j)+1
                exit
             endif
          enddo
       enddo

       avdegbin=0.0d0
       np=0
       
       do i=1,n_vert
          do j=1,n_bins
             if(real(indegree(i))<interv(j))then
                np(j)=np(j)+1
                avdegbin(j)=avdegbin(j)+real(indegree(i))
                exit
             endif
          enddo
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
          endif
       enddo
       
       open(20,file=fileout5,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'# Center of In-degree bin | Probability'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,105)avdegbin(i),real(np(i))/(n_vert*npoints(i))
          endif
       enddo
       close(20)

9001   continue

       mindeg=minoutdeg
       maxdeg=maxoutdeg
       if(real(mindeg+1)/real(maxdeg)>0.1d0)then
          open(20,file='Message_on_binning_outdegree.txt',status='unknown')
          write(20,*)'Out-degree varies too little: the logarithmic binning is not useful'
          close(20)
          goto 9002
       endif

       if(mindeg==0)then
          interv(0)=real(mindeg+1)
       else
          interv(0)=real(mindeg)
       endif
       binsize=(log(real(maxdeg)+0.1d0)-log(interv(0)))/n_bins
       
       do i=1,n_bins
          interv(i)=exp(log(interv(i-1))+binsize)
       enddo

       npoints=0
       do i=mindeg,maxdeg
          do j=1,n_bins
             if(real(i)<interv(j))then
                npoints(j)=npoints(j)+1
                exit
             endif
          enddo
       enddo

       avdegbin=0.0d0
       np=0
       
       do i=1,n_vert
          do j=1,n_bins
             if(real(outdegree(i))<interv(j))then
                np(j)=np(j)+1
                avdegbin(j)=avdegbin(j)+real(outdegree(i))
                exit
             endif
          enddo
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
          endif
       enddo
  
       open(20,file=fileout6,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'# Center of Out-degree bin | Probability'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,105)avdegbin(i),real(np(i))/(n_vert*npoints(i))
          endif
       enddo
       close(20)

9002   continue

       stop
     end program directed_degree_distr
