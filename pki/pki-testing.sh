#!/bin/sh
set -x
USER_KEYSTORE=./userstore.jks
SERVER_KEYSTORE=./serverstore.jks
PASSWORD=ncsa1235813

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

    keytool -export -alias $ALIAS -file ${ALIAS}.cer -storepass $PASSWORD -keystore $KEYSTORE_FILE
}

runExportPrivate() {
    ALIAS=$1
    PASSWORD=$2
    KEYSTORE_FILE=$3

    keytool -importkeystore -srckeystore $KEYSTORE_FILE -destkeystore ${ALIAS}.p12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass $PASSWORD -deststorepass $PASSWORD -srcalias $ALIAS -destalias $ALIAS -srckeypass $PASSWORD -destkeypass $PASSWORD -noprompt
}
runTrustPublic() {
    ALIAS=$1
    PASSWORD=$2

    keytool -import -noprompt -trustcacerts -alias $ALIAS -file ${ALIAS}.cer -keystore $SERVER_KEYSTORE -storepass $PASSWORD
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


# Create a bunch of sample users
$(createNewIdentity moriarty Brian_Moriarty)
$(createNewIdentity fring "Gus_Fring")
#$(createNewIdentity grimes "Rick_Grimes")
#$(createNewIdentity white "Walter_White")
#$(createNewIdentity trump "Donald_Trump")
#$(createNewIdentity sanders "Bernie_Sanders")
#$(createNewIdentity clinton "Hillary_Clinton")
#$(createNewIdentity carson "Ben_Carson")

# Create the server cert
$(createServerKeystore mpnov.sd.spawar.navy.mil mpnov.sd.spawar.navy.mil)


