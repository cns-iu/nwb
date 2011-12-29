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
      real*8 r,p
      character*60 sn_vert,sp,sibm
      character*25,allocatable,dimension(:):: attrN
      
      character*(25) addquotes

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
         write(attrN(i),*) i
         attrN(i) = addquotes(attrN(i))
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

      do i=1,n_vert
         do j=1,i-1
            ibm=ibm*16807                                       !  Here we create a random number
            r=0.25d0*(ibm/2147483648.0d0/2147483648.0d0+2.0d0)  !  r between 0 and 1
            if(r<p)then
               write(21,*)i,j
            endif
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
