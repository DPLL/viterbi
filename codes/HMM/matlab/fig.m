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



x = 1 : 1 : 10;
myObjScore = [0.95625 0.81625  0.72125 0.73  0.695 0.655 0.65875 0.56125 0.59875 0.5525 ];
sensorObjScore = [ 0.6 0.6025 0.59 0.6 0.6225  0.61 0.625 0.5875 0.60125 0.58625 ];
myStateScore = [ 0.9625 0.825 0.73625  0.7375 0.7025 0.665 0.66375 0.575  0.61125 0.5675 ];
sensorStateScore = [ 0.60625 0.61125 0.60625 0.60875  0.63375 0.62 0.63 0.605 0.615 0.60375 ];
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
title('Effect of degree of graph (objPerNode=5, recall = 0.6, pathLength=8)', 'FontSize', 18);

