       program outdegree_distr

  
!
!      It calculates the in-degree and the out-degree 
!      sequences and distributions for a directed network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,maxoutdeg,minoutdeg,mindeg,maxdeg,n_vert,n_edges,n_bins,maxind,minind,i1,i2
       integer, allocatable, dimension (:) :: outdegree,outdegdis,npoints,np
       real*8, allocatable, dimension (:) :: interv,degdis_binned,avdegbin
       real*8 binsize
       character*256 filename,fileout2,fileout5,str1,str2,sn_bins
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout2='out-degree_distribution.dat'
       fileout5='out-degree_distribution_binned.dat'

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

!      Here the arrays are allocated

       allocate(interv(0:n_bins))
       allocate(npoints(1:n_bins))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       
       minoutdeg=MINVAL(outdegree)
       maxoutdeg=MAXVAL(outdegree)
       
       allocate(outdegdis(minoutdeg:maxoutdeg))

       outdegdis=0

!      Here we evaluate the number of nodes having the same (in- or out-) degree;
!      to get the probability we divide by the total number of edges n_edges

       do k=1,n_vert
          outdegdis(outdegree(k))=outdegdis(outdegree(k))+1
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Outdegree    |    Probability'
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

       mindeg=minoutdeg
       maxdeg=maxoutdeg

       if(real(mindeg+1)/real(maxdeg)>0.1d0)then
          write(*,*)'Warning! Outdegree varies too little: the logarithmic binning is not useful'
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
       
       open(20,file=fileout5,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'# Center of Outdegree bin | Probability'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,105)avdegbin(i),real(np(i))/(n_vert*npoints(i))
          endif
       enddo
       close(20)

9001   continue

       stop
     end program outdegree_distr
