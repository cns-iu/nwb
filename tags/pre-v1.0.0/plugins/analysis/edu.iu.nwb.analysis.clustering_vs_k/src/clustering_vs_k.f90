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
       integer maxind,minind,i1,i2,ncluszero,nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
       integer, allocatable, dimension (:) :: degree,intdegree,clus_coef,np,ind,indc,nodes
       integer, allocatable, dimension (:) :: check_neigh,listlink
       real*8, allocatable, dimension (:) :: clus,interv,avclusbin,avdegbin
       logical, allocatable, dimension(:):: nodelist
       real*8 norm,Avcluscoeff,minclus,maxclus,abin,binsize,zero
       character*256 filename,fileout5,sn_bins
       character*25 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
       character*25,allocatable,dimension(:,:):: attrN,attrE
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins

       fileout5='clus_coeff_vs_degree_binned.dat'
       
!      Here the arrays are allocated

       n_edges=0
       n_vert0=0
       n_vert1=0
       maxind=1
       minind=10000000
       zero=0.0d0
       ch=0
       n_edges_N=0
       n_edges_E=0
       nattrN=0

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8103,end=8103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             read(20,*)str3
             if(str3(1:1)=='*')then
                ch=1
                goto 8103
             endif
             read(20,*)str2
             backspace(20)
             backspace(20)
             do k=1,20
                read(20,*)(str(j),j=1,k)
                if(str(k)(1:1)==str2(1:1))exit
                backspace(20)
             enddo
             backspace(20)
             nattrN=k-1
             do 
                read(20,*,err=8103,end=8103)str2
                if(str2(1:1)=='*')goto 8103
                n_edges_N=n_edges_N+1  
             enddo
          else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
             read(20,*)
             read(20,*)str2
             backspace(20)
             backspace(20)
             do k=1,20
                read(20,*)(str(j),j=1,k)
                if(str(k)(1:1)==str2(1:1))exit
                backspace(20)
             enddo
             backspace(20)
             nattrE=k-1
             do 
                read(20,*,err=9103,end=9103)
                n_edges_E=n_edges_E+1   
             enddo
          endif
       enddo
8103   continue
       backspace(20)
       do
          read(20,106,err=9103,end=9103)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             read(20,*)
             read(20,*)str2
             backspace(20)
             backspace(20)
             do k=1,20
                read(20,*)(str(j),j=1,k)
                if(str(k)(1:1)==str2(1:1))exit
                backspace(20)
             enddo
             backspace(20)
             nattrE=k-1
             do 
                read(20,*,err=9103,end=9103)
                n_edges_E=n_edges_E+1   
             enddo
          endif
       enddo
9103   continue
       close(20)
       allocate(nodes(1:n_edges_N))
       allocate(ind(1:n_edges_E))
       allocate(indc(1:n_edges_E))
       allocate(attrE(1:nattrE-2,1:n_edges_E))
       if(nattrN>1)then
          allocate(attrN(1:nattrN-1,1:n_edges_N))
       endif

       open(20,file=filename,status='unknown')
       do 
          read(20,106,err=8104,end=8104)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='N')then
             if(ch==1)then
                read(20,*)
                goto 8104
             endif
             read(20,*)(headattrN(i),i=1,nattrN)
             do k=1,n_edges_N
                read(20,*,err=8114,end=8114)nodes(n_vert0+1),(attrN(j,n_vert0+1),j=1,nattrN-1)
                n_vert0=n_vert0+1
                if(minind>nodes(n_vert0))minind=nodes(n_vert0)
                if(maxind<nodes(n_vert0))maxind=nodes(n_vert0)  
8114            continue
             enddo
             goto 8104
          else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
             read(20,*)(headattrE(i),i=1,nattrE)
             do k=1,n_edges_E
                read(20,*,err=9114,end=9114)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
                n_edges=n_edges+1
                if(minind>indc(n_edges))minind=indc(n_edges)
                if(minind>ind(n_edges))minind=ind(n_edges)
                if(maxind<ind(n_edges))maxind=ind(n_edges)
                if(maxind<indc(n_edges))maxind=indc(n_edges)
9114            continue
             enddo
             goto 9104
          endif
       enddo
8104   continue
       backspace(20)
       do
          read(20,106,err=9104,end=9104)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
             read(20,*)(headattrE(i),i=1,nattrE)
             do k=1,n_edges_E
                read(20,*,err=9214,end=9214)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
                n_edges=n_edges+1
                if(minind>indc(n_edges))minind=indc(n_edges)
                if(minind>ind(n_edges))minind=ind(n_edges)
                if(maxind<ind(n_edges))maxind=ind(n_edges)
                if(maxind<indc(n_edges))maxind=indc(n_edges)
9214            continue
             enddo
          endif
       enddo
9104   continue
       close(20)

       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on undirected networks'
          stop
       endif
       
       allocate(nodelist(minind:maxind))
       allocate(degree(minind:maxind))
       degree=0
       nodelist=.false.
       do i=1,n_vert0
          nodelist(nodes(i))=.true.
       enddo
       n_vert=n_vert0
       do i=1,n_edges
          if(nodelist(ind(i)).eqv..false.)then
             nodelist(ind(i))=.true.
             n_vert=n_vert+1
          endif
          if(nodelist(indc(i)).eqv..false.)then
             nodelist(indc(i))=.true.
             n_vert=n_vert+1
          endif
          degree(indc(i))=degree(indc(i))+1
          degree(ind(i))=degree(ind(i))+1
       enddo
       if(n_vert0<n_vert)then
          print*,'The nwb file is not properly formatted: not all nodes/labels are listed'
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

       do j=1,n_edges
          degree(indc(j))=degree(indc(j))+1
          degree(ind(j))=degree(ind(j))+1
          listlink(intdegree(indc(j))+degree(indc(j)))=ind(j)
          listlink(intdegree(ind(j))+degree(ind(j)))=indc(j)
       enddo

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
106    format(a25)

9001   continue

       stop
     end program clustering_vs_k
