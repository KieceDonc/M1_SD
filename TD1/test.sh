for i in `cat myhost.txt`
do
	ssh $i "cat /proc/cpuinfo"
done
