      program Diameter_netw

!
!     It performs Breadth_First_Search calculating the shortest path between 
!     each pair of nodes. The diameter of the network is calculated.
!

      implicit none
      integer, allocatable,dimension(:)::itnei,degree,indvic,distance,ind,ind0,nodes
      integer, allocatable,dimension(:)::itnei1,indc
      real*8, allocatable,dimension(:)::pathsize
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,nneigh,nneigh1,dist,diameter,n_vert,n_edges
      integer i1,i2,maxind,minind,nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
      real*8 avshpath,n_paths
      character*256 filename,fileout1,fileout2
      character*25 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
      character*25,allocatable,dimension(:,:):: attrN,attrE

      call GETARG(2,filename)

      maxind=1
      minind=1000000
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
      
      allocate(pathsize(1:n_vert))
      allocate(itnei(1:n_vert))
      allocate(itnei1(1:n_vert))
      allocate(indvic(minind:maxind))
      allocate(distance(minind:maxind))
      allocate(indc(1:2*n_edges))

      indvic(minind)=0
      diameter=1
      pathsize=0.0d0

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
                     pathsize(dist)=pathsize(dist)+0.00001d0
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

      write(*,*)'**************************************************************************'
      write(*,126)'The diameter of the network is ',diameter
      write(*,*)'**************************************************************************'

126   format(a31,i10)
106   format(a25)

      stop
      
    end program Diameter_netw
