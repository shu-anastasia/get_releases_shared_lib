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
        def curl_command = "curl -H 'Accept: application/vnd.github.v3+json' https://api.github.com/repos/${owner}/${repo_name}/releases -o releases-${repo_name}.txt"
        this.context.sh(curl_command)
    }
    else{
        this.context.error("Invalid repository address")
    }
  }

  boolean validateRepoURL(String url){
    def pattern = ~"(?:git@|https://)github.com[:/](.*)"
    return pattern.matcher(url).matches()
  }
}