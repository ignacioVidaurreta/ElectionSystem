#!/bin/bash

votes_path="/tmp/rand_votes.csv"




function run_gazzilion_votes(){
  REPS=${1:-30}
  echo $REPS
  for i in $(seq $REPS); do
    generate_random_file 3000
    ./run-vote.sh -DvotesPath="$votes_path" -DserverAddress="localhost" >> out.log&
  done
}

function run_concurrent_test() {
  REPS=${1:-10}
  for i in $(seq $REPS); do
    if [ "$((i % 3))" -eq 0 ]; then
      ./run-query.sh -DserverAddress="localhost" -DoutPath="/tmp/result$i.csv"&
    else
      echo "Hello"
      generate_random_file 3000
      ./run-vote.sh -DvotesPath="$votes_path" -DserverAddress="localhost" >> out.log&
    fi
  done
  sleep 10
  ./run-management.sh -Daction=close -DserverAddress="localhost"
  ./run-query.sh -DserverAddress="localhost" -DoutPath="/tmp/resultFinal.csv"
}

function generate_random_file(){
  rm -rf $votes_path
  booths=(1000 1001 1002 1003 1004 1005)
  parties=("TIGER" "JACKALOPE" "LEOPARD" "LYNX") # Only using a few parties, not all.
  provinces=("JUNGLE" "SAVANNAH" "TUNDRA")

  # The first line overwrites what was written before
  for i in $(seq "$1"); do
    echo "${booths[$((RANDOM % 6))]};${provinces[$((RANDOM % 3))]};TIGER|$((1 + RANDOM % 5)),LEOPARD|$((1 + RANDOM % 5)),JACKALOPE|$((1 + RANDOM % 5)),LYNX|$((1 + RANDOM % 5));${parties[$((RANDOM % 4))]}" >> $votes_path
  done
  
}


function set_up_testing(){
  rm -rf out.log
  generate_random_file 100
}

function main(){
  set_up_testing
  command="$1"
  shift
  case "$command" in
    "vote_bulk")
      run_gazzilion_votes "$@";
      ;;
    "concurrency")
      run_concurrent_test "$@";
      ;;
    *)
      echo "Invalid argument $command"
      exit 1
      ;;
  esac
}

main "$@"
