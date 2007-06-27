      program random_graph
!   
!     It builds a random graph a la Erdoes-Renyi: the probability
!     that there is an edge between any pair of vertices is p
!
!   * degree is the array which stores the degree of each node,
!     which is printed out at the end in the file "degree.dat"


      implicit none
      integer*8 ibm
      integer i,j,k,n_edges,iter,n_vert,n_edges_true,denom,denom0,k0
      integer, allocatable,dimension(:)::ind,indc,degree,label,listlink,intdegree
      real*8 r,p
      character*60 sn_vert,sp,sibm
      character*25,allocatable,dimension(:):: attrN

!     Reading of input parameters 
      
      call GETARG(2,sn_vert)
      call GETARG(4,sp)
      call GETARG(6,sibm)

      read(sn_vert,*)n_vert
      read(sp,*)p
      read(sibm,*)ibm

!     Array allocations

!     Opening of the file where the edges will be saved

      allocate(attrN(1:n_vert))

      denom=1000000000
      
      do i=1,n_vert
         denom0=denom
         k=i
         do
            if((k/denom0)/=0)exit
            denom0=denom0/10
         enddo
         attrN(i)(1:1)='"'
         j=2
         do
            attrN(i)(j:j)=char(48+k/denom0)
            k0=k-(k/denom0)*denom0
            k=k0
            denom0=denom0/10
            j=j+1
            if(denom0==0)exit
         enddo
         attrN(i)(j:j)='"'
      enddo

      open(21,file='network.nwb',status='unknown')
      write(21,108)'# Erdoes-Renyi graph'
      write(21,110)'# Linking probability ',p
      write(21,103)'*Nodes ',n_vert
      write(21,120)'id*int      label*string'
      do i=1,n_vert
         write(21,*)i,attrN(i)
      enddo
      write(21,109)'*UndirectedEdges'
      write(21,121)'source*int      target*int'

!     The average degree of the graph is given by the product of the
!     linking probability p and the number of nodes n_vert

      n_edges=(ceiling(n_vert*p)*(n_vert-1))/2
      n_edges_true=0

      allocate(ind(1:n_edges))
      allocate(indc(1:n_edges))
      allocate(degree(1:n_vert))
      allocate(label(1:n_vert))
      allocate(intdegree(1:n_vert))

      degree=0
      label=0

!     Here we build the graph

      do iter=1,n_edges
         ibm=ibm*16807                                       !  Here we create a random number
         r=0.25d0*(ibm/2147483648.0d0/2147483648.0d0+2.0d0)  !  r between 0 and 1
         i=ceiling(r*n_vert)
         ibm=ibm*16807                                       !  Here we create a random number
         r=0.25d0*(ibm/2147483648.0d0/2147483648.0d0+2.0d0)  !  r between 0 and 1
         j=ceiling(r*n_vert)

!     If the two randomly chosen vertices coincide, we disregard the pair

         if(i/=j)then
            n_edges_true=n_edges_true+1
            ind(n_edges_true)=j
            indc(n_edges_true)=i
            degree(i)=degree(i)+1
            degree(j)=degree(j)+1
         endif
      enddo

      allocate(listlink(1:2*n_edges_true))
      
      intdegree(1)=0
      
      do k=1,n_vert-1
         intdegree(k+1)=intdegree(k)+degree(k)
      enddo

      degree=0

      do i=1,n_edges_true
         degree(indc(i))=degree(indc(i))+1
         degree(ind(i))=degree(ind(i))+1
         listlink(intdegree(indc(i))+degree(indc(i)))=ind(i)
         listlink(intdegree(ind(i))+degree(ind(i)))=indc(i)
      enddo
      
      do i=1,n_vert
         do j=1,degree(i)
            label(listlink(intdegree(i)+j))=label(listlink(intdegree(i)+j))+1
            if(label(listlink(intdegree(i)+j))==1.AND.i<listlink(intdegree(i)+j))then
               write(21,*)i,listlink(intdegree(i)+j)
            endif
         enddo
         do j=1,degree(i)
            label(listlink(intdegree(i)+j))=0
         enddo
      enddo

      close(21)
         
101   format(i10,8x,i10)
102   format(a34,i10)
103   format(a6,i10)
104   format(i10,9x,e15.6)
105   format(4x,e15.6,4x,e15.6)
108   format(a21)
109   format(a16)
110   format(a22,e15.6)
120   format(a24)
121   format(a26)

      stop
    end program random_graph
