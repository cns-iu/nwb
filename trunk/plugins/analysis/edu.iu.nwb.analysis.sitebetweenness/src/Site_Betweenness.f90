      program Site_Betweenness

!
!     It calculates the site betweenness of Freeman 
!     for each node of a network by using breadth-first-search. 
!

      implicit none
      integer, allocatable,dimension(:)::itnei,degree,indvic,distance,lista,np
      integer, allocatable,dimension(:)::itnei1,indc,predec,n_predec,sp,totneigh
      real*8, allocatable,dimension(:)::s_btw,s_btw_it,interv,avsbwbin
      integer i,j,k,nneigh,nneigh1,dist,diameter,n_paths,ilist,n_vert,n_edges
      integer i1,i2,maxind,minind,n_bins
      character*256 filename,fileout1,fileout2,fileout3,str1,str2,sn_bins
      real*8 maxsbw,minsbw,abin

      call GETARG(2,sn_bins)
      call GETARG(4,filename)
      read(sn_bins,*)n_bins

      maxind=1
      minind=1000000
      n_edges=0

      fileout1='site_betweenness.dat'
      fileout2='site_betweenness_distr.dat'
      fileout3='site_betweenness_distr_binned.dat'

      open(20,file=filename,status='unknown')
      do 
         read(20,106,err=8103,end=8103)str1
         if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
            do 
               read(20,*,err=8103,end=8103)i1,i2
               if(minind>i1)minind=i1
               if(minind>i2)minind=i2
               if(maxind<i2)maxind=i2
               if(maxind<i1)maxind=i1
               n_edges=n_edges+1   
            enddo
         endif
      enddo
8103  continue
      close(20)

      if(n_edges==0)then
         write(*,*)'Error! The program should be applied on undirected networks'
         stop
      endif
      if(minind/=1)then
         write(*,*)'Error! The minimal node index is not 1'
         stop
      endif
      
      n_vert=maxind

      allocate(predec(1:2*n_edges))
      allocate(n_predec(1:n_vert))
      allocate(sp(1:n_vert))
      allocate(s_btw(1:n_vert))
      allocate(s_btw_it(1:n_vert))
      allocate(itnei(1:n_vert))
      allocate(itnei1(1:n_vert))
      allocate(totneigh(1:n_vert))
      allocate(indvic(1:n_vert))
      allocate(lista(1:n_vert-1))
      allocate(degree(1:n_vert))
      allocate(distance(1:n_vert))
      allocate(indc(1:2*n_edges))
      allocate(np(1:n_bins))
      allocate(avsbwbin(1:n_bins))
      allocate(interv(0:n_bins))

      indvic(1)=0
      s_btw=0.0d0
      degree=0
      
      open(20,file=filename,status='unknown')
      do 
         read(20,106)str1
         if(str1(1:1)=='*'.AND.str1(2:2)=='U')then
            do j=1,n_edges
               read(20,*)i1,i2
               degree(i1)=degree(i1)+1
               degree(i2)=degree(i2)+1
            enddo
            exit
         endif
      enddo
      close(20)
      
      do k=1,n_vert-1
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
      totneigh=0
      do i=1,n_vert
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
1001     continue
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
101      continue
         do k=1,ilist
            do j=1,n_predec(lista(ilist-k+1))
               s_btw_it(predec(indvic(lista(ilist-k+1))+j))=s_btw_it(predec(indvic(lista(ilist-k+1))+j))+&
                    &(s_btw_it(lista(ilist-k+1))*sp(predec(indvic(lista(ilist-k+1))+j)))/sp(lista(ilist-k+1))
            enddo
         enddo
         s_btw=s_btw+s_btw_it
         do k=1,ilist
            s_btw_it(k)=0.0d0
         enddo
         s_btw_it(i)=0.0d0
      enddo

      s_btw=s_btw-real(totneigh)
      maxsbw=MAXVAL(s_btw)
      minsbw=MINVAL(s_btw)
      if(minsbw<0.0001d0)minsbw=0.0001d0
      
      open(20,file=fileout1,status='unknown')
      write(20,103)'# Nodes ',n_vert
      write(20,*)'#   Node   |  Site betweenness'
      write(20,*)
      do i=1,n_vert
         write(20,128)i,s_btw(i)
      enddo

      close(20)

      interv(0)=minsbw-0.0001d0*minsbw
      abin=(maxsbw-minsbw+0.0002d0*minsbw)/n_bins

      do i=1,n_bins
         interv(i)=interv(i-1)+abin
      enddo

      np=0
       
      do i=1,n_vert
         do j=1,n_bins
            if(s_btw(i)<interv(j))then
               np(j)=np(j)+1
               exit
            endif
         enddo
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
      
      do i=1,n_vert
         do j=1,n_bins
            if(s_btw(i)<interv(j))then
               np(j)=np(j)+1
               avsbwbin(j)=avsbwbin(j)+s_btw(i)
               exit
            endif
         enddo
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
128   format(i10,e15.6)
106   format(a256)
104   format(8x,e15.6,6x,e15.6)

9001  continue

      stop
      
    end program Site_Betweenness
