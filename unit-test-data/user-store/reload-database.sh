#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TARGET_DIR=$SCRIPT_DIR/../../../../target
mkdir -p $TARGET_DIR

SAMPLE_USERS=$SCRIPT_DIR/../sample-users.txt
IMPORT_TEMPLATE=$SCRIPT_DIR/users-template.sql
IMPORT_SCRIPT=$TARGET_DIR/users-import.sql

# drop old
psql -w -Uauth -dauth -c "DROP TABLE IF EXISTS user_group_relation, users, groups"

# remove old sql script
rm -f $IMPORT_SCRIPT

# create new (empty)
psql -w -Uauth -dauth -f ${SCRIPT_DIR}/schema/users.sql
psql -w -Uauth -dauth -f ${SCRIPT_DIR}/schema/groups.sql
psql -w -Uauth -dauth -f ${SCRIPT_DIR}/schema/user-group-relation.sql

# Create we'll just make the first two users sponsors
limit=2
count=0
while read u; do
    count=$(($count+1))
    cn='dev_'${u// /_}
    parts=(${u// / })
    camelLast=${parts[1]}
    camelFirst=${parts[0]}
    alias='dev_'` echo $camelLast | tr '[:upper:]' '[:lower:]'`
    sed -e "s/%ID%/$count/g" -e "s/%CN%/$cn/g" -e "s/%LAST%/$camelLast/g" -e "s/%FIRST%/$camelFirst/g"  $IMPORT_TEMPLATE >> $IMPORT_SCRIPT
    if [ $count -le "$limit" ]; then
        # add to admin group
        echo "INSERT INTO user_group_relation (user_id, group_id) VALUES ($count, 1);" >> $IMPORT_SCRIPT
    fi
    # all users added to reader group
    echo "INSERT INTO user_group_relation (user_id, group_id) VALUES ($count, 2);" >> $IMPORT_SCRIPT
    echo >> $IMPORT_SCRIPT
done <$SAMPLE_USERS

# load sample data
psql -w -Uauth -dauth -c "INSERT INTO groups (id, groupname) VALUES ( 1, 'admin' ),( 2, 'reader' );"
psql -w -Uauth -dauth -f $IMPORT_SCRIPT

