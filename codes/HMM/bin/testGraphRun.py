#!/usr/bin/python

import os
import re
import array
from subprocess import Popen, PIPE, STDOUT
import sys
from multiprocessing import Process

cmd = 'java -cp /home/david/eclipse/jgrapht-0.9.0/lib/jgraph-5.13.0.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-core-0.9.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-demo-0.9.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-ext-0.9.0.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgrapht-ext-0.9.0-uber.jar:/home/david/eclipse/jgrapht-0.9.0/lib/jgraphx-2.0.0.1.jar:/home/david/eclipse/jung2-2_0_1/collections-generic-4.01.jar:/home/david/eclipse/jung2-2_0_1/colt-1.2.0.jar:/home/david/eclipse/jung2-2_0_1/concurrent-1.3.4.jar:/home/david/eclipse/jung2-2_0_1/j3d-core-1.3.1.jar:/home/david/eclipse/jung2-2_0_1/jung-3d-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-3d-demos-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-algorithms-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-api-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-graph-impl-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-io-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-jai-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-jai-samples-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-samples-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/jung-visualization-2.0.1.jar:/home/david/eclipse/jung2-2_0_1/stax-api-1.0.1.jar:/home/david/eclipse/jung2-2_0_1/vecmath-1.3.1.jar:/home/david/eclipse/jung2-2_0_1/wstx-asl-3.2.6.jar:. Simulation2 '

def runCmd(cmd, outputPath):
    #print cmd
    out = open(outputPath, "w")
    pathResult = Popen(cmd, shell=True, stdout=out)
    pathResult.wait()
    out.close()


def main():
    degree = '3 '
    height = '5 '
    objPerNode = '5 '    
    recall = '0.6 '
    #pathLength = ' 6 '
    runTime = ' 100'

    recallList = [0.7, 0.8, 0.9, 1.0]
    processList = []

#    for i in recallList:
    for i in range(6, 7):
        args = degree + height + objPerNode + recall + str(i) + runTime
        #outputPath = '../results/testTreeRootRecall' + str(i)
        outputPath = '../results/testTreeRootPathLength' + str(i)
        exeCmd = cmd + args
        p = Process(target=runCmd, args=(exeCmd, outputPath))
        processList.append(p)

    for p in processList:
        p.start()
    for p in processList:
        p.join()
        
if __name__ == '__main__':
    main()
