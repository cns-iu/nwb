      program small_world
!   
!     It builds a small world graph a la Watts-Strogatz: 
!     the rewiring probability is p
!
!   * degree is the array which stores the degree of each node,
!     which is printed out at the end in the file "degree.dat"


      implicit none
      integer*8 ibm
      integer i,j,k,vic,newvic,n_vert,k_nei,denom,denom0,k0
      integer, allocatable,dimension(:)::linklist
      integer, allocatable,dimension(:)::check_vic_in,check_vic_out
      real*8 r,p
      character*60 sn_vert,sk_nei,sp,sibm
      character*25,allocatable,dimension(:):: attrN
      
      character*(25) addquotes

!     Reading of input parameters 
      
      call GETARG(2,sn_vert)
      call GETARG(4,sk_nei)
      call GETARG(6,sp)
      call GETARG(8,sibm)

      read(sn_vert,*)n_vert
      read(sk_nei,*)k_nei
      read(sp,*)p
      read(sibm,*)ibm

!     Array allocations

      allocate(linklist(1:n_vert*k_nei))
      allocate(check_vic_in(1:n_vert))
      allocate(check_vic_out(1:n_vert))
      allocate(attrN(1:n_vert))

      denom=1000000000
      
      do i=1,n_vert
         write(attrN(i),*) i
         attrN(i) = addquotes(attrN(i))
      enddo

!     Opening of the file where the edges will be saved

      open(21,file='network.nwb',status='unknown')
      write(21,108)'# Small world network'
      write(21,102)'# Initial neighbors of each node ',k_nei
      write(21,110)'# Rewiring probability ',p
      write(21,103)'*Nodes ',n_vert
      write(21,120)'id*int      label*string'
      do i=1,n_vert
         write(21,*)i,attrN(i)
      enddo
      write(21,109)'*UndirectedEdges'
      write(21,121)'source*int      target*int'

!     Initialization of list of neighbors before rewiring

      do i=1,n_vert
         do j=1,k_nei
            vic=i+j
            if(vic>n_vert)vic=vic-n_vert
            linklist((i-1)*k_nei+j)=vic
         enddo
      enddo
      
      check_vic_in=0
      check_vic_out=0

!     Here we make the rewiring of the edges

      do j=1,k_nei
         do i=1,n_vert
            ibm=ibm*16807                                       !  Here we create a random number
            r=0.25d0*(ibm/2147483648.0d0/2147483648.0d0+2.0d0)  !  r between 0 and 1
            if(r<p)then
               do k=1,k_nei
                  check_vic_in(linklist((i-1)*k_nei+k))=1
               enddo
               check_vic_in(i)=1
               ibm=ibm*16807                                      
               r=0.25d0*(ibm/2147483648.0d0/2147483648.0d0+2.0d0)  
               newvic=ceiling(r*n_vert)
               if(check_vic_in(newvic)==0)then
                  do k=1,k_nei
                     check_vic_out(linklist((newvic-1)*k_nei+k))=1
                  enddo
                  if(check_vic_out(i)==0)then
                     linklist((i-1)*k_nei+j)=newvic
                  endif
                  do k=1,k_nei
                     check_vic_out(linklist((newvic-1)*k_nei+k))=0
                  enddo
               endif
               do k=1,k_nei
                  check_vic_in(linklist((i-1)*k_nei+k))=0
               enddo
               check_vic_in(i)=0
            endif
         enddo
      enddo

      do i=1,n_vert
         do j=1,k_nei
            write(21,*)i,linklist((i-1)*k_nei+j)
         enddo
      enddo

      close(21)

101   format(i10,8x,i10)
102   format(a34,i10)
103   format(a6,i10)
104   format(i10,9x,e15.6)
105   format(4x,e15.6,4x,e15.6)
108   format(a22)
109   format(a16)
110   format(a24,e15.6)
120   format(a24)
121   format(a26)

9001  continue

      stop
    end program small_world
    
    
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