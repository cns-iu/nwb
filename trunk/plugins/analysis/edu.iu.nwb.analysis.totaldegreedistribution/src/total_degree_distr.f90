       program total_degree_distr

  
!
!      It calculates the degree distribution of a network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,maxdeg,mindeg,n_vert,n_edges,n_bins,i1,i2,maxind,minind
       integer, allocatable, dimension (:) :: degree,degdis,npoints,np,deg
       real*8, allocatable, dimension (:) :: interv,avdegbin
       real*8 binsize
       character*256 filename,fileout1,fileout2,fileout3,str1,str2,sn_bins
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       fileout1='degree_sequence.dat'
       fileout2='degree_distribution.dat'
       fileout3='degree_distribution_binned.dat'
       n_vert=0
       n_edges=0

       open(20,file=filename,status='unknown')
       do 
          read(20,*)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             do 
                read(20,*,err=8103,end=8103)str2
                if(str2(1:1)=='*'.OR.str1(2:2)=='*')goto 8103
                n_edges=n_edges+1   
             enddo
          endif
       enddo
8103   continue
       close(20)

       allocate(deg(1:2*n_edges))

       deg=0
       maxind=1
       minind=n_edges

!      Here we update the degrees of each node
!      directly from the reading of the edges

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             do j=1,n_edges
                read(20,*)i1,i2
                if(minind>i1)minind=i1
                if(minind>i2)minind=i2
                if(maxind<i2)maxind=i2
                if(maxind<i1)maxind=i1
                deg(i1)=deg(i1)+1
                deg(i2)=deg(i2)+1
             enddo
             exit
          endif
       enddo
8203   continue
       close(20)
       if(minind/=1)then
          write(*,*)'The minimal node index is not 1'
          stop
       endif

       n_vert=maxind

!      Here the arrays are allocated

       allocate(degree(1:n_vert))
       allocate(interv(0:n_bins))
       allocate(npoints(1:n_bins))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       
!      Here we initialize to zero the degrees of the nodes

       degree=deg

       deallocate(deg)

!      Here we update the degrees of each node
!      directly from the reading of the edges

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

!      Here we calculate the minimum and maximum degree, allocate and 
!      initialize the histogram of the degree distribution (array degdis)

       mindeg=MINVAL(degree)
       maxdeg=MAXVAL(degree)
       
       allocate(degdis(mindeg:maxdeg))

       degdis=0

!      Here we evaluate the number of nodes having the same degree
!      to get the probability we divide by the total number of edges n_edges

       do k=1,n_vert
          degdis(degree(k))=degdis(degree(k))+1
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Degree    |    Probability'
       write(20,*)
       do k=mindeg,maxdeg
          write(20,104)k,real(degdis(k))/n_vert
       enddo
       close(20)

!      Here we calculate the binned degree distribution

       if(real(mindeg+1)/real(maxdeg)>0.1d0)then
          open(20,file='Message_on_binning_degree.txt',status='unknown')
          write(20,*)'Degree varies too little: the logarithmic binning is not useful'
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
             if(real(degree(i))<interv(j))then
                np(j)=np(j)+1
                avdegbin(j)=avdegbin(j)+real(degree(i))
                exit
             endif
          enddo
       enddo
       
       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
          endif
       enddo
       
       open(20,file=fileout3,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'# Center of Degree bin | Probability'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,105)avdegbin(i),real(np(i))/(n_vert*npoints(i))
          endif
       enddo
       close(20)
9001   continue       
       
       stop
     end program total_degree_distr
