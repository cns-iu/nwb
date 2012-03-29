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
      integer, allocatable,dimension(:)::out_deg,ind,indc,label_degzero,np,nodes
      real*8,  allocatable,dimension(:)::PR,PR_it,interv,avPRbin
      integer, allocatable, dimension (:) :: PRdis
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,icheck,n_degzero,n_vert,n_edges,n_bins,minind,maxind,i1,i2
      integer nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
      real*8 eps,PR_dangling,pq,pqv,minPR,maxPR,abin,q
      character*256 filename,fileout,fileout1,fileout2,fileout3,sq,sn_bins
      character*50 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
      character*25,allocatable,dimension(:,:):: attrN,attrE
	  
	  logical quoteit
      character*(25) addquotes
       
!     Here the program reads the input parameters

      call GETARG(2,sn_bins)
      call GETARG(4,sq)
      call GETARG(6,filename)
      read(sn_bins,*)n_bins
      read(sq,*)q

      fileout='PageRank.dat'
      fileout1='PageRank_distr.dat'
      fileout2='PageRank_distr_binned.dat'
      fileout3='network_pagerank.nwb'

      eps=0.00001d0
      pq=1.0d0-q
      n_edges=0
      maxind=1
      minind=10000000
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
         else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then 
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
         if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
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
      allocate(indc(1:n_edges_E))
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
               do i = 1, nattrN - 1, 1
                   if(quoteit(headattrN(i+1),attrN(i, n_vert0+1))) then
                       attrN(i, n_vert0+1) = addquotes(attrN(i, n_vert0+1))
                   endif
                enddo
               n_vert0=n_vert0+1
               if(minind>nodes(n_vert0))minind=nodes(n_vert0)
               if(maxind<nodes(n_vert0))maxind=nodes(n_vert0)  
8114           continue
            enddo
            goto 8104
         else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then 
            read(20,*)(headattrE(i),i=1,nattrE)
            do k=1,n_edges_E
               read(20,*,err=9114,end=9114)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
               do i = 1, nattrE -1, 1
                	if(quoteit(headattrE(i+2), attrE(i, n_edges+1))) then
                		attrE(i, n_edges+1) = addquotes(attrE(i, n_edges+1))
                	endif
                enddo
               n_edges=n_edges+1
               if(minind>indc(n_edges))minind=indc(n_edges)
               if(minind>ind(n_edges))minind=ind(n_edges)
               if(maxind<ind(n_edges))maxind=ind(n_edges)
               if(maxind<indc(n_edges))maxind=indc(n_edges)
9114           continue
            enddo
            goto 9104
         endif
      enddo
8104  continue
      backspace(20)
      do
         read(20,106,err=9104,end=9104)str1
         if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
            read(20,*)(headattrE(i),i=1,nattrE)
            do k=1,n_edges_E
               read(20,*,err=9214,end=9214)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
               do i = 1, nattrE -1, 1
                	if(quoteit(headattrE(i+2), attrE(i, n_edges+1))) then
                		attrE(i, n_edges+1) = addquotes(attrE(i, n_edges+1))
                	endif
                enddo
               n_edges=n_edges+1
               if(minind>indc(n_edges))minind=indc(n_edges)
               if(minind>ind(n_edges))minind=ind(n_edges)
               if(maxind<ind(n_edges))maxind=ind(n_edges)
               if(maxind<indc(n_edges))maxind=indc(n_edges)
9214           continue
            enddo
         endif
      enddo
9104  continue
      close(20)

      if(n_edges==0)then
         write(*,*)'Error! The program should be applied on directed networks'
         stop
      endif

      allocate(nodelist(minind:maxind))
      allocate(PR_it(minind:maxind))
      allocate(PR(minind:maxind))
      allocate(PRdis(1:n_bins))
      allocate(np(1:n_bins))
      allocate(avPRbin(1:n_bins))
      allocate(interv(0:n_bins))
      allocate(out_deg(minind:maxind))

      out_deg=0
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
         if(nodelist(indc(i)).eqv..false.)then
            nodelist(indc(i))=.true.
            n_vert=n_vert+1
         endif
         out_deg(indc(i))=out_deg(indc(i))+1
      enddo
      if(n_vert0<n_vert)then
         print*,'The nwb file is not properly formatted: not all nodes/labels are listed'
         stop
      endif

!     Here the matrix is read: the arrays ind and indc are defined
!     and the out-degree of each vertex is calculated
      

!     Here we initialize the PageRank of all vertices to the value 1/n_vert for the
!     first iteration of the algorithm 

      pqv=pq/n_vert
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

      n_vert1=n_vert0
      n_vert0=0
      open(20,file=fileout,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#     Node     |     PageRank'
      write(20,*)
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            n_vert0=n_vert0+1
            nodes(n_vert0)=i
            write(20,101)i,PR(i)
         endif
      enddo
      close(20)
101   format(i10,8x,e15.6)
103   format(a8,i10)
104   format(1x,e15.6,5x,e15.6)
106   format(a25)
109   format(20a50)      
110   format(i10,8x,i10,1x,18a25)
111   format(i10,10x,20a25)
112   format(a6)
113   format(a14)
120   format(i10,2x,a25,1x,e15.6)
121   format(i10,2x,2a25,1x,e15.6)
122   format(i10,2x,3a25,1x,e15.6)
123   format(i10,2x,4a25,1x,e15.6)
124   format(i10,2x,5a25,1x,e15.6)
125   format(i10,2x,6a25,1x,e15.6)
126   format(i10,2x,7a25,1x,e15.6)
127   format(i10,2x,8a25,1x,e15.6)
128   format(i10,2x,9a25,1x,e15.6)
129   format(i10,2x,10a25,1x,e15.6)
      
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

      open(20,file=fileout3,status='unknown')
      write(20,112)'*Nodes'
      write(20,109)(headattrN(i),i=1,nattrN),'pagerank*real'
      if(nattrN-1==1)then
         do i=1,n_vert
            write(20,120)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==2)then
         do i=1,n_vert
            write(20,121)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==3)then
         do i=1,n_vert
            write(20,122)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==4)then
         do i=1,n_vert
            write(20,123)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==5)then
         do i=1,n_vert
            write(20,124)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==6)then
         do i=1,n_vert
            write(20,125)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==7)then
         do i=1,n_vert
            write(20,126)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==8)then
         do i=1,n_vert
            write(20,127)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==9)then
         do i=1,n_vert
            write(20,128)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      else if(nattrN-1==10)then
         do i=1,n_vert
            write(20,129)nodes(i),(attrN(j,i),j=1,nattrN-1),PR(nodes(i))
         enddo
      endif
      write(20,113)'*DirectedEdges'
      write(20,109)(headattrE(i),i=1,nattrE)
      do i=1,n_edges
         write(20,110)indc(i),ind(i),(attrE(j,i),j=1,nattrE-2)
      enddo
      close(20)
      
      stop
    end program PageRank
    
    logical function quoteit(header, value)
		character*(*) header, value
		if(index(header, '*string') > 0 .AND. '*' /= value) then
			quoteit = .TRUE.
		else
			quoteit = .FALSE.
		endif
		return
	end 
	
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
