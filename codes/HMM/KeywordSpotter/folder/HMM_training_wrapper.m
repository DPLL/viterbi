%% Ignore this part
% randIdx=randperm(462, 30);
% filename=cellfun(@(number) sprintf('trainSignals/greasy_%02d.wav', number), num2cell(1:20), 'UniformOutput', 0);
% 
% cellfun(@(filename,y) audiowrite(filename, y, 16000), filename, words(7,randIdx(1:20)), 'UniformOutput', 0);
% 
% filename=cellfun(@(number) sprintf('testSignals/greasy_%02d.wav', number), num2cell(1:10), 'UniformOutput', 0);
% cellfun(@(filename,y) audiowrite(filename, y, 16000), filename, words(7,randIdx(21:30)), 'UniformOutput', 0);
% 
% filename=cellfun(@(number) sprintf('testSignals/dark_%02d.wav', number), num2cell(1:10), 'UniformOutput', 0);
% cellfun(@(filename,y) audiowrite(filename, y, 16000), filename, words(4,randIdx(1:10)), 'UniformOutput', 0);
% 

%% Load signals
addpath('audioread');
trainFilePaths=dir('trainSignals_bak/*.wav');
trainSignals=cellfun(@(trainFilePaths) audioread(['trainSignals_bak/', trainFilePaths]), {trainFilePaths.name}, 'UniformOutput', 0);


testFilePaths=dir('testSignals_bak/*.wav');
testSignals=cellfun(@(testFilePaths) audioread(['testSignals_bak/', testFilePaths]), {testFilePaths.name}, 'UniformOutput', 0);


%% Convert into MFCC
addpath('rastamat');
FRAMESIZE=512;
WINDOW=hann(FRAMESIZE, 'periodic');

trainMFCC=cellfun(@(x) melfcc(abs(x), 16000), trainSignals, 'UniformOutput', 0);
testMFCC=cellfun(@(x) melfcc(abs(x), 16000), testSignals, 'UniformOutput', 0);

trainMFCCdelta=cellfun(@(x) deltas(x), trainMFCC, 'UniformOutput', 0);
testMFCCdelta=cellfun(@(x) deltas(x), testMFCC, 'UniformOutput', 0);

trainMFCCdelta2=cellfun(@(x) deltas(x), trainMFCCdelta, 'UniformOutput', 0);
testMFCCdelta2=cellfun(@(x) deltas(x), testMFCCdelta, 'UniformOutput', 0);

finalTrainFeature=cellfun(@(x,y,z) [x;y;z], trainMFCC, trainMFCCdelta, trainMFCCdelta2, 'UniformOutput', 0)
finalTestFeature=cellfun(@(x,y,z) [x;y;z], testMFCC, testMFCCdelta, testMFCCdelta2, 'UniformOutput', 0);

% size(finalTrainFeature)
% finalTrainFeature;
% cell2mat(finalTrainFeature)

%% HMM Learning for "Greasy"
addpath(genpath('HMMall'))
M = 3; % for greasy, the previous value of M is 3
Q = 5; % for greasy, the previous value of Q is 5
O = size(finalTrainFeature{1},1);
cov_type = 'diag';
% initial guess of parameters
prior0 = normalise(rand(Q,1));
transmat0 = mk_stochastic(rand(Q,Q));
[mu0, Sigma0] = mixgauss_init(Q*M, cell2mat(finalTrainFeature), cov_type);
mu0 = reshape(mu0, [O Q M]);
Sigma0 = reshape(Sigma0, [O O Q M]);
mixmat0 = mk_stochastic(rand(Q,M));
[LL, prior1, transmat1, mu1, Sigma1, mixmat1] = ...
    mhmm_em(cell2mat(finalTrainFeature), prior0, transmat0, mu0, Sigma0, mixmat0, 'max_iter', 20);
%% HMM detection 

loglik=cellfun(@(x) mhmm_logprob(x, prior1, transmat1, mu1, Sigma1, mixmat1), finalTestFeature, 'UniformOutput', 0);
loglik=cell2mat(loglik); % from cell array results to double array
lengths=cell2mat(cellfun(@(x) length(x), testSignals, 'UniformOutput', 0)); % calculate the lengths of test signals
loglik=bsxfun(@rdivide, loglik, lengths); % normalize result by the lengths
loglik
figure;
plot(loglik) % check out that the log-likelihoods of the test items 11~20 (greasy) are bigger than 1~10 (dark)
xlabel('number of test data points');
ylabel('log likelihood');

