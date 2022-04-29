package com.as

class GetGitHubReleases implements java.io.Serializable {
  def context

  GetGitHubReleases(context) {
    this.context = context
  }

  public void getReleases(String repoUrl){
    def (protocol, hostname, owner, repo_name) = repoUrl.tokenize('/')
    def curl_command = "curl -H 'Accept: application/vnd.github.v3+json' https://api.github.com/repos/${owner}/${repo_name}/releases -o releases-${repo_name}.txt"
    this.context.sh(curl_command)
  }
}