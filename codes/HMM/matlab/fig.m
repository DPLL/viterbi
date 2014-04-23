% 
% x = 1: 0.1 : 2;
% map = [0.209 0.2098 0.2104 0.2111 0.2121 0.2127 0.2134 0.214 0.2148 0.2152 0.2158];
% plot(x, map);
% xlabel('k1');
% ylabel('MAP');
% title('myBM25 MAP vs. k1');

% x = 0 : 200 : 1000;
% map = [0.216 0.2158 0.2158 0.2158 0.2158 0.2158];
% plot(x, map);
% xlabel('k3');
% ylabel('MAP');
% title('myBM25 MAP vs. k3');

% x = 0.1 : 0.1 : 0.8;
% myObjScore = [0.8874237114561513 0.7710148111716169 0.8188136148113507 0.8153660691348886 0.7777983333242986 0.8333527484521154 .833911606491867 0.9374006721189054 ];
% sensorObjScore = [0.4280903210869078 0.4204277492231784 0.42821688716716155 0.4341548273246564 0.42620940445851474 0.42637851647913144 0.41786966778730933 .42669586797426684];
% myStateScore = [0.8333333333333333 0.6535714285714286 0.7166666666666667 0.715 0.6416666666666666 0.7333333333333333 0.75 0.9];
% plot(x, myObjScore, '-b', 'LineWidth', 12);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 12)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 12);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('density');
% ylabel('fidelity');
% title('Effect of density');


% x = 1 : 1 : 10;
% myObjScore = [1 0.9771725888867018 0.9152789394043515 0.934406780881956 0.8366892049584596 0.8333964011333865 0.8315538636004332 0.8152271090749859 0.8240515249974066 0.8234017763074337 ];
% sensorObjScore = [0.41715148575152394 0.4239812206660624 0.411991013011413 0.44097550655017737 0.43171120580690225 0.42925454197531105 0.43925454197531105 0.42548027916387543 0.42286834243426014 0.41780705677830243 ];
% myStateScore = [1 0.9666666666666666 0.8666666666666666 0.9 0.7333333333333334 0.7333333333333333 0.7333333333333332 0.7 0.7333333333333334 0.725];
% plot(x, myObjScore, '-b', 'LineWidth', 12);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 12)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 12);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('objPerNode');
% ylabel('fidelity');
% title('Effect of object per node (density=0.7, dimension=30, nodeNum=10, stdDev=30)');

% x = 10 : 1 : 19;
% myObjScore = [1 1 1 0.9806476181570577 1 1 0.9770237390411374 0.9820366917610416 1.0 0.9791427438114411 ];
% sensorObjScore = [0.6638286159009998 0.658143359908764 0.6354936692655665 0.6210497114238887 0.6004358468874997 0.5812841879648349 0.5742958481625683 0.5612905040992195 0.5478207941805118 0.5369640825223825];
% myStateScore = [1 1 1 0.975 1 1 0.9666666666666666 0.975 1.0 0.9666666666666668];
% plot(x, myObjScore, '-b', 'LineWidth', 12);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 12)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 12);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('stdDev');
% ylabel('fidelity');
% title('Effect of standard Deviation (density=0.6, dimension=30, nodeNum=10)');
% 
% x = 1 : 1 : 10;
% myObjScore = [1 1.0 1 1 1 1 1 1 1 1];
% sensorObjScore = [0.6833422439219224 0.6754776712595074 0.6675258682623834 0.6592576281314356 0.650144551221691 0.6430008260872162 0.6342200580932328 0.6199228865 0.6004675280212762 0.5985776740864552];
% myStateScore = [1 1.0 1 1 1 1 1 1 1 1];
% plot(x, myObjScore, '-b', 'LineWidth', 6);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 6)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 6);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('mean');
% ylabel('fidelity');
% title('Effect of mean (density=0.6, dimension=30, nodeNum=30, stdDev=10)');

% x = 10 : 2 : 30;
% myObjScore = [0.8243330669182874 0.8407568412027503 0.8891109740140946  0.892008216638352 0.9205598976346707 0.9354847372202745 0.9769556047263415  0.9755839811911761 0.9785566435963527  0.9764048317686737 0.9750283098616332];
% sensorObjScore = [0.5489641510935381 0.5349786549177286 0.5121957198844216 0.5030894945749249 0.49921148601760257 0.5159035755958441 0.5168998466620696 0.5113485632528767 0.518849512375595  0.5161726924850997 0.5214361460048369];
% myStateScore = [.7  0.7333333333333333 0.8333333333333334 0.8333333333333333 0.8666666666666668  0.9 0.9666666666666666 0.9666666666666668 0.9666666666666668 0.9666666666666668 0.9666666666666668];
% plot(x, myObjScore, '-b', 'LineWidth', 6);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 6)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 6);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('dimension');
% ylabel('fidelity');
% title('Effect of dimension (density=0.6, objPerNode=10, nodeNum=30, range=100, mean=0, stdDev=20)');
% 
% x = 2 : 2 : 14;
% myObjScore = [1 0.9786608510371732 0.956965347901981 0.9552521276203674 0.9150825647538984 0.9101918901911118 0.8952892982114318];
% sensorObjScore = [0.5180785387162601 0.5053624270194889 0.5063329209881438 0.5370489145057558 0.5275913426213635  0.5372546046726956  0.5007008449762572];
% myStateScore = [1 0.9666666666666666 0.9333333333333332  0.9333333333333332  0.8666666666666666 0.8666666666666668  0.8333333333333333];
% plot(x, myObjScore, '-b', 'LineWidth', 6);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 6)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 6);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('objPerNode');
% ylabel('fidelity');
% title('Effect of objPerNode (density=0.6, dimension=20, nodeNum=30, range=100, mean=0, stdDev=20)');


% figure(1)
% subplot(1,2,1);
% x = 0.1 : 0.1 : 0.8;
% myObjScore = [0.9700000000000002 0.9479999999999997 0.845 0.8725 0.8366666666666666 0.8600000000000001 0.8633333333333334 0.9166666666666667 ];
% sensorObjScore1 = [0.9085714285714288 0.8939999999999995 0.885 0.9075 0.8833333333333335 0.8900000000000002 0.8833333333333337 0.9233333333333336 ];
% myStateScore1 = [0.9728571428571429 0.9479999999999997 0.845 0.88 0.84 0.8600000000000001 0.8666666666666668 0.9166666666666667 ];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore1, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore1, 'yo--', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('density');
% ylabel('fidelity');
% title('Effect of density(numPerNode=10, nodeNum=20, recall=0.9)');
% 
% subplot(1,2,2);
% x1 = 0.1 : 0.1 : 0.8;
% myObjScore1 = [0.964 0.9279999999999997 0.9419999999999997 0.8759999999999996 0.9039999999999994 0.9079999999999997 0.9099999999999996 0.8999999999999995 ];
% sensorObjScore1 = [0.8939999999999995 0.8919999999999995 0.9019999999999996 0.8879999999999995 0.9139999999999995 0.9039999999999994 0.9099999999999996 0.9059999999999994 ];
% myStateScore1 = [0.966  0.9379999999999998 0.9419999999999997 0.8839999999999997 0.9059999999999994  0.9139999999999996 0.9119999999999995 0.9059999999999995 ];
% plot(x1, myObjScore1, '-b', 'LineWidth', 7);
% hold on;
% plot(x1, sensorObjScore1, '*r-', 'LineWidth', 7)
% hold on;
% plot(x1, myStateScore1, 'yo--', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('density');
% ylabel('fidelity');
% title('Effect of density(numPerNode=10, nodeNum=20, recall=0.9, pathLength=5)');


% x = 1 : 1 : 8;
% myObjScore = [0.8127619047619044 0.6691794871794866 0.5817619047619046 0.4808928571428572 0.41633333333333344 0.47799999999999976 0.4755 0.39];
% sensorObjScore = [0.474361111111111 0.5187097902097901 0.504218253968254 0.48136904761904764 0.4990000000000001 0.524333333333333 0.5179999999999998 0.515];
% myStateScore = [0.9242539682539682 0.747910256410256 0.6531666666666667 0.5358333333333334 0.45433333333333326 0.511 0.49699999999999994 0.4075];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('average in/out degree', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of degree (objectNumPerNode=3, dimension=10, nodeNum=30, range=100, stdDev=30)', 'FontSize', 18);


% x = 1 : 1 : 9;
% myObjScore = [0.8165198412698412 0.6810155122655119 0.6540176767676766 0.5848991841491842   0.5408703796203796 0.37857647907647896 0.3723491785991787 0.36861871461871465 0.36744211344211336 ];
% sensorObjScore = [0.7204426406926404 0.576046176046176 0.5152095959595959 0.4778739316239315  0.4328843101343104 0.3776341991341991 0.3795321345321344 0.36098742923742927 0.3719681429681429 ];
% myStateScore = [0.8165198412698412 0.7256998556998556 0.7349015151515149 0.6613517871017873  0.64647205572205594 0.4444556277056275 0.48881951381951366 0.4706531801531802 0.4934731934731934 ];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 0);
% xlabel('objPerNode', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of object per node (avgDegree=2, dimension=10, nodeNum=30)', 'FontSize', 18);

% 
% x = 0.1 : 0.1 : 0.9;
% myObjScore = [0.36666666666666664 0.5166666666666667 0.6666666666666666 0.7750000000000001 0.8083333333333332 0.875 0.9583333333333334 0.9583333333333334 0.9666666666666666 ];
% sensorObjScore = [0.09166666666666667 0.2166666666666667 0.2666666666666667 0.4083333333333334 0.5083333333333332 0.575  0.6916666666666667 0.8083333333333332 0.9166666666666666 ];
% myStateScore = [0.36666666666666664 0.5166666666666667 0.6749999999999999 0.7750000000000001 0.8083333333333332 0.875 0.9583333333333334 0.9583333333333334 0.9666666666666666 ];
% sensorStateScore = [0.09166666666666667 0.2166666666666667  0.2750000000000001 0.4083333333333334 0.5083333333333332 0.575  0.6916666666666667 0.8083333333333332 0.9166666666666666 ];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('recall', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of recall (avgDegree=2, objPerNode=2, height=6, pathLength=6)', 'FontSize', 18);



% x = 1 : 1 : 10;
% myObjScore = [0.95625 0.81625  0.72125 0.73  0.695 0.655 0.65875 0.56125 0.59875 0.5525 ];
% sensorObjScore = [ 0.6 0.6025 0.59 0.6 0.6225  0.61 0.625 0.5875 0.60125 0.58625 ];
% myStateScore = [ 0.9625 0.825 0.73625  0.7375 0.7025 0.665 0.66375 0.575  0.61125 0.5675 ];
% sensorStateScore = [ 0.60625 0.61125 0.60625 0.60875  0.63375 0.62 0.63 0.605 0.615 0.60375 ];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('degree', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of degree of graph (objPerNode=5, recall = 0.6, pathLength=8)', 'FontSize', 18);

% 
% 
% x = 1 : 1 : 6;
% myObjScore = [ 0.6 0.65 0.6733333333333335 0.7575 0.885999999999999  0.9066666666666663 ];
% sensorObjScore = [ 0.6 0.595  0.58 0.575 0.584 0.585];
% myStateScore = [ 0.6 0.65  0.6733333333333335 0.7575 0.885999999999999 0.9066666666666663 ];
% sensorStateScore = [ 0.6 0.595 0.58 0.575 0.584  0.585];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('pathLength', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of pathLength of Tree (avgDegree=2, objPerNode=2, recall=0.6,height=6)', 'FontSize', 18);


% x = 0.1 : 0.1 : 0.9;
% myObjScore = [ 0.125 0.2 0.35 0.53125 0.6375 0.725 0.90625 0.91875 0.975 ];
% sensorObjScore = [ 0.0875  0.20625 0.2625 0.36875 0.48125 0.55 0.7 0.83125 0.88125 ];
% myStateScore = [0.1375 0.225 0.36875 0.5375 0.65625 0.7375 0.91875 0.91875 0.975 ];
% sensorStateScore = [0.1  0.25625 0.2875 0.375 0.5 0.5625 0.7125 0.83125 0.88125];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('recall', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of Graph Recall (avgDegree=3, objPerNode=5, nodeNum=30, pathLength=8)', 'FontSize', 18);

% x = 1 : 1 : 10;
% myObjScore = [0.51 0.645 0.60966666666666668 0.66 0.626 0.6866666666666669 0.6814285714285714 0.7525 0.7888888888888882  0.7560000000000002 ];
% sensorObjScore = [0.51  0.61 0.5499999999998 0.5675 0.5879999999999999 0.5833333333333335 0.5657142857142858  0.60375  0.6199999999999999 0.6030000000000002
%  ];
% myStateScore = [0.51  0.655 0.61333333333332 0.68 0.6399999999999999 0.6900000000000002 0.701428571428571 0.76125 0.7988888888888883  0.7650000000000002];
% sensorStateScore = [0.51 0.62 0.553333333333333 0.59 0.6039999999999999 0.5900000000000003 0.5857142857142857 0.6125 0.6299999999999999  0.6140000000000003];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('pathLength', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of Graph pathLength (avgDegree=3, objPerNode=5, nodeNum=30, recall=0.6)', 'FontSize', 18);

% x = 1 : 10;
% myObjScore = [ 0.9083333333333333 0.97 0.8916666666666664 0.9033333333333328 0.9216666666666664 0.9433333333333335  0.8983333333333331  0.8949999999999992 0.8949999999999996 0.8799999999999999 ];
% sensorObjScore = [0.6233333333333333 0.5983333333333334 0.6183333333333334 0.6216666666666667 0.5999999999999999 0.5849999999999999 0.6466666666666666 0.625 0.5983333333333334 0.5900000000000001 ];
% myStateScore = [ 0.9083333333333333  0.9716666666666666 0.8933333333333331 0.9099999999999995  0.9249999999999998 0.9466666666666668 0.9049999999999998 0.8999999999999991 0.8966666666666663 0.8849999999999999 ];
% sensorStateScore = [0.6233333333333333  0.6 0.62  0.6283333333333334 0.6033333333333332 0.588333333333333 0.6533333333333333 0.6300000000000001 0.6 0.5950000000000001 ];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('objPerNode', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of objPerNode of Tree (avgDegree=2, pathLength=6, recall=0.6,height=6)', 'FontSize', 18);


% x = 1 : 10;
% myObjScore = [0.790625 0.754375 0.726875 0.779375 0.79375 0.77875 0.739375 0.68875 0.775625 0.725625 ];
% sensorObjScore = [ 0.60125 0.614375 0.606875 0.60625 0.62625 0.608125 0.586875 0.595 0.61375 0.609375 ];
% myStateScore = [0.790625 0.761875 0.73625 0.7925 0.8 0.7925 0.748125 0.699375 0.786875 0.738125];
% sensorStateScore = [ 0.60125 0.621875  0.6175 0.62125 0.6325 0.62312 0.595625 0.605625 0.62625 0.624375];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('objPerNode', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of objPerNode of Graph (avgDegree=3, pathLength=8, nodeNum=30, recall=0.6)', 'FontSize', 18);

% x = 15 : 3 : 42;
% myObjScore = [0.74687 0.72 0.713125 0.725 0.749375 0.735 0.809375 0.72625 0.683125 0.75625 ];
% sensorObjScore = [0.6225 0.581875 0.601875 0.600625 0.6025 0.620625 0.60625 .584375 0.573125 0.625625 ];
% myStateScore = [0.761875 0.736875 0.7275 0.74 0.75875 0.74625 0.818125 0.73375 0.69125 0.76 ];
% sensorStateScore = [0.638125 0.6 0.61625 0.6175 0.6125 0.633125 0.615 0.5925 0.5825 0.63];
% plot(x, myObjScore, '-b', 'LineWidth', 7);
% hold on;
% plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
% hold on;
% plot(x, myStateScore, 'yo--', 'LineWidth', 7);
% hold on;
% plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
% legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
% xlabel('nodeNum', 'FontSize', 18);
% ylabel('fidelity', 'FontSize', 18);
% title('Effect of nodeNum of Graph (avgDegree=3, pathLength=8, objPerNode=5, recall=0.6)', 'FontSize', 18);

x =  0.1 : 0.1 : 1;
myObjScore = [ 0.525 0.6333333333333332 0.7383333333333333 0.8616666666666666 0.895 0.9466666666666665 0.98 1 1 1];
sensorObjScore = [0.11499999999999996 0.21000000000000008 0.3166666666666667 0.39000000000000007 0.4933333333333334 0.5966666666666663 0.7033333333333331 0.8001 0.9003 1.0];
myStateScore = [0.525 0.6333333333333332 0.7383333333333333  0.8616666666666666 0.8983333333333334 0.9516666666666665 0.98 1 1 1];
sensorStateScore = [0.11499999999999996 0.21000000000000008 0.3166666666666667 0.39000000000000007 0.4966666666666668  0.5916666666666664 0.7033333333333331  0.8001 0.9003 1];
plot(x, myObjScore, '-b', 'LineWidth', 7);
hold on;
plot(x, sensorObjScore, '*r-', 'LineWidth', 7)
hold on;
plot(x, myStateScore, 'yo--', 'LineWidth', 7);
hold on;
plot(x, sensorStateScore, 'g-', 'LineWidth', 7);
legend('myObjScore', 'sensorObjScore', 'myStateScore', 'sensorStateScore');
xlabel('recall', 'FontSize', 18);
ylabel('fidelity', 'FontSize', 18);
title('Effect of recall of Tree(rooted) (avgDegree=3, objPerNode=5, height=5, pathLength=6)', 'FontSize', 18);

