SNR = 1;

inFiles = dir('./green_0*.wav');

for i = 1:length(inFiles)
    inName = inFiles(i).name;
    outName = strcat('green_noise', num2str(SNR), '_0', num2str(i), '.wav');
    x = wavread(inName);
    y = awgn(x, SNR, 'measured');
    wavwrite(y, 16000, 16, outName);
end