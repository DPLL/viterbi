For laptop, all the codes are in the directory of /home/david/Dropbox/projects/ER/speech/viterbi/codes/HMM/src .  And this directory is under version control of github.  However, when we open up the codes in eclipse, we open it through /home/david/workspace/viterbi/codes.

For the desktop, all the codes are in the directory of /home/workplace/viterbi/codes/HMM/src, and it is also in synch with github.  If we want to open it up in eclipse, it should be in the same place with the github directory.


for special2.out
    			graphGen.density = 0.5;
    			graphGen.objectPerNode = 2;
    			graphGen.dimension = 5;
    			graphGen.numVertex = 10;
    			graphGen.range = 100;
    			graphGen.mean = 0;
    			graphGen.stdDev = 0;
    			
    			graphGen.numEdge = 45;

for special.out
    			graphGen.density = 0.5;
    			graphGen.objectPerNode = 2;
    			graphGen.dimension = 1;
    			graphGen.numVertex = 3;
    			graphGen.range = 100;
    			graphGen.mean = 0;
    			graphGen.stdDev = 0;
    			
    			graphGen.numEdge = 6;

The correct results: (with similarity function of power of five)
V:
[[-1.0986122886681098, -1.0986122886681098, -1.0986122886681098], [-2.8150867495408867, -1.791759469228055, -2.2939290902006744], [-2.9870762707606198, -Infinity, -4.160720023492785]]
B:
[[0, 1, 2], [0, 1, 2], [2, 2, 1]]
X:
[[[0.0], [0.0], [0.0]], [[48.20234253015849], [66.71035279767145], [76.26586556148591]], [[94.10846327675657], [76.26586556148591], [76.26586556148591]]]



for graph2.out

numEdge is: 3
The randomGraph.vertexSet() is: [0 , 1 , 2 ]
([0 , 1 , 2 ], [(1 ,0 ), (1 ,2 ), (2 ,1 )])
3
[(2  : 1 ), (1  : 0 )]
The trueStates is: [2, 1, 0]
The trueObjects is as the following:
[37.38957217505584]
[79.36935937500014]
[5.896338987409688]

=================================================================
NOW WE ARE IN THE NEW ERA OF SPECIFYING THE CONFUSION MATRIX!!!

interesting setting:
when the density is 0.4, the true states has four nodes, then my algorithm is better, but when it comes to the density of 0.5 or 0.3, where there are only 3 nodes to 4 nodes.

So the number of nodes in the path really matters.

-----------------------------------------------------------------

numEdge is: 10
The randomGraph.vertexSet() is: [0 , 1 , 2 , 3 , 4 ]
5
([0 , 1 , 2 , 3 , 4 ], [(1 ,0 ), (0 ,3 ), (2 ,3 ), (3 ,0 ), (3 ,1 ), (3 ,2 ), (2 ,4 ), (4 ,0 ), (4 ,2 ), (4 ,3 )])
start_prob: 0.2 verSet.size(): 5
emission_prob: 0.3333333333333333 graphGen.objectPerNode: 3
4
hoho
hoho
hoho
hoho
hoho
could not find such path!
[(1  : 0 ), (0  : 3 ), (3  : 2 ), (2  : 4 )]
WHAT???


-----------------------------------------------------------------

The trueStates is: [1, 4, 2, 3]
in the 262 th interation, haha!!!!
----------------
numEdge is: 10
The randomGraph.vertexSet() is: [0 , 1 , 2 , 3 , 4 ]
5
([0 , 1 , 2 , 3 , 4 ], [(0 ,1 ), (1 ,0 ), (0 ,2 ), (2 ,1 ), (0 ,3 ), (3 ,0 ), (3 ,2 ), (3 ,4 ), (4 ,1 ), (4 ,2 )])
start_prob: 0.2 verSet.size(): 5
emission_prob: 0.3333333333333333 graphGen.objectPerNode: 3
4
hoho
hoho
hoho
hoho
hoho
could not find such path!
[(2  : 1 ), (1  : 0 ), (0  : 3 ), (3  : 4 )]
WHAT???

----------------------------------------------------------------

numEdge is: 10
The randomGraph.vertexSet() is: [0 , 1 , 2 , 3 , 4 ]
5
([0 , 1 , 2 , 3 , 4 ], [(0 ,1 ), (1 ,0 ), (0 ,2 ), (1 ,3 ), (3 ,0 ), (3 ,1 ), (0 ,4 ), (2 ,4 ), (3 ,4 ), (4 ,1 )])
start_prob: 0.2 verSet.size(): 5
emission_prob: 0.3333333333333333 graphGen.objectPerNode: 3
3
[]
hoho
[]
hoho
[]
hoho
[]
hoho
[]
hoho
could not find such path!
[(2  : 4 ), (4  : 1 ), (1  : 0 )]
WHAT???

