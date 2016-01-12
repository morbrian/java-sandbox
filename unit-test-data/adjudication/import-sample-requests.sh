#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TARGET_DIR=$SCRIPT_DIR/../../../../target
mkdir -p $TARGET_DIR

SAMPLE_USERS=$SCRIPT_DIR/../sample-users.txt
IMPORT_TEMPLATE=$SCRIPT_DIR/user-info-import-template.sql
IMPORT_SCRIPT=$TARGET_DIR/user-info-import.sql

rm -f $IMPORT_SCRIPT

# Create we'll just make the first two users sponsors
while read u; do
    count=$(($count+1))
    cn='dev_'${u// /_}
    parts=(${u// / })
    camelLast=${parts[1]}
    camelFirst=${parts[0]}
    alias='dev_'` echo $camelLast | tr '[:upper:]' '[:lower:]'`
    sed -e "s/%CN%/$cn/g" -e "s/%LAST%/$camelLast/g" -e "s/%FIRST%/$camelFirst/g"  $IMPORT_TEMPLATE >> $IMPORT_SCRIPT
done <$SAMPLE_USERS

psql -Uauth -dauth -c 'delete from user_info *'
psql -Uauth -dauth --file $IMPORT_SCRIPT
