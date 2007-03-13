       program clustering_vs_k

  
!
!      It calculates the clustering coefficients of the nodes
!      of an undirected network 
!      whose matrix is given as input as list of all edges; the 
!      distribution of the clustering
!      coefficients as well as the average clustering coefficient are also determined
!     

       implicit none

       integer i,j,k,l,maxdeg,mindeg,nCC,ntot_triangles,n_vert,n_edges,n_bins
       integer maxind,minind,i1,i2,ncluszero
       integer, allocatable, dimension (:) :: degree,intdegree,clus_coef,np
       integer, allocatable, dimension (:) :: check_neigh,listlink
       real*8, allocatable, dimension (:) :: clus,interv,avclusbin,avdegbin
       logical, allocatable, dimension(:):: nodelist
       real*8 norm,Avcluscoeff,minclus,maxclus,abin,binsize,zero
       character*256 filename,fileout5,sn_bins,str1,str2
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins

       fileout5='clus_coeff_vs_degree_binned.dat'
       
!      Here the arrays are allocated

       n_edges=0
       maxind=1
       minind=10000000
       zero=0.0d0

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
          else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
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
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
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
       allocate(degree(minind:maxind))
       degree=0
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
          else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
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
                degree(i1)=degree(i1)+1
                degree(i2)=degree(i2)+1
             enddo
             goto 9303
          endif
       enddo
9203   continue
       backspace(20)
       do
          read(20,106,err=9303,end=9303)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
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
                degree(i1)=degree(i1)+1
                degree(i2)=degree(i2)+1
             enddo
          endif
       enddo
9303   continue
       close(20)

       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on undirected networks'
          stop
       endif

       allocate(intdegree(minind:maxind))
       allocate(check_neigh(minind:maxind))
       allocate(clus_coef(minind:maxind))
       allocate(clus(minind:maxind))
       allocate(listlink(1:2*n_edges))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       allocate(avclusbin(1:n_bins))
       allocate(interv(0:n_bins))

!      Here we determine for each node the position of the first link
!      in the list of all links (array intdegree)

       intdegree(minind)=0

       do k=minind,maxind-1
          intdegree(k+1)=intdegree(k)+degree(k)
       enddo

!      Here we compile the list of all links (array listlink)

       degree=0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             do j=1,n_edges
                read(20,*)i1,i2
                degree(i1)=degree(i1)+1
                degree(i2)=degree(i2)+1
                listlink(intdegree(i1)+degree(i1))=i2
                listlink(intdegree(i2)+degree(i2))=i1
             enddo
             exit
          endif
       enddo
       close(20)

!      Here we calculate the clustering coefficients (array clus_coef) and their average
!      over the nodes for which the clustering coefficient is non-zero: the sequence
!      of the clustering coefficients is written out in the file 'clus_coeff_sequence.dat'

       clus_coef=0
       check_neigh=0
       Avcluscoeff=0.0d0
       nCC=0
       minclus=1.0d0
       maxclus=0.0d0
       clus=0.0d0
       ntot_triangles=0
       ncluszero=0

       do k=minind,maxind
          if(degree(k)>1)then
             do j=1,degree(k)
                check_neigh(listlink(intdegree(k)+j))=1
             enddo
             norm=real((degree(k)*(degree(k)-1)))
             do j=1,degree(k)
                do l=1,degree(listlink(intdegree(k)+j))
                   clus_coef(k)=clus_coef(k)+check_neigh(listlink(intdegree(listlink(intdegree(k)+j))+l))
                enddo
             enddo
             do j=1,degree(k)
                check_neigh(listlink(intdegree(k)+j))=0
             enddo
             ntot_triangles=ntot_triangles+clus_coef(k)
             clus(k)=clus_coef(k)/norm
             if(clus(k)>maxclus)maxclus=clus(k)
             if(clus_coef(k)>0)then
                if(clus(k)<minclus)minclus=clus(k)
             else
                ncluszero=ncluszero+1
             endif
             Avcluscoeff=Avcluscoeff+clus(k)
             nCC=nCC+1
          endif
       enddo

       mindeg=1000000
       maxdeg=0

       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             if(degree(i)>maxdeg)maxdeg=degree(i)
             if(degree(i)<mindeg)mindeg=degree(i)
          endif
       enddo

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
       avclusbin=0.0d0
       np=0
       
       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             do j=1,n_bins
                if(real(degree(i))<interv(j))then
                   np(j)=np(j)+1
                   avdegbin(j)=avdegbin(j)+real(degree(i))
                   avclusbin(j)=avclusbin(j)+clus(i)
                   exit
                endif
             enddo
          endif
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avclusbin(i)=avclusbin(i)/np(i)
             avdegbin(i)=avdegbin(i)/np(i)
          endif
       enddo
       
       open(20,file=fileout5,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Degree |   Clustering coefficient'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avdegbin(i),avclusbin(i)
          endif
       enddo
       close(20)

101    format(i10,13x,e15.6)
103    format(a8,i10)
104    format(8x,e15.6,6x,e15.6)
105    format(a40,e15.6)
106    format(a256)

9001   continue

       stop
     end program clustering_vs_k
