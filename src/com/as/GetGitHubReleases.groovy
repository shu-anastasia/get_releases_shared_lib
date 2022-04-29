package com.as

class GetGitHubReleases implements java.io.Serializable {
  def context

  GetGitHubReleases(context) {
    this.context = context
  }

  public void getReleases(String repoUrl){
    def validURL = validateRepoURL(repoUrl)
    if (validURL){
        this.context.sh("echo Getting the releases from ${repoUrl}...")
        def (protocol, hostname, owner, repo_name) = repoUrl.tokenize('/')

        def output_filename = "releases-${repo_name}.txt"

        repo_name = repo_name.tokenize('.')[0]
        def curl_command = "curl -H 'Accept: application/vnd.github.v3+json' https://api.github.com/repos/${owner}/${repo_name}/releases -o ${output_filename}"
        def response_code = this.context.sh(
            script: "curl -o status_code -s -w %{http_code} https://github.com/${owner}/${repo_name}",
            returnStdout: true).trim()
        if (response_code == '200'){
            this.context.sh(curl_command)
            createArtifact(output_filename)
        }
        else{
            this.context.error("HTTP request to ${repoUrl} was not successful. Response code: ${response_code}")
        }
    }
    else{
        this.context.error("Invalid repository address")
    }
    cleanup()
  }

  boolean validateRepoURL(String url){
    def pattern = ~"(?:git@|https://)github.com[:/](.*)"
    return pattern.matcher(url).matches()
  }

  public cleanup(){
    this.context.echo "Cleaning the workspace..."
    this.context.cleanWs()
  }

  public createArtifact(String filename){
    this.context.echo "Writing output to file..."
    this.context.archiveArtifacts artifacts: filename, caseSensitive: false, onlyIfSuccessful: true
  }
}