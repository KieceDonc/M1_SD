for x in {1..5};do
  xterm -e "java Client; sleep 100" &
done