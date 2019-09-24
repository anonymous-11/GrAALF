# Behavior Graph 

This tool shows a graph of activities. 

## Install 
sysdig is required for running this tool to install sysdig : 
	
	curl -s https://s3.amazonaws.com/download.draios.com/stable/install-sysdig | sudo bash
	
## to run Sysdig for data collection  

Program runs on two modes of logs for now, 

* Long Format : this will assume all 84 fields of the sysdig logs to be present in the report 
* Short Format : this will assume a smaller set of fields, this is to save disk space when log is stored. 

### Long format

to collect information, sysdig is uised, to invoke sysdig : (output can be piped/sent to a file for latter processing)

	 sudo sysdig -p *"%fd.num=&|||&=%fd.type=&|||&=%fd.typechar=&|||&=%fd.name=&|||&=%fd.directory=&|||&=%fd.filename=&|||&=%fd.ip=&|||&=%fd.cip=&|||&=%fd.sip=&|||&=fd.port=&|||&=%fd.cport=&|||&=%fd.sport=&|||&=%fd.l4proto=&|||&=%fd.sockfamily=&|||&=%fd.is_server=&|||&=%proc.pid=&|||&=%proc.exe=&|||&=%proc.name=&|||&=%proc.args=&|||&=%proc.cmdline=&|||&=%proc.cwd=&|||&=%proc.nchilds=&|||&=%proc.ppid=&|||&=%proc.pname=&|||&=%proc.apid=&|||&=%proc.aname=&|||&=%proc.loginshellid=&|||&=%proc.duration=&|||&=%proc.fdopencount=&|||&=%proc.fdlimit=&|||&=%proc.fdusage=&|||&=%proc.vmsize=&|||&=%proc.vmrss=&|||&=%proc.vmswap=&|||&=%thread.pfmajor=&|||&=%thread.pfminor=&|||&=%thread.tid=&|||&=%thread.ismain=&|||&=%thread.exectime=&|||&=%thread.totexectime=&|||&=%evt.num=&|||&=%evt.time=&|||&=%evt.time.s=&|||&=%evt.datetime=&|||&=%evt.rawtime=&|||&=%evt.rawtime.s=&|||&=%evt.rawtime.ns=&|||&=%evt.reltime=&|||&=%evt.reltime.s=&|||&=%evt.reltime.ns=&|||&=%evt.latency=&|||&=%evt.latency.s=&|||&=%evt.latency.ns=&|||&=%evt.deltatime=&|||&=%evt.deltatime.s=&|||&=%evt.deltatime.ns=&|||&=%evt.dir=&|||&=%evt.type=&|||&=%evt.cpu=&|||&=%evt.args=&|||&=%evt.info=&|||&=%evt.buffer=&|||&=%evt.res=&|||&=%evt.rawres=&|||&=%evt.failed=&|||&=%evt.is_io=&|||&=%evt.is_io_read=&|||&=%evt.is_io_write=&|||&=%evt.io_dir=&|||&=%evt.is_wait=&|||&=evt.is_syslog=&|||&=evt.count=&|||&=%user.uid=&|||&=%user.name=&|||&=user.homedir=&|||&=user.shell=&|||&=group.gid=&|||&=group.name=&|||&=syslog.facility.str=&|||&=syslog.facility=&|||&=%syslog.severity.str=&|||&=syslog.severity=&|||&=syslog.message" "(evt.type=read or evt.type=write or evt.type=open or evt.type=close or evt.type=pwrite64 or evt.type=writev or evt.type=pwritev or evt.type=socket or evt.type=connect or evt.type=accept or  evt.type=sendto or evt.type=recvfrom or  evt.type=sendmsg or evt.type=recvmsg or evt.type=clone or evt.type=fork or evt.type=vfork or evt.type=execve or evt.type=pipe2 or evt.type=pipe or evt.type=accept4 or evt.type=pread64 or evt.type=readv or evt.type=preadv or  evt.type=rename or evt.type=renameat or evt.type=unlink or evt.type=link or evt.type=kill) and evt.failed!=true and evt.dir=<"


	
	
### Short format 

to collect logs to be used with the short format, use the following invocation for sysdig 

	 sudo sysdig -p *"%evt.datetime=&|||&=%evt.type=&|||&=%thread.tid=&|||&=%proc.name=&|||&=%proc.args=&|||&=%proc.cwd=&|||&=%proc.cmdline=&|||&=%proc.pname=&|||&=%proc.pid=&|||&=%proc.ppid=&|||&=%fd.cip=&|||&=%fd.cport=&|||&=%fd.directory=&|||&=%fd.filename=&|||&=fd.ip=&|||&=%fd.name=&|||&=%fd.num=&|||&=%fd.sip=&|||&=%fd.sockfamily=&|||&=%fd.sport=&|||&=%fd.type=&|||&=%fd.typechar=&|||&=%user.name=&|||&=%user.uid=&|||&=%evt.num=&|||&=%evt.args=&|||&=%user.shell" "(evt.type=read or evt.type=write or evt.type=open or evt.type=close or evt.type=pwrite64 or evt.type=writev or evt.type=pwritev or evt.type=socket or evt.type=connect or evt.type=accept or  evt.type=sendto or evt.type=recvfrom or  evt.type=sendmsg or evt.type=recvmsg or evt.type=clone or evt.type=fork or evt.type=vfork or evt.type=execve or evt.type=pipe2 or evt.type=pipe or evt.type=accept4 or evt.type=pread64 or evt.type=readv or evt.type=preadv or  evt.type=rename or evt.type=renameat or evt.type=unlink or evt.type=link or evt.type=kill) and evt.failed!=true and not proc.name contains gnome and evt.dir=<"
	 
	 
	
## Running Jar File and keywords

there is a build jar file in tool_bin dir. to run it :

	java -jar dcf.jar [arguments]
	
arguments : 

	* ssql :  save a copy of all the recods read to a postgress database
	* gv : create the graph in verbose mode
	* g : create the graph in non-verbose mode (do not use with new graph tool)
	* file : sets the source of logs to be file, if this key is used path= has to be provided ; if this key is not provided , stdin will be assumed the source of logs 
	*  sf : saves the formated out put to be used in other tools (should be used with outpath= to choose where to save)
	* path=[path to input file] : path to input file
	* outpath=[path to file] : path to which the formated output is supposed to be stored.
	* rm: to select in memory query adapter
	* rspg:  to select simple postgress query adapter
	* short: to enable short format logs being read from input 
	* rsn4j: to choose neo4j query adapter 
	* c0 : no compression 
	* c1 : level one compression, same edge will keep the time stamps
	* c2 : level two compression, same edge will only keep the first and last time stamps
	* c3 : level three compression, only the first time stamp of an edge is kept
	
## Query model 


OQL is capable supports projection and filter and it’s based on the sub graphs initiated by the nodes identified through the criterias. 
In our data model we divide the data in two parts : 
	Resources : are represented as vertices in our causal graph representation, and include processes, threads, files, pipes, networks, activities, etc. we identify them by color coding based on type in the graph. 
	Access Calls : are represented as edges in our  causal graph and represent access to resources, systems calls, initiations, API calls, Binder Calls, etc. we identify them by color coding based on the type in the graph.

The general structure of our query language is as follows : 
	
	1- {set/get} [variable_name] [value]
	
	2- [verbose] [back] [forward] select {* ,[ projection of Access Types ]} from {*,[ projection of Resource Types ]} [where [[field] [operator] [value]]^ ] [;]
	
	3- describe [verbose] [orderby={pid|pname|fname|seq}] [path=/path/to/file]
	
	4- {exit|quit}
	
	5- info

### Variable setting/ Getting query

THis model is used to set or get environment variables, variables are as follows (case insensitive) : 
 
 * FORWARD_DEPTH : this sets the depth to which each forward tree is traversed 
 * BACK_DEPTH : this sets the depth to which each backward tracking is done 
 
 
### Searching query
The parts in the query account for the followings :
[verbose] : we have two options to show only existence of the relation between resources or seeing all the relations on the graph. 
[back] : back tracks from selected nodes to first node with input degree = 0 in the graph. 
[forward] : forward tracks from selected nodes to all resources it has touched and this will recursively continue. 

[projection of access types]  : this option is either * for all, or is a selection of system calls including read, write, open, exec, etc. NOTE : because we do not capture starting of all processes, `exec` denotes an execution of a process by it's parent but time stamp will be the first time it has been executed which might or might not be when it was started.
[projection of Resource types] : this option is either * for all, or is a selection of “file”, "process", "soc" for all types of socket calls, "pipe" or "unix"
, ..
Criteria : criterias are formated as `[type] field operator value` . 
type parameter can be any of resource types described in projection of resources section. This part is optional, skipping it or putting `any` means matching of all types is desired. 


The “field” is one of the options :
*  pid : for process id.
*  name : searches in titles of resources 
*  user_name : to search fir activities done by the user
*  user_id : to search fir activities done by the user


 The “operator” is either “is” or “has” which account for exact match and the contains operator. Different criterions can be added in the query using the separator “,also,”; “or” logical operator would be applied to these criterias. we have used this format to minimize the parsing efforts.  

The “;” in the end indicates whether to add the results of the current query to the graph which is already present in the window; so we can have results of multiple queries create the whole picture piece by piece. 

### Describe 

This keyword is to be used for creating a textuall representation of the graph. When this keyword is used a textual representation of the graph in the window will be presented. This can also be printed to a file with use of optional `path=` parameter. 

by default describe, flattens the similar calls after eachother into a signle record. if seperate row for each call is desired user should use the optional verbose keyword.  

to order the items in the list one can use `orderby=` with one the given arguments as follows : 

* seq : sorts by sequence number 
* pname : sorts by process name 
* pid : sorts by processid 
* fname : sorts by file ( edge's destination ) name 

*NOTE* : sort filed will also control how the merger of rows happens, eg. if sort by seq is selected the tow records which come after each other having same user,from, to, commands will be merged. if sorted by processid , the same will happen except they will be dealt with based on the order of processid .

### Exiting

to exit the program gracefully, one the commands `exit` or `quit` can be used- both case insensetive. 

### Getting graph info and stats

to get information ( for now, edge and vertex counts ) use `info` keyword.