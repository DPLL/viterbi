#! /usr/bin/env python2.7

import os
from pprint import pprint as pp
from string import digits
import scipy.io as sio
import numpy

root_data_dir = '..'
file_size_threshold = 1000L
all_files = filter(lambda x:x[0] != '.' and not x.startswith('recall') and not x.startswith('plots') and not x.startswith('Icon'), os.listdir(root_data_dir))
for file in all_files:
    file_path = os.path.join(root_data_dir, file) 
    if os.stat(file_path).st_size <= file_size_threshold:
        os.remove(file_path)
all_files = filter(lambda x:x[0] != '.' and not x.startswith('recall') and not x.startswith('plots') and not x.startswith('Icon'), os.listdir(root_data_dir))
plot_types = sorted(map(lambda x:x[:-1] if x[-1]=='.' else x, set(map(lambda x:x.translate(None, digits), all_files))))
plot_legends = ['myObjectScore', 'sensorObjectScore', 'myStateScore', 'asrStatePercentage']
num_legends = len(plot_legends)
mat = {}
for plot_type in plot_types:
    print plot_type
    files = filter(lambda x:x.startswith(plot_type), all_files)
    xs = sorted(map(lambda x:float(x.replace(plot_type, '')), files))
    num_xs = len(xs)
    plot_data = []
    for file in files:
        print file
        lines = open(os.path.join(root_data_dir, file)).readlines()
        for legend in plot_legends:
            legend_data = map(lambda x:float(x.strip().split()[-1]), filter(lambda x:x.startswith(legend), lines))
            plot_data.extend(legend_data[:-1])
    raw_array = numpy.array(plot_data)
    # data = raw_array.reshape(num_xs, num_legends, len(raw_array)/num_legends/num_xs)
    data = raw_array.reshape(num_xs, num_legends, len(raw_array)/num_legends/num_xs)
    mat[plot_type] = {'xs':xs, 'data':data}
sio.savemat('mat.mat', mat)
