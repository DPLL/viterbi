function plots()

plotLength = 800;
plotHeight = 450;
fatplotfontsize = 22;
squareplotfontsize = 22;
fontweight = 'bold';
linewidth = 3;
markersize = 11;
fontname = 'times';
legendloc = 'southeast';

xmargin = 0.5;

mat = load('mat.mat');
targetdir = 'figures';
plotNames = fields(mat);
numFields = length(plotNames);
for iPlotName = 1:numFields
    plotName = plotNames{iPlotName};
    xs = mat.(plotName).('xs');
    data = permute(mat.(plotName).('data'), [3 2 1]);
    disp(length(xs))
    disp(size(data))
    numClusters = length(xs);
    clusterMeans = [];
    clusterStds = [];
    for iCluster = 1 : numClusters
        clusterData = data(:,:,iCluster);
        clusterMeans = [clusterMeans ; mean(clusterData)];
        clusterStds = [clusterStds ; std(clusterData)];
    end
    
    close all
    figure;
    set(gcf, 'PaperPositionMode', 'auto', 'Position', [0 0 plotLength plotHeight]);
    clusterErrors = cat(3, clusterStds, clusterStds);
    barwitherr(clusterErrors, clusterMeans);
    xlabel(plotName, 'fontsize', squareplotfontsize, 'fontname', fontname, 'fontweight', fontweight);
    ylabel('fidelity', 'fontsize', squareplotfontsize, 'fontname', fontname, 'fontweight', fontweight);
    set(gca, 'xticklabel', xs, 'fontname', fontname, 'fontsize', fatplotfontsize, 'fontweight', 'bold');
    xlim([1-xmargin length(xs)+xmargin])
    ylim([0 1])
    legend({'myObjectScore', 'sensorObjectScore', 'myStateScore', 'asrStatePercentage'}, 'location', legendloc, 'fontsize', squareplotfontsize, 'fontname', fontname, 'fontweight', fontweight);
    saveas(gcf, [targetdir, filesep, plotName, '.eps'], 'epsc');
%     saveas(gcf, [targetdir, filesep, field, '.fig']);
%     saveas(gcf, [targetdir, filesep, field, '.png']);
end

end