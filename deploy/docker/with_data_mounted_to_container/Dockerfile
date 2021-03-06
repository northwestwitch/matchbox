FROM openjdk:8-jdk

RUN apt-get update \
 && apt-get install -y wget \
 && apt-get install -y git \
 && apt-get install -y maven 

MAINTAINER MacArthur Lab

ADD settings.xml /root/.m2/settings.xml
ADD entrypoint.sh  /root/bin/entrypoint.sh

env MVN=mvn

#first get Exomiser built in the local maven for matchbox to import in
#---------------------------------------------------------------------------
# _n.b._ check that the tag here is the same as the exomiser.version declared in the pom

RUN git clone https://github.com/exomiser/Exomiser
WORKDIR Exomiser
RUN $MVN -DskipTests=true clean install

#now matchbox (and it will see Exomiser in local maven repo)
#---------------------------------------------------------------------------

RUN git clone https://github.com/macarthur-lab/matchbox
WORKDIR matchbox
RUN $MVN -Dmaven.test.skip=true clean install package

env MATCHBOX_JAR=/Exomiser/matchbox/target/matchbox-0.1.0.jar
env MATCHBOX_CONFIG_DIR=/Exomiser/matchbox/config
env MATCHBOX_DEPLOYMENT_CONFIG_DIR=/matchbox_deployment/config

#Now set matchbox up for deployment and copy over jar and config files
#---------------------------------------------------------------------------
WORKDIR /matchbox_deployment
RUN cp -rf $MATCHBOX_CONFIG_DIR . \
 && cp $MATCHBOX_JAR .


#############################################
#                                           #
# Please note the EXOMISER_DATA_DIR         #
# value. The file system path with ref      #
# data (viewable by docker daemon) must     #  
# be mounted to this location in            #
# container at the docker run step          #
#                                           #
#############################################
env EXOMISER_DATA_DIR=/Exomiser/matchbox/data/data
env EXOMISER_PHENOTYPE_DATA_VERSION=1711


#############################################
#                                           #
# This defines if matches that have no      #
# genotypes in common, BUT have a high      #
# phenotype score should be returned as     #
# results                                   #
#                                           #
#############################################
env ALLOW_NO_GENE_IN_COMMON_MATCHES=false

#############################################
#                                           #
# Environment variables for Mongo           #
# connection. Please populate before        #
# doing docker build command                #
#                                           #
#############################################
env MONGODB_HOSTNAME=
env MONGODB_PORT=
env MONGODB_USERNAME=
env MONGODB_PASSWORD=
env MONGODB_DATABASE=


#############################################
#                                           #
# Default server port is 9020, this will    #
# be overwritten if you chose HTTPS below   #
#                                           #
#############################################
env SERVER_PORT=9020


#########################################################################
#                                                                       #
# MME REQUIRES HTTPS IF YOUR SERVER IS NOT PROXIED BEHIND HTTPS.        #
# You can activate HTTPS by,                                            #
#                                                                       #
# 1. UNCOMMENT ALL the following                                        #
# 2. SET variable USE_HTTPS	to be true                                  #
#                                                                       #
# You can override sever port here                                      #
#                                                                       #
#########################################################################
env USE_HTTPS=false
#env SERVER_PORT=8443
#env HTTPS_SSL_KEY_STORE=matchbox_keystore
#env HTTPS_SSL_KEY_STORE_PASSWORD=changeit
#env HTTPS_SSL_KEY_PASSWORD=temp_ks_pwd__change_me!

#RUN keytool -genkey -noprompt \
#					-alias matchbox \
#					-dname "CN=, OU=, O=, L=, S=, C=" \
#					-keystore $HTTPS_SSL_KEY_STORE \
#					-storepass $HTTPS_SSL_KEY_STORE_PASSWORD \
#					-keypass $HTTPS_SSL_KEY_PASSWORD
					

#############################################
#                                           #
# This port is exposed by container         #
#                                           #
#############################################
EXPOSE $SERVER_PORT


###########################################################################
# IN PRODUCTION, PLEASE MANAGE THESE FILES EXTREMELY CAREFULLY AS THEY	  #
# WILL CONTAIN SENSITIVE ACCESS TO YOUR, AND OTHER NODES!		          #
#                                           				              #
# These files govern/contain,						                      #
#	1. Tokens that gives OTHER NODES, access to matchbox (config.xml)     #
#	2. Tokens that gives MATCHBOX, access to other nodes (nodes.json)     #
#                                           				              #
###########################################################################
ADD config.xml $MATCHBOX_DEPLOYMENT_CONFIG_DIR
ADD nodes.json $MATCHBOX_DEPLOYMENT_CONFIG_DIR


CMD ["/root/bin/entrypoint.sh"]
