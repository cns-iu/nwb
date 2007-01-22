      program PageRank
        
!     It calculates the PageRank of all vertices of a directed
!     network given as input as the list of its edges. The
!     calculation is performed with the power method, which consists in
!     multiplying an arbitrary (normalized) vector many times by the transition matrix 
!     of the mixed random walk. 
!           
!     prob_stat = the transition matrix of the random walk, stored only for active edges 
!     PR= PageRank at the (n-1)th iteration
!     PR_it = PageRank at the n-th iteration
!     out_deg= outdegree of the vertices
!     q is actually 1-d where d is the original damping factor introduced by Brin and Page
!     eps is the precision of the calculation, it determines when the program must stop
!     We fixed eps to 10^(-5)
!     ind and indc are the extremes of each edge, meant from ind to indc.
!     PR_dangling is the contribution to the PageRank of each vertex given
!     by all vertices which have no out-degree. 

      implicit none
      integer, allocatable,dimension(:)::out_deg,ind,indc,label_degzero,np
      real*8,  allocatable,dimension(:)::PR,PR_it,interv,avPRbin
      integer, allocatable, dimension (:) :: PRdis
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,icheck,n_degzero,n_vert,n_edges,n_bins,minind,maxind,i1,i2
      real*8 eps,PR_dangling,pq,pqv,minPR,maxPR,abin,q
      character*256 filename,fileout,fileout1,fileout2,sq,sn_bins,str1,str2

!     Here the program reads the input parameters

      call GETARG(2,sn_bins)
      call GETARG(4,sq)
      call GETARG(6,filename)
      read(sn_bins,*)n_bins
      read(sq,*)q

      fileout='PageRank.dat'
      fileout1='PageRank_distr.dat'
      fileout2='PageRank_distr_binned.dat'

      eps=0.00001d0
      pq=1.0d0-q
      n_edges=0
      maxind=1
      minind=10000000

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
         else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then 
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
         if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
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
      allocate(ind(1:n_edges))         
      allocate(indc(1:n_edges))
      allocate(out_deg(minind:maxind))
      out_deg=0
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
         else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
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
               out_deg(i1)=out_deg(i1)+1
               ind(i)=i2
               indc(i)=i1
            enddo
            goto 9303
         endif
      enddo
9203  continue
      backspace(20)
      do
         read(20,106,err=9303,end=9303)str1
         if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
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
               out_deg(i1)=out_deg(i1)+1
               ind(i)=i2
               indc(i)=i1
            enddo
         endif
      enddo
9303  continue
      close(20)

      if(n_edges==0)then
         write(*,*)'Error! The program should be applied on directed networks'
         stop
      endif

      pqv=pq/n_vert

      allocate(PR_it(minind:maxind))
      allocate(PR(minind:maxind))
      allocate(PRdis(1:n_bins))
      allocate(np(1:n_bins))
      allocate(avPRbin(1:n_bins))
      allocate(interv(0:n_bins))

!     Here the matrix is read: the arrays ind and indc are defined
!     and the out-degree of each vertex is calculated
      

!     Here we initialize the PageRank of all vertices to the value 1/n_vert for the
!     first iteration of the algorithm 

      PR=1.0d0/n_vert

!     Here we store in the array label_degzero the labels of all vertices
!     with zero out-degree

      n_degzero=0
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            if(out_deg(i)==0)then
               n_degzero=n_degzero+1
            endif
         endif
      enddo

      allocate(label_degzero(1:n_degzero))

      n_degzero=0
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            if(out_deg(i)==0)then
               n_degzero=n_degzero+1
               label_degzero(n_degzero)=i
            endif
         endif
      enddo

1008  continue

!     Here we calculate the contribute to the PageRank of each vertex 
!     due to the existence of vertices with zero out-degree

      PR_dangling=0.0d0
      do j=1,n_degzero
         PR_dangling=PR_dangling+pqv*PR(label_degzero(j))
      enddo

!     Here we perform the iteration of the algorithm

      PR_it=PR_dangling+q/n_vert
      do j=1,n_edges
         PR_it(ind(j))=PR_it(ind(j))+(pq/out_deg(indc(j)))*PR(indc(j))
      enddo

!     Here we check if the new PageRank values are close enough 
!     to the values obtained after the previous iteration: 
!     the relative error must be smaller than eps 

      icheck=0
      do j=minind,maxind
         if(nodelist(j).eqv..true.)then
            if(abs((PR_it(j)-PR(j))/PR(j))>eps)then
               icheck=icheck+1
            endif
         endif
      enddo
      if(icheck>0)then
         PR=PR_it
         goto 1008
      endif

!     Here the program writes out the final PageRank of all vertices

      open(20,file=fileout,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#     Node     |     PageRank'
      write(20,*)
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            write(20,101)i,PR(i)
         endif
      enddo
      close(20)
101   format(i10,8x,e15.6)
103   format(a8,i10)
104   format(1x,e15.6,5x,e15.6)
106   format(a256)

!     Here we calculate the distribution of PageRank with equal bins
      
      minPR=MINVAL(PR)
      maxPR=MAXVAL(PR)

      PRdis=0
      abin=(maxPR-minPR+0.0002d0*minPR)/n_bins
       
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            PRdis(ceiling((PR(i)-minPR+0.0001d0*minPR)/abin))=PRdis(ceiling((PR(i)-minPR+0.0001d0*minPR)/abin))+1
         endif
      enddo
       
      open(20,file=fileout1,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#   PageRank     |     Probability'
      write(20,*)
      do i=1,n_bins
         write(20,104)minPR+(i-1)*abin+abin/2,real(PRdis(i))/n_vert
      enddo
      close(20)

!     Here we calculate the distribution of PageRank with logarithmic bins

      if(real(minPR)/real(maxPR)>0.1d0)then
         write(*,*)'PageRank varies too little: the logarithmic binning is not useful'
         goto 9001
      endif
      interv(0)=minPR-0.0001d0*minPR
      abin=(log(maxPR+0.0001d0*minPR)-log(interv(0)))/n_bins

      do i=1,n_bins
         interv(i)=exp(log(interv(i-1))+abin)
      enddo

      avPRbin=0.0d0
      np=0

      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            do j=1,n_bins
               if(PR(i)<interv(j))then
                  np(j)=np(j)+1
                  avPRbin(j)=avPRbin(j)+PR(i)
                  exit
               endif
            enddo
         endif
      enddo
      
      do i=1,n_bins
         if(np(i)>0)then
            avPRbin(i)=avPRbin(i)/np(i)
         endif
      enddo
      
      open(20,file=fileout2,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#   PageRank     |     Probability'
      write(20,*)
      do i=1,n_bins
         if(np(i)>0)then
            write(20,104)avPRbin(i),real(np(i))/(n_vert*(interv(i)-interv(i-1)))
         endif
      enddo
      close(20)
      
9001  continue

      stop
    end program PageRank
