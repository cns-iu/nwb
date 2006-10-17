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
      integer i,j,k,n_edges,index,maxdeg,mindeg,m,n_vert,m0,n_bins
      integer, allocatable,dimension(:)::degree,list_edges,loc_edges,np
      integer, allocatable,dimension(:)::degdis,list_newedges,npoints
      real*8, allocatable,dimension(:)::interv,avdegbin
      logical, allocatable,dimension(:)::label
      real*8 r, binsize
      character*60 sn_vert,sm0,sn_bins


      call GETARG(2,sn_vert)
      call GETARG(4,sm0)
      call GETARG(6,sn_bins)
      read(sn_vert,*)n_vert
      read(sm0,*)m0
      read(sn_bins,*)n_bins

!     Reading of input parameters 

      m=m0+1

!     Array allocations

      allocate(degree(1:n_vert))
      allocate(label(1:n_vert))
      allocate(list_edges(1:2*m0*n_vert))
      allocate(list_newedges(1:m0))
      allocate(loc_edges(1:m0))
      allocate(interv(0:n_bins))
      allocate(avdegbin(1:n_bins))
      allocate(npoints(1:n_bins))
      allocate(np(1:n_bins))

!     Opening of the file where the edges will be saved

      open(21,file='network.nwb',status='unknown')
      write(21,108)'// Barabasi-Albert network'
      write(21,102)'// Links created by each node ',m0
      write(21,103)'*Nodes ',n_vert
      write(21,109)'*UndirectedEdges'

!     Initialization of the number of edges and the total degree of all nodes
!     The entries of the array "label" are initialized to .false.

      n_edges=0
      degree=0
      label=.false.

!     ibm is the seed of the multiplicative random number generator used
!     in this program 

      ibm=10

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

!     Here we write out the total degree of all nodes 

      open(20,file='degree_BA.dat',status='unknown')
      write(20,*)'# Barabasi-Albert undirected network'
      write(20,102)'# Links created by each node ',m0
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#     Node     |     Degree'
      write(20,*)
      do i=1,n_vert
         write(20,101)i,degree(i)
      enddo
      close(20)
101   format(i10,8x,i10)
102   format(a29,i10)
103   format(a6,i10)
104   format(i10,9x,e15.6)
105   format(4x,e15.6,4x,e15.6)
108   format(a26)
109   format(a16)

!     Here we determine the minimum and maximum degree of the graph

      mindeg=MINVAL(degree)
      maxdeg=MAXVAL(degree)

!     Here we allocate the histogram of the degree distribution

      allocate(degdis(mindeg:maxdeg))

!     Here we initialize the histogram of the degree distribution
!     and evaluate the occurrence of each degree 

      degdis=0

      do i=1,n_vert
         degdis(degree(i))=degdis(degree(i))+1
      enddo
      
!     Here we write out the degree distribution: the occurrence of each degree is 
!     transformed in a probability by normalizing by the total degree 

      open(20,file='degree_distr_BA.dat',status='unknown')
      write(20,*)'# Barabasi-Albert undirected network'
      write(20,102)'# Links created by each node ',m0
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#     Degree    |    Probability'
      write(20,*)
      do i=mindeg,maxdeg
         if(degdis(i)>0)then
            write(20,104)i,real(degdis(i))/n_vert
         endif
      enddo
      close(20)

!     Here we calculate the binned degree distribution

      if(real(mindeg+1)/real(maxdeg)>0.1d0)then
         open(20,file='Message_on_binning_degree.txt',status='unknown')
         write(20,*)'Degree varies too little: the logarithmic binning is not useful'
         close(20)
         goto 9001
      endif

      if(mindeg==0)then
         interv(0)=real(mindeg+1)
      else
         interv(0)=real(mindeg)
      endif
      binsize=(log(real(maxdeg)+0.1d0)-log(interv(0)))/n_bins

      do i=1,n_bins
         interv(i)=exp(log(interv(i-1))+binsize)
      enddo

      npoints=0
      do i=mindeg,maxdeg
         do j=1,n_bins
            if(real(i)<interv(j))then
               npoints(j)=npoints(j)+1
               exit
            endif
         enddo
      enddo
      
      avdegbin=0.0d0
      np=0

      do i=1,n_vert
         do j=1,n_bins
            if(real(degree(i))<interv(j))then
               np(j)=np(j)+1
               avdegbin(j)=avdegbin(j)+real(degree(i))
               exit
            endif
         enddo
      enddo

      do i=1,n_bins
         if(np(i)>0)then
            avdegbin(i)=avdegbin(i)/np(i)
         endif
      enddo

      open(20,file='degree_distr_binned_BA.dat',status='unknown')
      write(20,*)'# Barabasi-Albert undirected network'
      write(20,102)'# Links created by each node ',m0
      write(20,103)'# Nodes ',n_vert
      write(20,*)'# Center of Degree bin | Probability'
      write(20,*)
      do i=1,n_bins
         if(np(i)>0)then
            write(20,105)avdegbin(i),real(np(i))/(n_vert*npoints(i))
         endif
      enddo
      close(20)

9001  continue

      stop
    end program BA
