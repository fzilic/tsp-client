#!/bin/bash

_alg=
_souce=
_directory=
_digest=
_query=
_reply=
_url=
_gen_nonce=
_nonce=
_policy=
_verbosity=

_options="a:s:d:gqru:on:p:hv:"

_script_dir=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

usage() {
  echo """
Usage $0 -a <algorithm> -s <source-file> -d <directory> -g -q -r -u <url> -o -n <nonce> -p <policy id> -h -v
  -a - hash algorithm, can be one of the following MD5 SHA1 SHA256
  -s - file to be hashed
  -d - (optional) output directory to store intermediate files
  -g - (optional) store digest file (<directory>/digest.<algorithm>)
  -q - (optional) store RFC3161 query (<directory>/query.<algorithm>.tsq)
  -r - (optional) store RFC3161 reply (<directory>/reply.<algorithm>.tsr)
  -u - URL to execute RFC3161 request to
  -o - (optional) - generate nonce integer
  -n - (optional) - use specified nonce integer
  -p - (optional) - request timestemp using policy OID
  -h - (optional) this help
  -v - (optional) verbosity level, can be V1 or V2, the higher the number more verbose output
""" >&2
}

while getopts $_options _option; do
  case $_option in
    a )
      _alg=$(echo $OPTARG | tr '[:lower:]' '[:upper:]')

      if [[ ! $_alg =~ ^MD5|SHA1|SHA256|SHA384|SHA512$ ]]; then
        echo "Invalid hashing algorithm $_alg" >&2
        usage
        exit 1
      fi

      ;;
    s )
      _souce=$OPTARG

      if [ ! -e $_souce ]; then
        echo "Source $_souce file doesn't exist." >&2
        usage
        exit 1
      fi

      ;;
    d )
      _directory=$OPTARG
      ;;
    g )
      _digest="t"
      ;;
    q )
      _query="t"
      ;;
    r )
      _reply="t"
      ;;
    u )
      _url=$OPTARG
      ;;
    o )
      _gen_nonce="t"
      ;;
    n )
      _nonce=$OPTARG
      ;;
    p )
      _policy=$OPTARG
      ;;
    v )
      _verbosity=$OPTARG
      ;;
    h )
      usage
      exit 0
      ;;
    \? )
      echo "Error. Unknown option: -$OPTARG" >&2
      exit 1
      ;;
    : )
      echo "Error. Missing option argument for -$OPTARG" >&2
      exit 1
      ;;
  esac
done

if [ -z "$(which java)" ]; then
  echo "Missing java." >&2
  usage
  exit 1;
fi

_client_jar=$(find $_script_dir -name tsp-client.jar)

if [ -z "$_client_jar" ]; then
  echo "Missing client jar file." >&2
  usage
  exit 1
fi

if [ -z "$_alg" -o -z "$_souce" -o -z "$_url" ]; then
  echo "Mandatory arguments not set" >&2
  usage
  exit 1
fi

if [ -z "$_directory" ] && [ -n "$_digest" -o -n "$_query" -o -n "$_reply" ]; then
  _directory=$_alg
  echo "Warning directory not set and storage requested, setting directory to $_alg" >&2
fi

if [ -n "$_directory" -a ! -d "$_directory" ]; then
  mkdir -p $_directory
fi

_command_args=" -a $_alg -d $_souce"

if [ -n "$_digest" ]; then
  _command_args=$_command_args" -g $_directory/digest.$_alg"
fi

if [ -n "$_query" ]; then
  _command_args=$_command_args" -q $_directory/query."$_alg".tsq"
fi

if [ -n "$_reply" ]; then
  _command_args=$_command_args" -r $_directory/reply."$_alg".tsr"
fi

if [ -n "$_url" ]; then
  _command_args=$_command_args" -u $_url"
fi

if [ -n "$_gen_nonce" ]; then
  _command_args=$_command_args" -o "
fi

if [ -n "$_nonce" ]; then
  _command_args=$_command_args" -n $_nonce"
fi

if [ -n "$_policy" ]; then
  _command_args=$_command_args" -p $_policy"
fi

if [ -n "$_verbosity" ]; then
  _command_args=$_command_args" -v $_verbosity"
fi

java -jar $_client_jar $_command_args