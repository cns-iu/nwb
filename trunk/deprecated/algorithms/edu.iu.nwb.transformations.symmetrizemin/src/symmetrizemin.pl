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
			}
			else
			{
			$linku{$from}{$to}=1;
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
			}
			else
			{
			$linkd{$from}{$to}=1;
			}
		}
		else
		{
		next;
		}
	}
	
}

######################################################
############################ PRINT OUTPUT ############
######################################################

print OUTPUT "//Symmetrized by Maximum ....Cesifoti..:-)..NWB\n"; 
print OUTPUT "*Nodes $NumberOfNodes\n";

$C=0;


foreach $n (sort {$a <=> $b} (keys %NodeName))
{
		print OUTPUT "$n $NodeName{$n}";
		
		if (exists($NodeAttributes{$n}))
		{
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

	print OUTPUT "*UndirectedEdges\n";
	foreach $node1 (sort {$a <=> $b} (keys %linku))
	{
		foreach $node2 (sort {$a <=> $b} (keys % {$linku{$node1}}))
		{
			print OUTPUT "$node1 $node2 $linku{$node1}{$node2}\n";
		}
	}
############################################################
	foreach $node1 (sort {$a <=> $b} (keys %linkd))
	{
		foreach $node2 (sort {$a <=> $b} (keys % {$linkd{$node1}}))
		{
			if (exists($skip{$node1}{$node2}))
			{
			next;	
			}
			if (exists($linkd{$node2}{$node1}))
			{
				if ($linkd{$node1}{$node2} <= $linkd{$node2}{$node1})
				{
				print OUTPUT "$node1 $node2 $linkd{$node1}{$node2}\n";
				}
				else
				{
				print OUTPUT "$node1 $node2 $linkd{$node2}{$node1}\n";
				}
				
				$skip{$node2}{$node1}=1;
			}
			else
			{
				next;
			}
		}
	}
