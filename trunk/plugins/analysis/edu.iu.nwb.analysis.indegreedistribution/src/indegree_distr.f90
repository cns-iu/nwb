       program indegree_distr

  
!
!      It calculates the in-degree and the out-degree 
!      sequences and distributions for a directed network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,maxindeg,minindeg,mindeg,maxdeg,n_vert,n_edges,n_bins,maxind,minind,i1,i2
       integer, allocatable, dimension (:) :: indegree,indegdis,npoints,np
       real*8, allocatable, dimension (:) :: interv,degdis_binned,avdegbin
       logical, allocatable, dimension(:):: nodelist
       real*8 binsize
       character*256 filename,fileout2,fileout5,str1,str2,sn_bins
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout2='in-degree_distribution.dat'
       fileout5='in-degree_distribution_binned.dat'

       n_edges=0
       maxind=1
       minind=10000000

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8103,end=8103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             n_vert=0
             do 
                read(20,*,err=8103,end=8103)i1
                if(minind>i1)minind=i1
                if(maxind<i1)maxind=i1
                n_vert=n_vert+1  
             enddo
          else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then 
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
       indegree=0
       nodelist=.false.
       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=9203,end=9203)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             n_vert=0
             do 
                read(20,*,err=9203,end=9203)i1
                nodelist(i1)=.true.
                n_vert=n_vert+1
             enddo
          else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
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
             enddo
             goto 9303
          endif
       enddo
9203   continue
       backspace(20)
       do
          read(20,106,err=9303,end=9303)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
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
             enddo
          endif
       enddo
9303   continue
       close(20)
       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on directed networks'
          stop
       endif

!      Here the arrays are allocated

       allocate(interv(0:n_bins))
       allocate(npoints(1:n_bins))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       
       minindeg=1000000
       maxindeg=0

       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             if(indegree(i)>maxindeg)maxindeg=indegree(i)
             if(indegree(i)<minindeg)minindeg=indegree(i)
          endif
       enddo
       
       allocate(indegdis(minindeg:maxindeg))

       indegdis=0

!      Here we evaluate the number of nodes having the same (in- or out-) degree;
!      to get the probability we divide by the total number of edges n_edges

       do k=minind,maxind
          if(nodelist(k).eqv..true.)then
             indegdis(indegree(k))=indegdis(indegree(k))+1
          endif
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     In-degree    |    Probability'
       write(20,*)
        do k=minindeg,maxindeg
          write(20,104)k,real(indegdis(k))/n_vert
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
          write(*,*)'Warning! In-degree varies too little: the logarithmic binning is not useful'
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
       
       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             do j=1,n_bins
                if(real(indegree(i))<interv(j))then
                   np(j)=np(j)+1
                   avdegbin(j)=avdegbin(j)+real(indegree(i))
                   exit
                endif
             enddo
          endif
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

       stop
     end program indegree_distr
