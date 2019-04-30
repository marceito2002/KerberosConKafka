$SCRIPTNAME = $MyInvocation.ScriptName
$SCRIPTPATH = $PSScriptRoot

$USAGE = @"
    usage $MyInvocation.ScriptName:
        help: show this help
        version: show script version
        start:  brings-up all services (detached) and the consumer in interactive mode
        login:  open bash shell in consumer
        exec <cmd> <args>: execute cmd (with args) in consumer
        stop: stop all services and consumer (deletes service containers)
        pause: stop all services and consumer (do not delete service containers)
        reset: remove all (containers, images and consumer)
"@

$DRAMA = @"
    BIG DRAMA!
        Something's gone terribly wrong.

        - (both setupper and users) Be sure tha the script is in "$pname_project_root/ci/" path (where $pname_project_root is the root git path of $pname project)
        - (setupper only) if you are setting up your project for your team, make sure you have added all the ci/ tree to git and committed it.
          Make sure you have also committed the configuration file "ci/ciconf.ps1"
        - (users only) if you're using a repository that someone else has set up for you, make sure you have the file "ci/ciconf.ps1"
          If you do not find the file ask who did the job: "what the hell? Where is my conf file?"
"@

function exitecho
{
    param ($p1)
    Write-Error $p1
    exit 1
}

if (!(Test-Path $SCRIPTPATH))
{
	exitecho($DRAMA)
}

. ("$SCRIPTPATH\ciconf.ps1")

if ("$WORKSPACE" -eq ""){
	exitecho("PLEASE fill the file ci\ciconf.ps1")
}
$bname = Split-Path $SCRIPTPATH -Leaf
if ("$bname" -ne "ci")
{
	exitecho($DRAMA)
}


$binaries = @("git", "docker", "docker-compose")
foreach ($binc in $binaries) {
    try {
        Get-Command -Name $binc -ErrorAction Stop | Out-Null
    }
    catch {
        exitecho("$binc NOT FOUND!")
    }
}

switch ($args[0]){
    "-h" {
        Write-Host $USAGE
        exit 0f
    }
    
    "start" {
        docker-compose -f $WORKSPACE/$serviceyaml -p $pname up -d
        docker build $WORKSPACE/$dockerdir $BUILDARGS -t $pname
        echo "docker run -ti --rm --name $pname $RUNARGS -v ${WORKSPACE}:/WORKSPACE -e WORKSPACE=/WORKSPACE --env-file $WORKSPACE/ci/local/consumer.env --network ${pname}_default ${pname} $CMDARGS"
        docker run -ti --rm --name $pname $RUNARGS -v ${WORKSPACE}:/WORKSPACE -e WORKSPACE=/WORKSPACE --env-file $WORKSPACE/ci/local/consumer.env --network ${pname}_default ${pname} $CMDARGS
        
        break
    }

    "login" {
    	docker exec -ti $pname bash
        exit $LASTEXITCODE
    }

    "exec" {
        $first, $arghi = $args
        docker exec -ti $pname "$arghi"
        exit $LASTEXITCODE
    }

    "stop" {
        docker stop $pname
        docker-compose -f $WORKSPACE/$serviceyaml -p $pname down
        exit 0
    }
    
    "pause" {
        docker stop $pname
        docker-compose -f $WORKSPACE/$serviceyaml -p $pname stop
        exit 0
    }

    "reset" {
        docker rmi --force $pname
        docker-compose -f  $WORKSPACE/$serviceyaml -p $pname down --rmi 'all'
        exit 0
    }
    
    "version" {
        Write-Host $scriptversion
        exit 0
    }
    
    default {"Error: unknown option $args[0]"; exit 1}
}