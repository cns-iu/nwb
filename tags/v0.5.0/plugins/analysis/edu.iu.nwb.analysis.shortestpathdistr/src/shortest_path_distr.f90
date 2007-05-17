      program shortest_path_distr

!
!     It performs Breadth_First_Search calculating the shortest path between 
!     each pair of nodes. The distribution of shortest path lengths is calculated.
!

      implicit none
      integer, allocatable,dimension(:)::itnei,degree,indvic,distance
      integer, allocatable,dimension(:)::itnei1,indc,pathsize
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,nneigh,nneigh1,dist,diameter,n_paths,n_vert,n_edges
      integer i1,i2,maxind,minind
      real*8 avshpath
      character*256 filename,fileout1,fileout2,str1,str2

      call GETARG(2,filename)

      maxind=1
      minind=1000000
      n_edges=0
      fileout2='shortest_path_histo.dat'

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
      
      allocate(pathsize(1:n_vert))
      allocate(itnei(1:n_vert))
      allocate(itnei1(1:n_vert))
      allocate(indvic(minind:maxind))
      allocate(distance(minind:maxind))
      allocate(indc(1:2*n_edges))

      indvic(minind)=0
      diameter=1
      pathsize=0

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
            distance=n_vert
            dist=1
            distance(i)=0
            nneigh=1
            itnei(nneigh)=i
1001        continue
            nneigh1=0
            do k=1,nneigh
               do j=1,degree(itnei(k))
                  if(distance(indc(indvic(itnei(k))+j))>dist)then
                     nneigh1=nneigh1+1
                     itnei1(nneigh1)=indc(indvic(itnei(k))+j)
                     distance(itnei1(nneigh1))=dist
                     pathsize(dist)=pathsize(dist)+1
                  endif
               enddo
            enddo
            if(nneigh1==0)then
               if(dist-1>diameter)diameter=dist-1
               goto 101
            endif
            dist=dist+1
            nneigh=nneigh1
            do k=1,nneigh
               itnei(k)=itnei1(k)
            enddo
            goto 1001
101         continue
         endif
      enddo

      n_paths=0

      do i=1,diameter
         n_paths=n_paths+pathsize(i)
      enddo
      
      open(20,file=fileout2,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'# Path length   |  Probability'
      write(20,*)
      do i=1,diameter
         write(20,128)i,real(pathsize(i))/n_paths
      enddo

      close(20)

103   format(a8,i10)
128   format(i10,e15.6)
106   format(a256)

      stop
      
    end program shortest_path_distr
