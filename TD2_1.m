hold on
for f=1:5
    %fd1 = f*2/1000;
    fd2 = f*2/10;
    n1 = 1:64;
    n2 = [8;16;32;64];
    %S1 = n2./(1+(n2-1).*fd1);
    S2 = n1./(1+(n1-1).*fd2);
    
    plot(S2)
end

hold off