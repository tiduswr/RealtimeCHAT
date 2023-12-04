rm -f metrics.csv

while true; do
  current_time=$(date +"%H:%M:%S")
  pod_metrics=$(kubectl top pods --namespace=webchat --no-headers=true)
  formatted_metrics=$(echo "$pod_metrics" | awk -v time="$current_time" '{ printf "%s %s\n", time, $0 }')
  echo "$formatted_metrics" | tee -a metrics.csv
  sleep 5
done
