import re
import sys
import fileinput
import random

RADIUS = 22.75 # points

# Optimized for rendering by Ghostview.
def header(title, numPages):
    return '''%!PS-Adobe-2.0
%%Creator: codebook_planes.py
%%Title: ''' + title + '''
%%Pages: ''' + str(numPages) + '''
%%PageOrder: Ascend
%%BoundingBox: 0 0 11020 9350
%%Orientation: Landscape
%%EndComments

/myLN {
gsave
	setgray
	0.01 setlinewidth
	translate
	%radius radius scale
	newpath		
		0  radius moveto
		0.86603 radius mul  0.5 radius mul lineto
		0.86603 radius mul -0.5 radius mul lineto
		0 -1 radius mul lineto
		-0.86603 radius mul -0.5 radius mul lineto
		-0.86603 radius mul  0.5 radius mul lineto
		0  radius lineto
	closepath
	gsave fill grestore
	0.9 setgray stroke
grestore
} def

/radius ''' + str(RADIUS) + ''' def
'''


def show(mesh):
    return 'gsave /Garamond findfont 30 scalefont setfont 0.1 setgray 120 50 moveto (' + mesh + ') show grestore'


def main():
    inDatasetFilename = 'item-distributions-over-200-tags.dat'
    inCodebookFilename = 'trained.cod'
    
    # Use hard-coded input filename unless one argument is given
    if len(sys.argv) > 1:
        inCodebookFilename = sys.argv[1]

    planes = range(1, 1 + 200)#[78, 29, 501, 385, 472, 114, 176, 126, 443, 84, 223, 70, 526, 348, 439, 496, 76, 459, 147, 383, 420, 175, 20, 237, 138, 68, 210, 239, 450, 448]#set([168, 221, 151, 472, 501, 29, 385, 520, 210, 491, 443, 114, 173, 450, 20, 126, 99, 496, 176, 180, 406, 239, 68, 223, 285, 175, 75, 78, 178, 514, 529, 118, 348, 420, 142, 237, 70, 202, 245, 76, 145, 256, 14, 72, 446, 432, 147, 84, 495]#[279, 73, 519, 63, 452, 298, 11, 261, 484, 322, 168, 221, 151, 472, 501, 29, 385, 520, 210]#[501, 459, 198, 61, 461, 40, 382, 363, 100, 283, 139, 228, 414, 47, 184, 52, 9, 193, 155, 332, 345, 122, 21, 356, 408, 435, 33, 284, 344, 164, 110, 95, 220, 294, 22, 524, 480, 390, 395, 153, 330, 257, 342, 424, 484, 371, 334, 352, 212, 104, 316]#, 406, 50, 36, 130, 150, 340, 230, 357, 426, 447, 520, 474, 154, 421, 239, 373, 11, 3, 290, 7, 267, 171, 383, 315, 68, 185, 481, 111, 35, 495, 30, 331, 156, 387, 88, 124, 425, 452, 280, 242, 24, 97, 361, 204, 26, 365, 132, 341, 70, 350, 174, 380, 455, 485, 339, 497, 71, 449, 391, 178, 57, 326, 302, 349, 388, 410, 368, 116, 353, 34, 59, 46, 54, 473, 251, 493, 335, 351, 191, 312, 76, 310, 456, 2, 137, 292, 79, 168, 329, 338, 66, 431, 477, 348, 112, 93, 75, 488, 405, 143, 229, 458, 523, 443, 490, 399, 502, 17, 125, 94, 5, 63, 240, 454, 27, 418, 109, 190, 253, 113, 265, 69, 358, 509, 375, 432, 401, 12, 183, 60, 430, 224, 215, 298, 67, 514, 18, 28, 29, 200, 91, 222, 328, 189, 44, 19, 277, 412, 271, 105, 131, 205, 107, 101, 453, 379, 86, 232, 81, 313, 166, 263, 308, 62, 366, 16, 437, 73, 411, 45, 85, 392, 439, 288, 396, 442, 188, 123, 321, 482, 343, 476, 202, 223, 511, 441, 177, 389, 367, 372, 8, 324, 398, 507, 87, 208, 415, 460, 423, 129, 179, 235, 119, 521, 98, 180, 433, 377, 457, 197, 417, 422, 15, 218, 53, 20, 503, 500, 469, 464, 149, 434, 420, 381, 152, 6, 522, 120, 386, 465, 146, 77, 56, 397, 499, 41, 106, 248, 78, 322, 72, 467, 80, 466, 147, 304, 385, 508, 165, 448, 82, 261, 89, 275, 506, 384, 258, 347, 492, 512, 148, 378, 273, 170, 127, 160, 169, 136, 226, 176, 74, 246, 470, 475, 233, 115, 436, 305, 102, 505, 92, 192, 141, 214, 236, 252, 359, 527, 221, 486, 249, 519, 333, 10, 244, 108, 276, 142, 287, 238, 428, 413, 133, 161, 227, 451, 39, 99, 1, 318, 211, 196, 400, 515, 260, 374, 207, 296, 83, 38, 489, 319, 291, 172, 286, 117, 163, 254, 65, 25, 376, 58, 337, 445, 446, 175, 491, 237, 4, 279, 181, 151, 266, 498, 336, 206, 355, 513, 162, 126, 49, 14, 496, 402, 201, 529, 278, 311, 325, 84, 96, 327, 440, 429, 293, 103, 216, 32, 256, 483, 243, 517, 307, 128, 167, 416, 145, 369, 134, 140, 199, 186, 468, 281, 370, 13, 285, 320, 173, 43, 300, 404, 419, 317, 31, 462, 438, 158, 210, 247, 427, 48, 346, 364, 118, 213, 268, 526, 295, 55, 444, 203, 518, 255, 504, 270, 301, 463, 494, 360, 362, 282, 187, 209, 472, 51, 231, 64, 394, 393, 23, 403, 487, 219, 264, 471, 274, 299, 259, 159, 234, 138, 42, 182, 144, 525, 323, 157, 114, 225, 407, 450, 272, 195, 479, 90, 516, 306, 241, 314, 135, 478, 510, 409, 269, 194, 245, 289, 37, 297, 121, 262, 217, 303, 309, 354, 250, 528]#range(1, 3 + 1)

    # Read the MeSH term corresponding to each plane/ID/coordinate.
    print 'Reading training data for coordinate labels.. ',
        
    meshIDToName = dict()
    for line in fileinput.input(inDatasetFilename):
        if fileinput.isfirstline():
            continue
        if line.startswith('#'):
            marker, index, mesh = map(lambda s: s.strip(), line.split(None, 2))
            meshIDToName[int(index)] = mesh
        else:
            break
    fileinput.close()
    print 'Done.'
    

    # Figure out the positions and gray levels of the nodes for each plane.
    nodesPerPlane = dict()
    print 'Reading codebook file.. ',
    codebookFile = open(inCodebookFilename)
    dim, topology, columns, rows, neighborhood = codebookFile.next().split()
    lineIndex = 0
    for line in codebookFile:
        for plane in planes:
            coords = line.split()
            coord = coords[plane - 1]
            i, j = (int(lineIndex / int(columns)), lineIndex % int(columns))
            # Rotate to portrait orientation if it better suits the dimensions.
            #if columns < rows:
            #    temp = j
            #    j = i
            #    i = temp                

            nodesPerPlane[plane] = nodesPerPlane.setdefault(plane, []) + [(i, j, float(coord))]
        
        lineIndex += 1
    codebookFile.close()
    print 'Done.' 

    #if columns < rows:
    #    temp = columns
    #    columns = rows
    #    rows = temp
    yscale = 600.0 / (2 * RADIUS * int(rows))
    xscale = 800.0 / (2 * RADIUS * int(columns))
    print xscale, yscale
    scale = min(xscale, yscale)

    # Create PostScript.
    outPSFilename = '.'.join(inCodebookFilename.split('.')[:-1]) + '.planes.ps'
    outPSFile = open(outPSFilename, 'w')

    print >> outPSFile, header(outPSFilename, len(planes))

    pageNumber = 1

    # One page per plane.
    for plane in planes:
        # This might be cheating, depending on the training metric.  Probably okay for Jensen-Shannon.
        maxGray = 0
        for i, j, gray in nodesPerPlane[plane]:
            if gray > maxGray:
                maxGray = gray
        print 'Plane', plane, 'maxGray =', maxGray
        #maxGray = 1

        
        mesh = meshIDToName[plane]#str(plane)
        print >> outPSFile, '%%Page: ' + str(plane) + ' ' + str(pageNumber)
        pageNumber += 1
        print >> outPSFile, '% Topic: ' + mesh
        print >> outPSFile, '90 rotate', '0 -620 translate'
        print >> outPSFile, show(str(plane) + ' (' + str(round(maxGray, 2)) + '): '  + mesh)        
        print >> outPSFile, '120 120 translate'
        print >> outPSFile, scale, scale, 'scale'


        # Draw neurons.
        for node in nodesPerPlane[plane]:
            i, j, gray = node
            print >> outPSFile, str(19.7 + 39.4*j + ((i%2)*19.7)), str(17 + 34*i), str(1 - (gray / maxGray)), 'myLN'

        print >> outPSFile, 'showpage'

        if pageNumber % 5 == 0:
            print str((100.0 * (pageNumber - 1.0) / len(planes))) + '% complete..'
        
    outPSFile.close()

    print 'Done.'


main()
