#!/usr/bin/perl -w

# Parse web

# Algorithm that randomizes a Directed Network conserving the degree distribution as proposed by Maslov and 
# Sneppen. Usage ./randomize.pl INPUTFILE
# Outout Randomized.txt
# The input must represent a list of edges.
# Wrote by: Cesar A. Hidalgo R. (--Cesifoti--)
# Does not consider self-loops

open (INPUT,$ARGV[0]);
open (OUTPUT,">$ARGV[1]");

$cd=0;
$cu=0;
$Switcher=0;
$once=0;

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
	$L=scalar(@data);
		
	$from=$data[0];
	$to=$data[1];

		if ($from ne $to)
		{
			if ($L>2)
			{
			$linku{$from}{$to}=$data[2];
			$outu[$cu]=$from;
			$inu[$cu]=$to;
			$cu+=1;
			}
			else
			{
			$linku{$from}{$to}=1;
			$outu[$cu]=$from;
			$inu[$cu]=$to;
			$cu+=1;
			}
		}
		else
		{
		next;
		}
	}
	
	if ($Switcher==3)
	{
	$L=scalar(@data);
		
	$from=$data[0];
	$to=$data[1];

		if ($from ne $to)
		{
			if ($L>2)
			{
			$linkd{$from}{$to}=$data[2];
			$outd[$cd]=$from;
			$ind[$cd]=$to;
			$cd+=1;
			}
			else
			{
			$linkd{$from}{$to}=1;
			$outd[$cd]=$from;
			$ind[$cd]=$to;
			$cd+=1;
			}
		}
		else
		{
		next;
		}
	}
	
}

#print STDERR "there are $c links ...\n";
########################################## RANDOMIZE UNDIRECTED EDGES ##############################
$Lfrom=@outu -1;
for($i=0; $i<2*$cu; $i++)
{
	$maxfrom=$Lfrom;
	
	$elegido1=int(rand($maxfrom + 1));
	$elegido2=int(rand($maxfrom + 1));
	
	if (exists($linku{$outu[$elegido1]}{$inu[$elegido2]}) |  exists($linku{$outu[$elegido2]}{$inu[$elegido1]}) | $outu[$elegido1] eq $inu[$elegido2] | $outu[$elegido2] eq $inu[$elegido1] )
	{
		next;
	}
	
#	print STDERR "i am changing the pair $out[$elegido1] $in[$elegido1] with $out[$elegido2] $in[$elegido2]\n";
	#$valid+=1;
	
	$linku{$outu[$elegido1]}{$inu[$elegido2]}=$linku{$outu[$elegido1]}{$inu[$elegido1]};
	$linku{$outu[$elegido2]}{$inu[$elegido1]}=$linku{$outu[$elegido2]}{$inu[$elegido2]};
	delete $linku{$outu[$elegido1]}{$inu[$elegido1]};
	delete $linku{$outu[$elegido2]}{$inu[$elegido2]};
	
#	print STDERR "to the pairs $out[$elegido1] $in[$elegido2] and $out[$elegido2] $in[$elegido1]\n";
	$vase=$inu[$elegido2];
	$inu[$elegido2]=$inu[$elegido1];
	$inu[$elegido1]=$vase;	
}

####################################### RANDOMIZE DIRECTED EDGES ####################################
$Lfrom=@outd -1;
for($i=0; $i<2*$cd; $i++)
{
	$maxfrom=$Lfrom;
	
	$elegido1=int(rand($maxfrom + 1));
	$elegido2=int(rand($maxfrom + 1));
	
	if (exists($linkd{$outd[$elegido1]}{$ind[$elegido2]}) |  exists($linkd{$outd[$elegido2]}{$ind[$elegido1]}) | $outd[$elegido1] eq $ind[$elegido2] | $outd[$elegido2] eq $ind[$elegido1] )
	{
		next;
	}
	
#	print STDERR "i am changing the pair $out[$elegido1] $in[$elegido1] with $out[$elegido2] $in[$elegido2]\n";
	#$valid+=1;
	$linkd{$outd[$elegido1]}{$ind[$elegido2]}=$linkd{$outd[$elegido1]}{$ind[$elegido1]};
	$linkd{$outd[$elegido2]}{$ind[$elegido1]}=$linkd{$outd[$elegido2]}{$ind[$elegido2]};
	delete $linkd{$outd[$elegido1]}{$ind[$elegido1]};
	delete $linkd{$outd[$elegido2]}{$ind[$elegido2]};
	
#	print STDERR "to the pairs $out[$elegido1] $in[$elegido2] and $out[$elegido2] $in[$elegido1]\n";
	$vase=$ind[$elegido2];
	$ind[$elegido2]=$ind[$elegido1];
	$ind[$elegido1]=$vase;	
}

#print STDERR "\n ...  valid changes $valid\n";

############################ PRINT OUTPUT ############

print OUTPUT "//Randomized by Cesifoti.... :-) .... NWB\n"; 
print OUTPUT "*Nodes $NumberOfNodes\n";

$C=0;
foreach $n (sort {$a <=> $b} (keys %NodeName))
{
		print OUTPUT "$n $NodeName{$n}";
		
		if (exists($NumAttributes{$n}))
		{
		#	print STDERR "$n $NumAttributes{$n} $NodeAttributes{$n}{0}\n";
			for ($r=0;$r<$NumAttributes{$n}-2;$r++)
			{
			print OUTPUT " $NodeAttributes{$n}{$r}";	
			}
		}
		
		print OUTPUT "\n";
		$C+=1;
}

#print STDERR "There are $C nodes\n";


$C=0;
if ($cd>0)
{
	print OUTPUT "*DirectedEdges\n";
	foreach $node1 (sort {$a <=> $b} (keys %linkd))
	{
		foreach $node2 (sort {$a <=> $b} (keys % {$linkd{$node1}}))
		{
			print OUTPUT "$node1 $node2 $linkd{$node1}{$node2}\n";
			$C+=1;
		}
	}
}
if ($cu>0)
{
	print OUTPUT "*UndirectedEdges\n";
	foreach $node1 (sort {$a <=> $b} (keys %linku))
	{
		foreach $node2 (sort {$a <=> $b} (keys % {$linku{$node1}}))
		{
			print OUTPUT "$node1 $node2 $linku{$node1}{$node2}\n";
			$C+=1;
		}
	}
}
#print STDERR "and $C edges\n";

