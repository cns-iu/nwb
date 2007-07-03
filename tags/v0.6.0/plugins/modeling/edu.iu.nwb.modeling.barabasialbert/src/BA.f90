      program BA
!   
!
!     It builds a network based on linear preferential attachment (Barabasi-Albert)
!
!   * degree is the array which stores the total degree of each node,
!     which is printed out at the end in the file "degree.dat"
!   * list_edges is the array which stores the labels of the vertices 
!     as many times as they have neighbors: it speeds up the part of the algorithm where
!     the new vertex has to choose which of the other vertices it has to be attached to.
!   * loc_edges stores the labels of the pre-existing vertices which 
!     get linked to a new vertex 
!   * label and list_newedges are needed to avoid multiple edges between a pair of nodes
!   * degdis is proportional to the probability distribution of degree
!   * degdis_binned is the probability distribution of degree but on degree bins: the bins increase logarithmically in size  
!   * interv indicates the extremes of the degree bins

      implicit none
      integer*8 ibm
      integer i,j,k,n_edges,index,m,n_vert,m0,denom,denom0,k0
      integer, allocatable,dimension(:)::degree,list_edges,loc_edges
      integer, allocatable,dimension(:)::list_newedges
      logical, allocatable,dimension(:)::label
      real*8 r, binsize
      character*60 sn_vert,sm0,sibm
      character*25,allocatable,dimension(:):: attrN
      
      character*(25) addquotes

      call GETARG(2,sn_vert)
      call GETARG(4,sm0)
      call GETARG(6,sibm)

      read(sn_vert,*)n_vert
      read(sm0,*)m0
      read(sibm,*)ibm

!     Reading of input parameters 

      m=m0+1

!     Array allocations

      allocate(degree(1:n_vert))
      allocate(label(1:n_vert))
      allocate(list_edges(1:2*m0*n_vert))
      allocate(list_newedges(1:m0))
      allocate(loc_edges(1:m0))
      allocate(attrN(1:n_vert))

      denom=1000000000
      
      do i=1,n_vert
         write(attrN(i),*) i
         attrN(i) = addquotes(attrN(i))
      enddo


!     Opening of the file where the edges will be saved

      open(21,file='network.nwb',status='unknown')
      write(21,108)'# Barabasi-Albert network'
      write(21,102)'# Links created by each node ',m0
      write(21,103)'*Nodes ',n_vert
      write(21,110)'id*int      label*string'
      do i=1,n_vert
         write(21,*)i,attrN(i)
      enddo
      write(21,109)'*UndirectedEdges'
      write(21,111)'source*int      target*int'

!     Initialization of the number of edges and the total degree of all nodes
!     The entries of the array "label" are initialized to .false.

      n_edges=0
      degree=0
      label=.false.

!     ibm is the seed of the multiplicative random number generator used
!     in this program 

!     Here the network building starts with the formation of a clique of m vertices

      do i=1,m
         do j=i+1,m
            degree(j)=degree(j)+1
            degree(i)=degree(i)+1
            n_edges=n_edges+1
            list_edges(n_edges)=i
            n_edges=n_edges+1
            list_edges(n_edges)=j
            write(21,*)i,j
         enddo
      enddo

!     Here we deal with the special case in which the user inputs m0=1 (m=0) or m0=2 (m=1)
!     In these cases the initial clique consists of a single node with degree 1.
!     Since there are no other vertices this is not actually correct,
!     but it serves to cure the disease of zero-degree vertices 

      if(m<=1)then
         n_edges=1
         list_edges(n_edges)=1
         m=1
      endif

!     Here the real evolution of the network takes place according to
!     linear preferential attachment. The practical implementation consists 
!     in choosing at random one element of the array list_edges for each new edge we
!     want to set between the last added vertex and the rest of the network.

      do i=1,n_vert-m
         index=0
         do j=1,m0
            ibm=ibm*16807                                       !  Here we create a random number
            r=0.25d0*(ibm/2147483648.0d0/2147483648.0d0+2.0d0)  !  r between 0 and 1
            loc_edges(j)=list_edges(ceiling(n_edges*r))
            if(label(loc_edges(j)).eqv..false.)then
               write(21,*)i+m,loc_edges(j)
               n_edges=n_edges+1
               list_edges(n_edges)=loc_edges(j)
               index=index+1
               list_newedges(index)=loc_edges(j)
               label(loc_edges(j))=.true.
               degree(loc_edges(j))=degree(loc_edges(j))+1
            endif
         enddo
         do j=1,index
            n_edges=n_edges+1
            list_edges(n_edges)=i+m
            label(list_newedges(j))=.false.
         enddo
         degree(i+m)=degree(i+m)+index
      enddo
      close(21)

9001  continue

101   format(i10,8x,i10)
102   format(a29,i10)
103   format(a6,i10)
104   format(i10,9x,e15.6)
105   format(4x,e15.6,4x,e15.6)
108   format(a26)
109   format(a16)
110   format(a24)
111   format(a26)

      stop
    end program BA

    
    character*(25) function addquotes(value)
		character*(*) value
		character*22 longname
		
		if(len(TRIM(ADJUSTL(value))) >= 23) then
			longname = ADJUSTL(value)
			addquotes = '"' // longname // '"'
		else
			addquotes = '"' // TRIM(ADJUSTL(value)) // '"'
		endif
		return
	end