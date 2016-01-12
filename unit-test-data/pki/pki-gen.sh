#!/bin/sh
set -x
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TARGET_DIR=$SCRIPT_DIR/../../../../target
CERTS_DIR=$TARGET_DIR/certs
mkdir -p $CERTS_DIR

SAMPLE_USERS=$SCRIPT_DIR/../sample-users.txt

SERVER_NAME=localhost
USER_KEYSTORE=$CERTS_DIR/userstore.jks
SERVER_KEYSTORE=$CERTS_DIR/serverstore.jks
PASSWORD=changeme

runGenkey() {
    ALIAS=$1
    CN=$2
    PASSWORD=$3
    KEYSTORE_FILE=$4

    keytool -genkey -alias $ALIAS -keyalg RSA -validity 365 -keypass $PASSWORD -storepass $PASSWORD -keystore $KEYSTORE_FILE -storetype JKS -dname "CN=$CN, OU=$CN, O=$CN, L=San Diego, S=CA, C=US"
}

runExportPublic() {
    ALIAS=$1
    PASSWORD=$2
    KEYSTORE_FILE=$3

    keytool -export -alias $ALIAS -file ${CERTS_DIR}/${ALIAS}.cer -storepass $PASSWORD -keystore $KEYSTORE_FILE
}

runExportPrivate() {
    ALIAS=$1
    PASSWORD=$2
    KEYSTORE_FILE=$3

    keytool -importkeystore -srckeystore $KEYSTORE_FILE -destkeystore ${CERTS_DIR}/${ALIAS}.p12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass $PASSWORD -deststorepass $PASSWORD -srcalias $ALIAS -destalias $ALIAS -srckeypass $PASSWORD -destkeypass $PASSWORD -noprompt
}
runTrustPublic() {
    ALIAS=$1
    PASSWORD=$2

    keytool -import -noprompt -trustcacerts -alias $ALIAS -file ${CERTS_DIR}/${ALIAS}.cer -keystore $SERVER_KEYSTORE -storepass $PASSWORD
}

createNewIdentity() {
    ALIAS=$1
    CN=$2

    $(runGenkey $ALIAS $CN $PASSWORD $USER_KEYSTORE)

    $(runExportPublic $ALIAS $PASSWORD $USER_KEYSTORE)

    $(runExportPrivate $ALIAS $PASSWORD $USER_KEYSTORE)

    $(runTrustPublic $ALIAS $PASSWORD)

}

createServerKeystore() {
    ALIAS=$1
    CN=$2
    $(runGenkey $ALIAS $CN $PASSWORD $SERVER_KEYSTORE)
}

# Clean up previous artifacts
rm $CERTS_DIR/*.cer $CERTS_DIR/*.jks $CERTS_DIR/*.p12

# Create a bunch of sample users
while read u; do
    cn='dev_'${u// /_}
    parts=(${u// / })
    camelLast=${parts[1]}
    alias='dev_'` echo $camelLast | tr '[:upper:]' '[:lower:]'`
    $(createNewIdentity $alias $cn)
done <$SAMPLE_USERS

## Create the server cert
$(createServerKeystore $SERVER_NAME $SERVER_NAME)


