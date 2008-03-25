       program outdegree_distr

  
!
!      It calculates the out-degree distribution for a directed network 
!      whose matrix is given as input as list of all edges
!

       implicit none

       integer i,j,k,maxoutdeg,minoutdeg,mindeg,maxdeg,n_vert,n_edges,n_bins,maxind,minind,i1,i2
       integer nattrN,nattrE,n_edges_N,n_edges_E,n_vert0,ch,n_vert1,i0
       integer, allocatable, dimension (:) :: outdegree,outdegdis,npoints,np,ind,indc,nodes
       real*8, allocatable, dimension (:) :: interv,degdis_binned,avdegbin
       logical, allocatable, dimension(:):: nodelist
       real*8 binsize
       character*256 filename,fileout2,fileout5,sn_bins
       character*25 str(1:20),headattrN(1:20),headattrE(1:20),str1,str2,str3
       character*25,allocatable,dimension(:,:):: attrN,attrE
       
!      Here the program reads the input parameters
       
       call GETARG(2,sn_bins)
       call GETARG(4,filename)
       read(sn_bins,*)n_bins
       
       fileout2='out-degree_distribution.dat'
       fileout5='out-degree_distribution_binned.dat'

       n_edges=0
       n_vert0=0
       n_vert1=0
       maxind=1
       minind=10000000
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
8103   continue
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
9103   continue
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
                n_vert0=n_vert0+1
                if(minind>nodes(n_vert0))minind=nodes(n_vert0)
                if(maxind<nodes(n_vert0))maxind=nodes(n_vert0)  
8114            continue
             enddo
             goto 8104
          else if(str1(1:1)=='*'.AND.str1(2:2)=='D')then 
             read(20,*)(headattrE(i),i=1,nattrE)
             do k=1,n_edges_E
                read(20,*,err=9114,end=9114)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
                n_edges=n_edges+1
                if(minind>indc(n_edges))minind=indc(n_edges)
                if(minind>ind(n_edges))minind=ind(n_edges)
                if(maxind<ind(n_edges))maxind=ind(n_edges)
                if(maxind<indc(n_edges))maxind=indc(n_edges)
9114            continue
             enddo
             goto 9104
          endif
       enddo
8104   continue
       backspace(20)
       do
          read(20,106,err=9104,end=9104)str1
          if(str1(1:1)=='*'.AND.str1(2:2)=='D')then
             read(20,*)(headattrE(i),i=1,nattrE)
             do k=1,n_edges_E
                read(20,*,err=9214,end=9214)indc(n_edges+1),ind(n_edges+1),(attrE(j,n_edges+1),j=1,nattrE-2)
                n_edges=n_edges+1
                if(minind>indc(n_edges))minind=indc(n_edges)
                if(minind>ind(n_edges))minind=ind(n_edges)
                if(maxind<ind(n_edges))maxind=ind(n_edges)
                if(maxind<indc(n_edges))maxind=indc(n_edges)
9214            continue
             enddo
          endif
       enddo
9104   continue
       close(20)

       if(n_edges==0)then
          write(*,*)'Error! The program should be applied on directed networks'
          stop
       endif
       
       allocate(nodelist(minind:maxind))
       allocate(outdegree(minind:maxind))
       outdegree=0
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
          outdegree(indc(i))=outdegree(indc(i))+1
       enddo
       if(n_vert0<n_vert)then
          print*,'The nwb file is not properly formatted: not all nodes/labels are listed'
          stop
       endif

!      Here the arrays are allocated

       allocate(interv(0:n_bins))
       allocate(npoints(1:n_bins))
       allocate(np(1:n_bins))
       allocate(avdegbin(1:n_bins))
       
       minoutdeg=1000000
       maxoutdeg=0

       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             if(outdegree(i)>maxoutdeg)maxoutdeg=outdegree(i)
             if(outdegree(i)<minoutdeg)minoutdeg=outdegree(i)
          endif
       enddo
       
       allocate(outdegdis(minoutdeg:maxoutdeg))

       outdegdis=0

!      Here we evaluate the number of nodes having the same outdegree;
!      to get the probability we divide by the total number of edges n_edges

       do k=minind,maxind
          if(nodelist(k).eqv..true.)then
             outdegdis(outdegree(k))=outdegdis(outdegree(k))+1
          endif
       enddo

       open(20,file=fileout2,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'#     Out-degree    |    Probability'
       write(20,*)
        do k=minoutdeg,maxoutdeg
          write(20,104)k,real(outdegdis(k))/n_vert
       enddo
       close(20)

101    format(i10,8x,i10)
103    format(a8,i10)
104    format(2x,i10,9x,e15.6)
105    format(4x,e15.6,6x,e15.6)
106    format(a25)

!      Here we calculate the binned degree distributions

       mindeg=minoutdeg
       maxdeg=maxoutdeg

       if(real(mindeg+1)/real(maxdeg)>0.1d0)then
          write(*,*)'Warning! In-degree varies too little: the logarithmic binning is not useful'
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
       
       do i=minind,maxind
          if(nodelist(i).eqv..true.)then
             do j=1,n_bins
                if(real(outdegree(i))<interv(j))then
                   np(j)=np(j)+1
                   avdegbin(j)=avdegbin(j)+real(outdegree(i))
                   exit
                endif
             enddo
          endif
       enddo

       do i=1,n_bins
          if(np(i)>0)then
             avdegbin(i)=avdegbin(i)/np(i)
          endif
       enddo
       
       open(20,file=fileout5,status='unknown')
       write(20,103)'# Nodes ',n_vert
       write(20,*)'# Center of out-degree bin | Probability'
       write(20,*)
       do i=1,n_bins
          if(np(i)>0)then
             write(20,105)avdegbin(i),real(np(i))/(n_vert*npoints(i))
          endif
       enddo
       close(20)

9001   continue

       stop
     end program outdegree_distr
