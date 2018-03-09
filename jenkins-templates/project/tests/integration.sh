#!/bin/bash

WORKSPACE=`pwd`
npm install ../tests/resources
node_modules/.bin/gulp wdio-sauce-grade1

cd ${RESULTSDIR}
 
if ! (ls *.json >/dev/null 2>&1 ); then 
   exit 1 
else
   find . -type f -name "* *.json" -exec bash -c 'mv "$0" "${0// /_}"' {} \;
   sed -i 's+'${WORKSPACE}'/++g'  *.* || true
fi
