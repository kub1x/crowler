PHANTOM_PATH="/usr/bin/phantomjs"
#SCRIPT=kub1x.sowl
SCRIPT=$1
java -jar crowler.jar --scenario "${SCRIPT}" --rdfDir results --phantom "${PHANTOM_PATH}"
