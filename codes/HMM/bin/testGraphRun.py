#!/usr/bin/python

# This file is the parallel verion of graphRun.py, which spawns multiple processes to run the test.  testTreeRun.py is the parallel version of treeRun.py.  2testTreeRun is also a parallel version of treeRun.py.  It by so far only tests objectPerNode on rooted tree.

import os
import re
import array
from subprocess import Popen, PIPE, STDOUT
import sys
from multiprocessing import Process

cmd = 'java -cp /home/david/eclipse/jgrapht-0.9.0/lib/jgraph-5.13.0.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-core-0.9.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-demo-0.9.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-ext-0.9.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-ext-0.9.0-uber.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgraphx-2.0.0.1.jar:/home/david/eclipse/jung2-2_0_1/collections-generic-4.01.jar:/home/david/eclipse/jung2-2_0_1/colt-1.2.0.jar:/home/david/eclipse/jung2-2_0_1/concurrent-1.3.4.jar:/home/david/eclipse/jung2-2_0_1/j3d-core-1.3.1.jar:/home/david/eclipse/jung2-2_0_1/jung-3d-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-3d-demos-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-algorithms-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-api-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-graph-impl-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-io-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-jai-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-jai-samples-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-samples-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-visualization-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/stax-api-1.0.1.jar:/home/david/eclipse/jung2-2_0_1/vecmath-1.3.1.jar:/home/david/eclipse/jung2-2_0_1/wstx-asl-3.2.6.jar:. Simulation2RandomGraph '

def runCmd(cmd, outputPath):
    #print cmd
    out = open(outputPath, "w")
    pathResult = Popen(cmd, shell=True, stdout=out)
    pathResult.wait()
    out.close()


def main():
    degree = '3 '
    objPerNode = '5 '    
    #nodeNum = '30 '
    recall = ' 0.6 '
    pathLength = '8 '
    graphNum = '2 '
    runPerGraph = '1000'

    recallList = [0,1, 0,2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]
    processList = []

#    for i in recallList:
#    for i in range(1, 11):
    for i in range(15, 45, 3):
        args = degree + objPerNode + str(i) + recall + pathLength + graphNum + runPerGraph
        #outputPath = '../results/testGraphPathLength' + str(i)
        outputPath = '../results/testGraphNodeNum' + str(i)
        exeCmd = cmd + args
        p = Process(target=runCmd, args=(exeCmd, outputPath))
        processList.append(p)

    for p in processList:
        p.start()
    for p in processList:
        p.join()
        
if __name__ == '__main__':
    main()
