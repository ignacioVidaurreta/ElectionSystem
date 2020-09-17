#!/bin/bash

VOTE_PATH="/tmp/g3/rand_votes"
function run_gazzilion_votes(){
  REPS=${1:-30}
  echo $REPS
  acum=0
  for i in $(seq $REPS); do
    generate_random_file "$acum"
    echo "$VOTE_PATH"_"$acum".csv
    ./run-vote.sh -DvotesPath="$VOTE_PATH"_"$acum".csv -DserverAddress="localhost" >> out.log&
    acum=$(( acum + 1 ))
  done
}

function run_concurrent_test() {
  REPS=${1:-10}
  acum=0
  for i in $(seq $REPS); do
    if [ "$(($acum % 3))" -eq 0 ]; then
      ./run-query.sh -DserverAddress="localhost" -DoutPath="/tmp/g3/result$acum.csv"&
    else
      generate_random_file "$acum"
      ./run-vote.sh -DvotesPath="$VOTE_PATH"_"$acum".csv -DserverAddress="localhost" >> out.log&
    fi
  done
  sleep 10
  ./run-management.sh -Daction=close -DserverAddress="localhost"
  ./run-query.sh -DserverAddress="localhost" -DoutPath="/tmp/resultFinal.csv"
}

function generate_random_file(){
  booths=(1000 1001 1002 1003 1004 1005)
  parties=("TIGER" "JACKALOPE" "LEOPARD" "LYNX") # Only using a few parties, not all.
  provinces=("JUNGLE" "SAVANNAH" "TUNDRA")
  for i in $(seq 3000); do
    echo "${booths[$((RANDOM % 6))]};${provinces[$((RANDOM % 3))]};TIGER|$((1 + RANDOM % 5)),LEOPARD|$((1 + RANDOM % 5)),JACKALOPE|$((1 + RANDOM % 5)),LYNX|$((1 + RANDOM % 5));${parties[$((RANDOM % 4))]}" >> "$VOTE_PATH"_"$1".csv
  done
  
}

function run_fiscal_test(){
  rm -f /tmp/g3/rand_votes_fiscal.csv
  generate_random_file "fiscal"
  echo "1002;JUNGLE;TIGER|1,LEOPARD|3,JACKALOPE|5,LYNX|1;JACKALOPE" >> /tmp/g3/rand_votes_fiscal.csv
  ./run-fiscal.sh -DserverAddress="localhost" -Did=1002 -Dparty=JACKALOPE > fiscal_output.txt&
  sleep 3
  echo "Opening elections..."
  ./run-management.sh -Daction=open -DserverAddress="localhost"
  echo "Emititing faux vote"
  ./run-vote.sh -DvotesPath=/tmp/g3/rand_votes_fiscal.csv -DserverAddress="localhost"
  lines=$(wc -l fiscal_output.txt | grep -o "[0-9]*")
  echo $lines
  if [ $lines -lt 3 ]; then
    echo "Test Failed! :("
    exit 1
  fi
  echo "Test passed"


}

function main(){
  # Setup testing workplace
  if [ ! -d "/tmp/g3/" ]; then
    mkdir /tmp/g3
  fi
  command="$1"
  shift
  case "$command" in
    "vote_bulk")
      run_gazzilion_votes "$@";
      ;;
    "concurrency")
      run_concurrent_test "$@";
      ;;
    "fiscal_test")
      run_fiscal_test "$@";
      ;;
    *)
      echo "Invalid argument $command"
      exit 1
      ;;
  esac
}

main "$@"
