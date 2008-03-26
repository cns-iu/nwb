#!/usr/bin/perl -w
# Parse web

### open input file
open (INPUT,$ARGV[0]);

open (OUTPUTfromto,">$ARGV[1]");

$Switcher=0;
$once=0;

while (<INPUT>)
{
chomp;
@data=split;
	
	if (scalar(@data)<1)
	{
		next;
	}
			
	@checkcomment=split(//,$data[0]);
	if ($checkcomment[0]=~/\// && $checkcomment[1]=~/\//)
	{
	next;
	}

	if ($data[0]=~m/\*Nodes/)
	{
	$NumberOfNodes=$data[1];
	$Switcher=1;
	next;
	}
	elsif ( $data[0]=~m/\*UndirectedEdges/) 
	{
	$Switcher=2;
	$once+=1;
	$Direction=$data[0];
	next;
	}
	elsif ( $data[0]=~m/\*DirectedEdges/)
	{
	$Switcher=3;
	$once+=1;
	$Direction=$data[0];
	next;
	}
	

	if ($Switcher==1)
	{
		if (scalar(@data)>1)
		{
		$NodeName{$data[0]}=$data[1];
		$L=scalar(@data);
			if ($L>2)
			{
				for ($s=2;$s<$L;$s++)
				{
				$NodeAttributes{$data[0]}{$s-2}=$data[$s];
				}
				$NumAttributes{$data[0]}=$L;
			}
		}	
	}
	
	if ($Switcher==2)
	{
	$from=$data[0];
	$to=$data[1];

		if ($from ne $to)
		{
		$Directed_i_p{$from}{$to}=1;
		$Directed_p_i{$to}{$from}=1;
		$Directed_i_p{$to}{$from}=1;
		$Directed_p_i{$from}{$to}=1;
		$Interactors{$from}=1;
		$Interactors{$to}=1;
		$Promoters{$from}=1;
		$Promoters{$to}=1;
		$AllLinks{$from}{$to}=1;
		$AllLinks{$to}{$from}=1;
		}
		else
		{
		next;
		}
	}
	
	if ($Switcher==3)
	{

		$from=$data[0];
		$to=$data[1];

		if ($from ne $to)
		{
		$AllLinks{$from}{$to}=1;
		$AllLinks{$to}{$from}=1;
		$Directed_i_p{$from}{$to}=1;
		$Directed_p_i{$to}{$from}=1;
		$Interactors{$from}=1;
		$Promoters{$to}=1;
		}
		else
		{
		next;
		}
	}
}
##########################################################

##### Calculate Topological Overlap for Directed Case for Interactors##

#print "calculating overlap between interactors....!!! \n";
$kjh=0;
$sss=0;
foreach $node1 (keys %Interactors)
{
	$kjh+=1;
	$sss+=1;
	if ($sss==100)
	{
#	print STDERR "$kjh nodes processed -> Direction\n";
	$sss=0;
	}
	
	@Node1Friends = (keys % {$Directed_i_p{$node1}});
	#print "...................... $node1 Dude1 has as Friends @Node1Friends\n";
	
	@friends1=(keys % {$AllLinks{$node1}});
	foreach $buddy (@friends1)
	{
			$F1{$buddy}=1;
			@friends2=(keys % {$AllLinks{$buddy}});
			foreach $buddo (@friends2)
			{
			$F1{$buddo}=1;
			}
	}
	
	foreach $node2 (keys %F1)
	{
		@Node2Friends = (keys % {$Directed_i_p{$node2}});
		#print "$node2 Dude 2 has as Friends @Node2Friends\n";			
		
		if (scalar(@Node2Friends)<1)
		{
			next;
		}
		
		########## find the overlap between the dudes ##############
		$marcador=0;
		foreach $f1 (@Node1Friends)
		{
			foreach $f2 (@Node2Friends)
			{	
				if ($f1 eq $f2)
				{
				$DirectedOverlap_i_p{$node1}{$node2} += 1;
				$marcador=1;
				}		
			}	
		}
		
		### if there is no overlap set it equal to zero
		
		if ($marcador==0)
		{
			$DirectedOverlap_i_p{$node1}{$node2} = 0;
		}
		
		### if there is a link between them add one to the overlap
		
		
		### normalize by dividing by the minimum
		
		$k1=scalar(@Node1Friends);
		$k2=scalar(@Node2Friends);
		
		$DirectedOverlap_i_p{$node1}{$node2}=($DirectedOverlap_i_p{$node1}{$node2}**(2))/($k1*$k2);	
					
		#print "and their overlap is $DirectedOverlap_i_p{$node1}{$node2} \n";
	}
	undef %F1;
	undef @friends1;
	undef @friends2;
}

############################ PRINT OUTPUT ############

print OUTPUTfromto "//Calculated by Cesifoti.... :-) .... NWB\n"; 
print OUTPUTfromto "*Nodes $NumberOfNodes\n";

#$C=0;
foreach $n (sort {$a <=> $b} (keys %NodeName))
{
		if (exists($NumAttributes{$n}))
		{
			print OUTPUTfromto "$n $NodeName{$n}"; 
			for ($r=0;$r<$NumAttributes{$n}-2;$r++)
			{
			print OUTPUTfromto " $NodeAttributes{$n}{$r}";
			}
			print OUTPUTfromto "\n";
		}
		else
		{
		print OUTPUTfromto "$n $NodeName{$n}\n";
#		$C+=1;
		}
}
#print STDERR "There are $C nodes\n";


#$C=0;
	print OUTPUTfromto "*DirectedEdges\n";
	foreach $node1 (sort(keys %DirectedOverlap_i_p))
	{
		foreach $node2 (sort(keys % {$DirectedOverlap_i_p{$node1}}))
		{
			if ($DirectedOverlap_i_p{$node1}{$node2} eq 0)
			{
			next;
			}	
			else
			{
			print OUTPUTfromto "$node1 $node2 $DirectedOverlap_i_p{$node1}{$node2}\n";
			}
		}
	}

###############################################################################################################
