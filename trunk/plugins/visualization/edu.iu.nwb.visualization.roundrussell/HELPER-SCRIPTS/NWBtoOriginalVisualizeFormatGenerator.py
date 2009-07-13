'''
Created on Jul 10, 2009

Used to convert a valid NWB file into the format expected by original visualize.py i.e.
1. Nodes file <id, label, strength>
2. Edge file <0_index_based_source_id, 0_index_based_target_id, weight>
3. Level file(3) <0_index_based_node_id, community_value>
@author: cdtank
'''
import re

# Source input nwb file.
input = open("round-russell-circ-viz.nwb")

# Creates output file based on input filename
output_nodes_file = open(input.name[:-4] + ".nodes", "w")
output_edges_file = open(input.name[:-4] + ".edges", "w")

# Default strength & weight column index in case the network in unweighted & no-strength.  
strength_column_index = -1
weight_column_index = -1

output_level_files = []

# Hard code the level names corresponding to those in the nwb file and their 0_index_based_column_index
level_attributes_column = {'blondel_community_level_0' : 3, 'blondel_community_level_1' : 4, 'blondel_community_level_2' : 5}

for level in level_attributes_column:
    output_level_files.append(open(input.name[:-4] + "_" + level + ".level", "w"))

# Process only the node section.
for index, line in enumerate(input):
#    Ignore the first 2 headers of nwb file having schema inforamtion/
    if index < 2:
        pass
    else:
        if re.search(r'^(\*DirectedEdges|\*UndirectedEdges)', line):
            break
        else:
            node_row = line.rstrip().split("\t")
            if strength_column_index == -1: 
                current_strength = 1.0
            else:
                current_strength = node_row[strength_column_index]

            # Prepare the nodes file
            output_nodes_file.writelines(str(node_row[0] + "\t" + node_row[1].strip('"') + "\t" + str(current_strength) + "\n"))
            
            # Prepare the level file(s)           
            for index,(level_name,level_column_index) in enumerate(level_attributes_column.items()):
                # 1 is subtracted to make it 0_based since nwb files are 1_based                
                output_level_files[index].writelines(str(int(node_row[0]) - 1) + "\t" + node_row[level_column_index].strip('"') + "\n")

# Process only the edge section.
for index, edge_row in enumerate(input):
    if index == 0:
        pass
    else:
        current_edge = edge_row.rstrip().split("\t")
        if weight_column_index == -1: 
            current_weight = 1.0
        else:
            current_weight = node_row[weight_column_index]
        output_edges_file.writelines(str(str(int(current_edge[0]) - 1) + "\t" + str(int(current_edge[1]) - 1) + "\t" + str(current_weight) + "\n")) 

# Close all the file handlers        
input.close()            
output_nodes_file.close()
output_edges_file.close()
for level in output_level_files:
    level.close() 
            