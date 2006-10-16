       program clustering

  
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
       integer, allocatable, dimension (:) :: clusdis,clusdis_bin
       real*8, allocatable, dimension (:) :: clus,interv,avclusbin,avdegbin
       real*8 norm,Avcluscoeff,minclus,maxclus,abin,binsize,zero
       character*256 filename,fileout,fileout1,fileout2,fileout3,fileout4,fileout5,sn_bins,str1,str2
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins

       fileout1='clus_coeff_sequence.dat'
       fileout2='Avcluscoeff.dat'
       fileout3='clus_coeff_distr.dat'
       fileout4='clus_coeff_distr_binned.dat'
       fileout5='clus_coeff_vs_degree_binned.dat'
       
!      Here the arrays are allocated

       n_edges=0
       maxind=1
       minind=1000000
       zero=0.0d0

       open(20,file=filename,status='unknown')
       do 
          read(20,106)str1
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

       if(minind/=1)then
          write(*,*)'The minimal node index is not 1'
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
       if(minind/=1)then
          write(*,*)'The minimal node index is not 1'
          stop
       endif

       allocate(intdegree(1:n_vert))
       allocate(check_neigh(1:n_vert))
       allocate(clus_coef(1:n_vert))
       allocate(clus(1:n_vert))
       allocate(listlink(1:2*n_edges))
       allocate(clusdis(0:n_bins))
       allocate(np(1:n_bins))
       allocate(avclusbin(1:n_bins))
       allocate(avdegbin(1:n_bins))
       allocate(interv(0:n_bins))

!      Here we determine for each node the position of the first link
!      in the list of all links (array intdegree)

       intdegree(1)=0

       do k=1,n_vert-1
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

       open(20,file=fileout1,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Node     |   Clustering coefficient'
       write(20,*)

       do k=1,n_vert
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
             write(20,101)k,clus_coef(k)/norm
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

       close(20)

!      Here we write out the average clustering coefficient

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,105)'# The average clustering coefficient is ',Avcluscoeff/nCC 
       close(20)

101    format(i10,13x,e15.6)
103    format(a8,i10)
104    format(8x,e15.6,6x,e15.6)
105    format(a40,e15.6)
106    format(a256)

       if(ntot_triangles==0)then
          open(20,file='Message_on_vanishing_clustering.txt',status='unknown')
          write(20,*)'Average clustering coefficient is zero: the network is a tree!'
          close(20)
          goto 9001
       endif
       if(abs(minclus-maxclus)<0.0000000001d0)then
          open(20,file='Message_on_single_clustering_coefficient.txt',status='unknown')
          write(20,*)'The clustering coefficient is the same for all nodes,'
          write(20,*)'except possibly for those nodes with zero clustering'
          write(20,*)'The fraction of nodes with zero clustering is ',real(ncluszero)/n_vert
          write(20,*)'The remaining fraction ', real(n_vert-ncluszero)/n_vert, ' of nodes &
               & has clustering coefficient ',minclus
          close(20)
          goto 9001
       endif

!      Here we calculate the distribution of clustering with equal bins

       clusdis=0

       abin=(maxclus-minclus+0.0002d0*minclus)/n_bins
       
       do i=1,n_vert
          clusdis(ceiling((clus(i)-minclus+0.0001d0*minclus)/abin))=clusdis(ceiling((clus(i)-minclus+0.0001d0*minclus)/abin))+1
       enddo
       open(20,file=fileout3,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Clustering coefficient |   Probability'
       write(20,*)
       if(ncluszero>0)then
          write(20,104)zero,real(ncluszero)/n_vert
       endif
       do i=1,n_bins
          write(20,104)minclus+(i-1)*abin+abin/2,real(clusdis(i))/n_vert
       enddo
       close(20)

!      Here we calculate the distribution of clustering with logarithmic bins


       if(real(minclus)/real(maxclus)>0.1d0)then
          open(20,file='Message_on_binning_clustering_coefficient.txt',status='unknown')
          write(20,*)'Clustering coefficient varies too little: the logarithmic binning is not useful'
          close(20)
          goto 9001
       endif

       interv(0)=minclus-0.0001d0*minclus
       abin=(log(maxclus+0.0001d0*minclus)-log(interv(0)))/n_bins
       
       do i=1,n_bins
          interv(i)=exp(log(interv(i-1))+abin)
       enddo
       
       avclusbin=0.0d0
       np=0
       
       do i=1,n_vert
          do j=1,n_bins
             if(clus(i)<interv(j))then
                np(j)=np(j)+1
                avclusbin(j)=avclusbin(j)+clus(i)
                exit
             endif
          enddo
       enddo
       
       do i=1,n_bins
          if(np(i)>0)then
             avclusbin(i)=avclusbin(i)/np(i)
          endif
       enddo
       
       open(20,file=fileout4,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#   Clustering coefficient |   Probability'
       write(20,*)
       if(ncluszero>0)then
          write(20,104)zero,real(ncluszero)/n_vert
       endif
       do i=1,n_bins
          if(np(i)>0)then
             write(20,104)avclusbin(i),real(np(i))/(n_vert*(interv(i)-interv(i-1)))
          endif
       enddo
       close(20)
       mindeg=MINVAL(degree)
       maxdeg=MAXVAL(degree)

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
       
       do i=1,n_vert
          do j=1,n_bins
             if(real(degree(i))<interv(j))then
                np(j)=np(j)+1
                avdegbin(j)=avdegbin(j)+real(degree(i))
                avclusbin(j)=avclusbin(j)+clus(i)
                exit
             endif
          enddo
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

9001   continue

       stop
     end program clustering
