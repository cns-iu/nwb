       program directed_KNN

  
!
!      It calculates the degree-degree correlations 
!      for a directed network 
!      whose matrix is given as input as list of all edges
!      There are four coefficients:
!      
!      knn_in_in = average in-degree of in-neighbors
!      knn_in_out = average out-degree of in-neighbors
!      knn_out_in = average in-degree of out-neighbors
!      knn_out_out = average out-degree of out-neighbors
!

       implicit none

       integer i,j,k,maxindeg,minindeg,maxoutdeg,minoutdeg,i1,i2,n_bins,minind,maxind,mindeg,maxdeg
       integer n_vert,n_edges
       integer, allocatable, dimension (:) :: indegree,outdegree,np
       real*8, allocatable, dimension (:) :: knn_in_in,knn_in_out
       real*8, allocatable, dimension (:) :: knn_out_in,knn_out_out,avdegbin,interv,avoutbin,avout2bin
       real*8, allocatable, dimension (:) :: knn_in_in_his,knn_in_out_his
       real*8, allocatable, dimension (:) :: knn_out_in_his,knn_out_out_his
       integer, allocatable, dimension (:) :: count_out,count_in
       logical, allocatable, dimension(:):: nodelist
       real*8 binsize,avk,avkin2,avkout2,avkinkout
       character*256 filename,fileout1,fileout2,sn_bins,str1,str2,fileout3,fileout4
       character*256 fileout5,fileout6,fileout7,fileout8
       
!      Here the program reads the input parameters
       
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins

       fileout1='knn_in_in.dat'
       fileout2='knn_in_out.dat'
       fileout3='knn_out_in.dat'
       fileout4='knn_out_out.dat'
       fileout5='knn_in_in_binned.dat'
       fileout6='knn_in_out_binned.dat'
       fileout7='knn_out_in_binned.dat'
       fileout8='knn_out_out_binned.dat'

!      Here the arrays are allocated

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
       allocate(outdegree(minind:maxind))
       indegree=0
       outdegree=0
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

       allocate(knn_in_in(minind:maxind))
       allocate(knn_in_out(minind:maxind))
       allocate(knn_out_in(minind:maxind))
       allocate(knn_out_out(minind:maxind))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       allocate(avoutbin(1:n_bins))
       allocate(avout2bin(1:n_bins))
       allocate(interv(0:n_bins))
       
!      Here we calculate the minimum and maximum in-degree and out-degree, allocate and 
!      initialize the histograms of the degree-degree correlations 
!      (arrays knn_in_in_his,knn_in_out_his,knn_out_in_his,knn_out_out_his)

       minindeg=1000000
       maxindeg=0
       minoutdeg=1000000
       maxoutdeg=0

       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             if(indegree(i)>maxindeg)maxindeg=indegree(i)
             if(indegree(i)<minindeg)minindeg=indegree(i)
             if(outdegree(i)>maxoutdeg)maxoutdeg=outdegree(i)
             if(outdegree(i)<minoutdeg)minoutdeg=outdegree(i)
          endif
       enddo
       
       allocate(knn_in_in_his(minindeg:maxindeg))
       allocate(knn_in_out_his(minindeg:maxindeg))
       allocate(knn_out_in_his(minoutdeg:maxoutdeg))
       allocate(knn_out_out_his(minoutdeg:maxoutdeg))
       allocate(count_in(minindeg:maxindeg))
       allocate(count_out(minoutdeg:maxoutdeg))

!      Here we calculate the correlation coefficients

       knn_in_in=0.0d0
       knn_in_out=0.0d0
       knn_out_in=0.0d0
       knn_out_out=0.0d0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             do j=1,n_edges
                read(20,*)i1,i2
                knn_in_in(i2)=knn_in_in(i2)+real(indegree(i1))/indegree(i2)
                knn_in_out(i2)=knn_in_out(i2)+real(outdegree(i1))/indegree(i2)
                knn_out_in(i1)=knn_out_in(i1)+real(indegree(i2))/outdegree(i1)
                knn_out_out(i1)=knn_out_out(i1)+real(outdegree(i2))/outdegree(i1)
             enddo
             exit
          endif
       enddo
       close(20)

!      Here we average the correlation coefficients among nodes with equal degree
!      (arrays knn_in_in_his, knn_in_out_his, knn_out_in_his, knn_out_out_his)
!      and write out the final averages in four files, one for each type of correlation

       knn_in_in_his=0.0d0
       knn_in_out_his=0.0d0
       knn_out_in_his=0.0d0
       knn_out_out_his=0.0d0
       count_in=0
       count_out=0
       avk=0.0d0
       avkin2=0.0d0
       avkout2=0.0d0
       avkinkout=0.0d0

       do k=minind,maxind
          if(nodelist(k).eqv..true.)then
             knn_in_in_his(indegree(k))=knn_in_in_his(indegree(k))+knn_in_in(k)
             knn_in_out_his(indegree(k))=knn_in_out_his(indegree(k))+knn_in_out(k)
             count_in(indegree(k))=count_in(indegree(k))+1
             knn_out_in_his(outdegree(k))=knn_out_in_his(outdegree(k))+knn_out_in(k)
             knn_out_out_his(outdegree(k))=knn_out_out_his(outdegree(k))+knn_out_out(k)
             count_out(outdegree(k))=count_out(outdegree(k))+1
             avk=avk+real(indegree(k))/n_vert
             avkin2=avkin2+(real(indegree(k))/n_vert)*indegree(k)
             avkout2=avkout2+(real(outdegree(k))/n_vert)*outdegree(k)
             avkinkout=avkinkout+(real(outdegree(k))/n_vert)*indegree(k)
          endif
       enddo

       open(21,file=fileout1,status='unknown')
       open(22,file=fileout2,status='unknown')
       open(23,file=fileout3,status='unknown')
       open(24,file=fileout4,status='unknown')
       write(21,103)'# Nodes ',n_vert
       write(21,*)'#   Indegree |   knn_in_in'
       write(21,*)
       write(22,103)'# Nodes ',n_vert
       write(22,*)'#   Indegree |   knn_in_out'
       write(22,*)
       write(23,103)'# Nodes ',n_vert
       write(23,*)'#   Outdegree |   knn_out_in'
       write(23,*)
       write(24,103)'# Nodes ',n_vert
       write(24,*)'#   Outdegree |   knn_out_out'
       write(24,*)

       do k=minindeg, maxindeg
          if(count_in(k)>0)then
             knn_in_in_his(k)=knn_in_in_his(k)/count_in(k)
             knn_in_out_his(k)=knn_in_out_his(k)/count_in(k)
             write(21,107)k,(knn_in_in_his(k)*avk/avkinkout)
             write(22,107)k,(knn_in_out_his(k)*avk/avkout2)
          endif
       enddo

       do k=minoutdeg, maxoutdeg
          if(count_out(k)>0)then
             knn_out_in_his(k)=knn_out_in_his(k)/count_out(k)
             knn_out_out_his(k)=knn_out_out_his(k)/count_out(k)
             write(23,107)k,(knn_out_in_his(k)*avk/avkin2)
             write(24,107)k,(knn_out_out_his(k)*avk/avkinkout)
          endif
       enddo

       close(21)
       close(22)
       close(23)
       close(24)

       mindeg=minindeg
       maxdeg=maxindeg

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
       avout2bin=0.0d0
       np=0
       
       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             do j=1,n_bins
                if(real(indegree(i))<interv(j))then
                   np(j)=np(j)+1
                   avdegbin(j)=avdegbin(j)+real(indegree(i))
                   avoutbin(j)=avoutbin(j)+knn_in_in(i)
                   avout2bin(j)=avout2bin(j)+knn_in_out(i)
                   exit
                endif
             enddo
          endif
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
             avoutbin(i)=avoutbin(i)/np(i)
             avout2bin(i)=avout2bin(i)/np(i)
          endif
       enddo

       open(20,file=fileout5,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Indegree |   knn_in_in'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),(avoutbin(i)*avk)/avkinkout
          endif
       enddo
       close(20)

       open(20,file=fileout6,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Indegree |   knn_in_out'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),(avout2bin(i)*avk)/avkout2
          endif
       enddo
       close(20)

       mindeg=minoutdeg
       maxdeg=maxoutdeg

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
       avout2bin=0.0d0
       np=0
       
       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             do j=1,n_bins
                if(real(outdegree(i))<interv(j))then
                   np(j)=np(j)+1
                   avdegbin(j)=avdegbin(j)+real(outdegree(i))
                   avoutbin(j)=avoutbin(j)+knn_out_in(i)
                   avout2bin(j)=avout2bin(j)+knn_out_out(i)
                   exit
                endif
             enddo
          endif
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
             avoutbin(i)=avoutbin(i)/np(i)
             avout2bin(i)=avout2bin(i)/np(i)
          endif
       enddo

       open(20,file=fileout7,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Outdegree |   knn_out_in'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),(avoutbin(i)*avk)/avkin2
          endif
       enddo
       close(20)

       open(20,file=fileout8,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Outdegree |   knn_out_out'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),(avout2bin(i)*avk)/avkinkout
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
     end program directed_KNN
