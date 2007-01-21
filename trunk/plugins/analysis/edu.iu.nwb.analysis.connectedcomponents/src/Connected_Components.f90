      program Connected_Components

!
!     It identifies the connected components of a graph     
!

      implicit none
      integer, allocatable,dimension(:)::itnei,degree,indvic,cluster,itnei1,indc,clsize
      logical, allocatable,dimension(:)::liste
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,ncluster,nneigh,nneigh1,n_vert,n_edges
      integer i1,i2,maxind,minind
      character*256 filename,fileout,fileout2,str1,str2

      call GETARG(2,filename)

      fileout='cluster.dat'
      fileout2='cluster_size.dat'

      maxind=1
      minind=10000000
      n_edges=0

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
8103  continue
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
9103  continue
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
9203  continue
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
9303  continue
      close(20)

      if(n_edges==0)then
         write(*,*)'Error! The program should be applied on undirected networks'
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

      open(20,file=filename,status='unknown')
      do 
         read(20,106)str1
         if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
            do j=1,n_edges
               read(20,*)i1,i2
               degree(i1)=degree(i1)+1
               degree(i2)=degree(i2)+1
               indc(indvic(i1)+degree(i1))=i2
               indc(indvic(i2)+degree(i2))=i1
            enddo
            exit
         endif
      enddo
      close(20)

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
106   format(a256)
      

      end program Connected_Components
