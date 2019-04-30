####################################################################################################################
##                                     FILL THIS FILE AND copy it as .ciconf                                       #
####################################################################################################################

$WORKSPACE="C:\Docomo\proyectos\fraudwall\tracking\tracking"  #C:\Docomo\proyectos\fraudwall\microservice\tracking"        # fill with your git root for project crm-api

$RUNARGS="-p9090:9090"           # optional: args passed to consumer docker run
$BUILDARGS=""        # optional: args passed to consumer docker build
$CMDARGS="/bin/bash"			# optional: cmd string when running cosnumer docker


####################################################################################################################
##                                      DO NOT EDIT PLEASE                                                         #
####################################################################################################################
$PNAME="crm-api"
$pname="crm-api"
$serviceyaml="ci/local/composer.yml"
$consumerenv="ci/local/consumer.env"
$dockerdir="ci/consumer/"
$dockerfile="Dockerfile"
$scriptversion="0.0.1-ALPHA"


