      program Site_Betweenness

!
!     It calculates the site betweenness of Freeman 
!     for each node of a network by using breadth-first-search. 
!

      implicit none
      integer, allocatable,dimension(:)::itnei,degree,indvic,distance,lista,np,ind,ind0,nodes
      integer, allocatable,dimension(:)::itnei1,indc,predec,n_predec,sp,totneigh
      real*8, allocatable,dimension(:)::s_btw,s_btw_it,interv,avsbwbin
      logical, allocatable, dimension(:):: nodelist
      integer i,j,k,nneigh,nneigh1,dist,diameter,n_paths,ilist,n_vert,n_edges
      integer i1,i2,maxind,minind,n_bins,nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
      character*256 filename,fileout1,fileout2,fileout3,fileout4,sn_bins
      real*8 maxsbw,minsbw,abin
      character*50 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
      character*25,allocatable,dimension(:,:):: attrN,attrE
      
      logical quoteit
      character*(25) addquotes

      call GETARG(2,sn_bins)
      call GETARG(4,filename)
      read(sn_bins,*)n_bins

      maxind=1
      minind=1000000
      n_edges=0
      n_vert0=0
      n_vert1=0
      ch=0
      n_edges_N=0
      n_edges_E=0
      nattrN=0

      fileout1='site_betweenness.dat'
      fileout2='site_betweenness_distr.dat'
      fileout3='site_betweenness_distr_binned.dat'
      fileout4='network_sitebetweenness.nwb'

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
         else if(str1(1:1)=='*'.AND.str1(2:2)=='U')then 
            read(20,*)(headattrE(i),i=1,nattrE)
            do k=1,n_edges_E
               read(20,*,err=9114,end=9114)ind0(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
               do i = 1, nattrE -1, 1
                	if(quoteit(headattrE(i+2), attrE(i, n_edges+1))) then
                		attrE(i, n_edges+1) = addquotes(attrE(i, n_edges+1))
                	endif
                enddo
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
               do i = 1, nattrE -1, 1
                	if(quoteit(headattrE(i+2), attrE(i, n_edges+1))) then
                		attrE(i, n_edges+1) = addquotes(attrE(i, n_edges+1))
                	endif
                enddo
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

      allocate(predec(1:2*n_edges))
      allocate(n_predec(minind:maxind))
      allocate(sp(minind:maxind))
      allocate(s_btw(minind:maxind))
      allocate(s_btw_it(minind:maxind))
      allocate(itnei(1:n_vert))
      allocate(itnei1(1:n_vert))
      allocate(totneigh(minind:maxind))
      allocate(indvic(minind:maxind))
      allocate(distance(minind:maxind))
      allocate(lista(1:n_vert-1))
      allocate(indc(1:2*n_edges))
      allocate(np(1:n_bins))
      allocate(avsbwbin(1:n_bins))
      allocate(interv(0:n_bins))

      indvic(minind)=0
      s_btw=0.0d0
      
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

      totneigh=0
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            ilist=0
            s_btw_it(i)=0.0d0
            sp=0
            n_predec=0
            distance=n_vert
            dist=1
            distance(i)=0
            nneigh=1
            itnei(nneigh)=i
            sp(i)=1
1001        continue
            nneigh1=0
            do k=1,nneigh
               do j=1,degree(itnei(k))
                  if(distance(indc(indvic(itnei(k))+j))>dist)then
                     nneigh1=nneigh1+1
                     itnei1(nneigh1)=indc(indvic(itnei(k))+j)
                     distance(itnei1(nneigh1))=dist
                     s_btw_it(itnei1(nneigh1))=1.0d0
                     n_predec(itnei1(nneigh1))=n_predec(itnei1(nneigh1))+1
                     predec(indvic(itnei1(nneigh1))+n_predec(itnei1(nneigh1)))=itnei(k)
                     ilist=ilist+1
                     lista(ilist)=itnei1(nneigh1)
                     sp(lista(ilist))=sp(lista(ilist))+sp(itnei(k))
                  else if(distance(indc(indvic(itnei(k))+j))==dist)then
                     n_predec(indc(indvic(itnei(k))+j))=n_predec(indc(indvic(itnei(k))+j))+1
                     predec(indvic(indc(indvic(itnei(k))+j))+n_predec(indc(indvic(itnei(k))+j)))=itnei(k)
                     sp(indc(indvic(itnei(k))+j))=sp(indc(indvic(itnei(k))+j))+sp(itnei(k))
                  endif
               enddo
            enddo
            if(nneigh1==0)goto 101
            dist=dist+1
            nneigh=nneigh1
            totneigh(i)=totneigh(i)+nneigh1
            do k=1,nneigh
               itnei(k)=itnei1(k)
            enddo
            goto 1001
101         continue
            do k=1,ilist
               do j=1,n_predec(lista(ilist-k+1))
                  s_btw_it(predec(indvic(lista(ilist-k+1))+j))=s_btw_it(predec(indvic(lista(ilist-k+1))+j))+&
                       &(s_btw_it(lista(ilist-k+1))*sp(predec(indvic(lista(ilist-k+1))+j)))/sp(lista(ilist-k+1))
               enddo
            enddo
            s_btw=s_btw+s_btw_it
            s_btw_it(i)=0.0d0
         endif
      enddo

      s_btw=s_btw-real(totneigh)
      maxsbw=MAXVAL(s_btw)
      minsbw=MINVAL(s_btw)
      if(minsbw<0.0001d0)minsbw=0.0001d0
      
      n_vert1=n_vert0
      n_vert0=0
      open(20,file=fileout1,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#   Node   |  Site betweenness'
      write(20,*)
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            n_vert0=n_vert0+1
            nodes(n_vert0)=i
            write(20,138)i,s_btw(i)
         endif
      enddo

      close(20)

      interv(0)=minsbw-0.0001d0*minsbw
      abin=(maxsbw-minsbw+0.0002d0*minsbw)/n_bins

      do i=1,n_bins
         interv(i)=interv(i-1)+abin
      enddo

      np=0
       
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            do j=1,n_bins
               if(s_btw(i)<interv(j))then
                  np(j)=np(j)+1
                  exit
               endif
            enddo
         endif
      enddo

      open(20,file=fileout2,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#       Site betweenness |   Probability'
      write(20,*)
      do i=1,n_bins
         write(20,104)minsbw+(i-1)*abin+abin/2,real(np(i))/n_vert
      enddo
      close(20)


      if(real(minsbw)/real(maxsbw)>0.1d0)then
         write(*,*)'Warning! Site betweenness varies too little: the logarithmic binning is not useful'
         goto 9001
      endif
      
      interv(0)=minsbw-0.0001d0*minsbw
      abin=(log(maxsbw+0.0001d0*minsbw)-log(interv(0)))/n_bins
      
      do i=1,n_bins
         interv(i)=exp(log(interv(i-1))+abin)
      enddo
      
      avsbwbin=0.0d0
      np=0
      
      do i=minind,maxind
         if(nodelist(i).eqv..true.)then
            do j=1,n_bins
               if(s_btw(i)<interv(j))then
                  np(j)=np(j)+1
                  avsbwbin(j)=avsbwbin(j)+s_btw(i)
                  exit
               endif
            enddo
         endif
      enddo
      
      do i=1,n_bins
         if(np(i)>0)then
            avsbwbin(i)=avsbwbin(i)/np(i)
         endif
      enddo
      
      open(20,file=fileout3,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#       Site betweenness |   Probability'
      write(20,*)
      do i=1,n_bins
         if(np(i)>0)then
            write(20,104)avsbwbin(i),real(np(i))/(n_vert*(interv(i)-interv(i-1)))
         endif
      enddo
      close(20)
      
103   format(a8,i10)
138   format(i10,e15.6)
106   format(a25)
104   format(8x,e15.6,6x,e15.6)
109   format(20a50)      
110   format(i10,8x,i10,1x,18a25)
111   format(i10,10x,20a25)
112   format(a6)
113   format(a16)
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

9001  continue

      open(20,file=fileout4,status='unknown')
      write(20,112)'*Nodes'
      write(20,109)(headattrN(i),i=1,nattrN),'sitebetweenness*real'
      if(nattrN-1==1)then
         do i=1,n_vert
            write(20,120)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==2)then
         do i=1,n_vert
            write(20,121)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==3)then
         do i=1,n_vert
            write(20,122)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==4)then
         do i=1,n_vert
            write(20,123)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==5)then
         do i=1,n_vert
            write(20,124)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==6)then
         do i=1,n_vert
            write(20,125)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==7)then
         do i=1,n_vert
            write(20,126)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==8)then
         do i=1,n_vert
            write(20,127)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==9)then
         do i=1,n_vert
            write(20,128)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      else if(nattrN-1==10)then
         do i=1,n_vert
            write(20,129)nodes(i),(attrN(j,i),j=1,nattrN-1),s_btw(nodes(i))
         enddo
      endif
      write(20,113)'*UndirectedEdges'
      write(20,109)(headattrE(i),i=1,nattrE)
      do i=1,n_edges
         write(20,110)ind0(i),ind(i),(attrE(j,i),j=1,nattrE-2)
      enddo
      close(20)

      stop
      
    end program Site_Betweenness
    
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
