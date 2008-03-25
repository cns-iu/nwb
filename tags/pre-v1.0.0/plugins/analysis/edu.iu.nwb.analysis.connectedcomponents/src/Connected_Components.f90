      program Connected_Components

!
!     It identifies the connected components of a graph     
!

      implicit none
      integer, allocatable,dimension(:)::itnei,degree,indvic,cluster,itnei1,indc,clsize,ind,ind0,nodes
      logical, allocatable,dimension(:)::liste
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,ncluster,nneigh,nneigh1,n_vert,n_edges
      integer maxind,minind,i1,i2,ncluszero,nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
      character*25 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
      character*25,allocatable,dimension(:,:):: attrN,attrE
      character*256 filename,fileout,fileout2

      call GETARG(2,filename)

      fileout='cluster.dat'
      fileout2='cluster_size.dat'

      maxind=1
      minind=10000000
      n_edges=0
      n_vert0=0
      n_vert1=0
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
8103  continue
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
9103  continue
      close(20)
      allocate(nodes(1:n_edges_N))
      allocate(ind(1:n_edges_E))
      allocate(ind0(1:n_edges_E))
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
8114           continue
            enddo
            goto 8104
         else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
            read(20,*)(headattrE(i),i=1,nattrE)
            do k=1,n_edges_E
               read(20,*,err=9114,end=9114)ind0(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
               n_edges=n_edges+1
               if(minind>ind0(n_edges))minind=ind0(n_edges)
               if(minind>ind(n_edges))minind=ind(n_edges)
               if(maxind<ind(n_edges))maxind=ind(n_edges)
               if(maxind<ind0(n_edges))maxind=ind0(n_edges)
9114           continue
            enddo
            goto 9104
         endif
      enddo
8104  continue
      backspace(20)
      do
         read(20,106,err=9104,end=9104)str1
         if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
            read(20,*)(headattrE(i),i=1,nattrE)
            do k=1,n_edges_E
               read(20,*,err=9214,end=9214)ind0(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
               n_edges=n_edges+1
               if(minind>ind0(n_edges))minind=ind0(n_edges)
               if(minind>ind(n_edges))minind=ind(n_edges)
               if(maxind<ind(n_edges))maxind=ind(n_edges)
               if(maxind<ind0(n_edges))maxind=ind0(n_edges)
9214           continue
            enddo
         endif
      enddo
9104  continue
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
         if(nodelist(ind0(i)).eqv..false.)then
            nodelist(ind0(i))=.true.
            n_vert=n_vert+1
         endif
         degree(ind0(i))=degree(ind0(i))+1
         degree(ind(i))=degree(ind(i))+1
      enddo
      if(n_vert0<n_vert)then
         print*,'The nwb file is not properly formatted: not all nodes/labels are listed'
         stop
      endif

      allocate(liste(minind:maxind))
      allocate(itnei(1:n_vert))
      allocate(itnei1(1:n_vert))
      allocate(indvic(minind:maxind))
      allocate(cluster(minind:maxind))
      allocate(indc(1:2*n_edges))

      liste=.false.
      ncluster=0
      indvic(minind)=0

      do k=minind,maxind-1
         indvic(k+1)=indvic(k)+degree(k)
      enddo

      degree=0

      do j=1,n_edges
         degree(ind0(j))=degree(ind0(j))+1
         degree(ind(j))=degree(ind(j))+1
         indc(indvic(ind0(j))+degree(ind0(j)))=ind(j)
         indc(indvic(ind(j))+degree(ind(j)))=ind0(j)
      enddo

      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            if(liste(i).eqv..false.)then
               liste(i)=.true.
               ncluster=ncluster+1
               cluster(i)=ncluster
               nneigh=1
               itnei(nneigh)=i
1001           continue
               nneigh1=0
               do k=1,nneigh
                  do j=1,degree(itnei(k))
                     if(liste(indc(indvic(itnei(k))+j)).eqv..false.)then
                        nneigh1=nneigh1+1
                        itnei1(nneigh1)=indc(indvic(itnei(k))+j)
                        cluster(itnei1(nneigh1))=ncluster
                        liste(itnei1(nneigh1))=.true.
                     endif
                  enddo
               enddo
               if(nneigh1==0)goto 101
               nneigh=0
               do k=1,nneigh1
                  do j=1,degree(itnei1(k))
                     if(liste(indc(indvic(itnei1(k))+j)).eqv..false.)then
                        nneigh=nneigh+1
                        itnei(nneigh)=indc(indvic(itnei1(k))+j)
                        cluster(itnei(nneigh))=ncluster
                        liste(itnei(nneigh))=.true.
                     endif
                  enddo
               enddo
               if(nneigh==0)goto 101
               goto 1001
            endif
101         continue
         endif
      enddo
      open(20,file=fileout,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'# Node index   |    Cluster index'
      write(20,*)
      
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            write(20,*)i,cluster(i)
         endif
      enddo

      close(20)

      write(*,*)'**************************************************************************'
      write(*,*)'There are ',ncluster,' connected components!'
      write(*,*)'**************************************************************************'

      allocate(clsize(1:ncluster))

      clsize=0

      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            clsize(cluster(i))=clsize(cluster(i))+1
         endif
      enddo
      
      open(20,file=fileout2,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'# Cluster index   |    Cluster size'
      write(20,*)
      do i=1,ncluster
         write(20,104)i,clsize(i)
      enddo

      close(20)

103   format(a8,i10)
104   format(i10,9x,i10)
106   format(a25)
      

      end program Connected_Components
